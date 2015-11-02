package com.lubbo.client.network;

import com.lubbo.client.cluster.Provider;

/**
 * Created by benchu on 15/11/1.
 */
public interface ExchangerFactory <I,O>{
     Exchanger<I, O> newExchanger(Provider provider) ;
}
