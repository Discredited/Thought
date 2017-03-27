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
    public static String INDEX = HEADER + "/onelist/{0}/%E6%88%90%E9%83%BD?version=4.0.7&platform=android";

    //往期图文列表
    public static String TO_LIST_IMAGE_TEXT = HEADER + "/hp/bymonth/{0}%2000:00:00?version=4.0.7&platform=android";
    //阅读往期列表
    public static String TO_LIST_READING = HEADER + "/essay/bymonth/{0}%2000:00:00?&version=4.0.7&platform=android";
    //连载往期列表
    public static String TO_LIST_SERIALIZE = HEADER + "/serialcontent/bymonth/{0}%2000:00:00?version=4.0.7&platform=android";
    //问答往期列表
    public static String TO_LIST_QUESTION = HEADER + "/question/bymonth/{0}%2000:00:00?version=4.0.7&platform=android";
    //往期音乐列表
    public static String TO_LIST_MUSIC = HEADER + "/music/bymonth/{0}%2000:00:00?version=4.0.7&platform=android";

    //图文详情
    public static String IMAGE_TEXT_DETAIL = HEADER + "/hp/feeds/{0}/0?version=4.1.0&platform=android";
    //阅读详情
    public static String READING_DETAIL = HEADER + "/essay/{0}?source=channel_reading&source_id=10683&version=4.0.7&platform=android";


    //阅读动态
    public static String READING_DYNAMIC = HEADER + "/comment/praiseandtime/essay/{0}/{1}?version=4.0.7&platform=android";
}
