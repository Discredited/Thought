package com.project.xujun.juneutils.otherutils;

import android.content.Context;

/**
 * 视图工具类
 * Created by xujun on 2017/3/18.
 */

public class ViewUtils {

    public static int dp2px(Context context, float dipValue){
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    public static int px2dp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
