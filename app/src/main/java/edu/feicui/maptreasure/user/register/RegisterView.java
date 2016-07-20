package edu.feicui.maptreasure.user.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public interface RegisterView extends MvpView{
    /**显示进度条*/
    void showProgress();
    /**隐藏进度条*/
    void hideProgress();
    /**显示信息*/
    void showMessage(String msg);
    /**跳转到home界面*/
    void navigateToHome();
}
