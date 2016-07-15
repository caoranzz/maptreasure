package edu.feicui.maptreasure;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.feicui.maptreasure.ui.user.UserPref;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class MapTreasureApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserPref.init(this);
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions option=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_icon)
                .showImageForEmptyUri(R.drawable.user_icon)
                .showImageOnFail(R.drawable.user_icon)
                .cacheOnDisk(true)// 打开disk
                .cacheInMemory(true)// 打开cache
                .build();
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(option)
                .memoryCacheSize(5*1024*1024)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
