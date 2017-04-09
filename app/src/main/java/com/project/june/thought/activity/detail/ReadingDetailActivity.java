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
import com.project.june.thought.adapter.list.DynamicAdapter;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;

import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

public class ReadingDetailActivity extends BaseActivity {

    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.text_title)
    TextView text_title;
    @InjectView(R.id.text_author)
    TextView text_author;
    @InjectView(R.id.text_content)
    WebView text_content;
    @InjectView(R.id.charge_edt)
    TextView charge_edt;
    @InjectView(R.id.author_image)
    ImageView author_image;
    @InjectView(R.id.author_name)
    TextView author_name;
    @InjectView(R.id.author_des)
    TextView author_des;
    @InjectView(R.id.praise_comment_text)
    TextView praise_comment_text;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;
    @InjectView(R.id.list_view)
    ListView list_view;

    private String essayId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, ReadingDetailActivity.class);
        intent.putExtra("ESSAY_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reading_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        essayId = getIntent().getStringExtra("ESSAY_ID");
        if (null == essayId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("阅读 · 短篇");

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

        WebSettings settings = text_content.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setSupportZoom(true);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);//自动加载图片
        settings.setLoadWithOverviewMode(true);//适应屏幕

        adapter = new DynamicAdapter(mActivity);
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
        String path = MessageFormat.format(HttpUtils.READING_DYNAMIC, essayId, dynamicId);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<DynamicVo>() {
                    @Override
                    public void onResponse(DynamicVo response, int id) {
                        super.onResponse(response, id);
                        list_ptr.refreshComplete();
                        if (response.getRes() == 0) {
                            if (null != response.getData() && null != response.getData().getData()) {
                                if (response.getData().getData().size() > 0) {
                                    adapter.getItems().addAll(response.getData().getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    //没有更多了
                                    Toast.makeText(mActivity, "没有更多了", Toast.LENGTH_SHORT).show();
                                    list_ptr.setMode(PtrFrameLayout.Mode.REFRESH);
                                }
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
        //text_content.setText(Html.fromHtml(vo.getHp_content()));
        if (null != vo.getAuthor() && vo.getAuthor().size() > 0) {
            ReadingDetailVo.DataBean.AuthorBean authorBean = vo.getAuthor().get(0);
            if (null != authorBean) {
                text_author.setText("文 / " + vo.getAuthor().get(0).getUser_name());
                author_des.setText(authorBean.getDesc() + "    " + authorBean.getWb_name());
                author_name.setText(authorBean.getUser_name());
            }
            if (null != authorBean.getWeb_url() && !"".equals(authorBean.getWeb_url())){
                Picasso.with(mActivity).load(authorBean.getWeb_url()).transform(new CircleTransform()).into(author_image);
            }else {
                Picasso.with(mActivity).load(R.mipmap.user_default_image).into(author_image);
            }
        }

        praise_comment_text.setText(vo.getPraisenum() + " 喜欢    ·    " + vo.getCommentnum() + " 评论");

        Document document = Jsoup.parse(vo.getHp_content());
        Elements elements=document.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        String string1 = document.toString();
        String string2 = string1.replace("width:394px", "width:100%");
        String string3 = string2.replace("w/394", "w/344");
        String string4 = string3.replace("<br>", "<br><br>");
        String hp_content = string4.replace("100%\"><img", "100%\"><br><br><img");
        text_content.loadDataWithBaseURL(null, hp_content, "text/html", "utf-8", null);
        charge_edt.setText(vo.getHp_author_introduce() + "    " + vo.getEditor_email());
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
