package edu.feicui.maptreasure.ui.userapi;

import edu.feicui.maptreasure.ui.entity.LoginComplete;
import edu.feicui.maptreasure.ui.entity.RegisterComplete;
import edu.feicui.maptreasure.ui.entity.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/7/14 0014.
 * 将用户模块API，转为Java接口
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterComplete>register(@Body UserInfo userInfo);

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginComplete>login(@Body UserInfo userInfo);

}
