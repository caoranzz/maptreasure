package edu.feicui.maptreasure.treasure.home.hide;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public interface HideTreasureView extends MvpView{
    /**显示进度条*/
    void showProgress();
    /**隐藏进度条*/
    void hideProgress();
    /**显示信息*/
    void showMessage(String msg);
    /**跳转到home界面*/
    void navigateToHome();
}
