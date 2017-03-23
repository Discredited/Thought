package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.project.june.thought.R;
import com.project.june.thought.adapter.IndexFragmentPagerAdapter;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.model.OneIndexListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 首页
 * Created by June on 2017/3/10.
 */
public class IndexFragment extends BaseFragment {

    @InjectView(R.id.view_pager)
    PullToRefreshViewPager view_pager;

    private IndexFragmentPagerAdapter adapter;
    private List<IndexOneFragment> fragments = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_index;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化界面
        initUi();
        //请求数据
        requestData();
    }

    private void initUi() {
        FragmentManager manager = getFragmentManager();
        adapter = new IndexFragmentPagerAdapter(manager);
        ViewPager viewPager = view_pager.getRefreshableView();
        viewPager.setAdapter(adapter);
    }

    private void requestData() {
        OkHttpUtils.get()
                .url(HttpUtils.INDEX_LIST)
                .build()
                .execute(new ResultCallBack<OneIndexListVo>() {
                    @Override
                    public void onResponse(OneIndexListVo response, int id) {
                        super.onResponse(response, id);
                        //请求数据成功
                        if (response.getRes() == 0) {
                            List<String> stringList = response.getData();
                            for (String s : stringList) {
                                Bundle bundle = new Bundle();
                                bundle.putString("ITEM_ID", s);
                                fragments.add(IndexOneFragment.newInstance(bundle));
                            }
                            adapter.setFragments(fragments);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Log.e(HttpUtils.TAG, "网络请求失败    " + e.toString());
                    }
                });
    }

    public static IndexFragment newInstance(Bundle args) {
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }
}