package com.project.xujun.juneutils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.project.xujun.juneutils.R;


/**
 * 内容显示的对话框,没有任何按钮的
 */
public abstract class ContentDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private Object requestObject;
    private LayoutInflater layoutInflater;

    public ContentDialog(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    private ViewGroup l_layout_content;

    public ContentDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_content_dialog, null);
        // 获取自定义Dialog布局中的控件
        l_layout_content = (ViewGroup) view.findViewById(R.id.l_layout_content);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        // 调整dialog背景大小
        l_layout_content.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }
    public Dialog getDialog(){
        return dialog;
    }

    public void show(Object requestObject) {
        this.requestObject = requestObject;
        View view = layoutInflater.inflate(contentLayoutId(), l_layout_content, false);
        l_layout_content.removeAllViews();
        l_layout_content.addView(view);
        setViewData(view);
        //显示之前来加载控件
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public abstract int contentLayoutId();

    public abstract void setViewData(View view);
}
