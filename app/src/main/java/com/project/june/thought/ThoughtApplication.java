package com.project.june.thought;

import android.app.Application;

import com.project.june.thought.model.UserEntry;

import org.xutils.DbManager;
import org.xutils.JuneToolsApp;

/**
 * Created by June on 2017/4/12.
 */

public class ThoughtApplication extends Application {

    public static ThoughtApplication thoughtApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.thoughtApp = this;
        initDBTools();
    }

    private void initDBTools() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("bossapp.db");
        daoConfig.setAllowTransaction(true);
        daoConfig.setDbVersion(2);
        JuneToolsApp.init(this, daoConfig);
    }

    public static UserEntry getUserEntry() {
        try {
            UserEntry first = JuneToolsApp.getDbManager().findFirst(UserEntry.class);
            return first;
        } catch (Exception ex) {
            return null;
        }
    }
}
