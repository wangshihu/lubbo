package com.huihui.demo.comsumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lubbo.demo.api.IdRange;

/**
 * @author  benchu
 * @version on 15/10/31.
 */
public class Demo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IdRange idRange =  context.getBean(IdRange.class);
        for (int i = 0; i < 100; i++) {
            int r = idRange.getId();
            System.out.println(r);
        }

    }
}
