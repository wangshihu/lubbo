package com.lubbo.demo.providers;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.lubbo.core.annotation.Lubbo;
import com.lubbo.demo.api.IdRange;

/**
 * @author benchu
 * @version on 15/10/31.
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

    }

}


