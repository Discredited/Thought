package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DiaryEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.june.thought.utils.DateTools;
import com.project.xujun.juneutils.otherutils.RxBus;
import com.squareup.picasso.Picasso;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import butterknife.InjectView;
import butterknife.OnClick;

public class DiaryDetailActivity extends BaseActivity {

    @InjectView(R.id.title_img_left)
    ImageButton title_img_left;
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
    TextView one_content;
    @InjectView(R.id.one_author)
    TextView one_author;
    @InjectView(R.id.bottom_layout)
    RelativeLayout bottom_layout;
    private long diaryId;
    private DiaryEntry diary;

    public static void startThis(Context context, Long id) {
        Intent intent = new Intent(context, DiaryDetailActivity.class);
        intent.putExtra("DIARY_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        diaryId = getIntent().getLongExtra("DIARY_ID", -1l);
        if (-1l == diaryId) {
            finish();
            return;
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_diary_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText("小记详情");
        title_img_right.setImageResource(R.mipmap.icon_delete);
        bottom_layout.setVisibility(View.GONE);
        requestData();
    }

    private void requestData() {
        try {
            Selector<DiaryEntry> selector = JuneToolsApp.getDbManager().selector(DiaryEntry.class);
            WhereBuilder whereBuilder = WhereBuilder.b().and("id", "=", diaryId);
            selector.where(whereBuilder);
            DiaryEntry first = selector.findFirst();
            if (null != first) {
                title_img_right.setVisibility(View.VISIBLE);
                fillData(first);
            } else {
                title_img_right.setVisibility(View.GONE);
                Toast.makeText(mActivity, "该小记已删除", Toast.LENGTH_SHORT).show();
            }
        } catch (DbException e) {
        }
    }

    private void fillData(DiaryEntry entry) {
        diary = entry;
        one_date.setText(entry.getDate());
        one_position.setText(entry.getPosition());
        one_number.setText(entry.getNumber());
        one_draw.setText(entry.getDraw());
        one_content.setText(entry.getContent());
        one_author.setText(entry.getAuthor());

        Picasso.with(mActivity).load(entry.getImage()).into(one_img);
    }

    @OnClick({R.id.title_img_left, R.id.title_img_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.title_img_right:
                deleteDiary();
                break;
        }
    }

    private void deleteDiary() {
        try {
            JuneToolsApp.getDbManager().delete(diary);
            Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
            RxCollectListChange content = new RxCollectListChange();
            content.setCategory("100");
            RxBus.get().post(content);
            finish();
        } catch (DbException e) {
            Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
