package cn.waycube.wrjy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.MapActivity;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/18.
 */
public class HomepageFragment extends BaseFragment {


    @Bind(R.id.tv_brief)
    TextView tvBrief;
    @Bind(R.id.tv_feature)
    TextView tvFeature;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ll_location)
    LinearLayout llLocation;

    private  String data;
    private double longitude, latitude;

    public static HomepageFragment  getString(String data) {
        HomepageFragment instance = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    protected void initVariables() {
        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("name");
        }
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }

        JSONObject jsonObject= JSON.parseObject(data);
        //地址
        if (!jsonObject.getString("location").equals(""))
            tvLocation.setText(jsonObject.getString("location"));
        //特点
        if (!jsonObject.getString("feature").equals(""))
            tvFeature.setText(jsonObject.getString("feature"));
        //简介
        if (!jsonObject.getString("brief").equals(""))
            tvBrief.setText(jsonObject.getString("brief"));
        //设置经度
        if (!jsonObject.getString("longitude").equals(""))
            longitude = jsonObject.getDouble("longitude");
        //设置纬度
        if (!jsonObject.getString("latitude").equals(""))
            latitude = jsonObject.getDouble("latitude");
    }

    @Override
    protected void addListener() {

        llLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开地图
            case R.id.ll_location:
                Intent intent = new Intent(getActivity(), MapActivity.class);

                intent.putExtra(KeyUtil.KEY_ONE, longitude);
                intent.putExtra(KeyUtil.KEY_TWO, latitude);
                startActivity(intent);
                break;
            default:
                return;


        }
    }

}
