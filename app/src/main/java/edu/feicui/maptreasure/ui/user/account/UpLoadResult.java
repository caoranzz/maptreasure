package edu.feicui.maptreasure.ui.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class UpLoadResult {
// {
//    errcode : '文件系统上传成功！',                  //返回信息
//    urlcount: 1,                                   //返回值
//            　　　imgUrl:
//            　　　'/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0.png',
//            　　　smallImgUrl:
//            　　　'/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0_1.png'  //头像地址
//  }
    @SerializedName("errcode")
    private String message;
    @SerializedName("urlcount")
    private int count;
    @SerializedName("smallImgUrl")
    private String url;

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }
}
