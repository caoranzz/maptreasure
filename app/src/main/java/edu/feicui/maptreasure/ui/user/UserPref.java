package edu.feicui.maptreasure.ui.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class UserPref {
    private static final String PREFS_NAME = "user_info";
    private static final String KEY_TOKENID = "key_tokenid";
    private static final String KEY_PHOTO = "key_photo";

    private static UserPref userPref;
    private SharedPreferences sp;
    public static void init(Context context){
        userPref=new UserPref(context);
    }
    private UserPref(Context context){
        sp=context.getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
    }

    public static UserPref getInstance(){
        return userPref;
    }

    public void setKeyTokenid(int tokenid){
        sp.edit().putInt(KEY_TOKENID,tokenid).apply();
    }
    public void setKeyPhoto(String photoUrl){
        sp.edit().putString(KEY_PHOTO, photoUrl).apply();
    }

    public int getKeyTokenid(){
        return sp.getInt(KEY_TOKENID,-1);
    }
    public String getKeyPhoto(){
        return sp.getString(KEY_PHOTO,null);
    }


}
