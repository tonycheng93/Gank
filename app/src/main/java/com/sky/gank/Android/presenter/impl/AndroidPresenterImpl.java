package com.sky.gank.Android.presenter.impl;

import com.sky.gank.Android.model.IAndroidModel;
import com.sky.gank.Android.model.impl.AndroidModelImpl;
import com.sky.gank.Android.presenter.IAndroidPresenter;
import com.sky.gank.Android.view.IAndroidView;
import com.sky.gank.entity.GankEntity;

import java.util.List;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class AndroidPresenterImpl implements IAndroidPresenter,
        AndroidModelImpl.OnLoadAndroidListListener {

    private IAndroidView mAndroidView;
    private IAndroidModel mAndroidModel;

    public AndroidPresenterImpl(IAndroidView androidView) {
        mAndroidView = androidView;
        mAndroidModel = new AndroidModelImpl();
    }

    @Override
    public void loadAndroidList(int count, int page) {
        mAndroidView.showLoading();
        mAndroidModel.loadAndroidList(count, page, this);
    }

    @Override
    public void onSuccess(List<GankEntity> androidList) {
        mAndroidView.hideLoading();
        mAndroidView.addAndroid(androidList);
    }

    @Override
    public void onFail(Throwable e) {
        mAndroidView.hideLoading();
        mAndroidView.showLoadFailMsg();
    }
}
