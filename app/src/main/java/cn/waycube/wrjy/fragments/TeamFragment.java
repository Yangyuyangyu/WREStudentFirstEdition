package cn.waycube.wrjy.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.SearchActivity;
import cn.waycube.wrjy.adapter.GroupAdapter;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Group;
import cn.waycube.wrjy.utils.CommonUtils;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TeamFragment extends BaseFragment implements AdapterListenter, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.lv_team)
    ListView lvTeam;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.ll_no_join)
    LinearLayout llNoJoin;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout Refresh;
    @Bind(R.id.tv_city)
    TextView tvCity;

    /**
     * 适配器
     */
    private GroupAdapter mAdapter;

    private List<Group> mData = new ArrayList<>();

    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;

    /**
     * 显示页数
     */
    private int page = 0;


    /**
     * 百度地图
     */
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;

    public static boolean isRefresh = true;


    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    protected void initVariables() {
        Refresh.setColorSchemeResources(R.color.green_color);


        mAdapter = new GroupAdapter(getActivity(), mData);
        mAdapter.setOnLoadingMoreListener(this);
        lvTeam.setAdapter(mAdapter);


        initLocation();

    }

    private void initLocation() {
        //百度地图
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        myListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                tvCity.setText(bdLocation.getCity());
                MyApplication.city = bdLocation.getCity();

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
    public void onResume() {
        super.onResume();
        /*
         *查看是否需要更新
         */
        if (isRefresh) {
            mData.clear();
            getTeam(PULL_REFRESH, 0);
        }


    }

    /**
     * 获取当前用户的社团
     */
    private void getTeam(final int type, int page) {


        if (type == PULL_REFRESH) {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("正在加载...");
            Refresh.setRefreshing(false);
            pDialog.show();
            mAdapter.isLoadingMore = false;
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.isLoadingMore = false;
        }

        String url = UrlConfig.Team.ID_GET_TEAM + "id=" + MyApplication.mUser.getId() + "&page=" + page;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        if (!jsonObject.getString("data").equals("")) {


                            llNoJoin.setVisibility(View.GONE);
                            lvTeam.setVisibility(View.VISIBLE);

                            List<Group> groups = JSON.parseArray(jsonObject.getString("data"), Group.class);

                            mData.removeAll(groups);
                            mData.addAll(groups);
                            mAdapter.notifyDataSetChanged();

                            if (type == PULL_REFRESH) {
                                pDialog.dismiss();
                                isRefresh = false;
                            }
                        }
                        break;
                    //第一次没有加入社团
                    case -1:
                        if (type == PULL_REFRESH) {
                            llNoJoin.setVisibility(View.VISIBLE);
                            lvTeam.setVisibility(View.GONE);
                            pDialog.dismiss();
                            isRefresh = false;
                        } else {
                            CommonUtils.showToast("没有可加载的数据了。");
                        }
                        break;
                    default:
                        if (type == PULL_REFRESH) {
                            pDialog.dismiss();
                            isRefresh = false;
                        }
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == PULL_REFRESH) {
                    isRefresh = false;
                    pDialog.dismiss();
                }
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("get");
        MyApplication.getHttpQueue().add(request);
    }


    @Override
    protected void addListener() {
        ivSearch.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        Refresh.setOnRefreshListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开收索界面
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            //没有社团点击添加社团
            case R.id.tv_add:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
                return;

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("get");
        mLocationClient.stop();

    }

    /**
     * 加载更多
     */
    @Override
    public void adapterListenter() {
        page++;
        getTeam(LOADING_MORE, page);
        mAdapter.isLoadingMore = true;
    }

    @Override
    public void onRefresh() {
        mAdapter.isLoadingMore = false;
        mData.clear();
        page = 0;
        getTeam(PULL_REFRESH, page);
    }


}
