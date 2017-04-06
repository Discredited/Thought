package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.activity.ShowImageActivity;
import com.project.june.thought.activity.index.ToListActivity;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.model.OneIndexVo;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
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
    private OneIndexVo.DataBean.WeatherBean weather;

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
        //添加底部item
        View footerView = inflater.inflate(R.layout.layout_footer_to_list, list_view, false);
        list_view.addFooterView(footerView);

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
            public void bindData(int position, View convertView, final OneIndexVo.DataBean.ContentListBean itemData) {
                //"category":"0"  图文
                //"category":"1"  阅读
                //"category":"1"  动漫
                //"category":"2"  连载
                //"category":"3"  问答
                //"category":"4"  电台
                //"category":"4"  音乐
                //"category":"5"  影视

                ImageView image;
                TextView content;
                final TextView laud_number;

                if (position == IMAGE_TEXT) {
                    //图文
                    TextView one_date = JuneViewHolder.get(convertView, R.id.one_date);
                    TextView one_position = JuneViewHolder.get(convertView, R.id.one_position);
                    TextView one_number = JuneViewHolder.get(convertView, R.id.one_number);
                    TextView one_draw = JuneViewHolder.get(convertView, R.id.one_draw);
                    image = JuneViewHolder.get(convertView, R.id.one_img);
                    content = JuneViewHolder.get(convertView, R.id.one_content);
                    TextView one_author = JuneViewHolder.get(convertView, R.id.one_author);
                    final ImageView laud_img = JuneViewHolder.get(convertView, R.id.laud_img);
                    laud_number = JuneViewHolder.get(convertView, R.id.laud_count);
                    LinearLayout diary_layout = JuneViewHolder.get(convertView, R.id.diary_layout);
                    LinearLayout laud_layout = JuneViewHolder.get(convertView, R.id.laud_layout);
                    LinearLayout share_layout = JuneViewHolder.get(convertView, R.id.share_layout);

                    if (null != weather) {
                        one_date.setText(DateTools.dateString(weather.getDate()));
                        one_position.setText(weather.getClimate() + "    " + weather.getCity_name());
                    }
                    one_number.setText(itemData.getVolume());
                    one_draw.setText(itemData.getTitle() + " | " + itemData.getPic_info());
                    one_author.setText(itemData.getWords_info());
                    content.setText(itemData.getForward());
                    laud_number.setText(itemData.getLike_count() + "");

                    diary_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //小记页面
                            Toast.makeText(mActivity, "打开小记", Toast.LENGTH_SHORT).show();
                        }
                    });

                    laud_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //是否收藏
                            if (itemData.isLaud()) {
                                laud_img.setImageResource(R.mipmap.laud);
                                laud_number.setText((Integer.parseInt(laud_number.getText().toString()) - 1) + "");
                                itemData.setLaud(false);
                            } else {
                                laud_img.setImageResource(R.mipmap.laud_selected);
                                laud_number.setText((Integer.parseInt(laud_number.getText().toString()) + 1) + "");
                                itemData.setLaud(true);
                            }
                        }
                    });

                    share_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //分享页面
                            Toast.makeText(mActivity, "分享页面", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    TextView item_type = JuneViewHolder.get(convertView, R.id.item_type);
                    TextView item_title = JuneViewHolder.get(convertView, R.id.item_title);
                    TextView item_author = JuneViewHolder.get(convertView, R.id.item_author);
                    image = JuneViewHolder.get(convertView, R.id.item_image);
                    //TextView item_sub_content = JuneViewHolder.get(convertView, R.id.item_sub_content);
                    content = JuneViewHolder.get(convertView, R.id.item_content);
                    laud_number = JuneViewHolder.get(convertView, R.id.laud_count);
                    TextView item_time = JuneViewHolder.get(convertView, R.id.item_time);
                    final ImageView laud_img = JuneViewHolder.get(convertView, R.id.laud_img);
                    ImageView item_share = JuneViewHolder.get(convertView, R.id.item_share);

                    if (itemData.getCategory().equals("1")) {
                        //阅读  漫画/one story/实验室 ...
                        if (null != itemData.getTag_list() && itemData.getTag_list().size() > 0) {
                            item_type.setText("- " + itemData.getTag_list().get(0).getTitle() + " -");
                        }
                    } else if (itemData.getCategory().equals("2")) {
                        //连载
                        item_type.setText("- 连载 -");
                    } else if (itemData.getCategory().equals("3")) {
                        //问答
                        item_type.setText("- 问答 -");
                    }

                    item_title.setText(itemData.getTitle());
                    item_author.setText("文 / " + itemData.getAuthor().getUser_name());
                    content.setText(itemData.getForward());
                    laud_number.setText(itemData.getLike_count() + "");

                    item_time.setText("1天之前");
                    laud_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //是否收藏
                            if (itemData.isLaud()) {
                                laud_img.setImageResource(R.mipmap.laud);
                                laud_number.setText((Integer.parseInt(laud_number.getText().toString()) - 1) + "");
                                itemData.setLaud(false);
                            } else {
                                laud_img.setImageResource(R.mipmap.laud_selected);
                                laud_number.setText((Integer.parseInt(laud_number.getText().toString()) + 1) + "");
                                itemData.setLaud(true);
                            }
                        }
                    });
                    item_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //分享页面
                            Toast.makeText(mActivity, "分享页面", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Picasso.with(mActivity).load(itemData.getImg_url()).error(R.mipmap.opening_monday).into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowImageActivity.startThis(mActivity, itemData.getImg_url());
                    }
                });
            }
        };
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (adapter.getCount())){
                    //最后一个item,前往往期列表
                    ToListActivity.startThis(mActivity);
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
        weather = data.getWeather();

        List<OneIndexVo.DataBean.ContentListBean> beanList = new ArrayList<>(0);
        List<OneIndexVo.DataBean.ContentListBean> contentList = data.getContent_list();
        for (OneIndexVo.DataBean.ContentListBean bean : contentList) {
            String category = bean.getCategory();
            //过滤音乐和影视
            if (category.equals("4") || category.equals("5")) {
                continue;
            }

            //过滤阅读
            if (category.equals("1") && bean.getTag_list().size() == 0) {
                continue;
            }

            //过滤连载
            if (category.equals("2")) {
                continue;
            }

            beanList.add(bean);
        }

        adapter.getItems().clear();
        adapter.getItems().addAll(beanList);
        adapter.notifyDataSetChanged();
    }

    public static IndexOneFragment newInstance(Bundle args) {
        IndexOneFragment fragment = new IndexOneFragment();
        fragment.setArguments(args);
        return fragment;
    }
}