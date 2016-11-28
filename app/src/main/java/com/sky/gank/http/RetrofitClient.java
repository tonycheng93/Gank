package com.sky.gank.http;

import com.sky.gank.App;
import com.sky.gank.config.Constant;
import com.sky.gank.utils.Debugger;
import com.sky.gank.utils.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";

    private static final int DEFAULT_CONNECT_TIMEOUT = 5;
    private static final int DEFAULT_READ_TIMEOUT = 5;

    private static volatile RetrofitClient instance = null;


    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //方案一：无论有网还是没网，都先读缓存
//            Request request = chain.request();
//            Debugger.d(TAG, "request = " + request);
//            Response response = chain.proceed(request);
//            Debugger.d(TAG, "response = " + response);
//            String cacheControl = request.cacheControl().toString();
//            if (!TextUtils.isEmpty(cacheControl)) {
//                cacheControl = "public, max-age=60";
//            }
//            return response.newBuilder()
//                    .header("Cache-Control",cacheControl)
//                    .removeHeader("Pragma")
//                    .build();

            //方案二：无网读缓存，有网根据过期时间重新请求
            boolean networkConnection = NetworkUtil.hasNetworkConnection(App.getContext());
            Request request = chain.request();
            if (!networkConnection) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (networkConnection) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                Debugger.d(TAG, "cacheControl = " + cacheControl);
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };

    private static Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Debugger.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Debugger.d(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    };



    //私有化构造函数
    private RetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                //设置拦截器
                .addInterceptor(LoggingInterceptor)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(HttpCache.getCache())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
