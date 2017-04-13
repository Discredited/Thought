package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.ImageTextVo;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

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

        requestData();
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
        one_date.setText(DateTools.dateString(vo.getWeather().getDate()));
        one_position.setText(vo.getWeather().getClimate() + "    " + vo.getWeather().getCity_name());

        one_number.setText(vo.getVolume());
        one_draw.setText(vo.getTitle() + "  |  " + vo.getPic_info());
        one_content.setText(vo.getForward());
        one_author.setText(vo.getWords_info());
        laud_count.setText(vo.getLike_count() + "");

        Picasso.with(mActivity).load(vo.getImg_url()).into(one_img);
    }

    @OnClick({R.id.title_img_left, R.id.diary_layout, R.id.laud_layout, R.id.share_layout})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.diary_layout:
                Toast.makeText(mActivity, "打开小记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.laud_layout:
                if (laud){
                    laud_img.setImageResource(R.mipmap.laud);
                    laud_count.setText((Integer.parseInt(laud_count.getText().toString()) - 1) + "");
                    laud = false;
                }else {
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

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
