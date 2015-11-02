/*
 * Copyright 2011-2014 Mogujie Co.Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lubbo.common.proxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 使用Javassit实现的ProxyFactory,copy from tesla
 *
 * @author mozhu
 *
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int proxyConter = 0;

    private int counter = 0;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> clazz, InvocationHandler invcationHandler) {
        Method[] methods = super.getMethodsCall(clazz).toArray(new Method[0]);
        ClassPool pool = ClassPool.getDefault();

        try {
            CtClass ctClazz = pool.get(clazz.getCanonicalName());

            CtClass proxyClass = pool.makeClass(clazz.getCanonicalName() + "_JavassistProxy" + proxyConter++);
            if (clazz.isInterface()) {
                proxyClass.addInterface(ctClazz);
            } else {
                proxyClass.setSuperclass(ctClazz);
            }
            // make fields
            final String invocationHandlerFieldName = "invocationHandler";
            CtClass invocationHandlerClass = pool.get(InvocationHandler.class.getCanonicalName());
            CtField invocationHandlerField = new CtField(invocationHandlerClass, invocationHandlerFieldName, proxyClass);
            proxyClass.addField(invocationHandlerField);

            final String methodArrayFieldName = "methods";
            CtClass methodArrayClass = pool.get(Method.class.getCanonicalName() + "[]");
            CtField methodArrayField = new CtField(methodArrayClass, methodArrayFieldName, proxyClass);
            proxyClass.addField(methodArrayField);

            // make constructor
            CtConstructor construtor = new CtConstructor(new CtClass[] { invocationHandlerClass, methodArrayClass },
                    proxyClass);
            StringBuilder builder = new StringBuilder();
            builder.append("{\n");
            builder.append("this.").append(invocationHandlerFieldName).append(" = $1;\n");
            builder.append("this.").append(methodArrayFieldName).append(" = $2;\n");
            builder.append("}");
            construtor.setBody(builder.toString());
            proxyClass.addConstructor(construtor);

            // make methods
            this.makeProxyMethods(proxyClass, methods, invocationHandlerFieldName, methodArrayFieldName);

            if (logger.isDebugEnabled()) {
                try {
                    proxyClass.writeFile("tmp");
                } catch (CannotCompileException | IOException e) {
                    // ignore
                    logger.debug("write proxyClass to tmp directory error.", e);
                }
            }

            return (T) proxyClass.toClass().getConstructors()[0].newInstance(invcationHandler, methods);
        } catch (CannotCompileException | NotFoundException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | SecurityException e) {
            throw new ProxyException(e);
        }

    }

    private void makeProxyMethods(CtClass proxyClass, Method[] methods, String invocationHandlerFieldName,
            String methodArrayFieldName) throws CannotCompileException {
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();

            StringBuilder builder = new StringBuilder(256);
            // build method declare line
            builder.append("public ").append(returnType.getCanonicalName()).append(" ").append(method.getName())
                    .append("(");
            final String parameterPrefix = "param";
            for (int j = 0; j < parameterTypes.length; j++) {
                Class<?> parameterType = parameterTypes[j];
                builder.append(parameterType.getCanonicalName()).append(" ").append(parameterPrefix).append(j);
                if (j != parameterTypes.length - 1) {
                    builder.append(",");
                }
            }
            builder.append("){\n");
            // build method execute body

            if (parameterTypes.length == 0) {
                builder.append("Object[] args = new Object[0];\n");
            } else {
                builder.append("Object[] args = {");
                for (int j = 0; j < parameterTypes.length; j++) {
                    Class<?> parameterType = parameterTypes[j];
                    String parameterName = parameterPrefix + j;
                    if (parameterType.isPrimitive()) {
                        builder.append(this.getCastToWrapperClassContent(parameterType, parameterName));
                    } else {
                        builder.append(parameterName);
                    }
                    if (j != parameterTypes.length - 1) {
                        builder.append(",");
                    }
                }
                builder.append("};\n");
            }

            boolean isReturnVoid = Void.TYPE.equals(returnType) || returnType.equals(Void.class);
            if (!isReturnVoid) {
                builder.append("Object ret = ");
            }
            builder.append("this.").append(invocationHandlerFieldName).append(".invoke(this, this.")
                    .append(methodArrayFieldName).append("[").append(i).append("], args);\n");

            builder.append("return ").append(isReturnVoid ? "" : this.getCastContent(returnType, "ret")).append(";\n}");

            logger.debug(" method body {}", builder);
            CtMethod proxyMethod = CtMethod.make(builder.toString(), proxyClass);
            proxyClass.addMethod(proxyMethod);
        }
    }

    @Override
    protected MethodCaller getMethodCaller(Method method, Class<?> clazz) {
        ClassPool pool = ClassPool.getDefault();
        logger.debug("start to get Caller of {}", method);
        CtClass methodCallerClass = pool.makeClass(clazz.getCanonicalName() + "$MethodCaller" + this.counter++ + "$"
                + method.getName());
        try {
            CtClass methodCallerInterface = pool.get(AbstractProxyFactory.class.getCanonicalName() + "$MethodCaller");
            methodCallerClass.addInterface(methodCallerInterface);

            CtMethod invokeMethod = this.makeInvokeMethod(method, methodCallerClass);
            methodCallerClass.addMethod(invokeMethod);
            if (logger.isDebugEnabled()) {
                methodCallerClass.writeFile("tmp");
            }
            MethodCaller caller = (MethodCaller) methodCallerClass.toClass().newInstance();
            logger.debug("generate caller {} of {}", caller, method);
            return caller;
        } catch (CannotCompileException | InstantiationException | IllegalAccessException | NotFoundException
                | IOException e) {
            throw new ProxyException(e);
        }
    }

    /**
     * @param method
     * @param methodCallerClass
     * @return
     * @throws CannotCompileException
     */
    private CtMethod makeInvokeMethod(Method method, CtClass methodCallerClass) throws CannotCompileException {
        StringBuilder builder = new StringBuilder(256);
        builder.append("public Object invoke(Object targetBean, Object[] args) throws Throwable{\n");
        Class<?> returnType = method.getReturnType();
        boolean isReturnVoid = Void.TYPE.equals(returnType) || returnType.equals(Void.class);
        if (isReturnVoid) {
            builder.append("Object ret = null;\n");
        } else {
            builder.append("Object ret = ");
        }
        String methodExcuteLine = this.getMethodExcuteLine(method);
        if (returnType.isPrimitive() && !isReturnVoid) {
            builder.append(this.getCastToWrapperClassContent(returnType, methodExcuteLine));
        } else {
            builder.append(methodExcuteLine);
        }
        builder.append(";\n");
        builder.append("return ret;\n");
        builder.append("}");
        String methodBody = builder.toString();
        logger.debug("generated method body of{}:\n{}", method, methodBody);
        return CtNewMethod.make(methodBody, methodCallerClass);
    }

    /**
     * 生成执行方法的代码行,如
     *
     * <pre>
     * targetBean.newItem((String) args[0], ((Interger) args[1]).intValue())
     * </pre>
     *
     * @param method
     * @param clazz
     * @return
     */
    private String getMethodExcuteLine(Method method) {
        StringBuilder builder = new StringBuilder(64);
        builder.append("((").append(method.getDeclaringClass().getCanonicalName()).append(")targetBean).")
                .append(method.getName()).append("(");
        int argIndex = 0;
        // add cast declare
        for (Class<?> parameterType : method.getParameterTypes()) {
            builder.append(this.getCastContent(parameterType, "args[" + argIndex++ + "]")).append(",");
        }
        if (argIndex > 0) {
            // 删除结尾多余的逗号
            builder.setLength(builder.length() - 1);
        }
        builder.append(")");
        return builder.toString();
    }

    /**
     * 字节码中需要写明类型。 即 <br/>
     * (String)arg<br/>
     * 或者<br/>
     * ((Integer)args[1]).intValue()
     *
     * @param parameterType
     *            参数类型
     * @param parameterName
     *            参数名
     * @return 经过转化后的参数声明
     */
    private String getCastContent(Class<?> parameterType, String parameterName) {
        StringBuilder builder = new StringBuilder(32);
        String parameterTypeName = parameterType.getCanonicalName();
        // 从args中取出的均为包装类型，非原始数据类型，需进行转换
        if (parameterType.isPrimitive()) {
            String wrapperClassName;
            // Character及Boolean的规则特殊需要特殊处理
            if (Character.TYPE.equals(parameterType)) {
                wrapperClassName = Character.class.getCanonicalName();
            } else if (Boolean.TYPE.equals(parameterType)) {
                wrapperClassName = Boolean.class.getCanonicalName();
            } else {
                wrapperClassName = "Number";
            }
            builder.append("((").append(wrapperClassName).append(")").append(parameterName).append(").")
                    .append(parameterTypeName).append("Value()");
        } else if (Number.class.isAssignableFrom(parameterType)) {

            builder.append(parameterType.getCanonicalName()).append(".valueOf(((Number)").append(parameterName)
                    .append(").").append(this.getPrimitiveClassName(parameterType)).append("Value())");
            // 兼容数字类型，进行转换 即((Number)parameterName).intVaule
        } else {
            builder.append("(").append(parameterTypeName).append(")").append(parameterName);
        }
        return builder.toString();
    }

    private String getPrimitiveClassName(Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "Double":
                return "double";
            case "Float":
                return "float";
            case "Long":
                return "long";
            case "Integer":
                return "int";
            case "Short":
                return "short";
            case "Byte":
                return "byte";
            default:
                throw new IllegalArgumentException();

        }
    }

    /**
     * 将原始数据类型转换成包装类型,如
     *
     * <pre>
     * Integer.valueOf(i)
     * 或者
     * Charactor.valueOf(c)
     * </pre>
     *
     * @param primitiveType
     * @param parameterName
     * @return the wrapped statement.
     */
    private String getCastToWrapperClassContent(Class<?> primitiveType, String parameterName) {
        String wrapperClassName;

        String primitiveTypeName = primitiveType.getCanonicalName();
        if (primitiveType.equals(Integer.TYPE)) {
            wrapperClassName = Integer.class.getCanonicalName();
        } else if (primitiveType.equals(Character.TYPE)) {
            wrapperClassName = Character.class.getCanonicalName();
        } else {
            wrapperClassName = Character.toUpperCase(primitiveTypeName.charAt(0)) + primitiveTypeName.substring(1);
        }
        return new StringBuilder().append(wrapperClassName).append(".valueOf(").append(parameterName).append(")")
                .toString();

    }
}
