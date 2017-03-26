package com.project.june.thought.activity.index;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.MusicListVo;
import com.project.june.thought.model.QuestionListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 问答往期列表
 */
public class QuestionListActivity extends BaseActivity {
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.list_view)
    ListView list_view;

    private String title;
    private String dateString;
    private JuneBaseAdapter<QuestionListVo.DataBean> adapter;

    public static void startThis(Context context, String title, String date) {
        Intent intent = new Intent(context, QuestionListActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("REQUEST_DATE", date);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_no_ptr_list_view;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        title = getIntent().getStringExtra("TITLE");
        dateString = getIntent().getStringExtra("REQUEST_DATE");
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText(title);

        initListView();
        requestData();
    }

    private void initListView() {
        adapter = new JuneBaseAdapter<QuestionListVo.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_text_content, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, QuestionListVo.DataBean itemData) {
                TextView text_title = JuneViewHolder.get(convertView, R.id.text_title);
                TextView text_author = JuneViewHolder.get(convertView, R.id.text_author);
                TextView text_content = JuneViewHolder.get(convertView, R.id.text_content);

                text_title.setText(itemData.getQuestion_title());
                text_author.setVisibility(View.GONE);
                text_content.setVisibility(View.GONE);

            }
        };
        list_view.setAdapter(adapter);
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.TO_LIST_QUESTION, dateString);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<QuestionListVo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(QuestionListVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            //请求成功
                            if (null != response.getData() && response.getData().size() > 0) {
                                adapter.getItems().addAll(response.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            //数据响应错误
                        }
                    }
                });
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}