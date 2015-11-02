package com.lubbo.core.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benchu on 15/10/25.
 */
public enum  MessageStatus {
    NORMAL(1),ERROR(2),TIMEOUT(3);
    private final int id;
    MessageStatus(int id) {
        this.id = id;
    }
    private static Map<Integer,MessageStatus> map = new HashMap<>();
    static {
        map.put(1,NORMAL);
    }

    public int getId() {
        return id;
    }
    public static MessageStatus get(int id){
        return map.get(id);
    }
}
