package com.project.june.thought.activity.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.MusicListVo;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.MessageFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/***
 * 音乐往期列表
 */
public class MusicListActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.title_img_right)
    ImageButton title_img_right;
    @InjectView(R.id.list_view)
    ListView list_view;

    private String title;
    private String dateString;
    private JuneBaseAdapter<MusicListVo.DataBean> adapter;

    public static void startThis(Context context, String title,String date) {
        Intent intent = new Intent(context, MusicListActivity.class);
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
        adapter = new JuneBaseAdapter<MusicListVo.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_to_list_music, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, MusicListVo.DataBean itemData) {
                ImageView music_image = JuneViewHolder.get(convertView, R.id.music_image);
                TextView music_title = JuneViewHolder.get(convertView, R.id.music_title);
                TextView music_author = JuneViewHolder.get(convertView, R.id.music_author);

                Picasso.with(mActivity).load(itemData.getCover()).into(music_image);

                music_title.setText(itemData.getTitle());
                music_author.setText(itemData.getAuthor().getUser_name());
            }
        };
        list_view.setAdapter(adapter);
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.TO_LIST_MUSIC, dateString);

        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<MusicListVo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(MusicListVo response, int id) {
                        super.onResponse(response, id);
                        if (response.getRes() == 0) {
                            //请求成功
                            if (null != response.getData() && response.getData().size() > 0){
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
