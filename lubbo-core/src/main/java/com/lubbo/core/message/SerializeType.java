package com.lubbo.core.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benchu on 15/10/25.
 */
public enum SerializeType {
    FAST_JSON(1);
    private final int id;
    SerializeType(int id) {
        this.id = id;
    }
    private static Map<Integer,SerializeType> map = new HashMap<>();
    static {
        map.put(1,FAST_JSON);
    }

    public int getId() {
        return id;
    }
    public static SerializeType get(int id){
        return map.get(id);
    }
}
