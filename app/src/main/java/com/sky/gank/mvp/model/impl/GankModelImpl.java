package com.sky.gank.mvp.model.impl;

import com.sky.gank.entity.GankEntity;
import com.sky.gank.http.ApiService;
import com.sky.gank.http.HttpResult;
import com.sky.gank.http.HttpResultSubscriber;
import com.sky.gank.http.RetrofitClient;
import com.sky.gank.mvp.model.IGankModel;
import com.sky.gank.utils.Debugger;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class GankModelImpl implements IGankModel {

    @Override
    public void loadGankList(String category,int count, int page, final OnloadGankListListener listener) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();

        Observable<HttpResult<List<GankEntity>>> androidList = apiService.getGankList(category, count, page);
        androidList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<List<GankEntity>>() {
                    @Override
                    public void onSuccess(HttpResult<List<GankEntity>> result) {
                        Debugger.d(result.getResults().toString());
                        listener.onSuccess(result.getResults());
                    }

                    @Override
                    public void onFail(Throwable e) {
                        listener.onFail(e);
                    }
                });
    }

    public interface OnloadGankListListener {
        void onSuccess(List<GankEntity> gankList);

        void onFail(Throwable e);
    }
}
