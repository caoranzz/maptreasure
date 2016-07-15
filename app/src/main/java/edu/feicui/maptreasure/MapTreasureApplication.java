package edu.feicui.maptreasure;

import android.app.Application;

import edu.feicui.maptreasure.ui.user.UserPref;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class MapTreasureApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserPref.init(this);
    }
}
