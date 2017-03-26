package com.project.june.thought.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.activity.index.ImageTextActivity;
import com.project.june.thought.model.ImageTextListVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文列表适配器
 * Created by June on 2017/3/25.
 */
public class ImageTextAdapter extends RecyclerView.Adapter<ImageTextAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageTextListVo.DataBean> dataBeanList = new ArrayList<>(0);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_item_image_text, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageTextListVo.DataBean bean = dataBeanList.get(position);
        Picasso.with(mContext).load(bean.getHp_img_url()).into(holder.text_image);
        holder.text_image_number.setText(bean.getHp_title());
        holder.text_image_date.setText(bean.getLast_update_date());
        holder.text_image_content.setText(bean.getHp_content());

        final String hpId = bean.getHpcontent_id();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转图文详情页
                ImageTextActivity.startThis(mContext, hpId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView text_image;
        TextView text_image_number, text_image_date, text_image_content;

        public ViewHolder(View itemView) {
            super(itemView);
            //不使用ButterKnife是因为在适配器中没有解绑
            text_image = (ImageView) itemView.findViewById(R.id.text_image);
            text_image_number = (TextView) itemView.findViewById(R.id.text_image_number);
            text_image_date = (TextView) itemView.findViewById(R.id.text_image_date);
            text_image_content = (TextView) itemView.findViewById(R.id.text_image_content);
        }
    }

    public void setDataList(List<ImageTextListVo.DataBean> list) {
        dataBeanList.addAll(list);
        notifyDataSetChanged();
    }
}
