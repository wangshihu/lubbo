package com.lubbo.demo.provider;


import java.util.List;

import org.junit.Test;

import com.lubbo.core.registry.ZKRegistry;

/**
 * Created by benchu on 15/10/31.
 */
public class ZKRegistryTest {
    ZKRegistry registry;

    @org.junit.Before
    public void before(){
        registry= new ZKRegistry("localhost:2181");
    }
    @Test
    public void test(){;
        List<String>list = registry.getChildren("/lubbo/com.lubbo.demo.api.IdRange/providers");
        System.out.println(list);
    }
    public void psout(List<String> list){
        if(list==null||list.isEmpty()){
            return;
        }
        for(String str:list){
            System.out.println(str);
            List<String> children = registry.getChildren(str);
        }
    }
}
