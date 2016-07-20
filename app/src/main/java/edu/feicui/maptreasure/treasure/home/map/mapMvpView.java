package edu.feicui.maptreasure.treasure.home.map;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import edu.feicui.maptreasure.treasure.Treasure;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public interface MapMvpView extends MvpView{

   void showMessage(String msg);
   void setData(List<Treasure> datas);
}
