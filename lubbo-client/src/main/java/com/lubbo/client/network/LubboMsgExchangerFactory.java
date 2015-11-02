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
package com.lubbo.client.network;

import com.lubbo.client.cluster.Provider;
import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.network.Channel;
import com.lubbo.core.network.Client;

/**
 * @author benchu
 *
 */
public class LubboMsgExchangerFactory implements ExchangerFactory<Invocation, Result> {

    protected Client client;

    protected ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> responseFutureFactory;

    public LubboMsgExchangerFactory() {
    }

    @Override
    public Exchanger<Invocation, Result> newExchanger(Provider provider) {
        Channel channel = this.client.connect(provider.getAddress());
        LubboExchanger exchanger = new LubboExchanger(provider,channel,responseFutureFactory);

        return exchanger;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setResponseFutureFactory(
            ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> responseFutureFactory) {
        this.responseFutureFactory = responseFutureFactory;
    }

}
