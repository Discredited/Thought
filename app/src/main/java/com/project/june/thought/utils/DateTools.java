package com.project.june.thought.utils;

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
}
