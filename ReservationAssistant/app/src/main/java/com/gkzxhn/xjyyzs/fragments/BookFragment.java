package com.gkzxhn.xjyyzs.fragments;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:给家属预约
 */
public class BookFragment extends BaseFragment {

    private static final String[] DATE_LIST = DateUtils.afterNDay(30).toArray(new String[DateUtils.afterNDay(30).size()]);// 时间选择;

    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_ic_card_number) EditText et_ic_card_number;
    @BindView(R.id.sp_date) Spinner sp_date;
    @BindView(R.id.bt_remote_meeting) Button bt_remote_meeting;
    @BindView(R.id.bt_fact_meeting) Button bt_fact_meeting;

    private ArrayAdapter<String> date_adapter;// 预约日期适配器

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_book, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        date_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, DATE_LIST);
        sp_date.setAdapter(date_adapter);
    }
}
