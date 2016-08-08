package com.gkzxhn.xjyyzs.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/22.
 * function:日期工具类
 */

public class DateUtils {

    /**
     * 返回当前日之后的n天日期(yyyy-MM-dd),过滤掉周末的
     * @param n
     * @return
     */
    public static List<String> afterNDay(int n){
        List<String> list = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            Calendar c = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.DATE, i);
            Date d2 = c.getTime();
            String s = df.format(d2);
            if(!(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && !(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 格式化日期
     * @param format  格式化后的格式
     * @param ms
     * @return
     */
    public static String formatDate(String format, long ms){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(ms);
        return simpleDateFormat.format(date);
    }
}
