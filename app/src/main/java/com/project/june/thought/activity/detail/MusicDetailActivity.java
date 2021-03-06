package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.adapter.list.DynamicAdapter;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.MusicDetailVo;
import com.project.june.thought.utils.BottomUtils;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

public class MusicDetailActivity extends BaseActivity {

    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.title_img_left)
    ImageButton title_img_left;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_img_right)
    ImageButton title_img_right;
    @InjectView(R.id.music_image)
    ImageView music_image;
    @InjectView(R.id.music_sub_title)
    TextView music_sub_title;
    @InjectView(R.id.music_play)
    ImageView music_play;
    @InjectView(R.id.music_title)
    TextView music_title;
    @InjectView(R.id.music_author)
    TextView music_author;
    @InjectView(R.id.music_content)
    WebView music_content;
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
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;
    @InjectView(R.id.praise_comment_text)
    TextView praise_comment_text;
    @InjectView(R.id.collect_image)
    ImageButton collect_image;
    @InjectView(R.id.laud_image)
    ImageButton laud_image;
    @InjectView(R.id.comment_image)
    ImageButton comment_image;

    private String musicId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;
    private boolean isPlayMusic = false;
    private Animation animation;
    private MediaPlayer player;

    public static void startThis(Context context, String musicId) {
        Intent intent = new Intent(context, MusicDetailActivity.class);
        intent.putExtra("MUSIC_ID", musicId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_music_detail;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        musicId = getIntent().getStringExtra("MUSIC_ID");
        if (null == musicId) {
            finish();
            return;
        }
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("音乐详情");

        initListView();
        initPtr();
        requestData();
        requestDynamic("0");
        initMusicPlay();
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
                adapter.getItems().clear();
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
        String path = MessageFormat.format(HttpUtils.MUSIC_DYNAMIC, musicId, dynamicId);

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
        String path = MessageFormat.format(HttpUtils.MUSIC_DETAIL, musicId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<MusicDetailVo>() {
                    @Override
                    public void onResponse(MusicDetailVo response, int id) {
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

    private void fillData(MusicDetailVo.DataBean vo) {
        music_title.setText(vo.getStory_title());
        music_sub_title.setText("· " + vo.getTitle() + " ·" + "\n" + vo.getAlbum());
        if (null != vo.getAuthor()) {
            music_author.setText("文 / " + vo.getStory_author().getUser_name());
            author_des.setText(vo.getStory_author().getDesc());
            author_name.setText(vo.getStory_author().getUser_name() + "    " + vo.getStory_author().getWb_name());
            if (null != vo.getAuthor().getWeb_url() && !"".equals(vo.getAuthor().getWeb_url())) {
                Picasso.with(mActivity).load(vo.getAuthor().getWeb_url()).transform(new CircleTransform()).into(author_image);
            } else {
                Picasso.with(mActivity).load(R.mipmap.user_default_image).into(author_image);
            }
        }

        Picasso.with(mActivity).load(vo.getCover()).transform(new CircleTransform()).into(music_image);

        praise_comment_text.setText(vo.getPraisenum() + " 喜欢    ·    " + vo.getCommentnum() + " 评论");

        Document document = Jsoup.parse(vo.getStory());
        Elements elements = document.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }

        String string1 = document.toString();
        String string2 = string1.replace("width:394px", "width:100%");
        String string3 = string2.replace("w/394", "w/344");
        String string4 = string3.replace("<br>", "<br><br>");
        String hp_content = string4.replace("100%\"><img", "100%\"><br><br><img");
        music_content.loadDataWithBaseURL(null, hp_content, "text/html", "utf-8", null);
        charge_edt.setText(vo.getCharge_edt() + "    " + vo.getEditor_email());
        copyright.setText(vo.getCopyright());

        CollectAndLaudVo bean = new CollectAndLaudVo();
        bean.setItemId(vo.getId());
        bean.setCategory(ThoughtConfig.MUSIC_CATEGORY);
        bean.setCollect(vo.getCollect());
        bean.setLaud(vo.getLaud());
        bean.setTitle(vo.getTitle());
        bean.setSummary(vo.getStory_summary());
        bean.setLaudNumber(vo.getPraisenum());
        bean.setCommentNumber(vo.getCommentnum());
        BottomUtils.bottomUtils(mActivity, collect_image, laud_image, bean, comment_image, praise_comment_text, adapter);
    }

    private void initMusicPlay() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(10 * 1000);
        animation.setRepeatCount(-1);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        animation.setRepeatMode(Animation.RESTART);

        player = MediaPlayer.create(this, R.raw.prisoner);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayMusic) {
                    //停止音乐
                    music_play.setImageResource(R.mipmap.detail_play);
                    animation.cancel();
                    if (null != player && player.isPlaying()) {
                        player.pause();
                        isPlayMusic = false;
                    }
                } else {
                    //开始播放音乐
                    music_play.setImageResource(R.mipmap.detail_stop);
                    music_image.startAnimation(animation);//开始动画
                    //这句话很重要，不然MediaPlayer会报状态异常的错误
                    if (null != player) {
                        player.stop();
                    }
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (null != player && !player.isPlaying()) {
                                player.start();
                                isPlayMusic = true;
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != player) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
