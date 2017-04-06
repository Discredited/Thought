package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.SerializeDetailVo;
import com.project.june.thought.model.SerializePartListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

public class SerializeDetailActivity extends BaseActivity {

    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.serialize_title)
    TextView serialize_title;
    @InjectView(R.id.serialize_author)
    TextView serialize_author;
    @InjectView(R.id.serialize_list)
    ImageView serialize_list;
    @InjectView(R.id.serialize_content)
    TextView serialize_content;
    @InjectView(R.id.charge_edt)
    TextView charge_edt;
    @InjectView(R.id.copyright)
    TextView copyright;
    @InjectView(R.id.author_image)
    ImageView author_image;
    @InjectView(R.id.author_name)
    TextView author_name;
    @InjectView(R.id.author_des)
    TextView author_des;
    @InjectView(R.id.recommend_1_title)
    TextView recommend_1_title;
    @InjectView(R.id.recommend_1_author)
    TextView recommend_1_author;
    @InjectView(R.id.recommend_1_layout)
    LinearLayout recommend_1_layout;
    @InjectView(R.id.recommend_2_title)
    TextView recommend_2_title;
    @InjectView(R.id.recommend_2_author)
    TextView recommend_2_author;
    @InjectView(R.id.recommend_2_layout)
    LinearLayout recommend_2_layout;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;
    @InjectView(R.id.pre_serialize)
    TextView pre_serialize;
    @InjectView(R.id.next_serialize)
    TextView next_serialize;
    @InjectView(R.id.recommend_layout)
    LinearLayout recommend_layout;

    private String serialId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, SerializeDetailActivity.class);
        intent.putExtra("SERIAL_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_serialize_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        serialId = getIntent().getStringExtra("SERIAL_ID");
        if (null == serialId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("连载");

        initListView();
        initPtr();
        requestData();
        requestDynamic("0");
    }

    private void initListView() {
        //设置头
        ((LinearLayout) header_view.getParent()).removeView(header_view);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        header_view.setLayoutParams(params);
        list_view.addHeaderView(header_view);

        adapter = new JuneBaseAdapter<DynamicVo.DataBeanX.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_dynamic, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, DynamicVo.DataBeanX.DataBean itemData) {
                ImageView dynamic_image = JuneViewHolder.get(convertView, R.id.dynamic_image);
                TextView praise_name = JuneViewHolder.get(convertView, R.id.praise_name);
                TextView praise_time = JuneViewHolder.get(convertView, R.id.praise_time);
                TextView praise_content = JuneViewHolder.get(convertView, R.id.praise_content);
                TextView praise_count = JuneViewHolder.get(convertView, R.id.praise_count);

                LinearLayout reply_layout = JuneViewHolder.get(convertView, R.id.reply_layout);
                TextView reply_content = JuneViewHolder.get(convertView, R.id.reply_content);

                if (null == itemData.getUser().getWeb_url() || itemData.getUser().getWeb_url().isEmpty()) {
                    Picasso.with(mActivity).load(R.mipmap.user_default_image).transform(new CircleTransform()).into(dynamic_image);
                } else {
                    Picasso.with(mActivity).load(itemData.getUser().getWeb_url()).transform(new CircleTransform()).into(dynamic_image);
                }

                if (null != itemData.getQuote() && null != itemData.getTouser()) {
                    //存在评论
                    reply_layout.setVisibility(View.VISIBLE);
                    reply_content.setText(itemData.getTouser().getUser_name() + " : " + itemData.getQuote());
                } else {
                    //不存在评论
                    reply_layout.setVisibility(View.GONE);
                }

                praise_name.setText(itemData.getUser().getUser_name());
                praise_time.setText(itemData.getCreated_at());
                praise_content.setText(itemData.getContent());
                praise_count.setText(itemData.getPraisenum() + "");
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
                requestDynamic("0");
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                DynamicVo.DataBeanX.DataBean dataBean = adapter.getItems().get(adapter.getCount() - 1);
                requestDynamic(dataBean.getId());
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
        //list_ptr.autoRefresh(300);
    }

    //请求动态列表
    private void requestDynamic(String dynamicId) {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_DYNAMIC, serialId, dynamicId);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<DynamicVo>() {
                    @Override
                    public void onResponse(DynamicVo response, int id) {
                        super.onResponse(response, id);
                        list_ptr.refreshComplete();
                        if (response.getRes() == 0) {
                            if (null != response.getData() && null != response.getData().getData() && response.getData().getData().size() > 0) {
                                adapter.getItems().addAll(response.getData().getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            //请求失败
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

    //内容填充
    private void requestData() {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_DETAIL, serialId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<SerializeDetailVo>() {
                    @Override
                    public void onResponse(SerializeDetailVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            //请求成功
                            if (null != response.getData()) {
                                fillData(response.getData());
                                //请求章节列表
                                requestSerializePartList(response.getData().getSerial_id(), response.getData().getNumber());
                            }
                        } else {
                            //请求异常
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillData(final SerializeDetailVo.DataBean vo) {
        serialize_title.setText(vo.getTitle());
        serialize_author.setText("文 / " + vo.getAuthor().getUser_name());
        serialize_content.setText(Html.fromHtml(vo.getContent()));

        charge_edt.setText(vo.getCharge_edt() + "    " + vo.getEditor_email());
        copyright.setText(vo.getCopyright());

        if (null != vo.getAuthor() && null != vo.getAuthor().getWeb_url()) {
            Picasso.with(mActivity).load(vo.getAuthor().getWeb_url()).into(author_image);
        } else {
            Picasso.with(mActivity).load(R.mipmap.user_default_image).into(author_image);
        }

        author_name.setText(vo.getAuthor().getUser_name() + "    " + vo.getAuthor().getWb_name());
        author_des.setText(vo.getAuthor().getDesc());

        //请求前一篇连载
        if (null != vo.getLastid() && !"0".equals(vo.getLastid())) {
            //前一篇存在 设置推荐
            recommend_1_layout.setVisibility(View.VISIBLE);
            requestRecommend1(vo.getLastid());
            pre_serialize.setTextColor(Color.parseColor("#666666"));
            pre_serialize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SerializeDetailActivity.startThis(mActivity, vo.getLastid());
                }
            });
        } else {
            //前一篇不存在
            recommend_1_layout.setVisibility(View.GONE);
            pre_serialize.setTextColor(Color.parseColor("#cccccc"));
            pre_serialize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "已经是第一章了", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //请求后篇
        if (null != vo.getNextid() && !"0".equals(vo.getNextid())) {
            //后一篇存在
            recommend_2_layout.setVisibility(View.VISIBLE);
            requestRecommend2(vo.getNextid());
            next_serialize.setTextColor(Color.parseColor("#666666"));
            next_serialize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SerializeDetailActivity.startThis(mActivity, vo.getNextid());
                }
            });
        } else {
            //后一篇不存在
            recommend_2_layout.setVisibility(View.GONE);
            next_serialize.setTextColor(Color.parseColor("#cccccc"));
            next_serialize.setText("即将更新，敬请期待");
            next_serialize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "即将更新，敬请期待", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if ("0".equals(vo.getLastid()) && "0".equals(vo.getNextid())){
            recommend_layout.setVisibility(View.GONE);
        }
    }

    private void requestSerializePartList(String serialId, final String number) {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_LIST, serialId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<SerializePartListVo>() {
                    @Override
                    public void onResponse(SerializePartListVo response, int id) {
                        super.onResponse(response, id);
                        if (null != response.getData()) {
                            //章节列表的处理
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestRecommend1(String id) {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_DETAIL, id);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<SerializeDetailVo>() {
                    @Override
                    public void onResponse(SerializeDetailVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            //请求成功
                            if (null != response.getData()) {
                                fillRecommend1(response.getData());
                            }
                        } else {
                            //请求异常
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillRecommend1(final SerializeDetailVo.DataBean vo) {
        recommend_1_title.setText(vo.getTitle());
        recommend_1_author.setText("文 / " + vo.getAuthor().getUser_name());
        recommend_1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerializeDetailActivity.startThis(mActivity, vo.getId());
            }
        });
    }

    private void requestRecommend2(String id) {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_DETAIL, id);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<SerializeDetailVo>() {
                    @Override
                    public void onResponse(SerializeDetailVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            //请求成功
                            if (null != response.getData()) {
                                fillRecommend2(response.getData());
                            }
                        } else {
                            //请求异常
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillRecommend2(final SerializeDetailVo.DataBean vo) {
        recommend_2_title.setText(vo.getTitle());
        recommend_2_author.setText("文 / " + vo.getAuthor().getUser_name());
        recommend_2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerializeDetailActivity.startThis(mActivity, vo.getId());
            }
        });
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
