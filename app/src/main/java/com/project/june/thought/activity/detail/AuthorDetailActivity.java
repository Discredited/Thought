package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.AuthorDetailVo;
import com.project.june.thought.model.AuthorEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.june.thought.utils.HttpUtils;
import com.project.june.thought.utils.ResultCallBack;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.imageutils.RoundedTransformation;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.project.xujun.juneutils.otherutils.RxBus;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.text.MessageFormat;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class AuthorDetailActivity extends BaseActivity {
    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.author_image)
    ImageView author_image;
    @InjectView(R.id.author_name)
    TextView author_name;
    @InjectView(R.id.author_summary)
    TextView author_summary;
    @InjectView(R.id.laud_button)
    Button laud_button;
    @InjectView(R.id.author_laud)
    TextView author_laud;
    @InjectView(R.id.author_layout)
    LinearLayout author_layout;
    @InjectView(R.id.list_view)
    ListView list_view;

    private String authorId;
    private JuneBaseAdapter<AuthorDetailVo.DataBean> adapter;
    private boolean isLaud = false;
    private AuthorDetailVo.DataBean.AuthorBean authorObj;

    public static void startThis(Context context, String id) {
        Intent intent = new Intent(context, AuthorDetailActivity.class);
        intent.putExtra("AUTHOR_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_author_detail;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        authorId = getIntent().getStringExtra("AUTHOR_ID");
    }

    @Override
    protected void logicProgress() {
        initListView();
        //是否已收藏此人
        isLaudAuthor();
        requestData();
    }

    private void initListView() {
        ((LinearLayout) author_layout.getParent()).removeView(author_layout);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1, -2);
        author_layout.setLayoutParams(params);
        list_view.addHeaderView(author_layout);

        adapter = new JuneBaseAdapter<AuthorDetailVo.DataBean>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_content, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, AuthorDetailVo.DataBean itemData) {
                TextView item_type = JuneViewHolder.get(convertView, R.id.item_type);
                TextView item_title = JuneViewHolder.get(convertView, R.id.item_title);
                TextView item_author = JuneViewHolder.get(convertView, R.id.item_author);
                ImageView item_image = JuneViewHolder.get(convertView, R.id.item_image);
                //TextView item_sub_content = JuneViewHolder.get(convertView, R.id.item_sub_content);
                TextView item_content = JuneViewHolder.get(convertView, R.id.item_content);
                TextView item_time = JuneViewHolder.get(convertView, R.id.item_time);
                TextView laud_count = JuneViewHolder.get(convertView, R.id.laud_count);

                String title;
                switch (itemData.getCategory()) {
                    case "0":
                        title = "图文";
                        break;
                    case "1":
                        title = "阅读";
                        break;
                    case "2":
                        title = "连载";
                        break;
                    case "3":
                        title = "问答";
                        break;
                    case "4":
                        title = "音乐";
                        break;
                    case "5":
                        title = "电影";
                        break;
                    default:
                        title = "资讯";
                        break;
                }
                item_type.setText("-  " + title + "  -");

                item_title.setText(itemData.getTitle());
                item_author.setText("文 / " + itemData.getAuthor().getUser_name());
                item_content.setText(itemData.getForward());
                item_time.setText("今天");
                laud_count.setText(itemData.getLike_count() + "");
                if (null != itemData.getImg_url() && !"".equals(itemData.getImg_url())) {
                    Picasso.with(mActivity).load(itemData.getImg_url()).transform(new RoundedTransformation(ViewUtils.dp2px(mActivity, 4f), 0)).into(item_image);
                } else {
                    Picasso.with(mActivity).load(R.mipmap.default_reading).transform(new RoundedTransformation(ViewUtils.dp2px(mActivity, 4f), 0)).into(item_image);
                }
            }
        };
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                AuthorDetailVo.DataBean dataBean = adapter.getItems().get(position);
                switch (dataBean.getCategory()) {
                    case ThoughtConfig.IMAGE_TEXT_CATEGORY:
                        ImageTextActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                    case ThoughtConfig.READING_CATEGORY:
                        ReadingDetailActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                    case ThoughtConfig.SERIALIZE_CATEGORY:
                        SerializeDetailActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                    case ThoughtConfig.QUESTION_CATEGORY:
                        QuestionDetailActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                    case ThoughtConfig.MUSIC_CATEGORY:
                        MusicDetailActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                    case ThoughtConfig.VIDEO_CATEGORY:
                        MovieDetailActivity.startThis(mActivity, dataBean.getItem_id());
                        break;
                }
            }
        });
    }

    private void requestData() {
        String path = MessageFormat.format(HttpUtils.AUTHOR_DETAIL, authorId);
        OkHttpUtils.get()
                .url(path)
                .build()
                .execute(new ResultCallBack<AuthorDetailVo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(AuthorDetailVo response, int id) {
                        super.onResponse(response, id);
                        if (null != response) {
                            if (null != response.getData() && response.getData().size() > 0) {
                                initAuthor(response.getData().get(0).getAuthor());
                                adapter.getItems().addAll(response.getData());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void initAuthor(AuthorDetailVo.DataBean.AuthorBean author) {
        if (null != author) {
            authorObj = author;
            if (isLaud) {
                laud_button.setText("取消关注");
            } else {
                laud_button.setText("关注");
            }
            title_center_text.setText(author.getUser_name());
            author_name.setText(author.getUser_name());
            author_summary.setText(author.getDesc());
            author_laud.setText(author.getFans_total());
            Picasso.with(mActivity).load(author.getWeb_url()).transform(new CircleTransform()).into(author_image);
        }
    }

    @OnClick({R.id.title_img_left, R.id.laud_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_img_left:
                onBackPressed();
                break;
            case R.id.laud_button:
                laudAuthor();
                break;
        }
    }

    private void laudAuthor() {
        if (null != authorObj) {
            if (isLaud) {
                //取消关注
                try {
                    Selector<AuthorEntry> selector = JuneToolsApp.getDbManager().selector(AuthorEntry.class);
                    WhereBuilder wb = WhereBuilder.b().and("userId", "=", authorId);
                    selector.where(wb);
                    AuthorEntry first = selector.findFirst();
                    if (null != first) {
                        JuneToolsApp.getDbManager().delete(first);
                    }
                    laud_button.setText("关注");
                    isLaud = false;
                } catch (DbException e) {
                }
            } else {
                //关注
                try {
                    AuthorEntry entry = new AuthorEntry();
                    entry.setUserId(authorObj.getUser_id());
                    entry.setUserName(authorObj.getUser_name());
                    entry.setDesc(authorObj.getDesc());
                    entry.setFansTotal(authorObj.getFans_total());
                    entry.setSummary(authorObj.getSummary());
                    JuneToolsApp.getDbManager().save(entry);
                    laud_button.setText("取消关注");
                    isLaud = true;
                } catch (DbException e) {
                }
            }
            RxCollectListChange content = new RxCollectListChange();
            content.setCategory(ThoughtConfig.FOCUS_CATEGORY);
            RxBus.get().post(content);
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        super.enableSwipeBack();
        return true;
    }

    public void isLaudAuthor() {
        //查询数据库是否有此人
        try {
            Selector<AuthorEntry> selector = JuneToolsApp.getDbManager().selector(AuthorEntry.class);
            WhereBuilder wb = WhereBuilder.b().and("userId", "=", authorId);
            selector.where(wb);
            List<AuthorEntry> all = selector.findAll();
            if (null != all && all.size() > 0) {
                //已收藏
                isLaud = true;
            } else {
                //未收藏
                isLaud = false;
            }
        } catch (DbException e) {
            isLaud = false;
        }
    }
}
