package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.fragments.CourseFragment;
import cn.waycube.wrjy.fragments.TeamFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.ShareReferenceUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class ChangePasswordActivity extends BaseActivity {
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.iv_look)
    ImageView ivLook;
    @Bind(R.id.et_password2)
    EditText etPassword2;
    @Bind(R.id.iv_look2)
    ImageView ivLook2;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.tv_affirm)
    TextView tvAffirm;

    /**
     * 表示有没有查看密码
     */
    private boolean look1 = false;

    private boolean look2 = false;


    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;

    @Override
    protected void aadListenter() {
        ivLook.setOnClickListener(this);
        ivLook2.setOnClickListener(this);
        tvAffirm.setOnClickListener(this);

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    view1.setBackgroundColor(getResources().getColor(R.color.green_color));
                } else {
                    // 此处为失去焦点时的处理内容
                    view1.setBackgroundColor(getResources().getColor(R.color.text_color2));
                }

            }
        });
        etPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    view2.setBackgroundColor(getResources().getColor(R.color.green_color));
                } else {
                    // 此处为失去焦点时的处理内容
                    view2.setBackgroundColor(getResources().getColor(R.color.text_color2));
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //查看第一个密码
            case R.id.iv_look:
                if (!look1) {
                    look1 = true;
                    ivLook.setImageResource(R.mipmap.see_password_on);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    look1 = false;
                    ivLook.setImageResource(R.mipmap.see_password_off);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
            //查看第二个密码
            case R.id.iv_look2:
                if (!look2) {
                    look2 = true;
                    ivLook2.setImageResource(R.mipmap.see_password_on);
                    etPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    look2 = false;
                    ivLook2.setImageResource(R.mipmap.see_password_off);
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            //确认修改
            case R.id.tv_affirm:
                affirm();
                break;
            default:
                return;
        }
    }

    /**
     * 确认修改密码
     */
    private void affirm() {
        if (etPassword.getText().toString().length()<6||etPassword2.getText().toString().length()<6){
            CommonUtils.showToast("请输入6-20位的密码");
            return;
        }

        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在修密码...");


        Dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Set.CHANGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        //关闭所有fragment
                        MyApplication.getInstance().exit();
                        //删除用户id
                        ShareReferenceUtils.putValue(KeyUtil.KEY_USER_ID, "");
                        MyApplication.mUser=null;
                        //删除推送标签
                        List<String> tag = new ArrayList<>();
                        tag.add(String.valueOf(MyApplication.mUser.getId()));
                        PushManager.delTags(getApplicationContext(), tag);

                        //进入主页要刷新
                        TeamFragment.isRefresh=true;
                        CourseFragment.isRefresh=true;

                        CommonUtils.showToast("修改密码成功请重新登录！");
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
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
                CommonUtils.showToast("网络错误！！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("student", String.valueOf(MyApplication.mUser.getId()));
                map.put("oldPass", etPassword.getText().toString());
                map.put("newPass",etPassword2.getText().toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("change");
        MyApplication.getHttpQueue().add(request);

    }

    @Override
    protected void initVariables() {


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_change_password);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("change");

    }

}
