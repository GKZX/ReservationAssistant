package com.gkzxhn.xjyyzs.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/22.
 * function:字符串工具类
 */

public class StringUtils {

    /**
     * 判断是否是手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-3,5-9])|(14[5,7])|(17[0,6-8])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
