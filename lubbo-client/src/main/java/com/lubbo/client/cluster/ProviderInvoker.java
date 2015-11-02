package com.lubbo.client.cluster;

import java.util.concurrent.ExecutionException;

import com.lubbo.client.network.Exchanger;
import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.Result;

/**
 * Created by benchu on 15/11/1.
 */
public class ProviderInvoker implements Invoker{
    private Exchanger<Invocation,Result> exchanger;

    public Exchanger<Invocation, Result> getExchanger() {
        return exchanger;
    }

    public void setExchanger(Exchanger<Invocation, Result> exchanger) {
        this.exchanger = exchanger;
    }

    //fix me
    @Override
    public Result invoke(Invocation invocation) {

        try {
            return exchanger.sendMessage(invocation).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
