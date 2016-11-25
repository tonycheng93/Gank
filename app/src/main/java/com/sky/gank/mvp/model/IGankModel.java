package com.sky.gank.mvp.model;

import com.sky.gank.mvp.model.impl.GankModelImpl;

/**
 * Created by tonycheng on 2016/11/24.
 */

public interface IGankModel {

    void loadAndroidList(int count, int page,
                         GankModelImpl.OnLoadAndroidListListener listener);
}
