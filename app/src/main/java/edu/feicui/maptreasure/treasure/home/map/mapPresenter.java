package edu.feicui.maptreasure.treasure.home.map;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import edu.feicui.maptreasure.net.NetClient;
import edu.feicui.maptreasure.treasure.Area;
import edu.feicui.maptreasure.treasure.Treasure;
import edu.feicui.maptreasure.treasure.TreasureApi;
import edu.feicui.maptreasure.treasure.TreasureRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MapPresenter extends MvpNullObjectBasePresenter<MapMvpView> {
    private Call<List<Treasure>> call;
    private Area area;
    public void getTreasure(Area area){
        if(TreasureRepo.getInstance().isCache(area)){
            return;
        }
        this.area=area;
        TreasureApi treasureApi = NetClient.getInstance().getTreasureApi();
        if(call!=null)call.cancel();
        call=treasureApi.getTreasureInArea(area);
        call.enqueue(callBack);
    }

    private Callback<List<Treasure>> callBack=new Callback<List<Treasure>>() {
        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            if(response!=null&&response.isSuccessful()){
                List<Treasure> body = response.body();
                if(body==null){
                    getView().showMessage("unKnown error");
                    return;
                }
                TreasureRepo.getInstance().addTreasure(body);
                TreasureRepo.getInstance().cache(area);
                getView().setData(body);
            }
        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
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
