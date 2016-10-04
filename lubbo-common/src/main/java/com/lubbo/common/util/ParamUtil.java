package com.lubbo.common.util;

import java.util.Map;

/**
 * @author benchu
 * @version 16/9/20.
 */
public class ParamUtil {
    public static int parseInt(Map<String, Object> param, String key, int defaultValue) {
        Object v = param.get(key);
        if (v == null) {
            return defaultValue;
        }
        return Integer.parseInt(v.toString());
    }
}
