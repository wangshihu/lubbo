package com.lubbo.server;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.lubbo.server.service.JobService;

/**
 * @author benchu
 * @version 16/9/13.
 */
public class LubboScannerTest extends AbstractTest {
    @Test
    public void testGetExposeConfig(){
        Map<String, Exposer> map = lubboScanner.getExposerMap();
        Exposer exposer = map.get("com.lubbo.server.service.JobService");
        ExposeConfig config = exposer.getConfig();
        Assert.assertEquals(config.getService(),"com.lubbo.server.service.JobService");
        Assert.assertEquals(config.getServiceClass(), JobService.class);
    }

}
