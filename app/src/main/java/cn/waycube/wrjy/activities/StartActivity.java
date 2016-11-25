package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.logging.Handler;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.model.User;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.ShareReferenceUtils;

/**
 * Created by Administrator on 2016/5/12.
 */
public class StartActivity extends BaseActivity {
    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        //表示已经不是第一次进入了不需要加载引导页
        if (!TextUtils.isEmpty(ShareReferenceUtils.getValue(KeyUtil.KEY_GUIDE))) {
            //获取用户id，模拟用户登录
            if (!ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID).equals("")) {
                //没有登录过，进入登录界面
                start();

            } else {
                //已经登录过，直接获取用户信息
                getUser();

            }

        } else {
            //用户第一次进入，进入引导页

            //获取用户id，模拟用户登录
            if (TextUtils.isEmpty(ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID))) {
                //没有登录过，进入登录界面
                start();

            } else {
                //已经登录过，直接获取用户信息
                getUser();

            }
        }

    }


    /**
     * 获取用户信息
     */
    private void getUser() {
        String url = UrlConfig.User.USER_ID + "studentId=" + ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        if (jsonObject != null && jsonObject.getString("data") != null)
                            MyApplication.mUser = JSON.parseObject(jsonObject.getString("data"), User.class);

                        start();
                        break;
                    default:
                        start();
                        return;
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                start();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(2* 1000,//默认超时时间，
                0,//默认最大尝试次数
                1.0f));
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("get");
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 进入如app
     */
    private void start() {
        new android.os.Handler().postDelayed(new Runnable() {

            public void run() {
                //表示模拟用户登录成功，进入主页面
                if (MyApplication.mUser != null) {

                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                } else {
                    //表示没有获取到用户的信息,进入登录界面
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }


            }
        }, 3000);//三秒之后执行
    }

    @Override
    protected void initLayout() {
        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MyApplication.getInstance().addActivity(this);


        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("get");

    }

}
