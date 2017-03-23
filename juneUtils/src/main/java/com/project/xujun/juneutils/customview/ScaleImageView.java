package com.project.xujun.juneutils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.project.xujun.juneutils.R;

/**
 * 固定比例的图片控件
 * Created by xujun on 2017/3/18.
 */
public class ScaleImageView extends ImageView {
    public static final int FIX_MODE_FIX_WIDTH = 1;
    public static final int FIX_MODE_FIX_NONE = 0;
    private static final int DEFAULT_WIDTH_SCALE = 16;
    private static final int DEFAULT_HEIGHT_SCALE = 9;
    private static final int DEFAULT_FIX_MODE = 0;
    private int fix_mode = 0;
    private int width_scale = 16;
    private int height_scale = 9;

    public ScaleImageView(Context context) {
        super(context);
        this.init(context, null, -1);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, -1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (fix_mode == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        if (fix_mode == 1) {//固定宽度
            int height = (int) (MeasureSpec.getSize(widthMeasureSpec) * ((float) height_scale / (float) width_scale));
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        } else {
            int width = (int) (MeasureSpec.getSize(heightMeasureSpec) * ((float) width_scale / (float) height_scale));
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
    }

    public void setImageScale(int fix_mode, int width_scale, int height_scale) {
        this.fix_mode = fix_mode;
        this.width_scale = width_scale;
        this.height_scale = height_scale;
        invalidate();
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView, defStyleAttr, 0);
            width_scale = a.getInt(R.styleable.ScaleImageView_width_scale, DEFAULT_WIDTH_SCALE);
            height_scale = a.getInt(R.styleable.ScaleImageView_height_scale, DEFAULT_HEIGHT_SCALE);
            fix_mode = a.getInt(R.styleable.ScaleImageView_fix_mode, DEFAULT_FIX_MODE);
            a.recycle();
        } else {
            width_scale = DEFAULT_WIDTH_SCALE;
            height_scale = DEFAULT_HEIGHT_SCALE;
            fix_mode = DEFAULT_FIX_MODE;
        }
    }
}
