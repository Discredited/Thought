package com.project.june.thought.fragment;

import android.os.Bundle;

import com.project.june.thought.base.BaseFragment;

/**
 * 阅读
 * Created by June on 2017/3/25.
 */

public class ReadFragment extends BaseFragment {
    @Override
    protected int getContentViewId() {
        return 0;
    }

    public static ReadFragment newInstance(Bundle bundle){
        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
