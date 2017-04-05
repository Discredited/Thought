package com.project.june.thought.activity.index;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.adapter.recycle.ImageTextAdapter;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.ImageTextListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 图文往期列表页面
 */
public class ImageTextListActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.recycle_view)
    RecyclerView recycle_view;

    private String dateString;
    private ImageTextAdapter adapter;
    private String title_text;

    public static void startThis(Context context, String title, String date) {
        Intent intent = new Intent(context, ImageTextListActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("REQUEST_DATE", date);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_image_text_list;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        dateString = getIntent().getStringExtra("REQUEST_DATE");
        title_text = getIntent().getStringExtra("TITLE");
    }

    @Override
    protected void logicProgress() {
        title_center_text.setText(title_text);

        initRecycleView();
        requestData();
    }

    private void initRecycleView() {
        adapter = new ImageTextAdapter();
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        recycle_view.setLayoutManager(manager);
        recycle_view.setAdapter(adapter);
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.TO_LIST_IMAGE_TEXT, dateString);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<ImageTextListVo>() {
                    @Override
                    public void onResponse(ImageTextListVo response, int id) {
                        super.onResponse(response, id);

                        if (response.getRes() == 0) {
                            List<ImageTextListVo.DataBean> beanList = response.getData();
                            if (null != beanList && !beanList.isEmpty()) {
                                adapter.setDataList(beanList);
                            }
                        } else {
                            //请求失败
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        Toast.makeText(mActivity, "网络异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.title_img_left)
    public void viewOnClick(View view) {
        onBackPressed();
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }
}
