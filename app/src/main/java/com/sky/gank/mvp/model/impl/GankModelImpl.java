package com.sky.gank.mvp.model.impl;

import com.sky.gank.mvp.model.IGankModel;
import com.sky.gank.config.Constant;
import com.sky.gank.entity.GankEntity;
import com.sky.gank.http.ApiService;
import com.sky.gank.http.HttpResult;
import com.sky.gank.http.HttpResultSubscriber;
import com.sky.gank.http.RetrofitClient;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class GankModelImpl implements IGankModel {

    @Override
    public void loadAndroidList(int count, int page, final OnLoadAndroidListListener listener) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();

        Observable<HttpResult<List<GankEntity>>> androidList = apiService.getGankList(Constant.CATEGORY_ANDROID, count, page);
        androidList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<List<GankEntity>>() {
                    @Override
                    public void onSuccess(HttpResult<List<GankEntity>> result) {
                        listener.onSuccess(result.getResults());
                    }

                    @Override
                    public void onFail(Throwable e) {
                        listener.onFail(e);
                    }
                });
    }

    public interface OnLoadAndroidListListener {
        void onSuccess(List<GankEntity> androidList);

        void onFail(Throwable e);
    }
}
