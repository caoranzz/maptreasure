package edu.feicui.maptreasure.ui.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class UpdataIcon {
//    {
//        　　　"Tokenid":3,"
//        　　　"HeadPic": "05a1a7e18ab940679dbd0e506be31add.jpg"
//    }

    @SerializedName("Tokenid")
    private int tokenId;
    @SerializedName("HeadPic")
    private String IconUrl;

    public UpdataIcon(int tokenId, String iconUrl) {
        this.tokenId = tokenId;
        IconUrl = iconUrl;
    }
}
