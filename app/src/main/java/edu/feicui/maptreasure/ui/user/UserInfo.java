package edu.feicui.maptreasure.ui.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class UserInfo {
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Password")
    private String userPwd;

    public UserInfo(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPwd() {
        return userPwd;
    }
}
