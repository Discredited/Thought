package com.project.xujun.juneutils.media;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.project.xujun.juneutils.R;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;


/**
 * 直播控件
 * Created by June on 2017/3/28.
 */

public class VideoPlayView extends FrameLayout implements ITXLivePlayListener{
    private AppCompatActivity mContext;
    //播放器
    private TXCloudVideoView video_view;
    private TXLivePlayer mLivePlayer;

    private View root_video_container;
    //菊花
    private View video_loading_progress_view;
    private ProgressBar progress_bar;
    private TextView play_message;
    //标题栏
    private View v_title_layout;
    private ImageView video_back_button;
    //声音和亮度调节
    private View v_l_layout;
    private TextView current_num;
    private TextView total_num;

    private int currentScreenLight;
    private AudioManager audioManager;

    private TextView live_title;

    ///////////////////////////构造方法 begin////////////////////////////////////
    public VideoPlayView(Context context) {
        this(context, null, -1);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = (AppCompatActivity) context;
        addView(LayoutInflater.from(context).inflate(R.layout.layout_live_telecast_view, this, false));
        bindViews();
        restTitleLayoutPadding();
        initViews();
        initPlayer();
    }
    ///////////////////////////构造方法 end////////////////////////////////////

    //绑定控件
    private void bindViews() {
        video_view = (TXCloudVideoView) findViewById(R.id.video_view);

        video_loading_progress_view = findViewById(R.id.video_loading_progress_view);
        video_loading_progress_view.setVisibility(View.VISIBLE);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        play_message = (TextView) findViewById(R.id.play_message);

        //标题栏
        root_video_container = findViewById(R.id.root_video_container);
        video_back_button = (ImageView) findViewById(R.id.video_back_button);

        v_title_layout = findViewById(R.id.v_title_layout);
        //音量和亮度调节
        v_l_layout = findViewById(R.id.v_l_layout);
        v_l_layout.setVisibility(View.INVISIBLE);
        current_num = (TextView) findViewById(R.id.current_num);
        total_num = (TextView) findViewById(R.id.total_num);

        live_title = ((TextView) findViewById(R.id.live_title));
    }

    private void initViews() {
        root_video_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewHelper.getTranslationY(v_title_layout) >= 0) {
                    hiddenControlLayout();
                } else {
                    showControlLayout();
                }
            }
        });

        video_back_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBack();
            }
        });

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        root_video_container.setOnTouchListener(rootOnTouchListener);
    }

    public void initPlayer() {
        mLivePlayer = new TXLivePlayer(getContext());
        mLivePlayer.setPlayListener(this);
        mLivePlayer.setPlayerView(video_view);
    }


    private void restTitleLayoutPadding() {
        if (Build.VERSION.SDK_INT >= 23) {
            v_title_layout.setPadding(0, ViewUtils.getStatusBarHeight(getContext()), 0, 0);
        } else {
            v_title_layout.setPadding(0, 0, 0, 0);
        }
    }

    public void playVideo(String url, String liveTitle) {
        live_title.setText(liveTitle);
        mLivePlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_VOD_MP4);
    }


    //显示控制条
    private void showControlLayout() {
        v_l_layout.removeCallbacks(autoHidden);
        v_l_layout.postDelayed(autoHidden, HIDDEN_DELAY);
        float translationY = ViewHelper.getTranslationY(v_title_layout);
        if (translationY < 0) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v_title_layout, "translationY", 0);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }
    }

    //隐藏控制条
    private void hiddenControlLayout() {
        float translationY = ViewHelper.getTranslationY(v_title_layout);
        if (translationY >= 0) {
            ObjectAnimator titleAn = ObjectAnimator.ofFloat(v_title_layout, "translationY", (float) (-v_title_layout.getHeight()));
            titleAn.setDuration(500);
            titleAn.start();
        }
    }


    private OnTouchListener rootOnTouchListener = new OnTouchListener() {
        float x1;
        float x2;
        float y1;
        float y2;
        float dx;
        float dy;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.x1 = motionEvent.getX();
                    this.y1 = motionEvent.getY();
                    if (scaleFrameLayoutTouch) {
                        scaleFrameLayoutTouch = false;
                        showControlLayout();
                    } else {
                        scaleFrameLayoutTouch = true;
                        hiddenControlLayout();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    v_l_layout.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.x2 = motionEvent.getX();
                    this.y2 = motionEvent.getY();
                    this.dx = this.x2 - this.x1;
                    this.dy = this.y2 - this.y1;
                    if (Math.abs(this.dx) > 50.0F || Math.abs(this.dy) > 50.0F) {
                        this.x1 = this.x2;
                        this.y1 = this.y2;
                        if (Math.abs(this.dx) < Math.abs(this.dy)) {
                            v_l_layout.setVisibility(View.VISIBLE);
                            if (this.x2 > root_video_container.getMeasuredWidth() / 2) {
                                if (this.dy > 0.0F) {
                                    decreaseVolume();
                                } else {
                                    increaseVolume();
                                }
                                current_num.setText(String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                                total_num.setText(String.valueOf(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
                            } else {
                                // 修改亮度
                                if (this.dy > 0 && Math.abs(this.dy) > 5) {
                                    currentScreenLight = currentScreenLight - 10;
                                    //降低亮度
                                } else if (this.dy < 0 && Math.abs(this.dy) > 5) {
                                    //增加亮度
                                    currentScreenLight = currentScreenLight + 10;
                                }
                                if (currentScreenLight < 0) {
                                    currentScreenLight = 0;
                                } else if (currentScreenLight > 254) {
                                    currentScreenLight = 254;
                                }
                                current_num.setText(currentScreenLight + "");
                                total_num.setText(254 + "");
                                //手机亮度设置
                                Window localWindow = mContext.getWindow();
                                WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
                                float f = currentScreenLight / 255.0F;
                                localLayoutParams.screenBrightness = f;
                                localWindow.setAttributes(localLayoutParams);
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    };

    private void increaseVolume() {
        this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    private void decreaseVolume() {
        this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    public void viewBack() {
        mContext.onBackPressed();
    }

    /////////////////////播放状态监听   activity生命周期监听///////////////////
    @Override
    public void onPlayEvent(int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
            video_loading_progress_view.setVisibility(View.VISIBLE);
            play_message.setVisibility(View.VISIBLE);
            play_message.setText("服务器连接成功");
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            //开始播放，隐藏菊花
            video_view.setKeepScreenOn(true);
            play_message.setVisibility(View.GONE);
            video_loading_progress_view.setVisibility(View.GONE);
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            video_view.setKeepScreenOn(false);
            String evt_description = bundle.getString("EVT_DESCRIPTION");
            if (evt_description.equals("step1：该流地址无视频")) {
                //暂无视频
                video_loading_progress_view.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
                play_message.setVisibility(View.VISIBLE);
                play_message.setText("主播还在休息中,请耐心等候...");
            } else {
                //网络连接失败，显示菊花
                video_loading_progress_view.setVisibility(View.VISIBLE);
                play_message.setVisibility(View.VISIBLE);
                play_message.setText("网络连接失败,请重试");
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            //视屏正在加载，显示菊花
            video_loading_progress_view.setVisibility(View.VISIBLE);
            play_message.setVisibility(View.VISIBLE);
            play_message.setText("视频正在加载...");
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            //播放结束
            Toast.makeText(mContext, "播放结束", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    private boolean onAttachedToWindow = false;
    private boolean scaleFrameLayoutTouch = false;
    private boolean mStartSeek = false;
    public static final int HIDDEN_DELAY = 4000;

    public Runnable autoHidden = new Runnable() {
        @Override
        public void run() {
            if (onAttachedToWindow) {
                if (scaleFrameLayoutTouch || mStartSeek) {
                    //自动再运行一次,直到调用hidden
                    v_l_layout.postDelayed(this, HIDDEN_DELAY);
                } else {
                    //调用隐藏
                    hiddenControlLayout();
                }
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        showControlLayout();
        onAttachedToWindow = true;
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        onAttachedToWindow = false;
        if (mLivePlayer != null) {
            mLivePlayer.pause();
            mLivePlayer.stopPlay(true);
        }
        super.onDetachedFromWindow();
    }

    public TXCloudVideoView getTXCloudVideoView() {
        return video_view;
    }
}