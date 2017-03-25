package com.project.june.thought.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseFragment;
import com.project.june.thought.utils.DateTools;
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
    private JuneBaseAdapter<String> adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_to_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListView();
        requestData();
    }

    private void initListView() {
        adapter = new JuneBaseAdapter<String>(mActivity) {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.list_item_to_list, parent, false);
                }
                return convertView;
            }

            @Override
            public void bindData(int position, View convertView, String itemData) {
                TextView date_text = JuneViewHolder.get(convertView, R.id.date_text);

                date_text.setText(itemData);
            }
        };
        list_view.setAdapter(adapter);
    }


    private void requestData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        List<String> list = new ArrayList<>(0);
        for (int i = year; i >= 2012; i--) {
            int minMonth = 0;
            if (i != year){
                month = 11;
            }
            if (i == 2012) {
                minMonth = 9;
            }
            for (int j = month; j >= minMonth; j--) {
                if (i == year && j == month){
                    list.add("本月");
                }else {
                    list.add(DateTools.monthType(j) + i);
                }
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
