package edu.feicui.maptreasure.user.account;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public interface AccountView extends MvpView {
    /**显示进度条*/
    void showProgress();
    /**隐藏进度条*/
    void hideProgress();
    /**显示信息*/
    void showMessage(String msg);
    /**上传头像*/
    void upLoadIcon(String url);
}
