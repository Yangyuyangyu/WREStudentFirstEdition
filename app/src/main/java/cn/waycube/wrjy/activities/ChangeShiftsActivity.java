package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.dialog.ChangeShifteDialog;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.AllGroup;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ChangeShiftsActivity extends BaseActivity {
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.ll_group)
    LinearLayout llGroup;
    @Bind(R.id.tv_subject)
    TextView tvSubject;
    @Bind(R.id.ll_Subject)
    LinearLayout llSubject;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.et_data)
    EditText etData;

    /**
     * 社团id
     */
    private int id;


    /**
     * 选择可选的社团Dialog
     */
    private ChangeShifteDialog dialog, dialog2;

    /**
     * 可选择的社团
     */
    private List<AllGroup> mData = new ArrayList<>();

    /**
     * 选择了第几个社团
     */
    private int grouo_num = -1;

    /**
     * 选择了第几个科目
     */
    private int subject_num = -1;
    /**
     * 可选择的科目
     */
    private List<AllGroup> mData2 = new ArrayList<>();

    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog,Dialog;


    @Override
    protected void aadListenter() {
        llGroup.setOnClickListener(this);
        llSubject.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //选择可以选择的社团
            case R.id.ll_group:
                getGroup();
                break;
            //选择可以加入的科目
            case R.id.ll_Subject:
                getSubject();
                break;
            //提交换班
            case R.id.tv_submit:
                submit();
                break;
            default:
                return;
        }
    }

    /**
     * 提交换班申请
     */
    private void submit() {
        if (grouo_num == -1) {
            CommonUtils.showToast("请选择社团！");
            return;
        } else if (subject_num == -1) {
            CommonUtils.showToast("请选择科目！");
            return;
        } else if (etData.getText().toString().length() == 0) {
            CommonUtils.showToast("请输入退课原因！");
            return;
        } else if(mData2.get(subject_num).getId()==id){
            CommonUtils.showToast("请选择不同科目");
            return;
        }else {

            Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog.setTitleText("正在提交申请...");
            Dialog.show();

            StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Team.CHANGE_CLASS, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            CommonUtils.showToast("申请已经提交,等待来时审核！");
                            Dialog.dismiss();
                            finish();
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
                    map.put("groupId", String.valueOf(mData.get(grouo_num).getId()));
                    map.put("subjectId", String.valueOf(mData2.get(subject_num).getId()));
                    map.put("reason", etData.getText().toString());
                    map.put("from", String.valueOf(id));
                    return map;
                }
            };
            setTmie(request);
            request.setTag("submit");
            MyApplication.getHttpQueue().add(request);


        }
    }

    /**
     * 获取可以加入的科目
     */
    private void getSubject() {
        if (grouo_num == -1) {
            CommonUtils.showToast("请先选择社团！");
        } else {
            pDialog.show();
            String url = UrlConfig.Team.ID_SUBJECT + "groupId=" + mData.get(grouo_num).getId();
            CommonUtils.tag(url);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            List<AllGroup> allGroups = JSON.parseArray(jsonObject.getString("data"), AllGroup.class);
                            mData2.remove(allGroups);
                            mData2.addAll(allGroups);
                            changeSubject();
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
                    CommonUtils.showToast("网络错误！");
                }
            });
            setTmie(request);
            //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
            request.setTag("getSubject");
            MyApplication.getHttpQueue().add(request);
        }
    }

    /**
     * 选择可以选择的科目
     */
    private void changeSubject() {
        if (dialog2 == null)
            dialog2 = new ChangeShifteDialog(this, mData2, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog2.GetText() >= 0) {
                        subject_num = dialog2.GetText();
                        tvSubject.setText(mData2.get(dialog2.GetText()).getName());
                    }
                    dialog2.dismiss();
                }
            });
        dialog2.show();
    }

    /**
     * 选择可以选择的社团
     */
    private void changeGroup() {
        if (dialog == null)
            dialog = new ChangeShifteDialog(this, mData, null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.GetText() >= 0) {
                        grouo_num = dialog.GetText();
                        tvGroup.setText(mData.get(dialog.GetText()).getName());
                        tvSubject.setText("");
                        subject_num=-1;
                    }
                    dialog.dismiss();
                }
            });
        dialog.show();

    }

    @Override
    protected void initVariables() {
        //原来科目的id
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);



    }

    /**
     * 获取可以选择的社团
     */
    private void getGroup() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在获取数据...");

        pDialog.show();
        String url = UrlConfig.Team.ID_GONFIG + "groupId=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        List<AllGroup> allGroups = JSON.parseArray(jsonObject.getString("data"), AllGroup.class);
                        mData.remove(allGroups);
                        mData.addAll(allGroups);
                        changeGroup();
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
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getGroup");
        MyApplication.getHttpQueue().add(request);


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_change_shifts);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getGroup");
        MyApplication.getHttpQueue().cancelAll("getSubject");
        MyApplication.getHttpQueue().cancelAll("submit");

    }
}
