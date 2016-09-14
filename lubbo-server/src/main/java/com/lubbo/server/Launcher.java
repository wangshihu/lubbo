package com.lubbo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author benchu on 15/10/31.
 */
public class Launcher {

    public static void init(){
        String serverXML = "classpath:META-INF/lubbo/server/server.xml";
        String coreXML = "classpath:META-INF/lubbo/core/core.xml";
        ApplicationContext context =
            new ClassPathXmlApplicationContext(serverXML,coreXML,"classpath:spring.xml");
    }
}
