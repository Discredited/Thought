package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
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
import okhttp3.Call;

import static com.project.june.thought.R.id.text_content;

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
        requestData();
        requestDynamic();
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

                if (null != itemData.getQuote() && null != itemData.getTouser()){
                    //存在评论
                    reply_layout.setVisibility(View.VISIBLE);
                    reply_content.setText(itemData.getTouser().getUser_name() + " : " + itemData.getQuote());
                }else {
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

    //请求动态列表
    private void requestDynamic() {
        String path = MessageFormat.format(HttpUtils.SERIALIZE_DYNAMIC, serialId, 0);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<DynamicVo>() {
                    @Override
                    public void onResponse(DynamicVo response, int id) {
                        super.onResponse(response, id);
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

    private void fillData(SerializeDetailVo.DataBean vo) {
        serialize_title.setText(vo.getTitle());
        serialize_author.setText("文 / " + vo.getAuthor().getUser_name());
        serialize_content.setText(Html.fromHtml(vo.getContent()));

        charge_edt.setText(vo.getCharge_edt() + "    " + vo.getEditor_email());
        copyright.setText(vo.getCopyright());

        if (null != vo.getAuthor() && null != vo.getAuthor().getWeb_url()){
            Picasso.with(mActivity).load(vo.getAuthor().getWeb_url()).into(author_image);
        }else {
            Picasso.with(mActivity).load(R.mipmap.user_default_image).into(author_image);
        }

        author_name.setText(vo.getAuthor().getUser_name() + "    " + vo.getAuthor().getWb_name());
        author_des.setText(vo.getAuthor().getDesc());
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
