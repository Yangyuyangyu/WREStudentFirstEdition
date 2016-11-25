package cn.waycube.wrjy.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.MyFragmentPagerAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.fragments.AssociationFragment;
import cn.waycube.wrjy.fragments.HomepageFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.TabLineIndicator;

/**
 * Created by Administrator on 2016/4/15.
 */
public class TeamActivity extends BaseActivity implements TabLineIndicator.OnTabReselectedListener {
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @Bind(R.id.homepage_maintype)
    TabLineIndicator homepageMaintype;
    @Bind(R.id.homepage_rl_addsbaner)
    RelativeLayout homepageRlAddsbaner;
    @Bind(R.id.homepage_vPager)
    ViewPager homepageVPager;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_brief)
    TextView tvBrief;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ArrayList<Fragment> views;// Tab页面列表
    private ArrayList<HashMap<String, String>> maps = null;


    private HomepageFragment homepageFragment;
    private AssociationFragment associationFragment;

    /**
     * 传递过来机构的id
     */
    private int id;
    /**
     * 社团详情
     */
    private JSONObject agency;

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);


        //获取机构详情
        getData();
    }

    /**
     * 获取机构详情
     */
    private void getData() {

        String url = UrlConfig.Team.ID_AGENCY + "agencyId=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        agency = (JSONObject) jsonObject.get("data");
                        initData();
                        break;
                    default:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("网络错误！");
            }
        });
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCode");
        MyApplication.getHttpQueue().add(request);

    }

    /**
     * 配置数据
     */
    private void initData() {
        //图片
        if (agency.getString("img") != null)
            ImageLoader.getInstance().displayImage(agency.getString("img"), ivAvatar);
        //社团名称
        if (agency.getString("name") != null)
            tvName.setText(agency.getString("name"));
        //社团简介
        if (agency.getString("brief") != null)
            tvBrief.setText(agency.getString("brief"));
        //初始化ViewPager
        InitViewPager();
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_team);
    }

    @Override
    public void onTabReselected(int position) {
        homepageVPager.setCurrentItem(position);
    }

    /**
     * 初始化ViewPage
     */
    private void InitViewPager() {
        views = new ArrayList<>();
        homepageFragment =HomepageFragment.getString(agency.toJSONString());
//        associationFragment = new AssociationFragment(agency.toString());
        associationFragment =  AssociationFragment.getString(agency.toString());
        views.add(homepageFragment);
        views.add(associationFragment);
        homepageVPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), views));
        homepageVPager.setCurrentItem(0);
        homepageVPager.addOnPageChangeListener(new MyOnPageChangeListener());


        homepageMaintype.setOnTabReselectedListener(this);
        maps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 2; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i == 0)
                map.put("name", "主页");
            else if (i == 1)
                map.put("name", "机构社团");
            maps.add(map);
        }
        homepageMaintype.addAdapter(maps);
        homepageMaintype.setPos(currIndex);

    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            switch (position) {
                case 0:
                    homepageMaintype.setPos(position);
                    currIndex = position;
                    break;
                case 1:
                    homepageMaintype.setPos(position);
                    currIndex = position;
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
