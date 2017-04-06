package com.project.june.thought.adapter.list;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by June on 2017/4/6.
 */
public class DynamicAdapter extends JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> {

    private BaseActivity mActivity;

    public DynamicAdapter(BaseActivity activity) {
        super(activity);
        mActivity = activity;
    }


    @Override
    public View getConvertView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_dynamic, parent, false);
        }
        return convertView;
    }

    @Override
    public void bindData(int position, View convertView, DynamicVo.DataBeanX.DataBean itemData) {
        ImageView dynamic_image = JuneViewHolder.get(convertView, R.id.dynamic_image);
        TextView praise_name = JuneViewHolder.get(convertView, R.id.praise_name);
        TextView praise_time = JuneViewHolder.get(convertView, R.id.praise_time);
        TextView praise_content = JuneViewHolder.get(convertView, R.id.praise_content);
        TextView praise_count = JuneViewHolder.get(convertView, R.id.praise_count);

        LinearLayout reply_layout = JuneViewHolder.get(convertView, R.id.reply_layout);
        TextView reply_content = JuneViewHolder.get(convertView, R.id.reply_content);

        //设置头像
        if (null == itemData.getUser().getWeb_url() || itemData.getUser().getWeb_url().isEmpty()) {
            Picasso.with(mActivity).load(R.mipmap.user_default_image).transform(new CircleTransform()).into(dynamic_image);
        } else {
            Picasso.with(mActivity).load(itemData.getUser().getWeb_url()).transform(new CircleTransform()).into(dynamic_image);
        }

        if (null != itemData.getQuote() && null != itemData.getTouser()) {
            //存在评论
            reply_layout.setVisibility(View.VISIBLE);
            reply_content.setText(itemData.getTouser().getUser_name() + " : " + itemData.getQuote());
        } else {
            //不存在评论
            reply_layout.setVisibility(View.GONE);
        }

        praise_name.setText(itemData.getUser().getUser_name());
        praise_time.setText(itemData.getCreated_at());
        praise_content.setText(itemData.getContent());
        praise_count.setText(itemData.getPraisenum() + "");
    }
}
