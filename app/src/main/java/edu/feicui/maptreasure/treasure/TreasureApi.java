package edu.feicui.maptreasure.treasure;


import java.util.List;

import edu.feicui.maptreasure.treasure.home.hide.HideTreasure;
import edu.feicui.maptreasure.treasure.home.hide.HideTreasureResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public interface TreasureApi {

    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area area);

    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure body);
}
