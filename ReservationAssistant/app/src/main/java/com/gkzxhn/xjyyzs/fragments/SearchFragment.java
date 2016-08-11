package com.gkzxhn.xjyyzs.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.adapter.SearchResultAdapter;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.inters.OnSearchResultCallBack;
import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.ToastUtil;
import com.gkzxhn.xjyyzs.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
 * function:历史查询
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener, OnSearchResultCallBack {

    private static final String TAG = "SearchFragment";
    @BindView(R.id.rg_status) RadioGroup rg_status;
    @BindView(R.id.rb_passed) RadioButton rb_passed;
    @BindView(R.id.rb_refused) RadioButton rb_refused;
    @BindView(R.id.bt_search_by_status) Button bt_search_by_status;
    @BindView(R.id.tv_start_date) TextView tv_start_date;
    @BindView(R.id.tv_end_date) TextView tv_end_date;
    @BindView(R.id.bt_search_by_time) Button bt_search_by_time;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    private ProgressDialog current_dialog;// 获取当天数据对话框
    private List<ApplyResult.AppliesBean> data;
    private SearchResultAdapter adapter;// 结果列表适配器

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_search, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        bt_search_by_status.setOnClickListener(this);
        bt_search_by_time.setOnClickListener(this);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_date:
                showDatePicker(tv_start_date);
                break;
            case R.id.tv_end_date:
                showDatePicker(tv_end_date);
                break;
            case R.id.bt_search_by_status:
                showToastMsgShort("状态筛选");
                break;
            case R.id.bt_search_by_time:
                showToastMsgShort("时间筛选");
                break;
        }
    }

    /**
     * 显示datePicker
     * @param tv
     */
    private void showDatePicker(final TextView tv) {
        DatePickerDialog start_picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i("start date select ---> ", year + "---" + (monthOfYear + 1) +
                        "---" + (dayOfMonth + 1));
                tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        start_picker.show();
    }

    @Override
    public void getData() {
        if(data == null || data.size() == 0) {
            Log.i(TAG, "go to get data");
            current_dialog = new ProgressDialog(getActivity());
            current_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            current_dialog.setCancelable(false);
            current_dialog.setCanceledOnTouchOutside(false);
            current_dialog.show();
            getCurrentData();
        }else {
            Log.i(TAG, "already has data !");
        }
    }

    /**
     * 获取当天数据
     */
    private void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_HEAD).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getCurrentDayData((String) SPUtil.get(getActivity(), "token", ""), (String) SPUtil.get(getActivity(), "organizationCode", ""))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApplyResult>() {
                    @Override public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "get current day data failed : " + e.getMessage());
                        showGetFailed();// 获取失败
                    }

                    @Override
                    public void onNext(ApplyResult result) {
                        Log.i(TAG, "get data success");
                        data = new ArrayList<>();
                        data.clear();
                        data = result.getApplies();
                        List<ApplyResult.AppliesBean> beanList = data;
                        data.addAll(beanList);
                        for (ApplyResult.AppliesBean bean : data){
                            Log.i(TAG, bean.toString());
                        }
                        setDataList();
                        showGetSuccess();
                    }
                });
    }

    /**
     * 设置列表数据
     */
    private void setDataList() {
        if(adapter == null){
            adapter = new SearchResultAdapter(getActivity(), data);
            recycler_view.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取成功
     */
    private void showGetSuccess() {
        if(current_dialog.isShowing())
            current_dialog.dismiss();
//        showToastMsgShort("加载成功");
    }

    /**
     * 获取失败
     */
    private void showGetFailed() {
        if(current_dialog.isShowing())
            current_dialog.dismiss();
        showToastMsgLong("加载失败，请稍后再试！");
    }
}
