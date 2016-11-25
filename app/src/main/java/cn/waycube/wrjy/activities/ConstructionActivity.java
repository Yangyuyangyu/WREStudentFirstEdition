package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ConstructionActivity extends BaseActivity {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_create_time)
    TextView tvCreateTime;
    @Bind(R.id.tv_studentNum)
    TextView tvStudentNum;
    @Bind(R.id.tv_adminNum)
    TextView tvAdminNum;
    @Bind(R.id.tv_subjectNum)
    TextView tvSubjectNum;

    /**
     * 对话框
     */
    private SweetAlertDialog Dialog;
    /**
     * 社团id
     */
    private int id;

    private JSONObject data;

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);


        getData();
    }

    /**
     * 获取社团建设
     */
    private void getData() {
        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");


        Dialog.show();
        String url = UrlConfig.Team.ID_BUILD + "groupId=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        data = (JSONObject) jsonObject.get("data");
                        CommonUtils.tag(data.toJSONString());
                        initData();
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

    /**
     * 配置数据
     */
    private void initData() {
        //配置名字
        if (!data.getString("name").equals(""))
            tvName.setText(data.getString("name"));
        //配置创建时间
        if (!data.getString("create_time").equals(""))
            tvCreateTime.setText(data.getString("create_time"));
        //配置学生人数
        if (!data.getString("studentNum").equals(""))
            tvStudentNum.setText(data.getString("studentNum"));
        //配置管理人数
        if (!data.getString("adminNum").equals(""))
            tvAdminNum.setText(data.getString("adminNum"));
        //配置科目
        if (!data.getString("subjectNum").equals(""))
            tvSubjectNum.setText(data.getString("subjectNum"));
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_construction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }
}
