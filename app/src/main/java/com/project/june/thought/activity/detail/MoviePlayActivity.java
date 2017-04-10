package com.project.june.thought.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.xujun.juneutils.media.VideoPlayView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MoviePlayActivity extends BaseActivity {


    @InjectView(R.id.movie_view)
    VideoPlayView movie_view;
    private String movieUrl;
    private String movieTitle;

    public static void startThis(Context context, String url, String title) {
        Intent intent = new Intent(context, MoviePlayActivity.class);
        intent.putExtra("MOVIE_URL", url);
        intent.putExtra("TITLE", title);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movie_play;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        movieUrl = getIntent().getStringExtra("MOVIE_URL");
        movieTitle = getIntent().getStringExtra("TITLE");
        if (null == movieUrl || "".equals(movieUrl)) {
            finish();
            return;
        }
    }

    @Override
    protected void logicProgress() {
        movie_view.playVideo(movieUrl, movieTitle);
    }
}
