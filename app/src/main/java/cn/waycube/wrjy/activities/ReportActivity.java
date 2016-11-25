package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.ImageAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.NoScrollGridView;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ReportActivity extends BaseActivity implements AdapterListenter {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_problem)
    TextView tvProblem;
    @Bind(R.id.tv_solution)
    TextView tvSolution;
    @Bind(R.id.tv_work_content)
    TextView tvWorkContent;
    @Bind(R.id.gv_work)
    NoScrollGridView gvWork;
    @Bind(R.id.tv_submit_job)
    TextView tvSubmitJob;
    @Bind(R.id.tv_ranking)
    TextView tvRanking;
    @Bind(R.id.ll_btn_bg)
    LinearLayout llBtnBg;
    /**
     * 加载框
     */
    private SweetAlertDialog pDialog;
    /**
     * 课程id
     */
    private int id;

    /**
     * 课程报告
     */
    private JSONObject data;

    /**
     * 图片
     */
    private ImageAdapter mAdapter;


    /**
     * 要显示的图片地址
     */
    private String image;


    private int Type;

    @Override
    protected void aadListenter() {
        tvRanking.setOnClickListener(this);
        tvSubmitJob.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //查看等级
            case R.id.tv_ranking:
                Intent intent2 = new Intent(this, ReportRankingActivity.class);
                intent2.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent2);
                break;
            //提交作业
            case R.id.tv_submit_job:
                Intent intent = new Intent(this, SubmitJobActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, data.getInteger("class_id"));
                startActivity(intent);
                break;

            default:
                return;
        }
    }

    @Override
    protected void initVariables() {

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, -1);
        Type = getIntent().getIntExtra(KeyUtil.KEY_TWO, 3);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取课程id
        getReport();
    }

    /**
     * 获取课程报告
     */
    private void getReport() {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在加载课程报告...");
        pDialog.show();
        String url = UrlConfig.Course.ID_REPORT + "courseId=" + id + "&studentId=" + MyApplication.mUser.getId();
        Log.e("查看课程报告url----",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("查看课程报告----",s);
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        pDialog.dismiss();
                        data = (JSONObject) jsonObject.get("data");
                        //配置数据
                        setData();
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
        request.setTag("getReport");
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 配置数据
     */
    private void setData() {
        /**
         * 小课关闭查看排名
         */
        if (Type == 1) {
            //大课
            tvRanking.setVisibility(View.VISIBLE);
            llBtnBg.setVisibility(View.VISIBLE);

            //查看该用户是否已经上传了作业
            //有作业
            if (data.getInteger("has_work") == 1) {

                if (data.getInteger("work_finished") == 0) {
                    //没有提交
                    tvSubmitJob.setVisibility(View.VISIBLE);
                } else {
                    //提交作业了
                    tvSubmitJob.setVisibility(View.GONE);
                }
            } else {
                //没有作业
                tvSubmitJob.setVisibility(View.GONE);
            }
        } else {
            //小课
            tvRanking.setVisibility(View.GONE);
            //查看该用户是否已经上传了作业
            if (data.getInteger("has_work") == 1) {
                if (data.getInteger("work_finished") == 0) {
                    //没有提交
                    tvSubmitJob.setVisibility(View.VISIBLE);
                    llBtnBg.setVisibility(View.VISIBLE);
                } else {
                    //提交作业了
                    tvSubmitJob.setVisibility(View.GONE);
                    llBtnBg.setVisibility(View.GONE);
                }
            } else {
                llBtnBg.setVisibility(View.GONE);

            }
        }


        //名称
        if (!data.getString("name").

                equals("")

                )
            tvName.setText(data.getString("name"));
        //上课时间
        if (!data.getString("class_time").

                equals("")

                )
            tvClassTime.setText(data.getString("class_time"));
        //上课内容
        if (!data.getString("content").

                equals("")

                )
            tvContent.setText(data.getString("content"));
        //上课内容及其问题
        if (!data.getString("problem").

                equals("")

                )
            tvProblem.setText(data.getString("problem"));
        //解决办法
        if (!data.getString("solution").

                equals("")

                )
            tvSolution.setText(data.getString("solution"));
        //判断是否有作业
        if (Integer.valueOf(data.getString("has_work")) == 0)
            tvWorkContent.setText("没有课后作业！");
        else if (!data.getString("work_content").

                equals("")

                )

        {
            tvWorkContent.setText(data.getString("work_content"));
        }
        //图片
        if (!data.getString("img").

                equals("")

                )

        {
            image = data.getString("img");
            List<String> str = new ArrayList<>();
            String[] as = image.split(",");
            for (int i = 0; i < as.length; i++) {
                str.add(as[i]);
            }
            mAdapter = new ImageAdapter(this, str);
            mAdapter.setlisetnter(this);
            gvWork.setAdapter(mAdapter);
            gvWork.setNumColumns(3);


        }

    }


    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_report);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getReport");

    }


    /*
     * 图片放大
     */
    @Override
    public void adapterListenter() {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(KeyUtil.KEY_ONE, image);
        startActivity(intent);


    }
}
