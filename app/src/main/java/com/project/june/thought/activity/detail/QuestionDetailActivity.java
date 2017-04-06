package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.project.june.thought.adapter.list.DynamicAdapter;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.QuestionDetailVo;
import com.project.june.thought.model.SerializeDetailVo;
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
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
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
    @InjectView(R.id.author_image)
    ImageView author_image;
    @InjectView(R.id.author_name)
    TextView author_name;
    @InjectView(R.id.author_des)
    TextView author_des;
    @InjectView(R.id.recommend_1_title)
    TextView recommend_1_title;
    @InjectView(R.id.recommend_1_author)
    TextView recommend_1_author;
    @InjectView(R.id.recommend_1_layout)
    LinearLayout recommend_1_layout;
    @InjectView(R.id.recommend_2_title)
    TextView recommend_2_title;
    @InjectView(R.id.recommend_2_author)
    TextView recommend_2_author;
    @InjectView(R.id.recommend_2_layout)
    LinearLayout recommend_2_layout;
    @InjectView(R.id.recommend_layout)
    LinearLayout recommend_layout;

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
        title_center_text.setText("问答 · 详情");

        initListView();
        initPtr();
        requestData();
        requestDynamic("0");
    }

    private void initListView() {
        //设置头
        ((LinearLayout) header_view.getParent()).removeView(header_view);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        header_view.setLayoutParams(params);
        list_view.addHeaderView(header_view);

        adapter = new DynamicAdapter(mActivity);
        list_view.setAdapter(adapter);
    }

    private void initPtr() {
        //下拉刷新控件
        list_ptr.setLastUpdateTimeRelateObject(this);
        list_ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestDynamic("0");
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                DynamicVo.DataBeanX.DataBean dataBean = adapter.getItems().get(adapter.getCount() - 1);
                requestDynamic(dataBean.getId());
            }
        });
        list_ptr.setResistance(1.7f);
        list_ptr.setRatioOfHeaderHeightToRefresh(1.2f);
        list_ptr.setDurationToClose(200);
        list_ptr.setDurationToCloseHeader(1000);
        //默认为false
        list_ptr.setPullToRefresh(false);
        list_ptr.setKeepHeaderWhenRefresh(true);
        //默认为true
        list_ptr.setmOnlyShowHeaderOrFooter(true);
        //默认为false
        list_ptr.disableWhenHorizontalMove(true);
        //默认没有任何加载方式
        list_ptr.setMode(PtrFrameLayout.Mode.BOTH);
        //list_ptr.autoRefresh(300);
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
                        list_ptr.refreshComplete();
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
                        list_ptr.refreshComplete();
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
        question_ask.setText("——" + vo.getAsker().getUser_name() + "问道");
        question_answer.setText(vo.getAnswerer().getUser_name() + "答：");
        answer_content.setText(Html.fromHtml(vo.getAnswer_content()));
        charge_edt.setText(vo.getCharge_edt());

        author_name.setText(vo.getAnswerer().getUser_name() + "    " + vo.getAnswerer().getWb_name());
        author_des.setText(vo.getAnswerer().getSummary());

        //请求前一篇连载
        if (null != vo.getPrevious_id() && !"0".equals(vo.getPrevious_id())) {
            //前一篇存在 设置推荐
            recommend_1_layout.setVisibility(View.VISIBLE);
            requestRecommend1(vo.getPrevious_id());
        } else {
            //前一篇不存在
            recommend_1_layout.setVisibility(View.GONE);
        }

        //请求后篇
        if (null != vo.getNext_id() && !"0".equals(vo.getNext_id())) {
            //后一篇存在
            recommend_2_layout.setVisibility(View.VISIBLE);
            requestRecommend2(vo.getNext_id());
        } else {
            //后一篇不存在
            recommend_2_layout.setVisibility(View.GONE);
        }

        if ("0".equals(vo.getPrevious_id()) && "0".equals(vo.getNext_id())) {
            recommend_layout.setVisibility(View.GONE);
        }
    }

    private void requestRecommend1(String id) {
        String path = MessageFormat.format(HttpUtils.QUESTION_DETAIL, id);

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
                                fillRecommend1(response.getData());
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

    private void fillRecommend1(final QuestionDetailVo.DataBean vo) {
        recommend_1_title.setText(vo.getQuestion_title());
        recommend_1_author.setText(vo.getAnswerer().getUser_name() + "    答    " + vo.getAsker().getUser_name());
        recommend_1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.startThis(mActivity, vo.getQuestion_id());
            }
        });
    }

    private void requestRecommend2(String id) {
        String path = MessageFormat.format(HttpUtils.QUESTION_DETAIL, id);

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
                                fillRecommend2(response.getData());
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

    private void fillRecommend2(final QuestionDetailVo.DataBean vo) {
        recommend_2_title.setText(vo.getQuestion_title());
        recommend_2_author.setText(vo.getAnswerer().getUser_name() + "  答  " + vo.getAsker().getUser_name());
        recommend_2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.startThis(mActivity, vo.getQuestion_id());
            }
        });
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
