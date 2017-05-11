package com.project.june.thought.activity.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.activity.detail.AuthorDetailActivity;
import com.project.june.thought.activity.detail.DiaryDetailActivity;
import com.project.june.thought.activity.detail.MovieDetailActivity;
import com.project.june.thought.activity.detail.MusicDetailActivity;
import com.project.june.thought.activity.detail.ReadingDetailActivity;
import com.project.june.thought.activity.detail.ImageTextActivity;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.AuthorEntry;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.DiaryEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.project.xujun.juneutils.otherutils.RxBus;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CollectionListActivity extends BaseActivity {

    @InjectView(R.id.title_center_text)
    TextView title_center_text;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.list_ptr)
    PtrClassicFrameLayout list_ptr;
    private String category;
    private JuneBaseAdapter<CollectAndLaudVo> adapter;
    private JuneBaseAdapter<DiaryEntry> diaryAdapter;
    private JuneBaseAdapter<AuthorEntry> authorAdapter;

    public static void startThis(Context context, String category) {
        Intent intent = new Intent(context, CollectionListActivity.class);
        intent.putExtra("CATEGORY", category);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collection_list;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        category = getIntent().getStringExtra("CATEGORY");
        if (null == category) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        bindRx();
        preInit();
        initListView();
        requestData();
    }

    private Observable<RxCollectListChange> collectListChangeObservable;

    private void bindRx() {
        collectListChangeObservable = RxBus.get().register(RxCollectListChange.class);
        collectListChangeObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxCollectListChange>() {
            @Override
            public void call(RxCollectListChange rx) {
                requestData();
            }
        });
    }

    private void preInit() {
        switch (category) {
            case "0":
                title_center_text.setText("图文收藏");
                break;
            case "1":
                title_center_text.setText("阅读收藏");
                break;
            case "4":
                title_center_text.setText("音乐收藏");
                break;
            case "5":
                title_center_text.setText("影视收藏");
                break;
            case "100":
                title_center_text.setText("我的小记");
                break;
            case "200":
                title_center_text.setText("我的关注");
                break;
        }
    }

    private void requestData() {
        if (category.equals("100")) {
            diaryAdapter.getItems().clear();
            //查询本地数据库
            try {
                List<DiaryEntry> all = JuneToolsApp.getDbManager().findAll(DiaryEntry.class);
                if (null != all && all.size() > 0) {
                    diaryAdapter.getItems().addAll(all);
                } else {
                    Toast.makeText(mActivity, "暂无数据", Toast.LENGTH_SHORT).show();
                }
            } catch (DbException e) {
                Toast.makeText(mActivity, "数据库查找小记失败", Toast.LENGTH_SHORT).show();
            }
            diaryAdapter.notifyDataSetChanged();
        } else if (category.equals("200")) {
            authorAdapter.getItems().clear();
            //查询本地数据库
            try {
                List<AuthorEntry> all = JuneToolsApp.getDbManager().findAll(AuthorEntry.class);
                if (null != all && all.size() > 0) {
                    authorAdapter.getItems().addAll(all);
                } else {
                    Toast.makeText(mActivity, "暂无数据", Toast.LENGTH_SHORT).show();
                }
            } catch (DbException e) {
                Toast.makeText(mActivity, "数据库查找关注失败", Toast.LENGTH_SHORT).show();
            }
            authorAdapter.notifyDataSetChanged();
        } else {
            adapter.getItems().clear();
            //查询本地数据库
            try {
                Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
                WhereBuilder whereBuilder = WhereBuilder.b().and("category", "=", this.category);
                selector.where(whereBuilder);
                List<CollectAndLaudVo> all = selector.findAll();
                if (null != all && all.size() > 0) {
                    adapter.getItems().addAll(all);
                } else {
                    Toast.makeText(mActivity, "暂无数据", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                Toast.makeText(mActivity, "数据库查找收藏失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initListView() {
        if (category.equals("100")) {
            diaryAdapter = new JuneBaseAdapter<DiaryEntry>(mActivity) {

                @Override
                public View getConvertView(int position, View convertView, ViewGroup parent) {
                    if (null == convertView) {
                        convertView = inflater.inflate(R.layout.list_item_collect, parent, false);
                    }
                    return convertView;
                }

                @Override
                public void bindData(int position, View convertView, DiaryEntry itemData) {
                    ImageView collect_image = JuneViewHolder.get(convertView, R.id.collect_image);
                    TextView collect_type = JuneViewHolder.get(convertView, R.id.collect_type);
                    TextView collect_title = JuneViewHolder.get(convertView, R.id.collect_title);

                    collect_image.setImageResource(R.mipmap.diary);
                    collect_type.setText(itemData.getDate());
                    collect_title.setText(itemData.getContent());
                }
            };
            list_view.setAdapter(diaryAdapter);
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DiaryEntry entry = diaryAdapter.getItems().get(position);
                    //跳转展示页面
                    DiaryDetailActivity.startThis(mActivity, entry.getId());
                }
            });
        } else if (category.equals("200")) {
            authorAdapter = new JuneBaseAdapter<AuthorEntry>(mActivity) {
                @Override
                public View getConvertView(int position, View convertView, ViewGroup parent) {
                    if (null == convertView) {
                        convertView = inflater.inflate(R.layout.list_item_collect, parent, false);
                    }
                    return convertView;
                }

                @Override
                public void bindData(int position, View convertView, AuthorEntry itemData) {
                    ImageView collect_image = JuneViewHolder.get(convertView, R.id.collect_image);
                    TextView collect_type = JuneViewHolder.get(convertView, R.id.collect_type);
                    TextView collect_title = JuneViewHolder.get(convertView, R.id.collect_title);

                    collect_image.setImageResource(R.mipmap.icon_user);
                    collect_type.setText(itemData.getUserName());
                    collect_title.setText(itemData.getDesc());
                }
            };
            list_view.setAdapter(authorAdapter);
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AuthorEntry authorEntry = authorAdapter.getItems().get(position);
                    if (null != authorEntry) {
                        AuthorDetailActivity.startThis(mActivity, authorEntry.getUserId());
                    }
                }
            });
        } else {
            adapter = new JuneBaseAdapter<CollectAndLaudVo>(mActivity) {

                @Override
                public View getConvertView(int position, View convertView, ViewGroup parent) {
                    if (null == convertView) {
                        convertView = inflater.inflate(R.layout.list_item_collect, parent, false);
                    }
                    return convertView;
                }

                @Override
                public void bindData(int position, View convertView, CollectAndLaudVo itemData) {
                    ImageView collect_image = JuneViewHolder.get(convertView, R.id.collect_image);
                    TextView collect_type = JuneViewHolder.get(convertView, R.id.collect_type);
                    TextView collect_title = JuneViewHolder.get(convertView, R.id.collect_title);

                    collect_title.setText(itemData.getTitle());
                    switch (itemData.getCategory()) {
                        case "0":
                            collect_image.setImageResource(R.mipmap.icon_home);
                            collect_type.setText("图文");
                            break;
                        case "1":
                            collect_image.setImageResource(R.mipmap.icon_read);
                            collect_type.setText("阅读");
                            break;
                        case "4":
                            collect_image.setImageResource(R.mipmap.icon_music);
                            collect_type.setText("音乐");
                            break;
                        case "5":
                            collect_image.setImageResource(R.mipmap.icon_movie);
                            collect_type.setText("影视");
                            break;
                    }
                }
            };
            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CollectAndLaudVo vo = adapter.getItems().get(position);
                    switch (vo.getCategory()) {
                        case "0":
                            ImageTextActivity.startThis(mActivity, vo.getItemId());
                            break;
                        case "1":
                            ReadingDetailActivity.startThis(mActivity, vo.getItemId());
                            break;
                        case "4":
                            MusicDetailActivity.startThis(mActivity, vo.getItemId());
                            break;
                        case "5":
                            MovieDetailActivity.startThis(mActivity, vo.getItemId());
                            break;
                    }
                }
            });
        }
    }

    @OnClick(R.id.title_img_left)
    public void viewOnClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(RxCollectListChange.class, collectListChangeObservable);
    }
}
