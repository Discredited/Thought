package com.project.june.thought.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.project.june.thought.R;
import com.project.xujun.juneutils.otherutils.ViewUtils;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by June on 2017/3/4.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    public BaseActivity mActivity;

    //返回布局文件id
    protected abstract int getContentViewId();

    //返回Fragment占位布局id
    protected abstract int getTempFragmentId();

    //持有intent
    protected void handleIntent(Intent intent) {}

    //逻辑处理
    protected abstract void logicProgress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.inject(this);
        mActivity = this;

        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        //是否启动侧滑布局
        getSwipeBackLayout().setEnableGesture(enableSwipeBack());

        logicProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    //是否启动滑动返回
    protected boolean enableSwipeBack() {
        getSwipeBackLayout().setEdgeSize(ViewUtils.dp2px(mActivity, 20));
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        return false;
    }
}
