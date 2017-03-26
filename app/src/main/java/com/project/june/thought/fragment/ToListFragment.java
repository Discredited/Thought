package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.activity.index.ImageTextListActivity;
import com.project.june.thought.activity.index.MusicListActivity;
import com.project.june.thought.activity.index.QuestionListActivity;
import com.project.june.thought.activity.index.ReadingListActivity;
import com.project.june.thought.activity.index.SerializeListActivity;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.model.DateTypeVo;
import com.project.june.thought.model.SerializeListVo;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by June on 2017/3/25.
 */
public class ToListFragment extends BaseFragment {

    @InjectView(R.id.list_view)
    ListView list_view;
    private JuneBaseAdapter<DateTypeVo> adapter;
    private String category;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_to_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        category = bundle.getString(ThoughtConfig.CATEGORY);

        initListView();
        requestData();
    }

    private void initListView() {
        adapter = new JuneBaseAdapter<DateTypeVo>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_to_list, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, DateTypeVo itemData) {
                TextView date_text = JuneViewHolder.get(convertView, R.id.date_text);

                date_text.setText(itemData.getMonthDateType());
            }
        };
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DateTypeVo dateTypeVo = adapter.getItems().get(i);
                switch (category) {
                    case "0":
                        ImageTextListActivity.startThis(mActivity, dateTypeVo.getMonthDateType(), dateTypeVo.getYearMonthType());
                        break;
                    case "1":
                        //阅读
                        ReadingListActivity.startThis(mActivity, dateTypeVo.getMonthDateType(),dateTypeVo.getYearMonthType());
                        break;
                    case "2":
                        SerializeListActivity.startThis(mActivity, dateTypeVo.getMonthDateType(),dateTypeVo.getYearMonthType());
                        break;
                    case "3":
                        QuestionListActivity.startThis(mActivity, dateTypeVo.getMonthDateType(),dateTypeVo.getYearMonthType());
                        break;
                    case "4":
                        //音乐
                        MusicListActivity.startThis(mActivity, dateTypeVo.getMonthDateType(), dateTypeVo.getYearMonthType());
                        break;
                }
            }
        });
    }


    private void requestData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        List<DateTypeVo> list = new ArrayList<>(0);
        DateTypeVo vo;
        for (int i = year; i >= 2012; i--) {
            int minMonth = 0;
            if (i != year) {
                month = 11;
            }
            if (i == 2012) {
                minMonth = 9;
            }
            if (category.equals(ThoughtConfig.MUSIC_CATEGORY) && i == 2015){
                break;
            }
            for (int j = month; j >= minMonth; j--) {
                vo = new DateTypeVo();
                if (i == year && j == month) {
                    vo.setMonthDateType("本月");
                    vo.setYearMonthType(i + "-" + (j + 1) + "-01");
                } else {
                    vo.setMonthDateType(DateTools.monthType(j) + i);
                    vo.setYearMonthType(i + "-" + (j + 1) + "-01");
                }
                list.add(vo);
            }
        }
        adapter.getItems().addAll(list);
        adapter.notifyDataSetChanged();
    }

    public static ToListFragment newInstance(Bundle bundle) {
        ToListFragment fragment = new ToListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
