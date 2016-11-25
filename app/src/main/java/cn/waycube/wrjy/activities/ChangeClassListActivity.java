package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.ClassListAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ChangeClassListActivity extends BaseActivity {
    @Bind(R.id.lv_list)
    ListView lvList;

    /**
     * 社团的id
     */
    private int id;

    private ClassListAdapter mAdapter;
    private List<ClassListe> mData = new ArrayList<>();


    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);


        mAdapter=new ClassListAdapter(this,mData,id);
        lvList.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取在该社团学生加入的科目
        getData();
    }

    private void getData() {

        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在查询课程...");


        Dialog.show();
        mData.clear();
        mAdapter.notifyDataSetChanged();
        String url = UrlConfig.Course.ID_SUBJECT + "groupId=" + id + "&sid=" + MyApplication.mUser.getId();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        List<ClassListe> classListes=JSON.parseArray(jsonObject.getString("data"),ClassListe.class);
                        mData.remove(classListes);
                        mData.addAll(classListes);
                        mAdapter.notifyDataSetChanged();
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
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCode");
        MyApplication.getHttpQueue().add(request);

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_change_class_list);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }
}
