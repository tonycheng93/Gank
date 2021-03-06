package com.sky.gank.http;

import com.sky.gank.entity.GankEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tonycheng on 2016/11/24.
 */

public interface ApiService {

    //http://gank.io/api/data/Android/10/1
    @Headers("Cache-Control: public, max-age=3600")
    @GET("data/{category}/{count}/{page}")
    Observable<HttpResult<List<GankEntity>>> getGankList(
            @Path("category") String category,
            @Path("count") int count,
            @Path("page") int page);
}
