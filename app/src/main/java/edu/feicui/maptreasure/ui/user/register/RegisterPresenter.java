package edu.feicui.maptreasure.ui.user.register;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.ui.entity.RegisterComplete;
import edu.feicui.maptreasure.ui.entity.UserInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView>{
    private Gson gson;
    private UserInfo userInfo;
    private Call call;
    public void register(UserInfo userInfo){
        gson=new Gson();
        this.userInfo=userInfo;
        new RegisterTask().execute();
    }
    public final class RegisterTask extends AsyncTask<Void,Void,RegisterComplete>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().showProgress();
        }

        @Override
        protected RegisterComplete doInBackground(Void... params) {
            //1创建网络链接
            OkHttpClient client = NetClient.getInstance().getOkHttpClient();
            //5请求体的内容来自json数据
            String content=gson.toJson(userInfo);
//            MediaType type = MediaType.parse("treasure");
            //4创建请求体
            RequestBody body=RequestBody.create(null,content);
            //3请求
            Request request=new Request.Builder()
                    .url("http://admin.syfeicuiedu.com/Handler/UserHandler.ashx?action=register")
                    .post(body)
                    .build();
            if(call!=null){
                call.cancel();
            }
            //2请求连接
            call=client.newCall(request);
            try {
                //6执行请求，得到响应
                Response response = call.execute();
                if (response == null) {
                    return null;
                }
                if(response.isSuccessful()){
                    //7连接成功，得到响应体里面的json数据
                    String str = response.body().string();
                    //8解析数据
                    RegisterComplete result = gson.fromJson(str, RegisterComplete.class);
                    return result;
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }
        @Override
        protected void onPostExecute(RegisterComplete result) {
            super.onPostExecute(result);
            getView().hideProgress();
            if(result==null){
                getView().showMessage("未知错误");
                return;
            }
            if(result.getCode()==1){
                getView().showMessage(result.getMessage());
                getView().navigateToHome();
            }else {
                getView().showMessage(result.getMessage());
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
