package com.project.june.thought.fragment;

import android.os.Bundle;

import com.project.june.thought.base.BaseFragment;

/**
 * Created by June on 2017/4/6.
 */
public class MusicFragment extends BaseFragment {
    @Override
    protected int getContentViewId() {
        return 0;
    }


    public static MusicFragment newInstance(Bundle bundle){
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
