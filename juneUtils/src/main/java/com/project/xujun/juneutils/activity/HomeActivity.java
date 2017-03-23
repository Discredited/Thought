package com.project.xujun.juneutils.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.project.xujun.juneutils.R;
import com.project.xujun.juneutils.customview.JuneButton;
import com.project.xujun.juneutils.imageutils.CircleTransform;
import com.project.xujun.juneutils.imageutils.RoundedTransformation;
import com.project.xujun.juneutils.otherutils.ViewUtils;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    private HomeActivity mActivity;
    private ImageView image3, image2, image1;
    private JuneButton button1;

    private int pressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivity = this;

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        button1 = (JuneButton) findViewById(R.id.button1);

        String portrait = "http://img0.imgtn.bdimg.com/it/u=4254307790,1072843418&fm=23&gp=0.jpg";
        String path1 = "http://img5.imgtn.bdimg.com/it/u=2642557808,3253510879&fm=23&gp=0.jpg";
        String path2 = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=570622022,2080467925&fm=23&gp=0.jpg";

        Picasso.with(mActivity).load(portrait).transform(new CircleTransform()).into(image1);
        Picasso.with(mActivity).load(path1).transform(new RoundedTransformation(ViewUtils.dp2px(mActivity, 100f), ViewUtils.dp2px(mActivity, 0f))).into(image2);
        Picasso.with(mActivity).load(path2).into(image3);

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity = HomeActivity.this;
                Log.e("sherry", ViewUtils.px2dp(mActivity, image3.getMeasuredWidth()) + "     " + ViewUtils.px2dp(mActivity, image3.getMeasuredHeight()));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pressCount == 0){
                    button1.setNormalColor(Color.rgb(100,110,220));
                    button1.setPressColor(Color.rgb(30,50,180));
                    button1.setText("第一次点击");
                }else if (pressCount == 1){
                    button1.setNormalColor(Color.rgb(200,80,100));
                    button1.setPressColor(Color.rgb(170,50,60));
                    button1.setText("第二次点击");
                }else {
                    button1.setText("不能再点击");
                    button1.setEnabled(false);
                }
                pressCount++;
            }
        });
    }
}
