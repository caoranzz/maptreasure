package edu.feicui.maptreasure.ui.user.register;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.ui.entity.RegisterComplete;
import edu.feicui.maptreasure.ui.entity.UserInfo;
import edu.feicui.maptreasure.ui.user.UserPref;
import edu.feicui.maptreasure.ui.userapi.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView>{
    private Call<RegisterComplete> call;


    public void register(UserInfo userInfo){
        UserApi userApi = NetClient.getInstance().getUserApi();
        Call<RegisterComplete> register = userApi.register(userInfo);
        getView().showProgress();
        register.enqueue(callBack);
    }
    private Callback<RegisterComplete> callBack=new Callback<RegisterComplete>() {
        @Override
        public void onResponse(Call<RegisterComplete> call, Response<RegisterComplete> response) {
            getView().hideProgress();
            if(response.isSuccessful()){
                RegisterComplete result = response.body();
                if(result==null){
                    getView().showMessage("未知错误");
                    return;
                }
                getView().showMessage(result.getMessage());
                if(result.getCode()==1){
                    UserPref.getInstance().setKeyTokenid(result.getTokenId());
                    getView().navigateToHome();
                }
                return;
            }
            getView().showMessage("网络连接失败");
        }

        @Override
        public void onFailure(Call<RegisterComplete> call, Throwable t) {
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
