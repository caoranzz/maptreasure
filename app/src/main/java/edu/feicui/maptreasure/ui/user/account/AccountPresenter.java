package edu.feicui.maptreasure.ui.user.account;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.ui.user.UserPref;
import edu.feicui.maptreasure.ui.userapi.UserApi;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class AccountPresenter extends MvpNullObjectBasePresenter<AccountView> {
    private Call<UpLoadResult> call;
    private Call<UpdataResult>updataCall;

    private String photoUrl;


    public void upLoadPhoto(File file){
        UserApi userApi = NetClient.getInstance().getUserApi();
        getView().showProgress();
        RequestBody body = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "photo.png", body);
        if (call != null) call.cancel();
        call=userApi.upload(part);
        call.enqueue(callBack);

    }


    private Callback<UpdataResult> updataCallBack=new Callback<UpdataResult>() {
        @Override
        public void onResponse(Call<UpdataResult> call, Response<UpdataResult> response) {
            getView().hideProgress();
            if(response!=null&&response.isSuccessful()){
                UpdataResult result = response.body();
                if(result==null){
                    getView().showMessage("未知错误");
                    return;
                }
                getView().showMessage(result.getMessage());
                if(result.getCode()!=1){
                    return;
                }

            }

        }

        @Override
        public void onFailure(Call<UpdataResult> call, Throwable t) {
            getView().hideProgress();
        }
    };

    private Callback<UpLoadResult> callBack=new Callback<UpLoadResult>() {
        @Override
        public void onResponse(Call<UpLoadResult> call, Response<UpLoadResult> response) {
            if(response!=null&&response.isSuccessful()){
                UpLoadResult result = response.body();
                if(result==null){
                    getView().showMessage("未知错误");
                    return;
                }
                getView().showMessage(result.getMessage());
                if(result.getCount()!=1){
                    return;
                }
                photoUrl=result.getUrl();
                UserPref.getInstance().setKeyPhoto(NetClient.BASE_URL +photoUrl);
                getView().upLoadIcon(NetClient.BASE_URL +photoUrl);

                String photoName=photoUrl.substring(photoUrl.lastIndexOf("/")+1,photoUrl.length());
                int tokenId= UserPref.getInstance().getKeyTokenid();
                UserApi userApi = NetClient.getInstance().getUserApi();
                updataCall=userApi.updata(new UpdataIcon(tokenId,photoName));
                updataCall.enqueue(updataCallBack);

            }else{
                getView().showMessage("网络连接失败");
            }
        }

        @Override
        public void onFailure(Call<UpLoadResult> call, Throwable t) {
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
        if(updataCall!=null){
            updataCall.cancel();
        }
    }
}
