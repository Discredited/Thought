package com.project.june.thought.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.DynamicVo;
import com.project.xujun.juneutils.dialog.ContentDialog;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;

import java.util.List;

/**
 * Created by June on 2017/4/12.
 */

public class ButtomUtils {
    public static void buttomUtils(final BaseActivity mActivity, final ImageView collect, final ImageView laud, final CollectAndLaudVo vo, ImageView comment, final TextView commentText, final JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter) {
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vo.getCollect()) {
                    //已收藏
                    collect.setImageResource(R.mipmap.collect);
                    vo.setCollect(false);
                } else {
                    //未收藏
                    collect.setImageResource(R.mipmap.collected);
                    vo.setCollect(true);
                }
            }
        });

        laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vo.getLaud()) {
                    //已点赞
                    laud.setImageResource(R.mipmap.laud);
                    vo.setLaud(false);
                    vo.setLaudNumber(vo.getLaudNumber() - 1);
                    commentText.setText(vo.getLaudNumber() + " 喜欢    ·    " + vo.getCommentNumber() + " 评论");
                } else {
                    //未点赞
                    laud.setImageResource(R.mipmap.laud_selected);
                    vo.setLaud(true);
                    vo.setLaudNumber(vo.getLaudNumber() + 1);
                    commentText.setText(vo.getLaudNumber() + " 喜欢    ·    " + vo.getCommentNumber() + " 评论");
                }
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开评论
                new ContentDialog(mActivity) {

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

                        comment_title.setText(vo.getTitle());
                        comment_summary.setText(vo.getSummary());

                        comment_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = comment_edit.getText().toString();
                                if (null == string || "".equals(string)) {
                                    Toast.makeText(mActivity, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    DynamicVo.DataBeanX.DataBean bean = new DynamicVo.DataBeanX.DataBean();
                                    DynamicVo.DataBeanX.DataBean.UserBean user = new DynamicVo.DataBeanX.DataBean.UserBean();
                                    user.setUser_name("June");
                                    user.setWeb_url(ThoughtConfig.USER_PHOTO);
                                    bean.setUser(user);
                                    bean.setContent(string);
                                    bean.setCreated_at(DateTools.getNowDate());
                                    bean.setPraisenum(0);
                                    List<DynamicVo.DataBeanX.DataBean> items = adapter.getItems();
                                    items.add(0, bean);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(mActivity, "评论成功", Toast.LENGTH_SHORT).show();
                                    vo.setCommentNumber(vo.getCommentNumber() + 1);
                                    commentText.setText(vo.getLaudNumber() + " 喜欢    ·    " + vo.getCommentNumber() + " 评论");
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
