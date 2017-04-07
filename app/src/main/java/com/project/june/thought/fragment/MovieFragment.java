package com.project.june.thought.fragment;

import android.os.Bundle;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseFragment;

/**
 * Created by June on 2017/4/7.
 */

public class MovieFragment extends BaseFragment {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_music;
    }

    public static MovieFragment newInstance(Bundle bundle){
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
