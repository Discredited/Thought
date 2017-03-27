package com.project.june.thought.adapter.pageAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.june.thought.fragment.ToListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 2017/3/25.
 */
public class ToListPagerAdapter extends FragmentPagerAdapter {

    private List<ToListFragment> fragments = new ArrayList<>(0);
    private List<String> tabTitleList = new ArrayList<>(0);

    public ToListPagerAdapter(FragmentManager fm, List<ToListFragment> fragments,List<String> tabTitleList) {
        super(fm);
        this.fragments.addAll(fragments);
        this.tabTitleList.addAll(tabTitleList);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }
}
