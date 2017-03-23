package com.project.xujun.juneutils.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class JuneBaseAdapter<T> extends BaseAdapter {
    private List<T> items = null;
    protected Activity activity;
    protected LayoutInflater inflater;

    public JuneBaseAdapter(Activity activity) {
        this.items = new ArrayList<>(0);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getConvertView(position, convertView, parent);
        bindData(position, view, getItem(position));
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return items;
    }

    /**
     * 获取控件
     *
     * @param position
     * @param convertView
     * @param parent
     */
    public abstract View getConvertView(int position, View convertView, ViewGroup parent);

    /**
     * 实现绑定数据的方法
     *
     * @param position
     * @param convertView
     * @param itemData
     */
    public abstract void bindData(int position, View convertView, T itemData);

}
