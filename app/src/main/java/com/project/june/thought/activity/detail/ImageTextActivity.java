package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.ThoughtApplication;
import com.project.june.thought.activity.common.EditDiaryActivity;
import com.project.june.thought.activity.common.ShowImageActivity;
import com.project.june.thought.activity.user.LoginActivity;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.ImageTextVo;
import com.project.june.thought.model.UserEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.otherutils.RxBus;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.text.MessageFormat;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 图文往期列表
 */
public class ImageTextActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_img_right)
    ImageView title_img_right;
    @InjectView(R.id.one_date)
    TextView one_date;
    @InjectView(R.id.one_position)
    TextView one_position;
    @InjectView(R.id.one_img)
    ImageView one_img;
    @InjectView(R.id.one_number)
    TextView one_number;
    @InjectView(R.id.one_draw)
    TextView one_draw;
    @InjectView(R.id.one_content)
    TextView one_content;
    @InjectView(R.id.one_author)
    TextView one_author;
    @InjectView(R.id.laud_img)
    ImageView laud_img;
    @InjectView(R.id.laud_count)
    TextView laud_count;

    private String id;
    private boolean laud = false;
    private boolean isCollect = false;
    private ImageTextVo.DataBean imageText;
    private CollectAndLaudVo first;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, ImageTextActivity.class);
        intent.putExtra("IMAGE_TEXT_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_image_text;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);

        id = getIntent().getStringExtra("IMAGE_TEXT_ID");
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("图文");
        title_img_right.setVisibility(View.VISIBLE);
        checkIsCollect(id);
        requestData();
    }

    private void checkIsCollect(String id) {
        //在数据库中查找是否已收藏
        try {
            Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
            WhereBuilder wb = WhereBuilder.b().and("itemId", "=", id);
            selector.where(wb);
            this.first = selector.findFirst();
            if (null == this.first) {
                //未收藏
                title_img_right.setImageResource(R.mipmap.save);
                isCollect = false;
            } else {
                //已收藏
                title_img_right.setImageResource(R.mipmap.icon_delete);
                isCollect = true;
            }
        } catch (DbException e) {
            Toast.makeText(this, "查询收藏失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.IMAGE_TEXT_DETAIL, id);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<ImageTextVo>() {

                    @Override
                    public void onResponse(ImageTextVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            if (null != response.getData()) {
                                fillData(response.getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }
                });
    }

    private void fillData(ImageTextVo.DataBean vo) {
        imageText = vo;

        one_date.setText(DateTools.dateString(vo.getWeather().getDate()));
        one_position.setText(vo.getWeather().getClimate() + "    " + vo.getWeather().getCity_name());

        one_number.setText(vo.getVolume());
        one_draw.setText(vo.getTitle() + "  |  " + vo.getPic_info());
        one_content.setText(vo.getForward());
        one_author.setText(vo.getWords_info());
        laud_count.setText(vo.getLike_count() + "");

        Picasso.with(mActivity).load(vo.getImg_url()).into(one_img);
    }

    @OnClick({R.id.title_img_left, R.id.title_img_right, R.id.one_img, R.id.diary_layout, R.id.laud_layout, R.id.share_layout})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.title_img_right:
                collectImageText();
                break;
            case R.id.one_img:
                ShowImageActivity.startThis(mActivity, imageText.getImg_url());
                break;
            case R.id.diary_layout:
                if (null != ThoughtApplication.getUserEntry()) {
                    EditDiaryActivity.startThis(mActivity, imageText.getItem_id());
                } else {
                    //还未登录
                    LoginActivity.startThis(this);
                }
                break;
            case R.id.laud_layout:
                if (laud) {
                    laud_img.setImageResource(R.mipmap.laud);
                    laud_count.setText((Integer.parseInt(laud_count.getText().toString()) - 1) + "");
                    laud = false;
                } else {
                    laud_img.setImageResource(R.mipmap.laud_selected);
                    laud_count.setText((Integer.parseInt(laud_count.getText().toString()) + 1) + "");
                    laud = true;
                }
                break;
            case R.id.share_layout:
                Toast.makeText(mActivity, "打开分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void collectImageText() {
        if (null == ThoughtApplication.getUserEntry()) {
            //请登录
            LoginActivity.startThis(mActivity);
        } else {
            if (isCollect) {
                //已收藏 删除
                if (null != first) {
                    try {
                        JuneToolsApp.getDbManager().delete(this.first);
                        title_img_right.setImageResource(R.mipmap.save);
                        isCollect = false;
                        Toast.makeText(mActivity, "删除图文成功", Toast.LENGTH_SHORT).show();
                    } catch (DbException e) {
                        Toast.makeText(mActivity, "删除收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                //未收藏 保存
                CollectAndLaudVo vo = new CollectAndLaudVo();
                vo.setCategory(ThoughtConfig.IMAGE_TEXT_CATEGORY);
                vo.setItemId(imageText.getItem_id());
                vo.setTitle(imageText.getForward());
                vo.setSummary(imageText.getWords_info());
                try {
                    JuneToolsApp.getDbManager().save(vo);
                    title_img_right.setImageResource(R.mipmap.icon_delete);
                    isCollect = true;
                    Toast.makeText(mActivity, "保存图文成功", Toast.LENGTH_SHORT).show();
                } catch (DbException e) {
                    Toast.makeText(mActivity, "图文收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
            RxCollectListChange content = new RxCollectListChange();
            content.setCategory(ThoughtConfig.IMAGE_TEXT_CATEGORY);
            RxBus.get().post(content);
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
