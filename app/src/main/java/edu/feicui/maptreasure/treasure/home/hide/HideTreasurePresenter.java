package edu.feicui.maptreasure.treasure.home.hide;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.treasure.TreasureApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class HideTreasurePresenter extends MvpNullObjectBasePresenter<HideTreasureView>{


    private Call<HideTreasureResult> hideCall;

    public void hideTreasure(HideTreasure hideTreasure){
        getView().showProgress();
        TreasureApi treasureApi = NetClient.getInstance().getTreasureApi();
        if(hideCall!=null)hideCall.cancel();
        hideCall=treasureApi.hideTreasure(hideTreasure);
        hideCall.enqueue(callBack);
    }
    private Callback<HideTreasureResult> callBack=new Callback<HideTreasureResult>() {
        @Override
        public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
            getView().hideProgress();
            if(response!=null&&response.isSuccessful()){
                HideTreasureResult result = response.body();
                if(result==null){
                    getView().showMessage("unKnown error");
                    return;
                }
                getView().showMessage(result.getMsg());
                if(result.getCode()==1){
                    getView().navigateToHome();
                }
            }
        }

        @Override
        public void onFailure(Call<HideTreasureResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(hideCall!=null){
            hideCall.cancel();
        }
    }
}
