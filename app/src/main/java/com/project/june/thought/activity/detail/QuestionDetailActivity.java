package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.QuestionDetailVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import okhttp3.Call;

public class QuestionDetailActivity extends BaseActivity {

    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.header_view)
    View header_view;
    @InjectView(R.id.question_title)
    TextView question_title;
    @InjectView(R.id.question_content)
    TextView question_content;
    @InjectView(R.id.question_ask)
    TextView question_ask;
    @InjectView(R.id.question_answer)
    TextView question_answer;
    @InjectView(R.id.answer_content)
    TextView answer_content;
    @InjectView(R.id.charge_edt)
    TextView charge_edt;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;

    private String questionId;
    private JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra("QUESTION_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        questionId = getIntent().getStringExtra("QUESTION_ID");
        if (null == questionId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        initListView();
        requestData();
        requestDynamic("0");
    }

    private void initListView() {
        //设置头
        ((RelativeLayout) header_view.getParent()).removeView(header_view);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        header_view.setLayoutParams(params);
        list_view.addHeaderView(header_view);

        adapter = new JuneBaseAdapter<DynamicVo.DataBeanX.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_dynamic, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, DynamicVo.DataBeanX.DataBean itemData) {
                ImageView dynamic_image = JuneViewHolder.get(convertView, R.id.dynamic_image);
                TextView praise_name = JuneViewHolder.get(convertView, R.id.praise_name);
                TextView praise_time = JuneViewHolder.get(convertView, R.id.praise_time);
                TextView praise_content = JuneViewHolder.get(convertView, R.id.praise_content);
                TextView praise_count = JuneViewHolder.get(convertView, R.id.praise_count);

                LinearLayout reply_layout = JuneViewHolder.get(convertView, R.id.reply_layout);
                TextView reply_content = JuneViewHolder.get(convertView, R.id.reply_content);

                if (null == itemData.getUser().getWeb_url() || itemData.getUser().getWeb_url().isEmpty()) {
                    Picasso.with(mActivity).load(R.mipmap.user_default_image).transform(new CircleTransform()).into(dynamic_image);
                } else {
                    Picasso.with(mActivity).load(itemData.getUser().getWeb_url()).transform(new CircleTransform()).into(dynamic_image);
                }

                if (null != itemData.getQuote() && null != itemData.getTouser()){
                    //存在评论
                    reply_layout.setVisibility(View.VISIBLE);
                    reply_content.setText(itemData.getTouser().getUser_name() + " : " + itemData.getQuote());
                }else {
                    //不存在评论
                    reply_layout.setVisibility(View.GONE);
                }

                praise_name.setText(itemData.getUser().getUser_name());
                praise_time.setText(itemData.getCreated_at());
                praise_content.setText(itemData.getContent());
                praise_count.setText(itemData.getPraisenum() + "");
            }
        };
        list_view.setAdapter(adapter);
    }

    //请求动态列表
    private void requestDynamic(String dynamicId) {
        String path = MessageFormat.format(HttpUtils.QUESTION_DYNAMIC, questionId, dynamicId);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<DynamicVo>() {
                    @Override
                    public void onResponse(DynamicVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            if (null != response.getData() && null != response.getData().getData() && response.getData().getData().size() > 0) {
                                adapter.getItems().addAll(response.getData().getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            //请求失败
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, ThoughtConfig.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //内容填充
    private void requestData() {
        String path = MessageFormat.format(HttpUtils.QUESTION_DETAIL, questionId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<QuestionDetailVo>() {
                    @Override
                    public void onResponse(QuestionDetailVo response, int id) {
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

    private void fillData(QuestionDetailVo.DataBean vo) {
        question_title.setText(vo.getQuestion_title());
        question_content.setText(vo.getQuestion_content());
        question_ask.setText("——" + vo.getAsker().getUser_name());
        question_answer.setText(vo.getAnswerer().getUser_name());
        answer_content.setText(Html.fromHtml(vo.getAnswer_content()));
        charge_edt.setText(vo.getCharge_edt());
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
