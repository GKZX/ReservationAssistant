package com.gkzxhn.xjyyzs.fragments;

import android.view.View;
import android.widget.ImageView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:消息
 */
public class MsgFragment extends BaseFragment {

    @BindView(R.id.iv_no_msg) ImageView iv_no_msg;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_msg, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
    }
}
