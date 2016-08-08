package com.gkzxhn.xjyyzs.requests;


import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:相关api请求服务
 */

public interface ApiService {

    /**
     * 登录
     * @param body
     * @return
     */
    @POST("login")
    Observable<LoginResult> login(
            @Body RequestBody body
    );

    /**
     * 获取当天申请结果列表
     * @param orgCode
     * @return
     */
    @GET("applies")
    Observable<ApplyResult> getCurrentDayData(
            @Header("Authorization") String token,
            @Query("orgCode") String orgCode
    );

}
