package com.project.june.thought;

import android.app.Application;

/**
 * Created by June on 2017/4/12.
 */

public class ThoughtApplication extends Application {

    public static ThoughtApplication thoughtApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.thoughtApp = this;
    }
}
