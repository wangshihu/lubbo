package com.lubbo.core;

import com.lubbo.core.network.MsgHandlerContext;

/**
 * Created by benchu on 15/10/25.
 */
public interface MsgHandler<I,O> {

     void execute(I message, MsgHandlerContext<O> ctx) ;
}
