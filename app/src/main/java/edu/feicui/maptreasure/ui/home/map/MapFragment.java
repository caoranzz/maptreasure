package edu.feicui.maptreasure.ui.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
/**
 * <p/>
 * 1. MapView基本操作
 * 2. 定位图层(当前我的位置,请先确认你已集成定位sdk包)
 * 2.1 开启定位图层 (?, 地图是图层有概念)
 * 2.2 设置你当前的位置在哪里 ()
 * 2.2.1 定位SDK初始化
 * 2.2.2 开始定位，及监听处理
 * 2.2.3 成功定位 -经纬度- 设置2.2
 * 2.3 将当前地图状态设置到这里去(将地图移动到你当前所在位置)
 * <p/>
 * 作者：yuanchao on 2016/7/18 0018 14:05
 * 邮箱：yuanchao@feicuiedu.com
 */
public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initBaiduMap();
        initLocation();
    }


    @Bind(R.id.map_frame)
    FrameLayout mapFrame;
    private MapView mapView;
    private BaiduMap baiduMap;

    private void initBaiduMap() {

        BaiduMapOptions options=new BaiduMapOptions();
        MapStatus status=new MapStatus.Builder()
                .overlook(-20)
                .zoom(15)
                .build();
        options.mapStatus(status);
        options.zoomControlsEnabled(false);
        mapView=new MapView(getActivity(),options);
        baiduMap=mapView.getMap();
        mapFrame.addView(mapView, 0);
    }

    private LocationClient locationClient;
    private LatLng myLocation;

    private void initLocation() {
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getActivity().getApplicationContext());
        LocationClientOption options=new LocationClientOption();
        options.setOpenGps(true);
        options.setScanSpan(60000);
        options.setCoorType("bd09ll");
        locationClient.setLocOption(options);
        locationClient.registerLocationListener(locationListener);
        locationClient.start();
        locationClient.requestLocation();

    }
    private BDLocationListener locationListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation==null){
                locationClient.requestLocation();
                return;
            }
            double lon=bdLocation.getLongitude();//经度
            double lat=bdLocation.getLatitude();//纬度
            myLocation=new LatLng(lat,lon);
            MyLocationData myLocationData=new MyLocationData.Builder()
                    .latitude(lat)
                    .longitude(lon)
                    .accuracy(100f)//精度
                    .build();
            baiduMap.setMyLocationData(myLocationData);
            animateMoveToMyLocation();
        }
    };


    @OnClick(R.id.tv_located)
    public void animateMoveToMyLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(myLocation)// 当前位置
                .rotate(0)// 地图摆正
                .zoom(19)
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(update);

    }

    @OnClick({R.id.iv_scaleUp,R.id.iv_scaleDown,R.id.tv_satellite,R.id.tv_compass})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_scaleUp:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.tv_satellite:
                int type=baiduMap.getMapType();
                type=type==BaiduMap.MAP_TYPE_NORMAL?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
                baiduMap.setMapType(type);
                break;
            case R.id.tv_compass:
                boolean isCompass=baiduMap.getUiSettings().isCompassEnabled();
                baiduMap.getUiSettings().setCompassEnabled(!isCompass);
                break;

        }
    }


}
