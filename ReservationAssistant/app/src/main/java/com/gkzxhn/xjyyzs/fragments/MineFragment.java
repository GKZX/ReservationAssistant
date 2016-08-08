package com.gkzxhn.xjyyzs.fragments;

import android.view.View;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:我的
 */
public class MineFragment extends BaseFragment {

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

    }
}
