package cn.waycube.wrjy.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.CourseAdapter;
import cn.waycube.wrjy.adapter.TutorialAdapter;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.ConfirmListenter;
import cn.waycube.wrjy.model.Course;
import cn.waycube.wrjy.model.CourseWeek;
import cn.waycube.wrjy.model.SmallCourse;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.views.NoScrollListView;

/**
 * Created by Administrator on 2016/4/12.
 */
public class CourseFragment extends BaseFragment implements ConfirmListenter, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_last_week)
    TextView tvLastWeek;
    @Bind(R.id.tv_next_week)
    TextView tvNextWeek;
    @Bind(R.id.v_monday)
    View vMonday;
    @Bind(R.id.ll_monday)
    LinearLayout llMonday;
    @Bind(R.id.v_tuesday)
    View vTuesday;
    @Bind(R.id.ll_tuesday)
    LinearLayout llTuesday;
    @Bind(R.id.v_wednesday)
    View vWednesday;
    @Bind(R.id.ll_wednesday)
    LinearLayout llWednesday;
    @Bind(R.id.v_thursday)
    View vThursday;
    @Bind(R.id.ll_thursday)
    LinearLayout llThursday;
    @Bind(R.id.v_friday)
    View vFriday;
    @Bind(R.id.ll_friday)
    LinearLayout llFriday;
    @Bind(R.id.v_saturday)
    View vSaturday;
    @Bind(R.id.ll_saturday)
    LinearLayout llSaturday;
    @Bind(R.id.v_week)
    View vWeek;
    @Bind(R.id.ll_week)
    LinearLayout llWeek;
    @Bind(R.id.tv_big)
    TextView tvBig;
    @Bind(R.id.lv_list)
    NoScrollListView lvList;
    @Bind(R.id.tv_small)
    TextView tvSmall;
    @Bind(R.id.lv_list2)
    NoScrollListView lvList2;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout idSwipeLy;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    /**
     * 大课适配器
     */
    private CourseAdapter mAdapter;
    private List<Course> mData = new ArrayList<>();

    /**
     * 小课适配器
     */
    private TutorialAdapter mAdapter2;
    private List<SmallCourse> mData2 = new ArrayList<>();

    /**
     * 大课
     */
    private List<CourseWeek> bigCourse = new ArrayList<>();

    /**
     * 当课表发生改变时返回该页面刷新数据
     */
    public static boolean isRefresh = true;


    /**
     * 表示查看那一周的课程
     */
    private static int week = 0;

    /**
     * 加载框
     */
    private SweetAlertDialog pDialog, dialog, cofirmDiaglog;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    protected void initVariables() {
        llFriday.setOnClickListener(this);
        llMonday.setOnClickListener(this);
        llSaturday.setOnClickListener(this);
        llThursday.setOnClickListener(this);
        llWednesday.setOnClickListener(this);
        llWeek.setOnClickListener(this);
        llTuesday.setOnClickListener(this);

        idSwipeLy.setColorSchemeResources(R.color.green_color);


        mAdapter = new CourseAdapter(getActivity(), mData);
        mAdapter.setListenter(this);
        lvList.setAdapter(mAdapter);

        mAdapter2 = new TutorialAdapter(getActivity(), mData2);
        lvList2.setAdapter(mAdapter2);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh)
            //获取课程表
            getCourse(week);
    }

    /**
     * 获取课程表
     * <p/>
     * week表示查看那周课程，0表示本周，-1表示上周，1表示下一周
     */
    private void getCourse(int week) {

        idSwipeLy.setRefreshing(false);

        mData.clear();
        mData2.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在加载课程表...");
        pDialog.show();

        String url = UrlConfig.Course.ID_COURSE + "id=" + MyApplication.mUser.getId() + "&week=" + week;
        Log.e("课程表url-------------",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        pDialog.dismiss();
                        //表示已经更新了
                        isRefresh = false;

                        JSONObject jsonObject2 = (JSONObject) jsonObject.get("data");
                        //大课
                        List<CourseWeek> course = JSON.parseArray(jsonObject2.getString("courseList"), CourseWeek.class);
                        bigCourse.clear();
                        if (jsonObject2.getString("courseList") != null){
                            bigCourse.addAll(course);
                            CommonUtils.tag(">>>>>>>>>>>>>>>>");
                        }

                        //小课
                        List<SmallCourse> smallCourses = JSON.parseArray(jsonObject2.getString("smallClass"), SmallCourse.class);
                        mData2.removeAll(smallCourses);
                        mData2.addAll(smallCourses);
                        mAdapter2.notifyDataSetChanged();

                        if (mData2.size() > 0) {
                            tvSmall.setVisibility(View.GONE);
                        } else {
                            tvSmall.setVisibility(View.VISIBLE);
                        }


                        //设置大课参数
                        initBigCourse();

                        break;
                    default:
                        bigCourse.clear();
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


    private void initBigCourse() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        //获取当前是星期几

        if (date.equals("8")) {
            String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            setAdapter(mWay);

        } else {
            setAdapter(date);
        }
    }


    /**
     * 标记用户是否选择了不是当天的课程
     */
    public static String date = "8";

    /**
     * 按时间设置课程表
     */
    private void setAdapter(String mWay) {
        mData.clear();
        mAdapter.notifyDataSetChanged();


        if ("1".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("7".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(6);
                    List<Course> course = new ArrayList<>();
                    bigCourse.get(i).setCourse(course);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        } else if ("2".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("1".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(0);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if ("3".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("2".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(1);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        } else if ("4".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("3".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(2);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        } else if ("5".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("4".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(3);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        } else if ("6".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("5".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(4);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        } else if ("7".equals(mWay)) {
            for (int i = 0; i < bigCourse.size(); i++) {
                if ("6".equals(bigCourse.get(i).getWeek())) {
                    changeBottomBtnStyle(5);
                    mData.removeAll(bigCourse.get(i).getCourse());
                    mData.addAll(bigCourse.get(i).getCourse());
                    if (bigCourse.get(i).getCourse().size() > 0) {
                        tvBig.setVisibility(View.GONE);
                    } else {
                        tvBig.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
        //适配小课

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //周一
            case R.id.ll_monday:
                date = "2";
                changeBottomBtnStyle(0);
                setAdapter("2");
                break;
            //周二
            case R.id.ll_tuesday:
                date = "3";
                changeBottomBtnStyle(1);
                setAdapter("3");
                break;
            //周三
            case R.id.ll_wednesday:
                date = "4";
                changeBottomBtnStyle(2);
                setAdapter("4");
                break;
            //周四
            case R.id.ll_thursday:
                date = "5";
                changeBottomBtnStyle(3);
                setAdapter("5");
                break;
            //周五
            case R.id.ll_friday:
                date = "6";
                changeBottomBtnStyle(4);
                setAdapter("6");
                break;
            //周六
            case R.id.ll_saturday:
                date = "7";
                changeBottomBtnStyle(5);
                setAdapter("7");
                break;
            //周日
            case R.id.ll_week:
                date = "1";
                changeBottomBtnStyle(6);
                setAdapter("1");
                break;
            //查看上一周课程
            case R.id.tv_last_week:

                if (-3 < week && week < 4)
                    week = week - 1;
                setTitle(week);
                getCourse(week);
                break;
            //查看下一周课程
            case R.id.tv_next_week:

                if (-4 < week && week < 3)
                    week = week + 1;
                setTitle(week);
                getCourse(week);
                break;
            default:
                return;
        }


    }

    /**
     * 设置标题连显示的标题
     */
    private void setTitle(int week){
        switch (week){
            case -3:
                tvTitle.setText("课程表(上三周)");
                break;
            case -2:
                tvTitle.setText("课程表(上二周)");
                break;
            case -1:
                tvTitle.setText("课程表(上一周)");
                break;
            case 0:
                tvTitle.setText("课程表(本周)");
                break;
            case 1:
                tvTitle.setText("课程表(下一周)");
                break;
            case 2:
                tvTitle.setText("课程表(下两周)");
                break;
            case 3:
                tvTitle.setText("课程表(下三周)");
                break;
            default:
                return;
        }



    }


    /**
     * 记录上次底部被点击按钮索引
     */
    private int pre_index = 0;

    /**
     * 修改对应的图标和颜色
     *
     * @param index
     */
    private void changeBottomBtnStyle(int index) {
        if (index < 0 || index > 6 || index == pre_index)
            return;

        View[] ivs = {vMonday, vTuesday, vWednesday, vThursday, vFriday, vSaturday, vWeek};

        ivs[index].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.ons));
        ivs[pre_index].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offs));

        pre_index = index;

    }

    @Override
    protected void addListener() {
        idSwipeLy.setOnRefreshListener(this);
        tvLastWeek.setOnClickListener(this);
        tvNextWeek.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCourse");
        MyApplication.getHttpQueue().cancelAll("add");

    }

    /**
     * 要确认上课的id
     */
    private int id;

    /**
     * 用户点击确认上课事件处理
     */
    @Override
    public void confirmListenter(int id) {
        this.id = id;
        dialog = new SweetAlertDialog(getActivity())
                .setTitleText("温馨提示!")
                .setContentText("是否确认上课?")
                .setCancelText("否")
                .setConfirmText("是")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //确认上课
                        cofirm();
                        dialog.dismiss();

                    }
                });

        dialog.show();

    }

    /**
     * 确认上课
     */
    private void cofirm() {

        cofirmDiaglog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        cofirmDiaglog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        cofirmDiaglog.setTitleText("正在确认上课...");
        cofirmDiaglog.setCancelable(false);

        cofirmDiaglog.show();

        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.COFIRM, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        cofirmDiaglog.dismiss();
                        CommonUtils.showToast("确认上课完成！");
                        //从新在获取一次课程表
                        getCourse(week);
                        break;
                    default:
                        cofirmDiaglog.dismiss();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                cofirmDiaglog.dismiss();
                CommonUtils.showToast("网络错误！！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("sid", String.valueOf(MyApplication.mUser.getId()));
                return map;
            }
        };
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }

    /*
     *下拉加载更多
     */
    @Override
    public void onRefresh() {
        getCourse(week);
    }

}
