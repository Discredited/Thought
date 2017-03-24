package com.project.june.thought.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;

/**
 * 往期列表
 */
public class ToListActivity extends BaseActivity {

    public static void startThis(Context context){
        Intent intent = new Intent(context, ToListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_to_list;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {

    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
