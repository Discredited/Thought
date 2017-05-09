package com.project.june.thought.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.ThoughtApplication;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.AuthorEntry;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.UserEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.june.thought.rx.RxUserLogin;
import com.project.june.thought.rx.RxUserLogout;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.customview.ObservableScrollView;
import com.project.xujun.juneutils.customview.ScaleImageView;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.otherutils.RxBus;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.squareup.picasso.Picasso;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    @InjectView(R.id.scroll_view)
    ObservableScrollView scroll_view;
    @InjectView(R.id.user_like_layout)
    LinearLayout user_like_layout;
    @InjectView(R.id.user_information_layout)
    LinearLayout user_information_layout;
    @InjectView(R.id.user_collect_layout)
    LinearLayout user_collect_layout;
    @InjectView(R.id.image_text_number)
    TextView image_text_number;
    @InjectView(R.id.article_number)
    TextView article_number;
    @InjectView(R.id.focus_number)
    TextView focus_number;
    @InjectView(R.id.music_number)
    TextView music_number;
    @InjectView(R.id.movie_number)
    TextView movie_number;

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
        bindRx();
        title_layout.setBackgroundColor(Color.TRANSPARENT);
        preInit();
        login();
    }

    private Observable<RxUserLogin> userLoginObservable;
    private Observable<RxUserLogout> userLogoutObservable;
    private Observable<RxCollectListChange> collectListChangeObservable;

    private void bindRx() {
        userLoginObservable = RxBus.get().register(RxUserLogin.class);
        userLoginObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxUserLogin>() {
            @Override
            public void call(RxUserLogin rxUserLogin) {
                login();
            }
        });

        userLogoutObservable = RxBus.get().register(RxUserLogout.class);
        userLogoutObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxUserLogout>() {
            @Override
            public void call(RxUserLogout rxUserLogout) {
                login();
            }
        });

        collectListChangeObservable = RxBus.get().register(RxCollectListChange.class);
        collectListChangeObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxCollectListChange>() {
            @Override
            public void call(RxCollectListChange rx) {
                collectChange(rx.getCategory());
            }
        });
    }

    private void collectChange(String category) {
        switch (category) {
            case "0":
                //图文
                image_text_number.setText(getCollectCount(category));
                break;
            case "1":
                //阅读
                article_number.setText(getCollectCount(category));
                break;
            case "4":
                //音乐
                music_number.setText(getCollectCount(category));
                break;
            case "5":
                //影视
                movie_number.setText(getCollectCount(category));
                break;
            case ThoughtConfig.FOCUS_CATEGORY:
                //关注
                focus_number.setText(getCollectCount(category));
                break;
        }
    }

    private void login() {
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

        image_text_number.setText(getCollectCount("0"));
        article_number.setText(getCollectCount("1"));
        music_number.setText(getCollectCount("4"));
        movie_number.setText(getCollectCount("5"));
        focus_number.setText(getCollectCount("200"));
    }

    private void logout() {
        //退出登录，清除数据库表
        try {
            JuneToolsApp.getDbManager().delete(UserEntry.class);
            RxBus.get().post(new RxUserLogout());
        } catch (DbException e) {
            Log.d("sherry", "删除用户数据异常");
        }
    }

    private String getCollectCount(String category) {
        if (null != category && !"".equals(category)) {
            if (category.equals("200")) {
                try {
                    List<AuthorEntry> all = JuneToolsApp.getDbManager().findAll(AuthorEntry.class);
                    if (null != all && all.size() >= 0) {
                        return all.size() + "";
                    } else {
                        return "0";
                    }
                } catch (DbException e) {
                    return "GG";
                }
            } else {
                try {
                    Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
                    WhereBuilder wb = WhereBuilder.b().and("category", "=", category);
                    selector.where(wb);
                    List<CollectAndLaudVo> all = selector.findAll();

                    if (null != all && all.size() >= 0) {
                        return all.size() + "";
                    } else {
                        return "0";
                    }
                } catch (DbException e) {
                    return "GG";
                }
            }
        }
        return "0";
    }

    @OnClick({R.id.title_img_left,
            R.id.my_focus_layout,
            R.id.user_image,
            R.id.user_diary_layout,
            R.id.user_music_layout,
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
                if (null == ThoughtApplication.getUserEntry()) {
                    //登录
                    LoginActivity.startThis(mActivity);
                } else {
                    //更换头像
                    Toast.makeText(mActivity, "暂不支持更换头像", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_focus_layout:
                break;
            case R.id.user_diary_layout:
                CollectionListActivity.startThis(mActivity, ThoughtConfig.DIARY_CATEGORY);
                break;
            case R.id.user_music_layout:
                Toast.makeText(mActivity, "暂未开启歌单，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.collect_img_txt_layout:
                CollectionListActivity.startThis(mActivity, ThoughtConfig.IMAGE_TEXT_CATEGORY);
                break;
            case R.id.collect_article_layout:
                CollectionListActivity.startThis(mActivity, ThoughtConfig.READING_CATEGORY);
                break;
            case R.id.collect_music_layout:
                CollectionListActivity.startThis(mActivity, ThoughtConfig.MUSIC_CATEGORY);
                break;
            case R.id.collect_movie_layout:
                CollectionListActivity.startThis(mActivity, ThoughtConfig.VIDEO_CATEGORY);
                break;
            case R.id.collect_config_layout:
                //暂时座位退出登录
                AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                dialog.setTitle("退出提示");
                dialog.setMessage("确定要退出登录？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出登录
                        logout();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(RxUserLogin.class, userLoginObservable);
        RxBus.get().unregister(RxUserLogout.class, userLogoutObservable);
        RxBus.get().unregister(RxCollectListChange.class, collectListChangeObservable);
    }
}
