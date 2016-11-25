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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.fragments.CourseFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.et_data)
    EditText etData;
    @Bind(R.id.et_e_mail)
    EditText etEMail;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;


    /**
     * 加载框
     */
    private SweetAlertDialog pDialog;

    @Override
    protected void aadListenter() {
        tvSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //提交意见反馈
        switch (v.getId()) {
            case R.id.tv_submit:
                submit();
                break;
            default:
                return;
        }
    }

    private void submit() {
        //没有输入意见
        if (etData.getText().length() == 0) {
            CommonUtils.showToast("请输入意见");
            return;
        }


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在提交你的意见...");

        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Set.FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        pDialog.dismiss();
                        CommonUtils.showToast("意见提交成功！我将尽快处理...");
                        finish();
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
                map.put("content", etData.getText().toString());
                map.put("email", etEMail.getText().toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }


    @Override
    protected void initVariables() {


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("add");

    }
}
