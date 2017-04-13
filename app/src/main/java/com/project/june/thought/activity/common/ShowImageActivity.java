package com.project.june.thought.activity.common;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.OnClick;

public class ShowImageActivity extends BaseActivity {

    @InjectView(R.id.big_image)
    ImageView big_image;
    @InjectView(R.id.activity_show_image)
    RelativeLayout activity_show_image;
    private String imageUrl;

    public static void startThis(Context context, String imageUrl){
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_show_image;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        imageUrl = intent.getStringExtra("IMAGE_URL");
        if (null == imageUrl){
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        Picasso.with(mActivity).load(imageUrl).into(big_image);
    }

    @OnClick({R.id.big_image,R.id.activity_show_image})
    public void viewOnClick(){
        onBackPressed();
    }
}
