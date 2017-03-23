package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.model.OneIndexVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHodler;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * 首页一个碎片
 * Created by June on 2017/3/10.
 */
public class IndexOneFragment extends BaseFragment {

    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;

    private String itemId;
    private JuneBaseAdapter<OneIndexVo.DataBean.ContentListBean> adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_index_item;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        itemId = bundle.getString("ITEM_ID");
        if (null == itemId) {
            return;
        }

        initListView();
        initPtr();
    }

    private void initListView() {
        adapter = new JuneBaseAdapter<OneIndexVo.DataBean.ContentListBean>(mActivity) {
            private int IMAGE_TEXT = 0;  //图文
            private int CONTENT_TEXT = 1;  //其他文本

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return IMAGE_TEXT;
                } else {
                    return CONTENT_TEXT;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    if (position == 0) {
                        //多布局图文
                        convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_index_image_text, parent, false);
                    } else {
                        //其他文本
                        convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_content, parent, false);
                    }
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, OneIndexVo.DataBean.ContentListBean itemData) {
                ImageView image;
                TextView content, laud_number;

                if (position == IMAGE_TEXT) {
                    //图文
                    TextView one_date = JuneViewHodler.get(convertView, R.id.one_date);
                    TextView one_position = JuneViewHodler.get(convertView, R.id.one_position);
                    TextView one_number = JuneViewHodler.get(convertView, R.id.one_number);
                    TextView one_draw = JuneViewHodler.get(convertView, R.id.one_draw);
                    image = JuneViewHodler.get(convertView, R.id.one_img);
                    content = JuneViewHodler.get(convertView, R.id.one_content);
                    TextView one_author = JuneViewHodler.get(convertView, R.id.one_author);
                    laud_number = JuneViewHodler.get(convertView, R.id.laud_count);

                    one_date.setText("年");
                    one_position.setText("天气" + "    " + "城市");
                    one_number.setText(itemData.getVolume());
                    one_draw.setText(itemData.getTitle() + " | " + itemData.getPic_info());
                    one_author.setText(itemData.getWords_info());
                } else {
                    TextView item_type = JuneViewHodler.get(convertView, R.id.item_type);
                    TextView item_title = JuneViewHodler.get(convertView, R.id.item_title);
                    TextView item_author = JuneViewHodler.get(convertView, R.id.item_author);
                    image = JuneViewHodler.get(convertView, R.id.item_image);
                    content = JuneViewHodler.get(convertView, R.id.item_content);
                    laud_number = JuneViewHodler.get(convertView, R.id.laud_count);

                    item_type.setText("- item类型 -");
                    item_title.setText(itemData.getTitle());
                    item_author.setText("文 / " + itemData.getAuthor().getUser_name());
                }

                //Picasso.with(mActivity).load(itemData.getImg_url()).placeholder(R.mipmap.opening_monday).error(R.mipmap.opening_monday).into(image);
                content.setText(itemData.getForward());
                laud_number.setText(itemData.getLike_count() + "");
            }
        };
        list_view.setAdapter(adapter);
    }

    private void initPtr() {
        //下拉刷新控件
        list_ptr.setLastUpdateTimeRelateObject(this);
        list_ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData();
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
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
        list_ptr.setMode(PtrFrameLayout.Mode.REFRESH);
        list_ptr.autoRefresh(300);
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.INDEX, itemId);
        OkHttpUtils.get().url(path).build().execute(new ResultCallBack<OneIndexVo>() {
            @Override
            public void onResponse(OneIndexVo response, int id) {
                list_ptr.refreshComplete();
                super.onResponse(response, id);
                if (response.getRes() == 0) {
                    if (null != response.getData()) {
                        fillData(response.getData());
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                list_ptr.refreshComplete();
                super.onError(call, e, id);
            }
        });
    }

    private void fillData(OneIndexVo.DataBean data) {
        adapter.getItems().clear();
        adapter.getItems().addAll(data.getContent_list());
        adapter.notifyDataSetChanged();
    }

    public static IndexOneFragment newInstance(Bundle args) {
        IndexOneFragment fragment = new IndexOneFragment();
        fragment.setArguments(args);
        return fragment;
    }
}