package com.gkzxhn.xjyyzs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.fragments.HomeFragment;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;

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
    private FragmentManager manager;
    private FragmentTransaction transaction = null;
    private HomeFragment homeFragment = null;

    private long mExitTime;//add by hzn 退出按键时间间隔

    private AlertDialog logout_dialog;// 注销对话框

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_main, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        setSupportActionBar(tool_bar);
        setTitleText("预约助手");
        initFragment();
        checkOnlineStatus();// 检查云信id在线状态
    }

    /**
     * 注销对话框
     */
    private void showLogoutDialog() {
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

    /**
     * 检查在线状态
     */
    private void checkOnlineStatus() {
        StatusCode code = NIMClient.getStatus();
        Log.i(TAG, code.name());
        if(code == StatusCode.KICKOUT){
            //  弹出提示框  重新登录
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
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.replace(R.id.fl_container, homeFragment);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout){
            showLogoutDialog();
        }else if(id == R.id.change_pwd){
            Intent intent = new Intent(this, ChangePwdActivity.class);
            startActivity(intent);
        }else if(id == R.id.change_phone){
            Intent intent = new Intent(this, SetWorkerPhoneActivity.class);
            startActivity(intent);
        }else if(id == R.id.update){
            checkUpdate();// 检查更新
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        showToastShortMsg("正在检查...");
    }
}
