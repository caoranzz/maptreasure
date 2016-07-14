package edu.feicui.maptreasure.ui.user.login;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.ui.entity.LoginComplete;
import edu.feicui.maptreasure.ui.entity.UserInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    private Gson gson;
    private UserInfo userInfo;
    private Call call;
    public void login(UserInfo userInfo){
        this.userInfo=userInfo;
        gson=new Gson();
        new LoginTask().execute();
    }

    public final class LoginTask extends AsyncTask<Void,Void,LoginComplete>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().showProgress();
        }

        @Override
        protected LoginComplete doInBackground(Void... params) {
            OkHttpClient okHttpClient = NetClient.getInstance().getOkHttpClient();

            String content=gson.toJson(userInfo);
            RequestBody body=RequestBody.create(null, content);
            Request request=new Request.Builder()
                    .url("http://admin.syfeicuiedu.com/Handler/UserHandler.ashx?action=login")
                    .post(body)
                    .build();
            if(call!=null){
                call.cancel();
            }
            call=okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                if(response==null){
                    return null;
                }
                if(response.isSuccessful()){
                    String result = response.body().string();
                    LoginComplete loginComplete = gson.fromJson(result, LoginComplete.class);
                    return loginComplete;
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginComplete loginComplete) {
            super.onPostExecute(loginComplete);
            getView().hideProgress();
            if (loginComplete==null) {
                getView().showMessage("未知错误");
                return;
            }
            getView().showMessage(loginComplete.getMessage());
            if(loginComplete.getCode()==1){
                getView().navigateToHome();
            }
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(call!=null){
            call.cancel();
        }
    }
}
