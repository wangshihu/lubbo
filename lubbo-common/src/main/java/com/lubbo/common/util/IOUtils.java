package com.lubbo.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author benchu
 * @version 16/9/18.
 */
public class IOUtils {
    public static  void closeQuitely(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException ignored) {

                    }
                }
            }
        }
    }
}
