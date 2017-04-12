package com.project.june.thought.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.ThoughtApplication;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.UserEntry;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.customview.ObservableScrollView;
import com.project.xujun.juneutils.customview.ScaleImageView;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserInformationActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_layout)
    RelativeLayout title_layout;
    @InjectView(R.id.user_image)
    ImageView user_image;
    @InjectView(R.id.user_name)
    TextView user_name;
    @InjectView(R.id.diary_image)
    ScaleImageView diary_image;
    @InjectView(R.id.music_image)
    ImageView music_image;
    @InjectView(R.id.scroll_view)
    ObservableScrollView scroll_view;
    @InjectView(R.id.user_like_layout)
    LinearLayout user_like_layout;
    @InjectView(R.id.user_information_layout)
    LinearLayout user_information_layout;
    @InjectView(R.id.user_collect_layout)
    LinearLayout user_collect_layout;

    public static void startThis(Context context, String userId) {
        Intent intent = new Intent(context, UserInformationActivity.class);
        intent.putExtra("USER_ID", userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_information;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {
        title_layout.setBackgroundColor(Color.TRANSPARENT);

        UserEntry userEntry = ThoughtApplication.getUserEntry();
        if (null == userEntry) {
            //暂未登录
            title_center_text.setText("未登录");
            title_center_text.setVisibility(View.GONE);
            title_center_text.setTextColor(Color.parseColor("#999999"));
            user_name.setText("未登录");
            user_like_layout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, ViewUtils.dp2px(mActivity, 200));
            user_information_layout.setLayoutParams(params);
            user_collect_layout.setVisibility(View.GONE);
            Picasso.with(mActivity).load(R.mipmap.logo).transform(new CircleTransform()).into(user_image);
        } else {
            //已登录
            title_center_text.setText(userEntry.getName());
            title_center_text.setVisibility(View.GONE);
            title_center_text.setTextColor(Color.parseColor("#999999"));
            user_name.setText(userEntry.getName());
            user_like_layout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, ViewUtils.dp2px(mActivity, 420));
            user_information_layout.setLayoutParams(params);
            user_collect_layout.setVisibility(View.VISIBLE);
            Picasso.with(mActivity).load(ThoughtConfig.USER_PHOTO).transform(new CircleTransform()).into(user_image);
        }
        preInit();
    }

    private void preInit() {
        scroll_view.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
                //计算透明度
                int total = ViewUtils.dp2px(mActivity, 120);
                if (y <= total) {
                    if (y < 50) {
                        title_center_text.setVisibility(View.INVISIBLE);
                        title_layout.setBackgroundColor(Color.argb(0X00, 0XF5, 0XF5, 0XF5));
                    } else {
                        if (y > (total / 2)) {
                            title_center_text.setVisibility(View.VISIBLE);
                        } else {
                            title_center_text.setVisibility(View.INVISIBLE);
                        }
                        int temp = (int) (y * (255f / total));
                        title_layout.setBackgroundColor(Color.argb(temp, 0XF5, 0XF5, 0XF5));
                        title_center_text.setTextColor(Color.argb(temp, 0X99, 0X99, 0X99));
                    }
                } else {
                    title_center_text.setVisibility(View.VISIBLE);
                    title_layout.setBackgroundColor(Color.argb(0XFF, 0XF5, 0XF5, 0XF5));
                    title_center_text.setTextColor(Color.argb(0X99, 0X99, 0X99, 0X99));
                }
            }
        });
    }

    @OnClick({R.id.title_img_left,
            R.id.my_focus_layout,
            R.id.user_image,
            R.id.collect_img_txt_layout,
            R.id.collect_article_layout,
            R.id.collect_music_layout,
            R.id.collect_movie_layout,
            R.id.collect_config_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.user_image:
                if (null == ThoughtApplication.getUserEntry()){
                    //登录
                    LoginActivity.startThis(mActivity);
                }else {
                    //更换头像
                    Toast.makeText(mActivity, "暂不支持更换头像", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_focus_layout:
                break;
            case R.id.collect_img_txt_layout:
                break;
            case R.id.collect_article_layout:
                break;
            case R.id.collect_music_layout:
                break;
            case R.id.collect_movie_layout:
                break;
            case R.id.collect_config_layout:
                break;
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
