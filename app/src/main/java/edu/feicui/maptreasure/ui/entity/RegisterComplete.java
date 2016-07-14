package edu.feicui.maptreasure.ui.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class RegisterComplete {
    @SerializedName("errcode")
    private int code;
    @SerializedName("errmsg")
    private String message;
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

}
