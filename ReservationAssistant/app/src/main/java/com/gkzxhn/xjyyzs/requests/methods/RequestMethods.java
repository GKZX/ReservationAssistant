package com.gkzxhn.xjyyzs.requests.methods;

import android.content.Context;

import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;
import com.gkzxhn.xjyyzs.requests.bean.SearchResultBean;
import com.gkzxhn.xjyyzs.requests.bean.UpdateInfo;
import com.gkzxhn.xjyyzs.utils.SPUtil;

import java.util.Map;

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
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
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
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .changePwd(token, token, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 检查更新
     * @param subscriber
     */
    public static void checkUpdate(Subscriber<UpdateInfo> subscriber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://103.37.158.17:8080/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .updateCheck()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取查询结果
     * @param context
     * @param map
     * @param subscriber
     */
    public static void getSearchResult(Context context, Map<String, String>
            map, Subscriber<SearchResultBean> subscriber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService searchByTime = retrofit.create(ApiService.class);
        String token  = SPUtil.get(context, "token", "") + "";
        String orgCode = SPUtil.get(context, "organizationCode", "") + "";
        searchByTime
                .searchByTime(token, map, orgCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
