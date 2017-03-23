package com.project.june.thought.utils;

/**
 * Created by June on 2017/3/10.
 */
public class HttpUtils {

    public static String TAG = "sherry";

    public static String HEADER = "http://v3.wufazhuce.com:8000/api";

    //首页数据列表(10条)
    public static String INDEX_LIST = HEADER + "/onelist/idlist/?version=4.0.7&platform=android";
    //首页数据详情
    public static String INDEX = HEADER + "/onelist/3827/%E6%88%90%E9%83%BD?version=4.0.7&platform=android";
}
