package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.adapter.list.DynamicAdapter;
import com.project.june.thought.adapter.pager.ReadingPagerAdapter;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.MovieDetailVo;
import com.project.june.thought.model.MovieStoryVo;
import com.project.june.thought.model.ReadingBannerListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

public class MovieDetailActivity extends BaseActivity {

    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.praise_comment_text)
    TextView praise_comment_text;
    @InjectView(R.id.title_img_left)
    ImageButton title_img_left;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_img_right)
    ImageButton title_img_right;
    @InjectView(R.id.view_pager)
    ViewPager view_pager;
    @InjectView(R.id.video_player_layout)
    RelativeLayout video_player_layout;
    @InjectView(R.id.video_player)
    ImageView video_player;
    @InjectView(R.id.movie_sub_title)
    TextView movie_sub_title;
    @InjectView(R.id.movie_title)
    TextView movie_title;
    @InjectView(R.id.movie_author)
    TextView movie_author;
    @InjectView(R.id.movie_content)
    WebView movie_content;
    @InjectView(R.id.copyright)
    TextView copyright;
    @InjectView(R.id.charge_edt)
    TextView charge_edt;
    @InjectView(R.id.author_image)
    ImageView author_image;
    @InjectView(R.id.author_name)
    TextView author_name;
    @InjectView(R.id.author_des)
    TextView author_des;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;

    private String movieId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;
    private ReadingPagerAdapter pagerAdapter;

    public static void startThis(Context context, String movieId) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("MOVIE_ID", movieId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        movieId = getIntent().getStringExtra("MOVIE_ID");
        if (null == movieId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("影视详情");

        initViewPager();
        initListView();
        initPtr();
        requestData();
        requestStory();
        requestDynamic("0");
    }

    private void initViewPager() {
        pagerAdapter = new ReadingPagerAdapter();
        view_pager.setAdapter(pagerAdapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    video_player.setVisibility(View.GONE);
                } else {
                    video_player.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListView() {
        //设置头
        ((LinearLayout) header_view.getParent()).removeView(header_view);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        header_view.setLayoutParams(params);
        list_view.addHeaderView(header_view);

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
        String path = MessageFormat.format(HttpUtils.MOVIE_DYNAMIC, movieId, dynamicId);

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
                        if (null != list_ptr) {
                            list_ptr.refreshComplete();
                        }
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //内容填充
    private void requestData() {
        String path = MessageFormat.format(HttpUtils.MOVIE_DETAIL, movieId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<MovieDetailVo>() {
                    @Override
                    public void onResponse(MovieDetailVo response, int id) {
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

    //请求故事
    private void requestStory() {
        String path = MessageFormat.format(HttpUtils.MOVIE_STORY, movieId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<MovieStoryVo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(MovieStoryVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            if (null != response.getData()) {
                                fillStory(response.getData().getData().get(0));
                            }
                        } else {
                            //请求返回错误
                        }
                    }
                });
    }

    private void fillStory(MovieStoryVo.DataBeanX.DataBean vo) {
        movie_title.setText(vo.getTitle());
        movie_author.setText("文 /" + vo.getUser().getUser_name());

        Document document = Jsoup.parse(vo.getContent());
        Elements elements = document.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }

        String string1 = document.toString();
        String string2 = string1.replace("width:394px", "width:100%");
        String string3 = string2.replace("w/394", "w/344");
        String string4 = string3.replace("<br>", "<br><br>");
        String hp_content = string4.replace("100%\"><img", "100%\"><br><br><img");
        movie_content.loadDataWithBaseURL(null, hp_content, "text/html", "utf-8", null);
        charge_edt.setText(vo.getCharge_edt() + "    " + vo.getEditor_email());
        copyright.setText(vo.getCopyright());

        if (null != vo.getUser()) {
            author_des.setText(vo.getUser().getDesc());
            author_name.setText(vo.getUser().getUser_name() + "    " + vo.getUser().getWb_name());
            if (null != vo.getUser().getWeb_url() && !"".equals(vo.getUser().getWeb_url())) {
                Picasso.with(mActivity).load(vo.getUser().getWeb_url()).transform(new CircleTransform()).into(author_image);
            } else {
                Picasso.with(mActivity).load(R.mipmap.user_default_image).into(author_image);
            }
        }
    }

    private void fillData(final MovieDetailVo.DataBean vo) {
        movie_sub_title.setText("· " + vo.getTitle() + " ·");
        praise_comment_text.setText(vo.getSharenum() + " 喜欢    ·    " + vo.getCommentnum() + " 评论");

        if (null != vo.getPhoto() && vo.getPhoto().size() > 0) {
            List<ReadingBannerListVo.DataBean> list = new ArrayList<>(0);
            ReadingBannerListVo.DataBean bean = new ReadingBannerListVo.DataBean();
            bean.setCover(vo.getDetailcover());
            list.add(bean);
            for (String url : vo.getPhoto()) {
                bean = new ReadingBannerListVo.DataBean();
                bean.setCover(url);
                list.add(bean);
            }
            pagerAdapter.setImages(list);
        } else {
            view_pager.setVisibility(View.GONE);
        }

        if (null == vo.getVideo() || "".equals(vo.getVideo())) {
            //当前没有视频
            video_player.setVisibility(View.GONE);
        } else {
            video_player.setVisibility(View.VISIBLE);
            video_player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoviePlayActivity.startThis(mActivity, vo.getVideo(), vo.getTitle());
                }
            });
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
