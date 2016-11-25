package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.Utils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class InviteActivity extends BaseActivity {
    @Bind(R.id.et_data)
    EditText etData;
    @Bind(R.id.tv_invite)
    TextView tvInvite;

    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog2;

    @Override
    protected void aadListenter() {
        tvInvite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //邀请好友
            case R.id.tv_invite:
                invite();
                break;
            default:
                return;

        }
    }

    /**
     * 邀请好友
     */
    private void invite() {
        if (!Utils.isPhoneNum(etData.getText().toString())) {
            CommonUtils.showToast("请输入正确的手机号码！");
            return;
        } else {

            Dialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            Dialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog2.setTitleText("正在发送邀请...");
            Dialog2.show();
            String url = UrlConfig.Set.INVITE + "mobile=" + etData.getText().toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            Dialog2.dismiss();
                            CommonUtils.showToast(jsonObject.getString("msg"));
                            break;
                        default:
                            Dialog2.dismiss();
                            CommonUtils.showToast(jsonObject.getString("msg"));
                            return;
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Dialog2.dismiss();
                    CommonUtils.showToast("网络错误！");
                }
            });
            setTmie(request);
            //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
            request.setTag("invite");
            MyApplication.getHttpQueue().add(request);

        }

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_invite);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("invite");
    }

}
