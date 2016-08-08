package com.gkzxhn.xjyyzs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.fragments.HomeFragment;
import com.gkzxhn.xjyyzs.fragments.MineFragment;
import com.gkzxhn.xjyyzs.fragments.MsgFragment;
import com.gkzxhn.xjyyzs.utils.DensityUtil;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:主Activity
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fl_container) FrameLayout fl_container;
    @BindView(R.id.rg_main_tabs) RadioGroup rg_main_tabs;
    @BindView(R.id.rb_main_home) RadioButton rb_main_home;
    @BindView(R.id.rb_main_msg) RadioButton rb_main_msg;
//    @BindView(R.id.rb_main_mine) RadioButton rb_main_mine;
    private List<BaseFragment> fragments = new ArrayList<>();
    private FragmentManager manager;
    private FragmentTransaction transaction = null;
    private HomeFragment homeFragment = null;
    private MsgFragment msgFragment = null;
    private MineFragment mineFragment = null;

    private long mExitTime;//add by hzn 退出按键时间间隔

    private AlertDialog logout_dialog;// 注销对话框

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_main, null);
        ButterKnife.bind(this, view);
        Drawable[] drawables = rb_main_home.getCompoundDrawables();
        drawables[1].setBounds(0, DensityUtil.dip2px(getApplicationContext(), 5), 60, 75);
        rb_main_home.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        Drawable[] drawables2 = rb_main_msg.getCompoundDrawables();
        drawables2[1].setBounds(0, DensityUtil.dip2px(getApplicationContext(), 5), 60, 75);
        rb_main_msg.setCompoundDrawables(drawables2[0], drawables2[1], drawables2[2], drawables2[3]);
//        Drawable[] drawables3 = rb_main_mine.getCompoundDrawables();
//        drawables3[1].setBounds(0, DensityUtil.dip2px(getApplicationContext(), 5), 60, 75);
//        rb_main_mine.setCompoundDrawables(drawables3[0], drawables3[1], drawables3[2], drawables3[3]);
        return view;
    }

    @Override
    protected void initData() {
        setTitleText("主页");
        setLogoutVisibility(View.VISIBLE);
        setLogoutClickedListener();
        initFragment();
        checkOnlineStatus();// 检查云信id在线状态
        rg_main_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_main_home: // home
                        switchFragment(0);
                        setTitleText("首页");
                        break;
                    case R.id.rb_main_msg:// msg
                        switchFragment(1);
                        setTitleText("消息");
                        break;
//                    case R.id.rb_main_mine:// mine
//                        switchFragment(2);
//                        setTitleText("我的");
//                        break;
                }
            }
        });
    }

    /**
     * 设置注销点击事件
     */
    private void setLogoutClickedListener() {
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View logout_dialog_view = View.inflate(MainActivity.this, R.layout.msg_ok_cancel_dialog, null);
                builder.setView(logout_dialog_view);
                TextView tv_cancel = (TextView) logout_dialog_view.findViewById(R.id.tv_cancel);
                final AlertDialog dialog = builder.create();
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView tv_ok = (TextView) logout_dialog_view.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        SPUtil.clear(MainActivity.this);
                        startActivity(intent);
                        NIMClient.getService(AuthService.class).logout();
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * 检查在线状态
     */
    private void checkOnlineStatus() {
        StatusCode code = NIMClient.getStatus();
        Log.i(TAG, code.name());
        if(code == StatusCode.KICKOUT){
            // ToDo 弹出提示框  重新登录
        }
        String isReLogin = (String) SPUtil.get(MainActivity.this, "relogin", "false");
        if(isReLogin.equals("true")){
            showReLoginDialog();
        }
    }

    /**
     * 显示重新登录的提示框
     */
    private void showReLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("账号下线提示");
        builder.setCancelable(false);
        builder.setMessage("您的账号" + SPUtil.get(this, "username", "") + "在其他设备登录，点击重新登录。");
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SPUtil.put(getApplicationContext(), "relogin", false);
                NIMClient.getService(AuthService.class).logout();
            }
        });
        logout_dialog = builder.create();
        logout_dialog.show();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fragments.clear();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if(homeFragment == null) {
            homeFragment = new HomeFragment();
            fragments.add(homeFragment);
        }
        transaction.add(R.id.fl_container, homeFragment);
        if(msgFragment == null) {
            msgFragment = new MsgFragment();
            fragments.add(msgFragment);
        }
        transaction.add(R.id.fl_container, msgFragment);
//        if(mineFragment == null) {
//            mineFragment = new MineFragment();
//            fragments.add(mineFragment);
//        }
//        transaction.add(R.id.fl_container, mineFragment);
        transaction.show(homeFragment).hide(msgFragment)
//                .hide(mineFragment)
        ;
        transaction.commitAllowingStateLoss();
    }

    /**
     * 切换fragment
     * @param index
     */
    private void switchFragment(int index){
        transaction = manager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if(index == i) {
                transaction.show(fragments.get(index));
            }else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if(logout_dialog != null){
            if(logout_dialog.isShowing())
                logout_dialog.dismiss();
            logout_dialog = null;
        }
        super.onDestroy();
    }
}
