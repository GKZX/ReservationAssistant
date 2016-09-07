package com.gkzxhn.xjyyzs.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.SystemUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/20.
 * function:启动页面
 */

public class LaunchActivity extends BaseActivity {

    private static final String TAG = "LaunchActivity";
    @BindView(R.id.tv_version) TextView tv_version;
    @BindView(R.id.rl_splash) RelativeLayout rl_splash;

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_launch, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        Log.d(TAG, "user token : " + SPUtil.get(this, "token", ""));
        removeTitleBar();
        // 设置版本号
        tv_version.setText("V " + SystemUtil.getVersionName(this));
        // playLaunchAnimation
        playLaunchAnimation();
//        test();
    }

    private void test() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.56.1:8080/hello-world/name=world")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().string());
            }
        });
    }

    /**
     * 播放启动动画
     */
    private void playLaunchAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 1.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        rl_splash.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
                Log.i(TAG, "launch animation start");
            }

            @Override public void onAnimationEnd(Animation animation) {
                //  动画结束进入登录页或者主页
                String username = (String) SPUtil.get(LaunchActivity.this, "userid", "");
                String password = (String) SPUtil.get(LaunchActivity.this, "token", "");
                Intent intent;
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    // 登录过直接进入主页  主页会判断是否自动登录成功
                    intent = new Intent(LaunchActivity.this, MainActivity.class);
                }else {
                    // 否则进入登录页
                    intent = new Intent(LaunchActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                LaunchActivity.this.finish();
                Log.i(TAG, "launch animation end");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "launch animation repeat");
            }
        });
    }
}
