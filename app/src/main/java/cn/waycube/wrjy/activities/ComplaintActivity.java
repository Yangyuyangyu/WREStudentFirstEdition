package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.IDN;
import java.util.HashMap;
import java.util.Map;

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
 * Created by Administrator on 2016/4/15.
 */
public class ComplaintActivity extends BaseActivity {
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_data)
    EditText etData;


    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;

    /**
     * 要投诉老师id
     */
    private String id;

    @Override
    protected void aadListenter() {
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //提交投诉
            case R.id.tv_submit:
                submit();
                break;
            default:
                return;
        }
    }

    /**
     * 处理用户投诉
     */
    private void submit() {

        if (etData.getText().toString().length() == 0) {
            CommonUtils.showToast("请输入投诉原因！");
            return;
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在投诉...");

        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.COMPLAINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.getString("投诉已提交！"));
                        pDialog.dismiss();
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
                CommonUtils.showToast("网络错误！！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("student", String.valueOf(MyApplication.mUser.getId()));
                map.put("teacherId", id);
                map.put("reason", etData.getText().toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("complaint");
        MyApplication.getHttpQueue().add(request);

    }

    @Override
    protected void initVariables() {



        if (getIntent().getStringExtra(KeyUtil.KEY_ONE) != null)
            tvName.setText(getIntent().getStringExtra(KeyUtil.KEY_ONE));
        if (getIntent().getStringExtra(KeyUtil.KEY_TWO) != null)
            id = (getIntent().getStringExtra(KeyUtil.KEY_TWO));
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_complaint);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("complaint");

    }
}
