package com.sky.gank.http;

import com.sky.gank.App;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by tonycheng on 2016/11/26.
 */

public class HttpCache {

    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 10;//10M

    //缓存路径->Android中推荐使用CacheDir存放缓存
    private static File cacheFile = new File(App.getContext().getCacheDir(), "eyepetizer_cache");

    public static Cache getCache() {
        return new Cache(cacheFile, MAX_CACHE_SIZE);
    }
}
