package com.gkzxhn.xjyyzs.activities;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.LoginInfo;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;
import com.gkzxhn.xjyyzs.service.LoginNimService;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.ToastUtil;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:login
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.btn_login) Button btn_login;

    private String username;
    private String password;
    private SweetAlertDialog loginDialog;

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_staff_loading, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        setTitleText("登录");
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    ToastUtil.showShortToast(this, "用户名或密码为空");
                }else if(password.length() < 6){
                    ToastUtil.showShortToast(this, "密码不能少于六位");
                }else {
                    // 登录
                    doLogin();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void doLogin() {
        loginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText("正在登录...");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService login = retrofit.create(ApiService.class);
        LoginInfo info = new LoginInfo();
        LoginInfo.LoginBean bean = info.new LoginBean();
        bean.setUserid(username);
        bean.setPassword(password);
        info.setSession(bean);
        Log.i(TAG, "login info : " + info.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(info));
        login.login(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "login failed : " + e.getMessage());
                        showLoginFailed();
                    }

                    @Override
                    public void onNext(LoginResult result) {
                        Log.i(TAG, "login success : " + result.toString());
                        saveUserInfo(result);// save
                        loginNim();// 登录云信
                        showLoginSuccess();// 登录成功
                    }
                });
    }

    /**
     * 保存用户信息
     * @param result
     */
    private void saveUserInfo(LoginResult result) {
        SPUtil.put(LoginActivity.this, "userid", result.getUser().getUserid());
        SPUtil.put(LoginActivity.this, "password", password);
        SPUtil.put(LoginActivity.this, "token", result.getUser().getToken());
        SPUtil.put(LoginActivity.this, "name", result.getUser().getName());
        SPUtil.put(LoginActivity.this, "cloudToken", result.getUser().getCloudMsg().getToken());
        SPUtil.put(LoginActivity.this, "cloudId", result.getUser().getCloudMsg().getCloudID());
        SPUtil.put(LoginActivity.this, "title", result.getUser().getOrgnization().getTitle());
        SPUtil.put(LoginActivity.this, "organizationCode", result.getUser().getOrgnization().getCode());
    }

    /**
     * 登录成功
     */
    private void showLoginSuccess() {
        loginDialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
        loginDialog.setTitleText("登录成功").setConfirmText("确定").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginDialog.dismiss();
                toNext();
            }
        }, 1000);
    }

    /**
     * 登录云信
     */
    private void loginNim() {
        Intent intent = new Intent(LoginActivity.this, LoginNimService.class);
        startService(intent);
    }

    /**
     * 下一步 进入主页
     */
    private void toNext() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /**
     * 登录失败
     */
    private void showLoginFailed() {
        loginDialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        loginDialog.setTitleText("登录失败，请稍后再试！")
                .setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        loginDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }
}
