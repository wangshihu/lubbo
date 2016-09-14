package com.lubbo.core;

import com.lubbo.core.network.MsgHandlerContext;

/**
 * @author  benchu
 * @version on 15/10/25.
 */
public interface MsgHandler<I,O> {

     void messageReceived(I message, MsgHandlerContext<O> ctx) ;
}
