package com.project.june.thought.activity.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.adapter.pager.ToListPagerAdapter;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.fragment.ToListFragment;
import com.project.june.thought.utils.ThoughtConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 往期列表
 */
public class ToListActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.tab_layout)
    TabLayout tab_layout;
    @InjectView(R.id.view_pager)
    ViewPager view_pager;

    private List<String> tabStringList = new ArrayList<>(0);
    private List<ToListFragment> fragments = new ArrayList<>(0);
    private ToListPagerAdapter adapter;

    public static void startThis(Context context) {
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
        initUi();
    }

    private void initUi() {
        title_center_text.setText("往期列表");

        tabStringList.add("图文");
        tabStringList.add("阅读");
        tabStringList.add("连载");
        tabStringList.add("问答");
        tabStringList.add("音乐");

        Bundle bundle = new Bundle();
        //图文
        bundle.putString(ThoughtConfig.CATEGORY, ThoughtConfig.IMAGE_TEXT_CATEGORY);
        fragments.add(ToListFragment.newInstance(bundle));
        //阅读
        bundle = new Bundle();
        bundle.putString(ThoughtConfig.CATEGORY, ThoughtConfig.READING_CATEGORY);
        fragments.add(ToListFragment.newInstance(bundle));
        //连载
        bundle = new Bundle();
        bundle.putString(ThoughtConfig.CATEGORY, ThoughtConfig.SERIALIZE_CATEGORY);
        fragments.add(ToListFragment.newInstance(bundle));
        //问答
        bundle = new Bundle();
        bundle.putString(ThoughtConfig.CATEGORY, ThoughtConfig.QUESTION_CATEGORY);
        fragments.add(ToListFragment.newInstance(bundle));
        //音乐
        bundle = new Bundle();
        bundle.putString(ThoughtConfig.CATEGORY, ThoughtConfig.MUSIC_CATEGORY);
        fragments.add(ToListFragment.newInstance(bundle));

        adapter = new ToListPagerAdapter(getSupportFragmentManager(), fragments, tabStringList);
        view_pager.setAdapter(adapter);

        tab_layout.setupWithViewPager(view_pager);
    }

    @OnClick(R.id.title_img_left)
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
