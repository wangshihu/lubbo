package com.lubbo.common;

import java.nio.charset.Charset;

import com.lubbo.common.util.IPUtil;

/**
 * @author benchu
 * @version on 15/10/21.
 */
public class LubboConstants {

    public static final String ZK_ROOT = "lubbo";
    public static final String ZK_SEPARATOR = "/";
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    //默认的lubbo端口,从20020开始,以此递增查找.
    public static final int LUBBO_PORT = IPUtil.getAvailablePort(20020);
    //本机的ip.
    public static final String LOCAL_HOST = IPUtil.getLocalIpByNetcard();

}
