package com.project.june.thought.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.june.thought.fragment.IndexOneFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页碎片适配器
 * Created by June on 2017/3/15.
 */
public class IndexFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<IndexOneFragment> fragments = new ArrayList<>(0);

    public IndexFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setFragments(List<IndexOneFragment> list){
        fragments.addAll(list);
        notifyDataSetChanged();
    }
}