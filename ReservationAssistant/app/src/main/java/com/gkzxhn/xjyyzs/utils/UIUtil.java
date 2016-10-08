package com.gkzxhn.xjyyzs.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * Author: Huang ZN
 * Date: 2016/10/8
 * Email:943852572@qq.com
 * Description:u相关工具类
 */

public class UIUtil {

    /**
     * show进队条对话框
     * @param context
     * @param title
     * @param msg
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String title,
                                                    String msg, boolean cancelable){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        if(!TextUtils.isEmpty(title))
            dialog.setTitle(title);
        if(!TextUtils.isEmpty(msg))
            dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }
}
