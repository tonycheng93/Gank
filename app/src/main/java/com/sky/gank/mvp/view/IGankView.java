package com.sky.gank.mvp.view;

import com.sky.gank.entity.GankEntity;

import java.util.List;

/**
 * Created by tonycheng on 2016/11/24.
 */

public interface IGankView {

    void showLoading();

    void addGank(List<GankEntity> gankList);

    void hideLoading();

    void showLoadFailMsg();
}
