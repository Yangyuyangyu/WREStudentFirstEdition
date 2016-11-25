package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.Utils;

/**
 * Created by Administrator on 2016/4/15.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.et_verify)
    EditText etVerify;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.cb_agree)
    CheckBox cbAgree;
    @Bind(R.id.tv_clause)
    TextView tvClause;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_agreement)
    TextView tvAgreement;
    //获取验证倒计时
    private TimeCount time;

    //验证码id
    private String log_id;

    //是否同意服务条款
    public static boolean isAgree = false;




    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;

    @Override
    protected void aadListenter() {
        tvLogin.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        tvClause.setOnClickListener(this);
        tvAgreement.setOnClickListener(this);

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = true;
                } else {
                    isAgree = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //获取验证码
            case R.id.tv_get_code:
                getCode();
                break;

            //用户点击注册
            case R.id.tv_login:
                register();
                break;
            //打开服务条款
            case R.id.tv_clause:
                Intent intent=new Intent(this,ClauseActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE,"1");
                startActivity(intent);
                break;
            //打开隐私保护
            case R.id.tv_agreement:
                Intent intent1=new Intent(this,ClauseActivity.class);
                intent1.putExtra(KeyUtil.KEY_ONE,"2");
                startActivity(intent1);
                break;
            default:
                return;
        }

    }


    /**
     * 注册
     */
    private void register() {
        if (!Utils.isPhoneNum(etNumber.getText().toString())) {
            CommonUtils.showToast("请输入正确的手机号码！");
            return;
        } else if (etPassword.getText().toString().length() < 6) {
            CommonUtils.showToast("请输入6-18位的密码");
            return;
        } else if (log_id == null) {
            //没有获取验证码
            CommonUtils.showToast("请先获取验证码！");
            return;
        } else if (!isAgree) {
            //服务条款
            CommonUtils.showToast("请确认服务条款！");
            return;
        }
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在注册...");

        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.User.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        time.cancel();
                        pDialog.dismiss();
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
                map.put("mobile", etNumber.getText().toString());
                map.put("pass", etPassword.getText().toString());
                map.put("code", etVerify.getText().toString());
                map.put("log_id", log_id);
                return map;
            }
        };
        setTmie(request);
        request.setTag("regisite");
        MyApplication.getHttpQueue().add(request);
    }


    /**
     * 获取验证码
     */
    private void getCode() {

        if (Utils.isPhoneNum(etNumber.getText().toString())) {
            time.start();
            String url = UrlConfig.User.USER_GET_CODE + etNumber.getText().toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            CommonUtils.showToast(jsonObject.getString("msg"));
                            log_id = jsonObject.getString("log_id");
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
        } else {
            CommonUtils.showToast("请输入正确的手机号！");

        }

    }


    @Override
    protected void initVariables() {
        time = new TimeCount(60000, 1000);


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_register);
    }



    /**
     * 验证倒计时
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setText(millisUntilFinished / 1000 + "秒");
            tvGetCode.setClickable(false);

        }

        @Override
        public void onFinish() {
            tvGetCode.setText("获取验证码");
            tvGetCode.setClickable(true);

        }

    }

    /*
        *点击取消
        */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getHttpQueue().cancelAll("regisite");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");
        MyApplication.getHttpQueue().cancelAll("regisite");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}
