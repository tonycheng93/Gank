package com.sky.gank.Android.view;

import com.sky.gank.entity.GankEntity;

import java.util.List;

/**
 * Created by tonycheng on 2016/11/24.
 */

public interface IAndroidView {

    void showLoading();

    void addAndroid(List<GankEntity> androidList);

    void hideLoading();

    void showLoadFailMsg();
}
