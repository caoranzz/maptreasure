package edu.feicui.maptreasure.ui.user.login;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.ui.user.UserInfo;
import edu.feicui.maptreasure.ui.user.UserPref;
import edu.feicui.maptreasure.ui.userapi.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    private Call<LoginComplete> call;


    public void login(UserInfo userInfo){
        UserApi userApi = NetClient.getInstance().getUserApi();
        // 执行登陆请求构建出call模型
        Call<LoginComplete> login = userApi.login(userInfo);
        getView().showProgress();
        // Call模型异步执行
        login.enqueue(callBack);
    }
    private Callback<LoginComplete> callBack=new Callback<LoginComplete>() {
        @Override
        public void onResponse(Call<LoginComplete> call, Response<LoginComplete> response) {
            getView().hideProgress();
            if(response.isSuccessful()){
                LoginComplete result = response.body();
                if(result==null){
                    getView().showMessage("unknown error");
                    return;
                }
                getView().showMessage(result.getMessage());
                if(result.getCode()==1){
                    UserPref.getInstance().setKeyTokenid(result.getTokenId());
                    UserPref.getInstance().setKeyPhoto(NetClient.BASE_URL+result.getIconUrl());
                    getView().navigateToHome();
                }
                return;
            }
            getView().showMessage("网络连接异常");
        }

        @Override
        public void onFailure(Call<LoginComplete> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };


    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(call!=null){
            call.cancel();
        }
    }
}
