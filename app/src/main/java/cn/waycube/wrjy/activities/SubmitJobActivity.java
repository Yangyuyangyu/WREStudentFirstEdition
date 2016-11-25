package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

/**
 * Created by Administrator on 2016/4/15.
 */
public class SubmitJobActivity extends BaseActivity {
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.et_data)
    EditText etData;
    @Bind(R.id.btn_are)
    RadioButton btnAre;
    @Bind(R.id.btn_no)
    RadioButton btnNo;
    @Bind(R.id.rg_btn)
    RadioGroup rgBtn;
    @Bind(R.id.et_remark)
    EditText etRemark;


    /**
     * 课程的id
     */
    private int id;

    /**
     * 标记用户是否完成作业
     */
    private int finish = -1;


    /**
     * 加入社团对话框
     */
    private SweetAlertDialog pDialog;

    @Override
    protected void aadListenter() {
        tvSubmit.setOnClickListener(this);
        rgBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //选择是
                if (checkedId == btnAre.getId()) {
                    finish = 1;
                } else {
                    //选择没有完成
                    finish = 0;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //提交
            case R.id.tv_submit:
                submit();

                break;

            default:
                return;


        }
    }

    /**
     * 处理用户提交作业
     */
    private void submit() {
        //没有输入上课时间
        if (etData.getText().length()==0){
            CommonUtils.showToast("请输入上课时间！");
            return;
        }else if (finish==-1){
            //没有选着是否完成
            CommonUtils.showToast("请选择是否完成！");
            return;
        }
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在提交作业...");

        //所有的填完了
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.HOME_WORK, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        pDialog.dismiss();
                        CommonUtils.showToast("作业提交成功！");
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
                map.put("course", String.valueOf(id));
                map.put("time", etData.getText().toString());
                map.put("finish", String.valueOf(finish));
                map.put("remark", etRemark.getText().toString());
                CommonUtils.tag(map.toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE,0);


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_submit_job);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("add");

    }

}
