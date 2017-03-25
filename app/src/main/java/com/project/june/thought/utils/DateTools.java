package com.project.june.thought.utils;

/**
 * 日期工具类
 * Created by June on 2017/3/25.
 */
public class DateTools {

    private static String[] monthList = {"Jan.", "Feb.", "Mar.", "Apr.", "May.", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

    //日期转换
    public static String monthType(int month) {
        return monthList[month];
    }

}
