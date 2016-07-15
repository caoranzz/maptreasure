package edu.feicui.maptreasure.ui.userapi;

import edu.feicui.maptreasure.ui.user.UserInfo;
import edu.feicui.maptreasure.ui.user.account.UpLoadResult;
import edu.feicui.maptreasure.ui.user.account.UpdataIcon;
import edu.feicui.maptreasure.ui.user.account.UpdataResult;
import edu.feicui.maptreasure.ui.user.login.LoginComplete;
import edu.feicui.maptreasure.ui.user.register.RegisterComplete;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2016/7/14 0014.
 * 将用户模块API，转为Java接口
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterComplete>register(@Body UserInfo userInfo);

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginComplete>login(@Body UserInfo userInfo);

    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UpLoadResult>upload(@Part MultipartBody.Part part);

    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdataResult>updata(@Body UpdataIcon updataIcon);

}
