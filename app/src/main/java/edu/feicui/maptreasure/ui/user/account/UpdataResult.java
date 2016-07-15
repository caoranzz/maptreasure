package edu.feicui.maptreasure.ui.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class UpdataResult {
    //{
//    　　　"errcode":1,             //状态值
//    　　　"errmsg":"修改成功!"     //返回信息
//}
    @SerializedName("errmsg")
    private String message;
    @SerializedName("errcode")
    private int code;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
