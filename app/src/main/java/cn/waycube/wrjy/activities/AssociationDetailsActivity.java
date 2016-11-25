package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import cn.waycube.wrjy.adapter.DynamicAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.dialog.SubjectAdapter;
import cn.waycube.wrjy.fragments.TeamFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.model.News;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.NoScrollListView;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AssociationDetailsActivity extends BaseActivity {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_brief)
    TextView tvBrief;
    @Bind(R.id.tv_agency_name)
    TextView tvAgencyName;
    @Bind(R.id.tv_admins)
    TextView tvAdmins;
    @Bind(R.id.lv_dynamic)
    NoScrollListView lvDynamic;
    @Bind(R.id.ll_project)
    LinearLayout llProject;
    @Bind(R.id.ll_construction)
    LinearLayout llConstruction;
    @Bind(R.id.ll_systen)
    LinearLayout llSysten;
    @Bind(R.id.tv_cause)
    TextView tvCause;
    @Bind(R.id.tv_add_change)
    TextView tvAddChange;
    @Bind(R.id.tv_no_add)
    TextView tvNoAdd;
    /**
     * 适配器
     */
    private DynamicAdapter mAdapter;
    private List<News> mData = new ArrayList<>();
    /**
     * 传递过来社团的id
     */
    private int id;
    /**
     * 社团详情
     */
    private JSONObject Details;

    /**
     * 加载对话框
     */
    private SweetAlertDialog affirmDialog, succeedDialog, pDialog, Dialog, Dialog2;
    /**
     * 判断在该社团的状态
     */
    private int type;
    /**
     * 选择可选的社团Dialog
     */
    private SubjectAdapter dialog;


    @Override
    protected void aadListenter() {
        llSysten.setOnClickListener(this);
        llConstruction.setOnClickListener(this);
        llProject.setOnClickListener(this);
        tvAddChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开社团管理制度
            case R.id.ll_systen:
                Intent intent = new Intent(this, SystenActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent);
                break;
            //打开社团建设
            case R.id.ll_construction:
                Intent intent2 = new Intent(this, ConstructionActivity.class);
                intent2.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent2);
                break;
            //打开课程规划
            case R.id.ll_project:
                Intent intent1 = new Intent(this, ProjectActivity.class);
                intent1.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent1);
                break;
            //添加和换班
            case R.id.tv_add_change:
                addChange();
                break;
            default:
                return;

        }
    }

    /**
     * 所选课程id
     */
    private int subject;
    private List<ClassListe> classListes;

    /**
     * 处理是换班还是添加社团
     */
    private void addChange() {
        if (type == -1 || type == 2) {

            Dialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            Dialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog2.setTitleText("正在获取课程...");

            Dialog2.show();
            String url = UrlConfig.Course.ID_SUBJECTS + "groupId=" + id;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            Dialog2.dismiss();
                            classListes = JSON.parseArray(jsonObject.getString("data"), ClassListe.class);
                            setSubject();

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
            request.setTag("getSubjects");
            MyApplication.getHttpQueue().add(request);


        } else if (type == 0) {
            CommonUtils.showToast("你已经申请加入该社团，请等待！！！");
        } else if (type == 1)

        {
            Intent intent = new Intent(this, ChangeClassListActivity.class);
            intent.putExtra(KeyUtil.KEY_ONE, id);
            startActivity(intent);
        }


    }

    /**
     * 设置课程
     */
    private void setSubject() {
        dialog = new SubjectAdapter(this, classListes, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.GetText() >= 0) {
                    subject = (classListes.get(dialog.GetText()).getId());
                    setSubjects();
                }
                dialog.dismiss();
            }


        });
        dialog.show();
    }

    private void setSubjects() {

        affirmDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("温馨提示！")
                .setContentText("是否加入" + Details.getString("name") + "？")
                .setConfirmText("是")
                .setCancelText("否")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        affirmDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //添加社团
                        add();
                    }
                });
        affirmDialog.show();
    }


    /**
     * 添加社团
     */
    private void add() {

        affirmDialog.dismiss();
        //加载对话框
        if (pDialog == null) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("正在提交申请...");
        }
        pDialog.show();
        //加入成功
        if (succeedDialog == null) {
            succeedDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("申请已!")
                    .setContentText("加入" + Details.getString("name") + "申请提交成功！");
        }

        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Team.ADD_GROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        pDialog.dismiss();
                        succeedDialog.show();
                        //修改本地状态
                        type = 0;
                        tvAddChange.setText("正在审核中");
                        TeamFragment.isRefresh = true;
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
                map.put("groupId", String.valueOf(id));
                map.put("studentId", String.valueOf(MyApplication.mUser.getId()));
                map.put("subjectId", String.valueOf(subject));
                return map;
            }
        };
        setTmie(request);
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }


    @Override
    protected void initVariables() {
        //社团id
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 1);

        //社团动态
        mAdapter = new DynamicAdapter(this, mData);
        lvDynamic.setAdapter(mAdapter);


        //根据id获取社团详情
        gteAssociationDetails();
    }

    /**
     * 根据社团id获取社团详情
     */
    private void gteAssociationDetails() {

        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");

        Dialog.show();
        String url = UrlConfig.Team.ID_TEAM + "groupId=" + id + "&studentId=" + MyApplication.mUser.getId();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("获取社团详情-----",s);
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        Details = (JSONObject) jsonObject.get("data");
                        /*
                         *初始化社团详情
                         */
                        initDetails();
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

    /**
     * 初始化社团详情
     */
    private void initDetails() {
        //名称
        if (!Details.getString("name").equals(""))
            tvName.setText(Details.getString("name"));
        //管理员
        if (!Details.getString("admins").equals(""))
            tvAdmins.setText(Details.getString("admins"));
        //设置简介
        if (!Details.getString("brief").equals(""))
            tvBrief.setText(Details.getString("brief"));
        //设置所属机构
        if (!Details.getString("agency_name").equals(""))
            tvAgencyName.setText(Details.getString("agency_name"));
        //设置动态
        if (!Details.getString("news").equals("")) {
            List<News> newses = JSON.parseArray(Details.getString("news"), News.class);
            mData.addAll(newses);
            mAdapter.notifyDataSetChanged();
            if (mData.size()<=0)
                tvNoAdd.setVisibility(View.VISIBLE);
            else
                tvNoAdd.setVisibility(View.GONE);
        }
        //设置是否已经加入社团了
        if (!Details.getInteger("joined").equals("")) {
            type = Details.getInteger("joined");
            if (type == -1) {
                //没有加入社团
                tvAddChange.setText("加入社团");
            } else if (type == 0) {
                //正在审核中
                tvAddChange.setText("正在审核中");
            } else if (type == 1) {
                //通过了可以换班
                tvAddChange.setText("换班");
            } else if (type == 2) {
                //被拒绝了还可以在申请
                tvCause.setText(Details.getString("refuse"));
                tvAddChange.setText("再次加入社团");
            }


        }


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_association_details);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");
        MyApplication.getHttpQueue().cancelAll("add");
        MyApplication.getHttpQueue().cancelAll("getSubjects");

    }

}
