package com.lubbo.core;

/**
 * 生命周期接口
 * Created by benchu on 15/10/10.
 */

public interface LifeCycle {
    /**
     * 生命周期停止,销毁相关资源
     */
    void stop();

    /**
     * 生命周期启动,初始化参数
     */
    void start();

    /**
     * 获得当前的运行状态
     *
     * @return
     */
    boolean isStart();

}
