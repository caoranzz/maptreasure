package edu.feicui.maptreasure.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class NetClient {
    private static NetClient netClient;
    private OkHttpClient okHttpClient;

    private NetClient(){
        okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
    }
    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
    public static NetClient getInstance(){
        if(netClient==null){
            netClient=new NetClient();
        }
        return netClient;
    }


}
