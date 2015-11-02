package com.lubbo.demo.providers;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.lubbo.demo.api.IdRange;
import com.lubbo.core.annotation.Lubbo;

/**
 * Created by benchu on 15/10/31.
 */
@Service("IdRange")
@Lubbo
public class IdRangeImpl implements IdRange {
    AtomicInteger generator = new AtomicInteger(1);

    @Override
    public int getId() {
        return generator.getAndIncrement();
    }
    public static void main(String[] args) {
        IdRangeImpl idRange = new IdRangeImpl();
        Annotation[] anootations = idRange.getClass().getAnnotations();
        System.out.println(anootations.length);
    }
}


