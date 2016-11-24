package com.sky.gank.Android.model;

import com.sky.gank.Android.model.impl.AndroidModelImpl;

/**
 * Created by tonycheng on 2016/11/24.
 */

public interface IAndroidModel {

    void loadAndroidList(int count, int page,
                         AndroidModelImpl.OnLoadAndroidListListener listener);
}
