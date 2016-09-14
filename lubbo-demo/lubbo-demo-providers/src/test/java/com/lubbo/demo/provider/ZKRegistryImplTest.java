package com.lubbo.demo.provider;


import java.util.List;

import org.junit.Test;

import com.lubbo.core.registry.ZKRegistryImpl;

/**
 * Created by benchu on 15/10/31.
 */
public class ZKRegistryImplTest {
    ZKRegistryImpl registry;

    @org.junit.Before
    public void before(){
        registry= new ZKRegistryImpl("localhost:2181");
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
