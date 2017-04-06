package com.project.june.thought.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    private Long exitTime = 0l;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - exitTime > 2000){
                Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
