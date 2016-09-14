package com.lubbo.server;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author benchu
 * @version 16/9/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/lubbo/server/server.xml","classpath:META-INF/lubbo/core/core.xml","classpath:spring.xml"})
public class AbstractTest {

    @Autowired
    protected LubboScanner lubboScanner;
    @BeforeClass
    public static void beforeClass(){
        Launcher.init();
    }


}
