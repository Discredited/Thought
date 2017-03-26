package com.project.june.thought.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.InjectView;

public class StartActivity extends BaseActivity {

    @InjectView(R.id.start_image)
    ImageView start_image;
    @InjectView(R.id.start_date)
    TextView start_date;
    private int[] weekDay;
    private Calendar calendar;
    private String[] years;
    private String[] months;
    private String[] days;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {
        calendar = Calendar.getInstance();
        weekDay = new int[]{
                R.mipmap.opening_sunday,
                R.mipmap.opening_monday,
                R.mipmap.opening_tuesday,
                R.mipmap.opening_wednesday,
                R.mipmap.opening_thursday,
                R.mipmap.opening_friday,
                R.mipmap.opening_saturday,
        };
        years = new String[]{"二〇一七年", "二〇一八年", "二〇一九年", "二〇二〇年", "二〇二一年", "二〇二二年", "二〇二三年", "二〇二四年", "二〇二五年"};
        months = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        days = new String[]{"一日", "二日", "三日", "四日", "五日", "六日", "七日", "八日", "九日", "十日", "十一日", "十二日",
                "十三日", "十四日", "十五日", "十六日", "十七日", "十八日", "十九日", "廿", "廿一", "廿二", "廿三",
                "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "卅", "卅一"};

        //星期几计算
        weekdayToday();
        //日期计算
        day();
        //延时跳转
        delayGo();
    }

    private void delayGo() {
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };
        time.schedule(task, 2000);
    }

    private void day() {
        int theYear = calendar.get(Calendar.YEAR);
        int theMonth = calendar.get(Calendar.MONTH);
        int theDay = calendar.get(Calendar.DAY_OF_MONTH);
        start_date.setText("新世纪 公元" + years[theYear - 2017] + months[theMonth] + days[theDay - 1]);
    }

    private void weekdayToday() {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Picasso.with(mActivity).load(weekDay[dayOfWeek - 1]).into(start_image);
    }

    @Override
    protected boolean enableSwipeBack() {
        return super.enableSwipeBack();
    }
}
