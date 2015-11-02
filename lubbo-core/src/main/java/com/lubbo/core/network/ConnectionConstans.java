package com.lubbo.core.network;

/**
 * Created by benchu on 15/11/1.
 */
public final class ConnectionConstans {
    /**
     * 私有构造函数
     */
    private ConnectionConstans() {
    }

    /**
     * 网络事件派发线程数
     */
    public static final int DEFAULT_BOSS_THREADS = 1;

    public static final int DEFAULT_WORKER_THREADS = Runtime.getRuntime().availableProcessors();

    public static final int DEFAULT_CONNECT_TIMEOUT_MILLS = 1000;

    public static final int IDEL_TIME_OUT = 80;

    public static final int DEFAULT_READ_IDEL_TIME_OUT = 40;

    public static final int DEFAULT_WRITE_IDEL_TIME_OUT = 20;

    /**
     * 空余读,写默认时间.
     */
    public static   int DEFAULT_READTIMEOUT=0;

    public static  int DEFAULT_WRITETIMEOUT=10;

    public static  int DEFAULT_IDLETIMEOUT=0;
}
