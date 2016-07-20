package edu.feicui.maptreasure.treasure.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.maptreasure.R;
import edu.feicui.maptreasure.commons.ActivityUtils;
import edu.feicui.maptreasure.components.TreasureView;
import edu.feicui.maptreasure.treasure.Area;
import edu.feicui.maptreasure.treasure.Treasure;
import edu.feicui.maptreasure.treasure.TreasureRepo;
import edu.feicui.maptreasure.treasure.home.hide.HideTreasureActivity;

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
public class MapFragment extends MvpFragment<MapMvpView,MapPresenter> implements MapMvpView {

    private LatLng target;
    private BitmapDescriptor dot= BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private BitmapDescriptor expanded=BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);
    private ActivityUtils activityUtils;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityUtils=new ActivityUtils(this);
        return inflater.inflate(R.layout.fragment_map,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        uiMode=UI_MODE_NORMAL;
        changUiMode(uiMode);
    }

    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initBaiduMap();
        initLocation();
        initGeoCoder();
    }


    @Bind(R.id.tv_currentLocation)
    TextView tv_currentLocation;
    private String address;
    private GeoCoder geocoder;
    private void initGeoCoder() {
        geocoder= GeoCoder.newInstance();
        geocoder.setOnGetGeoCodeResultListener(resultListener);
    }
    private OnGetGeoCoderResultListener resultListener=new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if(reverseGeoCodeResult==null){return;}
            if(reverseGeoCodeResult.error== SearchResult.ERRORNO.NO_ERROR){
                address="未知位置";
            }
            address=reverseGeoCodeResult.getAddress();
            tv_currentLocation.setText(address);
        }
    };

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
        options.mapStatus(status);//地图相关状态
        options.compassEnabled(true); // 指南针
        options.zoomGesturesEnabled(true); // 设置是否允许缩放手势
        options.rotateGesturesEnabled(true); // 设置是否允许旋转手势，默认允许
        options .scrollGesturesEnabled(true); // 设置是否允许拖拽手势，默认允许
        options.scaleControlEnabled(false); // 设置是否显示比例尺控件
        options.overlookingGesturesEnabled(false); // 设置是否允许俯视手势，默认允许
        options .zoomControlsEnabled(false); // 设置是否显示缩放控件
        mapView=new MapView(getActivity(),options);
        baiduMap=mapView.getMap();
        mapFrame.addView(mapView, 0);

        baiduMap.setOnMarkerClickListener(onMarkerClickListener);
        baiduMap.setOnMapStatusChangeListener(changeListener);
    }

    private LocationClient locationClient;
    private static LatLng myLocation;

    public static LatLng getMyLocation(){
        return myLocation;
    }



    private void initLocation() {
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getActivity().getApplicationContext());
        LocationClientOption options=new LocationClientOption();
        options.setOpenGps(true);
        options.setCoorType("bd09ll");
        options.setLocationNotify(true);//设置是否当gps有效时按照1S1次频率输出GPS结果
        options.SetIgnoreCacheException(false);//设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(options);
        locationClient.registerLocationListener(locationListener);
        locationClient.start();
        locationClient.requestLocation();


    }

    private boolean isFirstLocated=true;
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
            if (isFirstLocated) {
                animateMoveToMyLocation();
                isFirstLocated = false;
            }
        }
    };


    private void addMarker(LatLng position,int treasureId){
        MarkerOptions options=new MarkerOptions();
        options.position(position);
        options.icon(dot);
        options.anchor(0.5f, 0.5f);//添加锚点
        Bundle bundle=new Bundle();
        bundle.putInt("id",treasureId);
        options.extraInfo(bundle);
        baiduMap.addOverlay(options);
    }

    private Marker currentMarker;

    private BaiduMap.OnMarkerClickListener onMarkerClickListener=new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(currentMarker!=null){
                currentMarker.setVisible(true);
            }
            currentMarker=marker;
            currentMarker.setVisible(false);
            InfoWindow infoWindow=new InfoWindow(expanded,marker.getPosition(),0,infoWindowClickListener);
            baiduMap.showInfoWindow(infoWindow);
            int id=marker.getExtraInfo().getInt("id");
            Treasure treasure = TreasureRepo.getInstance().getTreasure(id);
            treasureView.bindTreasure(treasure);
            changUiMode(UI_MODE_SELECT);
            return false;
        }
    };
    private InfoWindow.OnInfoWindowClickListener infoWindowClickListener=new InfoWindow.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick() {
            currentMarker.setVisible(true);
            baiduMap.hideInfoWindow();
            changUiMode(UI_MODE_NORMAL);
        }
    };
    @Bind(R.id.et_treasureTitle)
    EditText etTreasureTitle;
    @OnClick({R.id.treasureView,R.id.hide_treasure})
    public void clickCardView(View v){
        switch (v.getId()){
            case R.id.hide_treasure:
                activityUtils.hideSoftKeyboard();
                String title = etTreasureTitle.getText().toString();
                if(title==null){
                    activityUtils.showToast(R.string.please_input_title);
                    return;
                }
                LatLng latLng = baiduMap.getMapStatus().target;
                changUiMode(UI_MODE_NORMAL);
                HideTreasureActivity.open(getContext(), title, address, latLng, 0);

                break;
            case R.id.treasureView:
                activityUtils.showToast("124466");
                break;
        }
    }

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

    private BaiduMap.OnMapStatusChangeListener changeListener=new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng target = mapStatus.target;
            if(target!=MapFragment.this.target){
                updateMapArea();
                if(uiMode==UI_MODE_HIDE){
                    ReverseGeoCodeOption option=new ReverseGeoCodeOption();
                    option.location(target);
                    geocoder.reverseGeoCode(option);
                    YoYo.with(Techniques.Bounce).duration(1000).playOn(btnHideHere);
                    YoYo.with(Techniques.Bounce).duration(1000).playOn(ivLocated);
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(btnHideHere);
//                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(conterLayout);
                }
                MapFragment.this.target=target;
            }
        }
    };

    public void updateMapArea(){
        MapStatus mapStatus = baiduMap.getMapStatus();
        double lat=mapStatus.target.latitude;
        double lng=mapStatus.target.longitude;
        Area area = new Area();
        area.setMaxLat(Math.ceil(lat));
        area.setMaxLng(Math.ceil(lng));
        area.setMinLat(Math.floor(lat));
        area.setMinLng(Math.floor(lng));
        getPresenter().getTreasure(area);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void setData(List<Treasure> datas) {
        for(Treasure treasure:datas){
            LatLng position=new LatLng(treasure.getLatitude(),treasure.getLongitude());
            addMarker(position,treasure.getId());
        }
    }
    /**宝藏信息提示,默认隐藏的(在屏幕下方位置,包括两种模式下的布局,选中模式时的信息展示卡片,埋藏模式时的信息录入)*/
    @Bind(R.id.layout_bottom)
    FrameLayout bottomLayout;
    /**宝藏信息展示卡片*/
    @Bind(R.id.treasureView)
    TreasureView treasureView;
    /**埋藏宝藏时需要(中心位置藏宝控件)*/
    @Bind(R.id.centerLayout)
    RelativeLayout conterLayout;
    /**埋藏宝藏时的信息录入卡片(在屏幕下方位置)*/
    @Bind(R.id.hide_treasure)
    RelativeLayout hideTreasure;
    /**埋藏宝藏时"藏在这里"的按钮*/
    @Bind(R.id.btn_HideHere)
    Button btnHideHere;
    @Bind(R.id.iv_located)
    ImageView ivLocated;

    private static final int UI_MODE_NORMAL = 0;// 普通
    private static final int UI_MODE_SELECT = 1;// 选中
    private static final int UI_MODE_HIDE = 2; // 埋藏

    private int uiMode=UI_MODE_NORMAL;

    private void changUiMode(int uiMode){
        if(this.uiMode==uiMode){
            return;
        }
        this.uiMode=uiMode;
        switch (uiMode){
            case UI_MODE_NORMAL:
                bottomLayout.setVisibility(View.GONE);
                conterLayout.setVisibility(View.GONE);
                break;
            case UI_MODE_SELECT:
                conterLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.VISIBLE);
                hideTreasure.setVisibility(View.GONE);
                treasureView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FlipInX).duration(1000).playOn(bottomLayout);
                break;
            case UI_MODE_HIDE:
                conterLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.GONE);
                btnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomLayout.setVisibility(View.VISIBLE);
                        hideTreasure.setVisibility(View.VISIBLE);
                        treasureView.setVisibility(View.GONE);
                        YoYo.with(Techniques.FlipInX).duration(1000).playOn(bottomLayout);
                    }
                });
                break;
        }
    }

    public void hideTreasure() {
        if(currentMarker!=null){
            currentMarker.setVisible(true);
            baiduMap.hideInfoWindow();
        }
        changUiMode(UI_MODE_HIDE);
    }

    public boolean onBackPressed(){
        if(this.uiMode!=UI_MODE_NORMAL){
            changUiMode(UI_MODE_NORMAL);
            return false;
        }
        return true;
    }
}
