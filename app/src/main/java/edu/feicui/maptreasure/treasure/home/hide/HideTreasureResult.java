package edu.feicui.maptreasure.treasure.home.hide;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class HideTreasureResult {
    //    {
//        "errcode":0,                                    //返回值
//            "errmsg":"参数格式不正确!请检测传入参数格式"   //返回信息
//    }

    @SerializedName("errcode")
    public int code;

    @SerializedName("errmsg")
    public String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
