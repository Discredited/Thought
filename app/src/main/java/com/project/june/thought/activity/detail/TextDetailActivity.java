package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.ReadingDetailVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.InjectView;
import okhttp3.Call;

public class TextDetailActivity extends BaseActivity {

    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_layout)
    RelativeLayout title_layout;
    @InjectView(R.id.text_title)
    TextView text_title;
    @InjectView(R.id.text_author)
    TextView text_author;
    @InjectView(R.id.text_content)
    WebView text_content;
    @InjectView(R.id.list_view)
    ListView list_view;

    private String essayId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;
    private String category;

    public static void startThis(Context context, String id, String category) {
        Intent intent = new Intent(context, TextDetailActivity.class);
        intent.putExtra("ESSAY_ID", id);
        intent.putExtra("CATEGORY", category);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_text_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        essayId = getIntent().getStringExtra("ESSAY_ID");
        category = getIntent().getStringExtra("CATEGORY");
        if (null == essayId || null == category) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("......");

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

        WebSettings settings = text_content.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setSupportZoom(true);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL.SINGLE_COLUMN);
        //settings.setDefaultTextEncodingName("UTF-8");
        //settings.setAppCacheEnabled(true);
        //settings.setLoadsImagesAutomatically(true);//自动加载图片
        //settings.setCacheMode(WebSettings.LOAD_DEFAULT | WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //settings.setLoadWithOverviewMode(true);//适应屏幕

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

                if (null == itemData.getUser().getWeb_url() || itemData.getUser().getWeb_url().isEmpty()){
                    Picasso.with(mActivity).load(R.mipmap.user_default_image).transform(new CircleTransform()).into(dynamic_image);
                }else {
                    Picasso.with(mActivity).load(itemData.getUser().getWeb_url()).transform(new CircleTransform()).into(dynamic_image);
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
        String path = null;
        switch (category) {
            case "1":
                path = MessageFormat.format(HttpUtils.READING_DYNAMIC, essayId, 0);
                break;
            case "2":
                path = MessageFormat.format(HttpUtils.SERIALIZE_DYNAMIC, essayId, 0);
                break;
            case "3":
                path = MessageFormat.format(HttpUtils.QUESTION_DYNAMIC, essayId, 0);
                break;
            case "4":
                path = MessageFormat.format(HttpUtils.MUSIC_DYNAMIC, essayId, 0);
                break;
            case "5":
                path = MessageFormat.format(HttpUtils.VIDEO_DYNAMIC, essayId, 0);
                break;
        }


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
        String path = MessageFormat.format(HttpUtils.READING_DETAIL, essayId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<ReadingDetailVo>() {
                    @Override
                    public void onResponse(ReadingDetailVo response, int id) {
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

    private void fillData(ReadingDetailVo.DataBean vo) {
        text_title.setText(vo.getHp_title());
        text_author.setText("文 / " + vo.getAuthor().get(0).getUser_name());
        //text_content.setText(Html.fromHtml(vo.getHp_content()));

        String string1 = vo.getHp_content();
        String string2 = string1.replace("width:394px", "width:100%");
        String string3 = string2.replace("w/394", "w/344");
        String string4 = string3.replace("<br>", "<br><br>");
        String hp_content = string4.replace("100%\"><img", "100%\"><br><br><img");

        text_content.loadDataWithBaseURL(null, hp_content, "text/html", "utf-8", null);
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
