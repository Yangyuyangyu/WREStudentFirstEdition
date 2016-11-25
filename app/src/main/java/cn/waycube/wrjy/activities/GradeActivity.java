package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

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
import cn.waycube.wrjy.adapter.GradeAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.Grade;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/13.
 */
public class GradeActivity extends BaseActivity {
    @Bind(R.id.lv_grade)
    ListView lvGrade;


    /**
     * 课程id
     */
    private int id;


    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;


    private GradeAdapter mAdapter;
    private List<Grade> mData = new ArrayList<>();

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        id=getIntent().getIntExtra(KeyUtil.KEY_ONE,0);

        mAdapter = new GradeAdapter(this, mData);
        lvGrade.setAdapter(mAdapter);




        getData();
    }

    /**
     * 获取评分
     */
    private void getData() {

        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");
        Dialog.show();

        String url = UrlConfig.Course.ID_GRADE + "courseId=" + id + "&studentId=" + MyApplication.mUser.getId();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        List<Grade> grades=JSON.parseArray(jsonObject.getString("data"),Grade.class);
                        mData.remove(grades);
                        mData.addAll(grades);
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        Dialog.dismiss();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Dialog.dismiss();
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCode");
        MyApplication.getHttpQueue().add(request);

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_grade);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

}
