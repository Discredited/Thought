package com.project.june.thought.utils;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.ThoughtApplication;
import com.project.june.thought.activity.user.LoginActivity;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.CollectAndLaudVo;
import com.project.june.thought.model.DynamicVo;
import com.project.june.thought.model.UserEntry;
import com.project.june.thought.rx.RxCollectListChange;
import com.project.xujun.juneutils.dialog.ContentDialog;
import com.project.xujun.juneutils.listview.JuneBaseAdapter;
import com.project.xujun.juneutils.otherutils.RxBus;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by June on 2017/4/12.
 */

public class BottomUtils {
    public static void bottomUtils(final BaseActivity mActivity, final ImageView collect, final ImageView laud, final CollectAndLaudVo vo, ImageView comment, final TextView commentText, final JuneBaseAdapter<DynamicVo.DataBeanX.DataBean> adapter) {
        try {
            Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
            WhereBuilder wb = WhereBuilder.b().and("itemId", "=", vo.getItemId()).and("category", "=", vo.getCategory());
            selector.where(wb);
            CollectAndLaudVo selectorFirst = selector.findFirst();

            if (null != selectorFirst){
                if (selectorFirst.getCollect()){
                    collect.setImageResource(R.mipmap.collected);
                }else {
                    collect.setImageResource(R.mipmap.collect);
                }

                if (selectorFirst.getLaud()){
                    laud.setImageResource(R.mipmap.laud_selected);
                }else {
                    laud.setImageResource(R.mipmap.laud);
                }
                commentText.setText(selectorFirst.getLaudNumber() + " 喜欢    ·    " + selectorFirst.getCommentNumber() + " 评论");
            }else {
                commentText.setText(vo.getLaudNumber() + " 喜欢    ·    " + vo.getCommentNumber() + " 评论");
            }
        } catch (DbException e) {
            Toast.makeText(mActivity, "GG", Toast.LENGTH_SHORT).show();
        }

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == ThoughtApplication.getUserEntry()) {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.startThis(mActivity);
                    return;
                }

                CollectAndLaudVo first = null;
                try {
                    Selector<CollectAndLaudVo> selector = JuneToolsApp.getDbManager().selector(CollectAndLaudVo.class);
                    WhereBuilder wb = WhereBuilder.b().and("itemId", "=", vo.getItemId()).and("category", "=", vo.getCategory());
                    selector.where(wb);
                    first = selector.findFirst();
                } catch (DbException e) {
                    Log.d("sherry", "数据库查找收藏失败");
                }

                if (null == first) {
                    //表中不存在，即没有收藏
                    first = vo;
                }

                if (first.getCollect()) {
                    //已收藏  收藏列表中删除
                    try {
                        JuneToolsApp.getDbManager().delete(first);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    collect.setImageResource(R.mipmap.collect);
                    first.setCollect(false);
                    RxCollectListChange rx = new RxCollectListChange();
                    rx.setCategory(first.getCategory());
                    RxBus.get().post(rx);

                } else {
                    //未收藏  收藏
                    try {
                        collect.setImageResource(R.mipmap.collected);
                        first.setCollect(true);
                        JuneToolsApp.getDbManager().save(first);
                        RxCollectListChange rx = new RxCollectListChange();
                        rx.setCategory(first.getCategory());
                        RxBus.get().post(rx);
                    } catch (DbException e) {
                        Log.d("sherry", "数据库保存收藏失败");
                    }
                }
            }
        });

        laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == ThoughtApplication.getUserEntry()) {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.startThis(mActivity);
                    return;
                }
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
                final UserEntry userEntry = ThoughtApplication.getUserEntry();
                if (null == userEntry) {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
                    LoginActivity.startThis(mActivity);
                    return;
                }
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
                                    user.setUser_name(userEntry.getName());
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
