package com.project.june.thought.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.fragment.IndexFragment;
import com.project.june.thought.fragment.MovieFragment;
import com.project.june.thought.fragment.MusicFragment;
import com.project.june.thought.fragment.ReadFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class HomeActivity extends BaseActivity {

    @InjectView(R.id.home_radio_group)
    RadioGroup home_radio_group;
    private FragmentManager manager;
    private List<BaseFragment> fragments;

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
        fragments = new ArrayList<>(0);
        fragments.add(IndexFragment.newInstance(new Bundle()));
        fragments.add(ReadFragment.newInstance(new Bundle()));
        fragments.add(MusicFragment.newInstance(new Bundle()));
        fragments.add(MovieFragment.newInstance(new Bundle()));

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(getTempFragmentId(), fragments.get(0))
                .add(getTempFragmentId(), fragments.get(1))
                .add(getTempFragmentId(), fragments.get(2))
                .add(getTempFragmentId(), fragments.get(3))
                .show(fragments.get(0))
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(3))
                .commit();

        RadioButton firstButton = (RadioButton) home_radio_group.getChildAt(0);
        firstButton.setChecked(true);
        home_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.isChecked()) {
                        manager.beginTransaction().show(fragments.get(i)).commit();
                    } else {
                        manager.beginTransaction().hide(fragments.get(i)).commit();
                    }
                }
            }
        });
    }


    @Override
    protected boolean enableSwipeBack() {
        return super.enableSwipeBack();
    }

    private Long exitTime = 0l;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
