package com.project.xujun.juneutils.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.project.xujun.juneutils.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * 自定义按下颜色，按钮失效颜色，正常状态颜色的Button
 */
public class JuneButton extends Button {
    private float ltRadius = 0;
    private float rtRadius = 0;
    private float rbRadius = 0;
    private float lbRadius = 0;
    private int normalColor = Color.rgb(0x00, 0XAA, 0XEE);
    private int pressColor = Color.rgb(0x00, 0X91, 0XCB);
    private int disableColor = Color.rgb(0xCC, 0XCC, 0XCC);
    private int rippleColor = Color.rgb(0x48, 0X3D, 0X8B);

    public JuneButton(Context context) {
        super(context);
        this.init(context, null, -1);
    }

    public JuneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, -1);
    }

    public JuneButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JuneButton, defStyleAttr, 0);
            ltRadius = a.getDimension(R.styleable.JuneButton_lt_radius, ltRadius);
            rtRadius = a.getDimension(R.styleable.JuneButton_rt_radius, rtRadius);
            rbRadius = a.getDimension(R.styleable.JuneButton_rb_radius, rbRadius);
            lbRadius = a.getDimension(R.styleable.JuneButton_lb_radius, lbRadius);
            normalColor = a.getColor(R.styleable.JuneButton_normal_color, normalColor);
            pressColor = a.getColor(R.styleable.JuneButton_press_color, pressColor);
            disableColor = a.getColor(R.styleable.JuneButton_disable_color, disableColor);
            rippleColor = a.getColor(R.styleable.JuneButton_ripple_color, rippleColor);
            a.recycle();
        }
        freshBackGroupDrawable();
    }

    public void freshBackGroupDrawable() {
        Drawable drawable = getSelector(getNormalDraw(), getPressedDraw(), getDisableDraw());
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = getRippleDrawable(drawable);
        }
        setBackground(drawable);
    }

    private Drawable getNormalDraw() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(0, Color.TRANSPARENT);
        gradientDrawable.setCornerRadii(new float[]{ltRadius, ltRadius, rtRadius, rtRadius, rbRadius, rbRadius, lbRadius, lbRadius});
        gradientDrawable.setColor(normalColor);
        return gradientDrawable;
    }

    private Drawable getPressedDraw() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(0, Color.TRANSPARENT);
        gradientDrawable.setCornerRadii(new float[]{ltRadius, ltRadius, rtRadius, rtRadius, rbRadius, rbRadius, lbRadius, lbRadius});
        gradientDrawable.setColor(pressColor);
        return gradientDrawable;
    }

    private Drawable getDisableDraw() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(0, Color.TRANSPARENT);
        gradientDrawable.setCornerRadii(new float[]{ltRadius, ltRadius, rtRadius, rtRadius, rbRadius, rbRadius, lbRadius, lbRadius});
        gradientDrawable.setColor(disableColor);
        return gradientDrawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Drawable getRippleDrawable(Drawable drawable) {
        ColorStateList colorList = createColorStateList(Color.TRANSPARENT, rippleColor);
        try {
            Class<?> aClass = Class.forName("android.graphics.drawable.RippleDrawable");
            Constructor<?>[] constructors = aClass.getConstructors();
            for (Constructor<?> c:constructors) {
                if (c.getGenericParameterTypes().length==3){
                    Type[] ts = c.getGenericParameterTypes();
                    if (ts[0].equals(ColorStateList.class) &&ts[1].equals(Drawable.class)&&ts[2].equals(Drawable.class)){
                        return (Drawable)c.newInstance(colorList, drawable, drawable);
                    }
                }
            }
        } catch (Exception ex){
            return null;
        }
        return null;
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        int[] colors = new int[]{pressed, normal};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    private StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw, Drawable disableDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDraw);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDraw);
        stateListDrawable.addState(new int[]{}, normalDraw);
        return stateListDrawable;
    }

    public void setLtRadius(float ltRadius) {
        this.ltRadius = ltRadius;
    }

    public void setRtRadius(float rtRadius) {
        this.rtRadius = rtRadius;
    }

    public void setRbRadius(float rbRadius) {
        this.rbRadius = rbRadius;
    }

    public void setLbRadius(float lbRadius) {
        this.lbRadius = lbRadius;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        freshBackGroupDrawable();
    }

    public void setPressColor(int pressColor) {
        this.pressColor = pressColor;
        freshBackGroupDrawable();
    }

    public void setDisableColor(int disableColor) {
        this.disableColor = disableColor;
        freshBackGroupDrawable();
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        freshBackGroupDrawable();
    }
}
