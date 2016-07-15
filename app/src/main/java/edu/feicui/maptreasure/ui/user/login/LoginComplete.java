package edu.feicui.maptreasure.ui.user.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class LoginComplete {
    @SerializedName("errcode")
    private int code;
    @SerializedName("errmsg")
    private String message;
    @SerializedName("headpic")
    private String iconUrl;
    @SerializedName("tokenid")
    private int tokenId;

    public int getTokenId() {
        return tokenId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
