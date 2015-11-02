package com.lubbo.core.message;

import java.util.HashMap;
import java.util.Map;

import com.lubbo.core.Invocation;
import com.lubbo.core.Result;

/**
 * Created by benchu on 15/10/21.
 */
public enum  MessageType {

    INVOCATION(1, Invocation.class), RESULT(2, Result.class);

    private  int id;
    private Class<?> clazz;
    MessageType(int id,Class<?> clazz) {
        this.id = id;
        this.clazz = clazz;
    }
    private static Map<Integer,MessageType> map = new HashMap<>();
    static {
        map.put(1,INVOCATION);
        map.put(2,RESULT);
    }

    public int getId() {
        return id;
    }

    public Class<?> getClazz() {
        return clazz;
    }
    public static MessageType get(int id){
        return map.get(id);
    }


}
