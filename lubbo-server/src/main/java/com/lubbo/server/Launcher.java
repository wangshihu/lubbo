package com.lubbo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by benchu on 15/10/31.
 */
public class Launcher {
    public static void main(String[] args) {
        String serverXML = "classpath:META-INF/lubbo/server/server.xml";
        String coreXML = "classpath:META-INF/lubbo/core/core.xml";
        ApplicationContext context =
            new ClassPathXmlApplicationContext(serverXML,coreXML,"classpath:spring.xml");
    }
}
