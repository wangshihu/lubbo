package com.lubbo.client.provider;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.lubbo.client.network.Exchanger;
import com.lubbo.client.network.ExchangerFactory;
import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.Result;

/**
 * @author benchu
 * @version 16/9/18.
 */
public class ProviderInvokerFactory implements InvokerFactory<Provider> {
    private ExchangerFactory<Invocation, Result> exchangerFactory;

    @Override
    public Invoker newInvoker(Provider provider) {
        ProviderInvoker invoker = new ProviderInvoker(provider);
        return invoker;
    }

    class ProviderInvoker implements Invoker {
        private Exchanger<Invocation, Result> exchanger;

        public ProviderInvoker(Provider provider) {
            exchanger = exchangerFactory.newExchanger(provider);
        }

        @Override
        public Result invoke(Invocation invocation) {
            //todo fixme
            try {
                return exchanger.send(invocation).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void close() throws IOException {
            exchanger.close();
        }
    }

    public void setExchangerFactory(ExchangerFactory<Invocation, Result> exchangerFactory) {
        this.exchangerFactory = exchangerFactory;
    }
}
