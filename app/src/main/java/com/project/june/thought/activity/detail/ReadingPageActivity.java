package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.ReadingPageDetailVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

public class ReadingPageActivity extends BaseActivity {

    @InjectView(R.id.title_img_left)
    ImageButton title_img_left;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_layout)
    RelativeLayout title_layout;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.text)
    TextView text;
    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.footer_layout)
    LinearLayout footer_layout;
    @InjectView(R.id.activity_reading_page)
    LinearLayout activity_reading_page;
    private String itemId, titleString, imageUrl, bottomText, backColor;
    private JuneBaseAdapter<ReadingPageDetailVo.DataBean> adapter;

    public static void startThis(Context context, String id, String titleString, String bottomText, String imageUrl, String bgColor) {
        Intent intent = new Intent(context, ReadingPageActivity.class);
        intent.putExtra("ITEM_ID", id);
        intent.putExtra("TITLE", titleString);
        intent.putExtra("BOTTOM", bottomText);
        intent.putExtra("URL", imageUrl);
        intent.putExtra("COLOR", bgColor);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reading_page;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        itemId = getIntent().getStringExtra("ITEM_ID");
        titleString = getIntent().getStringExtra("TITLE");
        imageUrl = getIntent().getStringExtra("URL");
        bottomText = getIntent().getStringExtra("BOTTOM");
        backColor = getIntent().getStringExtra("COLOR");
    }

    @Override
    protected void logicProgress() {
        title_center_text.setTextColor(Color.WHITE);
        title_img_left.setImageResource(R.mipmap.icon_back_white);
        title_layout.setBackgroundColor(Color.TRANSPARENT);
        title_center_text.setText(titleString);
        text.setText(bottomText);
        Picasso.with(mActivity).load(imageUrl).into(image);
        activity_reading_page.setBackgroundColor(Color.parseColor(backColor));

        initListView();
        requestData();
    }

    private void initListView() {
        adapter = new JuneBaseAdapter<ReadingPageDetailVo.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_reading_page, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, ReadingPageDetailVo.DataBean itemData) {
                TextView title = JuneViewHolder.get(convertView, R.id.title);
                TextView author = JuneViewHolder.get(convertView, R.id.author);
                TextView summary = JuneViewHolder.get(convertView, R.id.summary);

                title.setText((position + 1) + "  " + itemData.getTitle());
                author.setText("      " + itemData.getAuthor());
                summary.setText("      " + itemData.getIntroduction());
            }
        };
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < adapter.getItems().size()) {
                    ReadingPageDetailVo.DataBean dataBean = adapter.getItems().get(position);
                    if (dataBean.getType().equals("1")) {
                        //阅读详情
                        ReadingDetailActivity.startThis(mActivity, dataBean.getItem_id());
                    } else {
                        SerializeDetailActivity.startThis(mActivity, dataBean.getItem_id());
                    }
                }
            }
        });

        //添加footer
        ((LinearLayout) footer_layout.getParent()).removeView(footer_layout);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        footer_layout.setLayoutParams(params);
        list_view.addFooterView(footer_layout);
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.READING_PAGER, itemId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<ReadingPageDetailVo>() {
                    @Override
                    public void onResponse(ReadingPageDetailVo response, int id) {
                        super.onResponse(response, id);
                        if (null != response) {
                            List<ReadingPageDetailVo.DataBean> data = response.getData();
                            adapter.getItems().addAll(data);
                            adapter.notifyDataSetChanged();
                            footer_layout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }
                });
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
