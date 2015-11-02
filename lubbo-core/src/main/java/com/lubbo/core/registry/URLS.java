package com.lubbo.core.registry;


import static com.lubbo.common.Constants.*;

/**
 * Created by benchu on 15/10/21.
 */
public class URLS {

    public static String getServiceUrl(String service){

        StringBuilder builder = new StringBuilder(ZK_SEPARATOR+ZK_ROOT+ZK_SEPARATOR);
        builder.append(service);
        return builder.toString();
    }



    public static String getIpUrl(String serviceUrl, boolean isProvider, String ip) {
        StringBuilder builder = new StringBuilder(serviceUrl+ZK_SEPARATOR);
        if(isProvider){
            builder.append("providers");
        }else{
            builder.append("consumers");
        }
        builder.append(ZK_SEPARATOR+ip);
        return builder.toString();
    }
}
