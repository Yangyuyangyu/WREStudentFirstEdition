package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.User;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.ShareReferenceUtils;
import cn.waycube.wrjy.utils.Utils;

/**
 * Created by Administrator on 2016/4/14.
 */
public class LoggingActivity extends BaseActivity {
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.et_password)
    EditText etPassword;

    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;


    @Override
    protected void aadListenter() {
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //登录到主界面
            case R.id.tv_login:
                login();
                break;
            default:
                return;
        }
    }

    /**
     * 登录处理
     */
    private void login() {


        if (!Utils.isPhoneNum(etNumber.getText().toString())) {
            CommonUtils.showToast("请输入正确的手机号码！");
            return;
        } else if (etPassword.getText().toString().length() < 6) {
            CommonUtils.showToast("请输入6-20位的密码");
            return;
        }
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("登录");

        pDialog.show();
        String url = UrlConfig.User.LOGGING + "mobile=" + etNumber.getText().toString() + "&pass=" + etPassword.getText().toString();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        if (jsonObject != null && jsonObject.getString("data") != null){
                            MyApplication.mUser = JSON.parseObject(jsonObject.getString("data"), User.class);
                            //存储用户id
                            ShareReferenceUtils.putValue(KeyUtil.KEY_USER_ID,String.valueOf(MyApplication.mUser.getId()));
                        }


                        pDialog.dismiss();
                        //关闭掉所有activity
                        MyApplication.getInstance().exit();
                        finish();
                        startActivity(new Intent(LoggingActivity.this, MainActivity.class));
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
        request.setTag("logging");
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    protected void initVariables() {


    }


    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_logging);
    }


    /*
     *点击取消
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getHttpQueue().cancelAll("logging");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("logging");

    }

}
