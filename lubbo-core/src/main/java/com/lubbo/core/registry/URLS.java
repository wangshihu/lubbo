package com.lubbo.core.registry;

import static com.lubbo.common.LubboConstants.ZK_ROOT;
import static com.lubbo.common.LubboConstants.ZK_SEPARATOR;

/**
 * @author benchu
 * @version on 15/10/21.
 */
public class URLS {
    /**
     * /lubbo/${service}
     */
    public static String serviceUrl(String service) {
        return ZK_SEPARATOR + ZK_ROOT + ZK_SEPARATOR + service;
    }

    /**
     * /lubbo/${service}/providers/ip
     */
    public static String providerIp(String serviceUrl, String ip) {
        return serviceUrl + ZK_SEPARATOR + "providers" + ZK_SEPARATOR + ip;
    }

    /**
     * /lubbo/${service}/consumers/ip
     */
    public static String consumerIp(String serviceUrl, String ip) {
        return serviceUrl + ZK_SEPARATOR + "consumers" + ZK_SEPARATOR + ip;
    }

}
