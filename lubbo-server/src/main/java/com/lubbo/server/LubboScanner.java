package com.lubbo.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Preconditions;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.annotation.Lubbo;

/**
 * @author  benchu
 * @version on 15/10/21.
 */
public class LubboScanner implements ApplicationContextAware,DisposableBean {
    private ApplicationContext applicationContext;
    private static Logger logger = LoggerFactory.getLogger(LubboScanner.class);

    private InvokerFactory invokerFactory;
    private ExposeListener exposeListener;
    private ServerConfig serverConfig;
    private Map<String,Exposer> exposerMap = new ConcurrentHashMap<>();
    @PostConstruct
    public void init(){
        scanLubboBean();
    }


    private void scanLubboBean() {
        logger.debug("scanLubboBean()....");
        Map<String,Object> lubbos  = applicationContext.getBeansWithAnnotation(Lubbo.class);
        for(Object bean:lubbos.values()){
            ExposeConfig exposeConfig = getExposeConfig(bean);
            Invoker invoker = invokerFactory.newInvoker(exposeConfig);
            //构造exposer 并且暴露服务
            Exposer exposer = new LubboExposer(exposeListener,exposeConfig,invoker);
            exposer.expose();
            exposerMap.put(exposeConfig.getService(),exposer);
        }
    }

    public ExposeConfig getExposeConfig(Object bean) {
        Class<?> clazz = AopUtils.getTargetClass(bean);
        Class[] interfaces = clazz.getInterfaces();
        Preconditions.checkArgument(interfaces.length == 1, "class {} has too many interfaces", clazz.getName());
        //获得接口service
        Class service = interfaces[0];
        ExposeConfig exposeConfig = new ExposeConfig();
        exposeConfig.setServiceClass(service);
        exposeConfig.setTargetBean(bean);
        exposeConfig.setIp(serverConfig.getIp());
        exposeConfig.setPort(serverConfig.getPort());
        return exposeConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        for(Exposer exposer:exposerMap.values())
            exposer.close();
    }

    public void setInvokerFactory(InvokerFactory invokerFactory) {
        this.invokerFactory = invokerFactory;
    }

    public void setExposeListener(ExposeListener exposeListener) {
        this.exposeListener = exposeListener;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
