package org.xutils;

import android.app.Application;
import org.xutils.db.DbManagerImpl;

/**
 * 主要加载一些基础的东西, 比如图片加载等
 * Created by niexiaoqiang on 2016/7/21.
 */
public class JuneToolsApp {
    private static DbManager dbManager;

    public static DbManager getDbManager() {
        if (null == dbManager) {
            throw new RuntimeException("JuneToolsApp.init()未调用");
        }
        return dbManager;
    }

    public static void init(Application application, DbManager.DaoConfig daoConfig) {
        dbManager = DbManagerImpl.getInstance(application, daoConfig);
    }
}
