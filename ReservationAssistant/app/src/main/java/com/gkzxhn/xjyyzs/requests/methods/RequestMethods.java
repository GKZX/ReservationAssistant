package com.gkzxhn.xjyyzs.requests.methods;

import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/7.
 * description:请求方法封装类
 */

public class RequestMethods {

    /**
     * 登录
     * @param body
     * @param subscriber
     */
    public static void login(RequestBody body, Subscriber<LoginResult> subscriber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService login = retrofit.create(ApiService.class);
        login
                .login(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 预约会见
     * @param token
     * @param body
     * @param subscriber
     */
    public static void bookMeeting(String token, RequestBody body, Subscriber<ResponseBody> subscriber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .apply(token, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码和设置工作人员手机号都是此方法
     * @param token
     * @param body
     * @param subscriber
     */
    public static void setNumber(String token, RequestBody body, Subscriber<ResponseBody> subscriber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService changePwd = retrofit.create(ApiService.class);
        changePwd
                .changePwd(token, token, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
