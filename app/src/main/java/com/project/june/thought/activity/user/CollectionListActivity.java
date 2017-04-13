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
import com.project.june.thought.activity.detail.MovieDetailActivity;
import com.project.june.thought.activity.detail.MusicDetailActivity;
import com.project.june.thought.activity.detail.ReadingDetailActivity;
import com.project.june.thought.activity.detail.ImageTextActivity;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.CollectAndLaudVo;
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
        }
    }

    private void requestData() {
        adapter.getItems().clear();
        //查询本地数据库
        try {
            Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
            WhereBuilder whereBuilder = WhereBuilder.b().and("category", "=", this.category);
            selector.where(whereBuilder);
            List<CollectAndLaudVo> all = selector.findAll();
            if (null != all && all.size() > 0) {
                adapter.getItems().addAll(all);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(mActivity, "暂无收藏", Toast.LENGTH_SHORT).show();
            }
        } catch (DbException e) {
        }

    }

    private void initListView() {
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

    @OnClick(R.id.title_img_left)
    public void viewOnClick(){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(RxCollectListChange.class, collectListChangeObservable);
    }
}
