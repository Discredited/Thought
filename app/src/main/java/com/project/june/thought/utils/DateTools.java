package com.project.june.thought.utils;

import java.util.Calendar;

/**
 * 日期工具类
 * Created by June on 2017/3/25.
 */
public class DateTools {

    private static String[] monthList = {"Jan.", "Feb.", "Mar.", "Apr.", "May.", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

    //日期转换  返回 Jan. 类型的月份
    public static String monthType(int month) {
        return monthList[month];
    }

    //输入YYYY-MM-DD 返回 YYYY / MM / DD 类型
    public static String dateString(String date){
        String[] split = date.split("-");
        if (split.length == 0){
            return "";
        }else if (split.length == 3){
            return split[0] + "    /    " + split[1] + "    /    " + split[2];
        }
        return "";
    }

    //输入YYYY-MM-DD hh:mm:ss
    public static String allDateString(String date){
        String[] strings = date.split(" ");
        if (strings.length == 2){
            String[] split = strings[0].split("-");
            if (split.length == 0){
                return "";
            }else if (split.length == 3){
                return split[0] + "    /    " + split[1] + "    /    " + split[2];
            }
            return "";
        }else {
            return "";
        }
    }

    public static String getCommentDate(String date){
        if (null != date){
            String[] split = date.split(" ");
            String string = split[0];
            String[] strings = string.split("-");
            if (null != strings && strings.length > 0){
                return  strings[2] + "  " +monthList[Integer.parseInt(strings[1]) - 1] + strings[0] + "    " + split[1];
            }
            return "";
        }else {
            return "";
        }
    }

    public static String getNowDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}
