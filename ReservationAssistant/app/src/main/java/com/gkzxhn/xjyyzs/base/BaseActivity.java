package com.gkzxhn.xjyyzs.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.utils.ToastUtil;


/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:Activity基类、所有Activity须继承此类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private View ly_title_bar;
    private RelativeLayout rl_content;// 标题以下内容布局
    public Toolbar tool_bar; // toolbar
    private TextView tv_title;// 标题
    private ImageButton ib_back;// 返回
//    protected Button bt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.root_view);
        ly_title_bar = findViewById(R.id.ly_title_bar);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
//        bt_logout = (Button) findViewById(R.id.bt_logout);
        View view = initView();// 初始化view
        if(rl_content.getChildCount() != 0) {
            rl_content.removeAllViews();
        }
        rl_content.addView(view);
        initData();// 初始化数据操作
    }

    /**
     * 子类必须重写
     * @return
     */
    public abstract View initView();

    protected abstract void initData();

    /**
     * 隐藏标题栏
     */
    public void removeTitleBar(){
        ly_title_bar.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitleText(String title){
        tv_title.setText(title);
    }

    /**
     * 设置标题
     * @param title_ResID
     */
    public void setTitleText(int title_ResID){
        tv_title.setText(title_ResID);
    }

    /**
     * 设置注销是否可见
     * @param visibility
     */
//    public void setLogoutVisibility(int visibility){
//        bt_logout.setVisibility(visibility);
//    }

    /**
     * 显示返回按钮
     */
    protected void setBackVisibility(){
        ib_back.setVisibility(View.VISIBLE);
    }

    /**
     * 设置返回按钮监听
     * @param listener
     */
    protected void setBackPressListener(View.OnClickListener listener){
        ib_back.setOnClickListener(listener);
    }

    /**
     * short toast
     * @param msg
     */
    protected void showToastShortMsg(String msg){
        ToastUtil.showShortToast(this, msg);
    }

    /**
     * long toast
     * @param msg
     */
    protected void showToastLongMsg(String msg){
        ToastUtil.showLongToast(this, msg);
    }
}
