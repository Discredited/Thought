package com.project.xujun.juneutils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.xujun.juneutils.R;
import com.project.xujun.juneutils.customview.JuneButton;

public class TipDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private Object requestObject;

    public TipDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    private View lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private JuneButton btn_ok;
    private OnOkClickListner onOkClickListner;

    public TipDialog builder(boolean touchOutside, boolean cancel, OnOkClickListner onOkClickListner) {
        this.onOkClickListner = onOkClickListner;
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tip_dialog, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_ok = (JuneButton) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != TipDialog.this.onOkClickListner) {
                    TipDialog.this.onOkClickListner.OnOkClick();
                }
                TipDialog.this.dialog.dismiss();
            }
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(cancel);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public TipDialog setTitle(String title) {
        if (null != title && !"".equals(title)) {
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        } else {
            txt_title.setVisibility(View.GONE);
            txt_title.setText("");
        }
        return this;
    }

    public void show(Object requestObject) {
        this.requestObject = requestObject;
        dialog.show();
    }


    public TipDialog setMessage(String msg) {
        if (null != msg && !"".equals(msg)) {
            txt_msg.setVisibility(View.VISIBLE);
            txt_msg.setText(msg);
        } else {
            txt_msg.setVisibility(View.GONE);
            txt_msg.setText("");
        }
        return this;
    }

    public interface OnOkClickListner {
        void OnOkClick();
    }


}
