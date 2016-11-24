package com.sky.gank.http;

import com.sky.gank.utils.Debugger;

import rx.Subscriber;

/**
 * Created by tonycheng on 2016/11/24.
 */

public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onNext(HttpResult<T> tHttpResult) {
        if (!tHttpResult.isError()) {
            Debugger.d(tHttpResult.getResults().toString());
            onSuccess(tHttpResult);
        } else {
            onFail(new Throwable("error = " + tHttpResult.isError()));
        }
    }

    @Override
    public void onCompleted() {

    }

    public abstract void onSuccess(HttpResult<T> result);

    public abstract void onFail(Throwable throwable);
}
