package com.gkzxhn.xjyyzs.fragments;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.requests.bean.Apply;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.DateUtils;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.StringUtils;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:给家属预约
 */
public class BookFragment extends BaseFragment {

    private static final String[] DATE_LIST = DateUtils.afterNDay(30).toArray(new String[DateUtils.afterNDay(30).size()]);// 时间选择;
    private static final String TAG = "BookFragment";

    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_ic_card_number) EditText et_ic_card_number;
    @BindView(R.id.sp_date) Spinner sp_date;
    @BindView(R.id.bt_remote_meeting) Button bt_remote_meeting;

    private ArrayAdapter<String> date_adapter;// 预约日期适配器
    private String name;
    private String uuid;
    private String apply_date;
    private SweetAlertDialog apply_dialog;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_book, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        date_adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, DATE_LIST);
        sp_date.setAdapter(date_adapter);
    }

    @OnClick(R.id.bt_remote_meeting)
    public void onClick(){
        if(!checkEditText()){
            showToastMsgShort("请填写完整信息");
        }else try {
            if(!StringUtils.IDCardValidate(uuid).equals("")){
                showToastMsgShort("身份证号不合法");
            }else {
                apply();// 申请
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showToastMsgShort("身份证号不合法");
        }
    }

    /**
     * 申请
     */
    private void apply() {
        initAndShowDialog();// 进度条对话框
        RequestMethods.bookMeeting((String) SPUtil.get(getActivity(), "token", ""),
                getRequestBody(), new Subscriber<ResponseBody>() {
            @Override public void onCompleted() {}
            @Override public void onError(Throwable e) {
                String error = e.getMessage();
                Log.e(TAG, "apply failed : " + error);
                if(error.contains("400")){
                    showApplyFailedDialog("已申请过该日，请勿重复申请");
                }else if(error.contains("404")){
                    showApplyFailedDialog("抱歉，没有权限");
                }else {
                    showApplyFailedDialog("申请失败，请稍后再试");
                }
            }

            @Override public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.i(TAG, "apply success : " + result);
                    showApplySuccessDialog();// {"msg":"申请提交成功"}
                } catch (IOException e) {
                    e.printStackTrace();
                    showApplyFailedDialog("异常");
                }
            }
        });
    }

    /**
     * 申请成功
     */
    private void showApplySuccessDialog() {
        apply_dialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
        apply_dialog.setTitleText("申请成功").setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        apply_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        delayDismissDialog();
    }

    /**
     * 若用户没有手动点确定  延迟两秒自动dismiss
     */
    private void delayDismissDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(apply_dialog.isShowing()){
                    apply_dialog.dismiss();
                }
            }
        }, 2000);
    }

    /**
     * 申请失败对话框
     */
    private void showApplyFailedDialog(String titleText) {
        apply_dialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        apply_dialog.setTitleText(titleText).setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        apply_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        delayDismissDialog();
    }

    /**
     * 初始化并且显示对话框
     */
    private void initAndShowDialog() {
        apply_dialog = new SweetAlertDialog(getActivity(),
                SweetAlertDialog.PROGRESS_TYPE);
        apply_dialog.setTitleText("正在提交...").setCancelable(false);
        apply_dialog.show();
    }

    /**
     * 获取请求实体类
     * @return
     */
    private RequestBody getRequestBody() {
        Apply apply = new Apply();
        Apply.ApplyBean bean = apply.new ApplyBean();
        bean.setUuid(uuid);
        bean.setOrgCode((String) SPUtil.get(getActivity(), "organizationCode", ""));
        bean.setApplyDate(apply_date);
        apply.setApply(bean);
        String apply_json = new Gson().toJson(apply);
        return RequestBody.create(MediaType.
                parse("application/json; charset=utf-8"),  apply_json);
    }

    /**
     * 检查输入框
     * @return
     */
    private boolean checkEditText() {
        name = et_name.getText().toString().trim();
        uuid = et_ic_card_number.getText().toString().trim();
        apply_date = DATE_LIST[sp_date.getSelectedItemPosition()];
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(uuid) || TextUtils.isEmpty(apply_date)){
            return false;
        }
        return true;
    }
}
