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
    //阅读模块
    public static String READING = HEADER + "/channel/reading/more/{0}?version=4.0.7&platform=android";
    //阅读模块banner
    public static String READING_BANNER = HEADER + "/reading/carousel/?platform=android";
    //音乐模块
    public static String MUSIC = HEADER + "/channel/music/more/{0}?version=4.0.7&platform=android";
    //视频
    public static String MOVIE = HEADER + "/channel/movie/more/0?version=4.0.7&platform=android";

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
    //连载详情
    public static String SERIALIZE_DETAIL = HEADER + "/serialcontent/{0}?source=summary&source_id=10668&version=4.0.7&platform=android";
    //连载章节列表
    public static String SERIALIZE_LIST = HEADER + "/serial/list/{0}?version=4.0.7&platform=android";
    //问答详情
    public static String QUESTION_DETAIL = HEADER + "/question/{0}?source=summary&source_id=10666&version=4.0.7&platform=android";
    //音乐详情
    public static String MUSIC_DETAIL = HEADER + "";
    //影视详情
    public static String VIDEO_DETAIL = HEADER + "";

    //阅读动态
    public static String READING_DYNAMIC = HEADER + "/comment/praiseandtime/essay/{0}/{1}?version=4.0.7&platform=android";
    //连载动态
    public static String SERIALIZE_DYNAMIC = HEADER + "/comment/praiseandtime/serial/{0}/{1}?version=4.0.7&platform=android";
    //问答动态
    public static String QUESTION_DYNAMIC = HEADER + "/comment/praiseandtime/question/{0}/{1}?version=4.0.7&platform=android";
    //音乐动态
    public static String MUSIC_DYNAMIC = HEADER + "/comment/praiseandtime/serial/{0}/{1}?version=4.0.7&platform=android";
    //视频动态
    public static String VIDEO_DYNAMIC = HEADER + "/comment/praiseandtime/serial/{0}/{1}?version=4.0.7&platform=android";
}
