package com.project.june.thought.adapter.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.utils.DateTools;
import com.project.june.thought.utils.ThoughtConfig;
import com.project.xujun.juneutils.dialog.ContentDialog;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.listview.JuneViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    public void bindData(int position, View convertView, final DynamicVo.DataBeanX.DataBean itemData) {
        ImageView dynamic_image = JuneViewHolder.get(convertView, R.id.dynamic_image);
        ImageView comment_image = JuneViewHolder.get(convertView, R.id.comment_image);
        ImageView praise_image = JuneViewHolder.get(convertView, R.id.praise_image);
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

        if (itemData.getLaud()){
            praise_image.setImageResource(R.mipmap.comment_laud_selected);
        }else {
            praise_image.setImageResource(R.mipmap.comment_laud);
        }

        praise_name.setText(itemData.getUser().getUser_name());
        praise_time.setText(DateTools.getCommentDate(itemData.getCreated_at()));
        praise_content.setText(itemData.getContent());
        praise_count.setText(itemData.getPraisenum() + "");

        praise_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemData.getLaud()) {
                    itemData.setPraisenum(itemData.getPraisenum() - 1);
                    itemData.setLaud(false);
                }else {
                    itemData.setPraisenum(itemData.getPraisenum() + 1);
                    itemData.setLaud(true);
                }
                notifyDataSetChanged();
            }
        });

        comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开评论
                new ContentDialog(mActivity){

                    @Override
                    public int contentLayoutId() {
                        return R.layout.dialog_comment;
                    }

                    @Override
                    public void setViewData(View view) {
                        TextView comment_button = (TextView) view.findViewById(R.id.comment_button);
                        TextView comment_title = (TextView) view.findViewById(R.id.comment_title);
                        TextView comment_summary = (TextView) view.findViewById(R.id.comment_summary);
                        final EditText comment_edit = (EditText) view.findViewById(R.id.comment_edit);

                        comment_title.setText(itemData.getUser().getUser_name() + ":");
                        comment_summary.setText(itemData.getContent());

                        comment_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = comment_edit.getText().toString();
                                if (null == string || "".equals(string)){
                                    Toast.makeText(mActivity, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    DynamicVo.DataBeanX.DataBean bean = new DynamicVo.DataBeanX.DataBean();
                                    DynamicVo.DataBeanX.DataBean.TouserBean touser = new DynamicVo.DataBeanX.DataBean.TouserBean();
                                    touser.setUser_name(itemData.getUser().getUser_name());
                                    touser.setWeb_url(itemData.getUser().getWeb_url());
                                    bean.setTouser(touser);
                                    DynamicVo.DataBeanX.DataBean.UserBean user = new DynamicVo.DataBeanX.DataBean.UserBean();
                                    user.setUser_name("June");
                                    user.setWeb_url(ThoughtConfig.USER_PHOTO);
                                    bean.setUser(user);
                                    bean.setQuote(itemData.getContent());
                                    bean.setContent(string);
                                    bean.setCreated_at(DateTools.getNowDate());
                                    bean.setPraisenum(0);
                                    List<DynamicVo.DataBeanX.DataBean> items = getItems();
                                    items.add(0,bean);
                                    notifyDataSetChanged();
                                    Toast.makeText(mActivity, "评论成功", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });
                    }
                }.builder().show(null);
            }
        });
    }
}
