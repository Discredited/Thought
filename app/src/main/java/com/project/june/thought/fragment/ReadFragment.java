package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.activity.detail.ReadingDetailActivity;
import com.project.june.thought.adapter.pager.ReadingPagerAdapter;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.model.ReadingBannerListVo;
import com.project.june.thought.model.ReadingGatherVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.RoundedTransformation;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * 阅读
 * Created by June on 2017/3/25.
 */

public class ReadFragment extends BaseFragment {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;
    private ReadingPagerAdapter pagerAdapter;
    private JuneBaseAdapter<ReadingGatherVo.DataBean> adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_reading;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title_center_text.setText("阅读");

        initListView();
        initPtr();
        requestBanner();
        requestData("0");
    }

    private void initListView() {
        //给listView添加一个头
        View headerView = inflater.inflate(R.layout.layout_banner, list_view, false);
        ViewPager viewPager = (ViewPager) headerView.findViewById(R.id.banner_pager);
        pagerAdapter = new ReadingPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        list_view.addHeaderView(headerView);

        adapter = new JuneBaseAdapter<ReadingGatherVo.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_content, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, ReadingGatherVo.DataBean itemData) {
                TextView item_type = JuneViewHolder.get(convertView, R.id.item_type);
                TextView item_title = JuneViewHolder.get(convertView, R.id.item_title);
                TextView item_author = JuneViewHolder.get(convertView, R.id.item_author);
                ImageView item_image = JuneViewHolder.get(convertView, R.id.item_image);
                //TextView item_sub_content = JuneViewHolder.get(convertView, R.id.item_sub_content);
                TextView item_content = JuneViewHolder.get(convertView, R.id.item_content);
                TextView item_time = JuneViewHolder.get(convertView, R.id.item_time);
                TextView laud_count = JuneViewHolder.get(convertView, R.id.laud_count);

                if (null != itemData.getTag_list() && itemData.getTag_list().size() > 0) {
                    item_type.setText("- " + itemData.getTag_list().get(0).getTitle() + " -");
                } else {
                    item_type.setText("- 阅读 -");
                }

                item_title.setText(itemData.getTitle());
                item_author.setText("文 / " + itemData.getAuthor().getUser_name());
                item_content.setText(itemData.getForward());
                item_time.setText("今天");
                laud_count.setText(itemData.getLike_count() + "");
                Picasso.with(mActivity).load(itemData.getImg_url()).transform(new RoundedTransformation(ViewUtils.dp2px(mActivity, 4f), 0)).into(item_image);
            }
        };
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //跳转banner的页面
                } else {
                    ReadingGatherVo.DataBean bean = adapter.getItems().get(position - 1);
                    ReadingDetailActivity.startThis(mActivity, bean.getItem_id());
                }
            }
        });
    }

    private void initPtr() {
        //下拉刷新控件
        list_ptr.setLastUpdateTimeRelateObject(this);
        list_ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                adapter.getItems().clear();
                requestData("0");
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                ReadingGatherVo.DataBean dataBean = adapter.getItems().get(adapter.getCount() - 1);
                requestData(dataBean.getId());
            }
        });
        list_ptr.setResistance(1.7f);
        list_ptr.setRatioOfHeaderHeightToRefresh(1.2f);
        list_ptr.setDurationToClose(200);
        list_ptr.setDurationToCloseHeader(1000);
        //默认为false
        list_ptr.setPullToRefresh(false);
        list_ptr.setKeepHeaderWhenRefresh(true);
        //默认为true
        list_ptr.setmOnlyShowHeaderOrFooter(true);
        //默认为false
        list_ptr.disableWhenHorizontalMove(true);
        //默认没有任何加载方式
        list_ptr.setMode(PtrFrameLayout.Mode.BOTH);
        list_ptr.autoRefresh(300);
    }

    private void requestData(String readId) {
        String path = MessageFormat.format(HttpUtils.READING, readId);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<ReadingGatherVo>() {
                    @Override
                    public void onResponse(ReadingGatherVo response, int id) {
                        super.onResponse(response, id);
                        list_ptr.refreshComplete();
                        if (response.getRes() == 0) {
                            if (null != response.getData() && response.getData().size() > 0) {
                                adapter.getItems().addAll(response.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            //网络请求 false
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        list_ptr.refreshComplete();
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestBanner() {
        OkHttpUtils.get()
                .url(HttpUtils.READING_BANNER)
                .build()
                .execute(new ResultCallBack<ReadingBannerListVo>() {
                    @Override
                    public void onResponse(ReadingBannerListVo response, int id) {
                        super.onResponse(response, id);
                        if (0 == response.getRes()) {
                            if (null != response.getData() && response.getData().size() > 0) {
                                pagerAdapter.setImages(response.getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }
                });
    }

    public static ReadFragment newInstance(Bundle bundle) {
        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
