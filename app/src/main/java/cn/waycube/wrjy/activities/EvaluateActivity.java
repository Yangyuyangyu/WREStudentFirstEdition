package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/19.
 */
public class EvaluateActivity extends BaseActivity {

    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.et_data)
    EditText etData;
    /**
     * 要评价课程的id
     */
    private int id;

    /**
     *上传评论加载框
     */
    private SweetAlertDialog Diaglog;

    @Override
    protected void aadListenter() {
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //提交评价
            case R.id.tv_submit:

                submit();
                break;


            default:
                return;
        }
    }

    /**
     * 提交盘评价
     */
    private void submit() {
        //没有输入评价内容
        if (etData.getText().length()==0){
            CommonUtils.showToast("请输入评价内容");
            return;
        }
        Diaglog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Diaglog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Diaglog.setTitleText("正在上传评价...");

        Diaglog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Diaglog.dismiss();
                        CommonUtils.showToast("评价完成！");
                        //从新在获取一次课程表
                        CourseFragment.isRefresh=true;
                        finish();
                        break;
                    default:
                        Diaglog.dismiss();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Diaglog.dismiss();
                CommonUtils.showToast("网络错误！！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("studentId",String.valueOf( MyApplication.mUser.getId()));
                map.put("content", etData.getText().toString());
                Log.e("评价",map.toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }


    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);



    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_evaluate);
    }


    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("add");

    }
}
