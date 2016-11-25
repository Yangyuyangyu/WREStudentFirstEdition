package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.MyFragmentPagerAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.fragments.EvaluateFragment;
import cn.waycube.wrjy.fragments.ExamineFragment;
import cn.waycube.wrjy.fragments.ExplainFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.OnPopupListenter;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.NoScrollViewPager;
import cn.waycube.wrjy.views.PopupCourse;
import cn.waycube.wrjy.views.TabLineIndicator;

/**
 * Created by Administrator on 2016/4/15.
 */
public class CourseDetailsActivity extends BaseActivity implements TabLineIndicator.OnTabReselectedListener, OnPopupListenter {


    @Bind(R.id.homepage_maintype)
    TabLineIndicator homepageMaintype;
    @Bind(R.id.homepage_rl_addsbaner)
    RelativeLayout homepageRlAddsbaner;
    @Bind(R.id.homepage_vPager)
    NoScrollViewPager homepageVPager;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.ll_team)
    LinearLayout llTeam;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.tv_name2)
    TextView tvName2;
    @Bind(R.id.tv_brief)
    TextView tvBrief;
    @Bind(R.id.tv_agency)
    TextView tvAgency;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @Bind(R.id.tv_label_admin)
    TextView tvLabelAdmin;
    @Bind(R.id.ll_map)
    LinearLayout llMap;


    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ArrayList<Fragment> views;// Tab页面列表
    private ArrayList<HashMap<String, String>> maps = null;
    private EvaluateFragment evaluateFragment;
    private ExamineFragment examineFragment;
    private ExplainFragment explainFragment;


    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;
    /**
     * popupWindow
     */
    private PopupCourse popupCourse;

    /**
     * 课程详细id
     */
    private int cid;

    /**
     * id
     */
    private int id;

    /**
     * 课程详情
     */
    private JSONObject data;

    /**
     * 所属机构id
     */
    private int agency_id = -1;

    /**
     * 要投诉老师的id
     */
    private String teacher_id;
    /**
     * 摇头说老师的名字
     */
    private String name;

    @Override

    protected void aadListenter() {
        ivMore.setOnClickListener(this);
        llTeam.setOnClickListener(this);
        llMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开请假和投诉的popupWindow
            case R.id.iv_more:
                popupCourse = new PopupCourse(this);
                popupCourse.showPopupWindow(this, rlTitle);
                popupCourse.setOnPopupListenter(this);
                break;
            //打开所属机构
            case R.id.ll_team:
                if (agency_id != -1) {
                    Intent intent = new Intent(this, TeamActivity.class);
                    intent.putExtra(KeyUtil.KEY_ONE, agency_id);
                    startActivity(intent);
                } else {
                    CommonUtils.showToast("该老师没有所属机构！");
                }
                break;
            //打开地图
            case R.id.ll_map:

                break;

            default:

                return;
        }
    }

    @Override
    protected void initVariables() {


        //根据id获取课程详情
        cid = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);
        id = getIntent().getIntExtra(KeyUtil.KEY_TWO, 0);
        getCourseDetails();


    }


    /**
     * 获取课程详情
     */
    private void getCourseDetails() {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在加载数据...");


        pDialog.show();
        String url = UrlConfig.Course.ID_COURSE_DETAILS + "courseId=" + cid + "&studentId=" + MyApplication.mUser.getId() + "&id=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("课程详情数据", s);
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        data = (JSONObject) jsonObject.get("data");
                        CommonUtils.tag(data.toJSONString());
                        //配置数据
                        initData();
                        break;
                    default:
                        pDialog.dismiss();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCourse");
        MyApplication.getHttpQueue().add(request);

    }

    /**
     * 配置数据
     */
    private void initData() {

        //名称
        if (!data.getString("name").equals("")) {
            tvName.setText(data.getString("name"));
            tvName2.setText(data.getString("name"));
        }
        //图片
        if (!data.getString("img").equals("")) {
            ImageLoader.getInstance().displayImage(data.getString("img"), ivImg);
        }
        //简介
        if (!data.getString("brief").equals("")) {
            tvBrief.setText(data.getString("brief"));
        }
        //地址
        if (!data.getString("address").equals(""))
            tvAddress.setText(data.getString("address"));
        //所属机构
        if (!data.getString("agency").equals(""))
            tvAgency.setText(data.getString("agency"));
        //设置老师详情
        if (!data.get("teacherInfo").equals("")) {
            JSONObject jsonObject = data.getJSONObject("teacherInfo");
            //设置老师头像
            if (!jsonObject.getString("head").equals(""))
                ImageLoader.getInstance().displayImage(jsonObject.getString("head"), ivHead);
            //老师的名字
            if (!jsonObject.getString("name").equals(""))
                tvTeacherName.setText(jsonObject.getString("name"));
            //特点
            if (!jsonObject.getString("label_admin").equals(""))
                tvLabelAdmin.setText(jsonObject.getString("label_admin"));

        }

        //设置要投诉老师的id
        if (!data.get("teacherInfo").equals("")) {
            JSONObject jsonObject = data.getJSONObject("teacherInfo");
            if (!(jsonObject.getString("name")).equals(""))
                name = jsonObject.getString("name");
            if (!(jsonObject.getString("id")).equals(""))
                teacher_id = jsonObject.getString("id");
        }
        if (!(data.get("agency_id")).equals(""))
            agency_id = Integer.valueOf(data.getIntValue("agency_id"));

        pDialog.dismiss();
        //配置fragment
        InitViewPager();
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_course_details);
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
        evaluateFragment = EvaluateFragment.getString(data.toJSONString());
        examineFragment = ExamineFragment.getString(data.toJSONString());
        explainFragment = ExplainFragment.getString(data.toJSONString());
        views.add(explainFragment);
        views.add(examineFragment);
        views.add(evaluateFragment);
        homepageVPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), views));
        homepageVPager.setCurrentItem(0);
        homepageVPager.addOnPageChangeListener(new MyOnPageChangeListener());


        homepageMaintype.setOnTabReselectedListener(this);
        maps = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i == 0)
                map.put("name", "课程说明");
            else if (i == 1)
                map.put("name", "查看详情");
            else if (i == 2)
                map.put("name", "评价");
            maps.add(map);
        }
        homepageMaintype.addAdapter(maps);
        homepageMaintype.setPos(currIndex);

    }

    /**
     * popupWindow监听事件
     *
     * @param tag
     */
    @Override
    public void popupListener(int tag) {
        //打开请假页面
        if (tag == 0) {
            Intent intent = new Intent(this, LeaveActivity.class);
            intent.putExtra(KeyUtil.KEY_ONE, cid);
            startActivity(intent);
        }
        //打开投诉界面
        if (tag == 1) {
            Intent intent = new Intent(this, ComplaintActivity.class);
            intent.putExtra(KeyUtil.KEY_ONE, name);
            intent.putExtra(KeyUtil.KEY_TWO, teacher_id);
            startActivity(intent);
        }
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
                case 2:
                    homepageMaintype.setPos(position);
                    currIndex = position;
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCourse");

    }
}
