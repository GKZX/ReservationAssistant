package com.gkzxhn.xjyyzs.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.DateUtils;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.PhoneNumberUtil;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.StringUtils;
import com.gkzxhn.xjyyzs.utils.UIUtil;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;

import java.io.IOException;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:给家属预约
 */
public class BookFragment extends BaseFragment {

    private static final String[] DATE_LIST = DateUtils.afterNDay(30).
            toArray(new String[DateUtils.afterNDay(30).size()]);// 时间选择;
    private static final String TAG = "BookFragment";

    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_ic_card_number) EditText et_ic_card_number;
    @BindView(R.id.sp_date) Spinner sp_date;
    @BindView(R.id.bt_remote_meeting) Button bt_remote_meeting;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.ll_added) LinearLayout ll_added;
    @BindView(R.id.ll_added_item) LinearLayout ll_added_item;
    @BindView(R.id.add) ImageView add;

    private ArrayAdapter<String> date_adapter;// 预约日期适配器
    private String name;// 姓名
    private String phone; // 电话号码
    private String uuid; // 身份证
    private String apply_date; // 申请日期
    private SweetAlertDialog apply_dialog; // 进度条对话框

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

    /**
     * 申请
     */
    private void apply() {
        initAndShowDialog();// 进度条对话框
        RequestMethods.bookMeeting((String) SPUtil.get(getActivity(), "token", ""),
                UIUtil.getRequestBody(getActivity(), phone, uuid, apply_date), new Subscriber<ResponseBody>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) {
                        String error = e.getMessage();
                        Log.e(TAG, "apply failed : " + error);
                        if (error.contains("400")) {
                            showApplyFailedDialog("已申请过该日，请勿重复申请");
                        } else if (error.contains("404")) {
                            showApplyFailedDialog("抱歉，没有权限");
                        } else if (error.contains("500")) {
                            showApplyFailedDialog("服务器错误");
                        } else if (error.contains("302")) {
                            showApplyFailedDialog("该日司法所会见已满");
                        } else {
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
                if (apply_dialog.isShowing()) {
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
     * 检查输入框
     *
     * @return
     */
    private boolean checkEditText() {
        name = et_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        uuid = et_ic_card_number.getText().toString().trim();
        apply_date = DATE_LIST[sp_date.getSelectedItemPosition()];
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(uuid) || TextUtils.isEmpty(apply_date)) {
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.add, R.id.bt_remote_meeting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                // 添加预约条目
                showAddItemDialog();
                break;
            case R.id.bt_remote_meeting:
                checkText(et_ic_card_number.getText().toString().trim());// 检查文本
                break;
        }
    }

    /**
     * 检查输入框文本
     */
    private void checkText(String uuid) {
        if (!checkEditText()) {
            showToastMsgShort("请填写完整信息");
        } else try {
            PhoneNumberUtil.PhoneType type = PhoneNumberUtil.checkNumber(phone).getType();
            if (type == PhoneNumberUtil.PhoneType.INVALIDPHONE) {
                // 不是手机或者固话
                showToastMsgShort("电话号码不合法");
            } else if (!StringUtils.IDCardValidate(uuid).equals("")) {
                showToastMsgShort("身份证号不合法");
            } else {
                apply();// 申请
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showToastMsgShort("身份证号不合法");
        }
    }

    /**
     * 显示对话框  添加预约的人数
     */
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.add_item_dialog, null);
        builder.setView(view);
        final EditText et_name = (EditText) view.findViewById(R.id.et_name);
        final EditText et_ic_card_number = (EditText) view.findViewById(R.id.et_ic_card_number);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = UIUtil.checkInfoComplete(et_name, et_ic_card_number, et_phone);
                Log.i(TAG, "check result : " + result);
                if (result.equals("")){
                    // check全通过  添加成功
                    String name = et_name.getText().toString().trim();
                    String ic_card_number = et_ic_card_number.getText().toString().trim();
                    addItem(name, ic_card_number);
                    dialog.dismiss();
                }else {
                    showToastMsgShort(result);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 添加item
     * @param name
     * @param ic_card_number
     */
    private void addItem(String name, String ic_card_number) {
        final View view = View.inflate(context, R.layout.add_item, null);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_added_item.removeView(view);
                if (ll_added_item.getChildCount() == 0){
                    ll_added.setVisibility(View.GONE);
                }
            }
        });
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_id_card_number = (TextView) view.findViewById(R.id.tv_id_card_number);
        tv_name.setText(name);
        tv_id_card_number.setText(ic_card_number);
        ll_added.setVisibility(View.VISIBLE);
        ll_added_item.addView(view);
    }
}
