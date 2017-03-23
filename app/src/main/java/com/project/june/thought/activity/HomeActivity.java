package com.project.june.thought.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.fragment.IndexFragment;

import butterknife.InjectView;

public class HomeActivity extends BaseActivity {

    @InjectView(R.id.home_radio_group)
    RadioGroup home_radio_group;
    private FragmentManager manager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getTempFragmentId() {
        return R.id.temp_fragment;
    }

    @Override
    protected void logicProgress() {
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(getTempFragmentId(), IndexFragment.newInstance(new Bundle())).commit();
    }

    @Override
    protected boolean enableSwipeBack() {
        return super.enableSwipeBack();
    }
}
