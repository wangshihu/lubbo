package com.lubbo.core.protocol;

import com.alibaba.fastjson.JSON;
import com.lubbo.common.Constants;
import com.lubbo.core.message.SerializeType;

/**
 * @author benchu
 * @version on 15/10/28.
 */
public class SerializationFactory {
    public <T> T unSerialize(SerializeType serializeType, byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data, Constants.DEFUALT_CHARSET), clazz);
    }

    public byte[] serialize(SerializeType serializeType, Object object) {
        return JSON.toJSONString(object).getBytes(Constants.DEFUALT_CHARSET);
    }

}
