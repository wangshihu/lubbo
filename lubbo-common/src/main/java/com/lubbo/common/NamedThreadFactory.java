/*
 * Copyright 2011-2014 Mogujie Co.Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lubbo.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * InternalThreadFactory.copy from dubbo
 * 
 * @author qian.lei
 * @author zhaoxi@mogujie.com
 */

public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemo;

    private final ThreadGroup threadGroup;

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        if (null == prefix || prefix.isEmpty()) {
            throw new NullPointerException("Your prefix is Blank!");
        }
        final String suffix = "-thread-";
        this.prefix = new StringBuilder(prefix.length() + suffix.length()).append(prefix).append(suffix).toString();
        this.daemo = daemon;
        final SecurityManager s = System.getSecurityManager();
        this.threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();

    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + threadNum.getAndIncrement();
        Thread th = new Thread(threadGroup, runnable, name, 0);
        th.setDaemon(daemo);
        return th;
    }

    /**
     * @return the threadNum
     */
    public AtomicInteger getThreadNum() {
        return threadNum;
    }

    /**
     * @return the threadGroup
     */
    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

}
