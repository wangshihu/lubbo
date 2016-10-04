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
     * /lubbo/${service}/providers
     */
    public static String provider(String service) {
        return serviceUrl(service) + ZK_SEPARATOR + "providers";
    }
    /**
     * /lubbo/${service}/consumers
     */
    public static String consumer(String service) {
        return serviceUrl(service) + ZK_SEPARATOR + "consumers" ;

    }


    /**
     * /lubbo/${service}/providers/${ip}
     */
    public static String providerIp(String service, String ip) {
        return provider(service) + ZK_SEPARATOR + ip;
    }

    /**
     * /lubbo/${service}/consumers/${ip}
     */
    public static String consumerIp(String service, String ip) {
        return consumer(service) + ZK_SEPARATOR + ip;
    }

    /**
     * @param path /lubbo/${service}/providers/${ip}
     * @return ip
     */
    public static String getHostFromPath(String path) {
        return path.substring(path.lastIndexOf("/"));
    }
}
