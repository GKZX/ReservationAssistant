package com.gkzxhn.xjyyzs.requests.methods;

import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/31.
 * description:获取当天会见列表
 */

public class GetCurrentDayListMethod {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private ApiService apiService;

    /**
     * 构造私有
     */
    private GetCurrentDayListMethod() {
        // 创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.URL_HEAD)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 创建单例
     */
    private static class SingletonHolder{
        private static final GetCurrentDayListMethod INSTANCE = new GetCurrentDayListMethod();
    }

    /**
     * 获取单例
     * @return
     */
    public static GetCurrentDayListMethod getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取当天会见列表
     * @param subscriber
     * @param token
     * @param orgCode
     */
    public void getCurrentDayList(Subscriber<ApplyResult> subscriber, String token, String orgCode){
        apiService.getCurrentDayData(token, orgCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
