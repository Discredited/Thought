package com.project.june.thought.activity.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.ThoughtApplication;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DiaryEntry;
import com.project.june.thought.model.ImageTextVo;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.otherutils.RxBus;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.JuneToolsApp;
import org.xutils.ex.DbException;

import java.text.MessageFormat;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class EditDiaryActivity extends BaseActivity {
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_img_right)
    ImageButton title_img_right;
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
    EditText one_content;
    @InjectView(R.id.one_author)
    TextView one_author;
    private String itId;
    private ImageTextVo.DataBean image_text;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_diary;
    }

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, EditDiaryActivity.class);
        intent.putExtra("IMAGE_TEXT", id);
        context.startActivity(intent);
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        itId = getIntent().getStringExtra("IMAGE_TEXT");
        if (null == itId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("小记");
        title_img_right.setImageResource(R.mipmap.save);
        title_img_right.setVisibility(View.VISIBLE);
        requestData();
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.IMAGE_TEXT_DETAIL, itId);
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
        image_text = vo;
        one_date.setText(DateTools.dateString(vo.getWeather().getDate()));
        one_position.setText(vo.getWeather().getClimate() + "    " + vo.getWeather().getCity_name());
        Picasso.with(mActivity).load(vo.getImg_url()).into(one_img);
        one_number.setText(vo.getVolume());
        one_draw.setText(vo.getTitle() + "  |  " + vo.getPic_info());
        one_content.setText(vo.getForward());
        one_content.setSelection(one_content.getText().length());
        one_author.setText(ThoughtApplication.getUserEntry().getName());
    }

    private void saveDiary() {
        String string = one_content.getText().toString();
        if (null == string) {
            Toast.makeText(mActivity, "空的文本不能提交哦", Toast.LENGTH_SHORT).show();
            return;
        } else if (image_text.getForward().equals(string)) {
            Toast.makeText(mActivity, "什么都还没写呐", Toast.LENGTH_SHORT).show();
            return;
        }

        DiaryEntry entry = new DiaryEntry();
        entry.setItemId(image_text.getItem_id());
        entry.setCategory(ThoughtConfig.DIARY_CATEGORY);
        entry.setDate(DateTools.dateString(image_text.getWeather().getDate()));
        entry.setPosition(image_text.getWeather().getClimate() + "    " + image_text.getWeather().getCity_name());
        entry.setNumber(image_text.getVolume());
        entry.setImage(image_text.getImg_url());
        entry.setDraw(image_text.getTitle() + "  |  " + image_text.getPic_info());
        entry.setContent(string);
        entry.setAuthor(ThoughtApplication.getUserEntry().getName());

        //开始保存
        try {
            JuneToolsApp.getDbManager().save(entry);
            RxCollectListChange rxCollectListChange = new RxCollectListChange();
            rxCollectListChange.setCategory(ThoughtConfig.DIARY_CATEGORY);
            RxBus.get().post(rxCollectListChange);
            Toast.makeText(mActivity, "小记保存成功", Toast.LENGTH_SHORT).show();
        } catch (DbException e) {
            Toast.makeText(mActivity, "GG,小记保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.title_img_left, R.id.title_img_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.title_img_right:
                saveDiary();
                break;
        }
    }
}
