package com.lubbo.core.protocol;

import com.alibaba.fastjson.JSON;
import com.lubbo.common.LubboConstants;
import com.lubbo.core.message.SerializeType;

/**
 * @author benchu
 * @version on 15/10/28.
 */
public class SerializationFactory {
    public <T> T unSerialize(SerializeType serializeType, byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data, LubboConstants.DEFAULT_CHARSET), clazz);
    }

    public byte[] serialize(SerializeType serializeType, Object object) {
        return JSON.toJSONString(object).getBytes(LubboConstants.DEFAULT_CHARSET);
    }

}
