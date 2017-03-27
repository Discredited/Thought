package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import okhttp3.Call;

public class ReadingDetailActivity extends BaseActivity {


    private String essayId;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, ReadingDetailActivity.class);
        intent.putExtra("ESSAY_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reading_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        essayId = getIntent().getStringExtra("ESSAY_ID");
        if (null == essayId) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        initListView();
        requestData();
        requestDynamic();
    }

    private void initListView() {

    }

    private void requestDynamic() {
        String path = MessageFormat.format(HttpUtils.READING_DYNAMIC, essayId, 0);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<DynamicVo>() {
                    @Override
                    public void onResponse(DynamicVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            int count = response.getData().getCount();
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

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.READING_DETAIL,essayId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack(){

                });
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
