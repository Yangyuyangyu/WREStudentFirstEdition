package cn.waycube.wrjy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.overlayutil.DrivingRouteOverlay;

/**
 * Created by Administrator on 2016/5/4.
 */
public class MapActivity extends Activity {
    @Bind(R.id.bmap_View)
    MapView mMapView;


    private BaiduMap mBaiduMap;

    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        //添加ButterKife框架
        ButterKnife.bind(this);

        longitude = getIntent().getDoubleExtra(KeyUtil.KEY_ONE, 0);
        latitude = getIntent().getDoubleExtra(KeyUtil.KEY_TWO, 0);


        initBaidu();

    }


    /**
     * 百度地图
     */
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    //构建Marker图标
    public BitmapDescriptor bitmap;
    private OverlayOptions option;

    /**
     * 设置百度地图
     */
    private void initBaidu() {
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMaxAndMinZoomLevel(20, 15);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //百度地图
        mMapView.showScaleControl(false);//是否显示比例尺控件
        mMapView.showZoomControls(true);//是否显示缩放控件
        mMapView.removeViewAt(1);


        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //构建Marker图标
//        bitmap = BitmapDescriptorFactory
//                .fromResource(R.mipmap.icon_marka);
        myListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation.getLatitude()!=0&&bdLocation.getLongitude()!=0){

                    LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
                    mBaiduMap.animateMapStatus(u);


//                //构建MarkerOption，用于在地图上添加Marker
//                option = new MarkerOptions()
//                        .position(point)
//                        .icon(bitmap);
//                //在地图上添加Marker，并显示
//                mBaiduMap.addOverlay(option);

//                    LatLng point2 = new LatLng(longitude, latitude);
                    LatLng point2 = new LatLng(latitude, longitude);
                    RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
                    PlanNode stNode = PlanNode.withLocation(point);
                    PlanNode enNode = PlanNode.withLocation(point2);
                    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                        @Override
                        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                            //获取步行线路规划结果
                        }

                        @Override
                        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                            //获取公交换乘路径规划结果
                        }

                        @Override
                        public void onGetDrivingRouteResult(DrivingRouteResult result) {
                            //获取驾车线路规划结果

                            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                                Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                            }
                            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                                //result.getSuggestAddrInfo()
                                return;
                            }
                            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                        mBaiduMap);
                                drivingRouteOverlay.setData(result.getRouteLines()
                                        .get(0));// 设置一条驾车路线方案
                                mBaiduMap.setOnMarkerClickListener(drivingRouteOverlay);
                                drivingRouteOverlay.addToMap();
                                drivingRouteOverlay.zoomToSpan();
                            }
                        }

                        @Override
                        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                        }
                    };
                    mSearch.setOnGetRoutePlanResultListener(listener);

                    mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                    mLocationClient.stop();
                }

            }
        };


        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
