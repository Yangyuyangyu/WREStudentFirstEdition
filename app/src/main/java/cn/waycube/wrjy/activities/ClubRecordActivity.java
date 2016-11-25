package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.RecorrdAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Recorrd;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ClubRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterListenter {
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout Refresh;
    @Bind(R.id.tv_details)
    TextView tvDetails;

    private RecorrdAdapter mAdapter;
    private List<Recorrd> mData = new ArrayList<>();

    private int page = 0;

    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;


    private int id;

    @Override
    protected void aadListenter() {
        Refresh.setOnRefreshListener(this);
        mAdapter.setOnLoadingMoreListener(this);
        tvDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_details:
                Intent intent = new Intent(this, AssociationDetailsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent);
                break;
            default:
                return;


        }

    }

    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);

        mAdapter = new RecorrdAdapter(this, mData);
        lvList.setAdapter(mAdapter);
        Refresh.setColorSchemeResources(R.color.green_color);


        getData(PULL_REFRESH, page);
    }

    /**
     * 获取历史课程
     */
    private void getData(final int type, int pages) {

        if (type == PULL_REFRESH) {
            Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog.setTitleText("正在加载...");
            Refresh.setRefreshing(false);
            Dialog.show();
            mAdapter.isLoadingMore = false;
        } else {
            mAdapter.isLoadingMore = false;
        }
        String url = null;
        if (id == 0) {
            url = UrlConfig.Course.ID_RECORD + "id=" + MyApplication.mUser.getId();
        } else {
            url = UrlConfig.Course.ID_CLUB_RECORD + "id=" + MyApplication.mUser.getId() + "&groupId=" + id + "&page=" + pages;

        }


        if (url == null) {
            return;
        }
        Log.e("获取历史上课记录url", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("获取历史上课记录------", s);
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.tag(jsonObject.toJSONString());
                        List<Recorrd> recorrds = JSON.parseArray(jsonObject.getString("data"), Recorrd.class);
                        mData.removeAll(recorrds);
                        mData.addAll(recorrds);
                        if (type == PULL_REFRESH) {
                            Dialog.dismiss();
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        if (type == PULL_REFRESH) {
                            Dialog.dismiss();
                            CommonUtils.showToast("没有上课记录！");
                        }

                        if (type == LOADING_MORE)
                            CommonUtils.showToast("没有可加载的数据了！");
                        return;
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == PULL_REFRESH)
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
        setContentView(R.layout.activity_club_record);
    }


    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

    @Override
    public void adapterListenter() {
        page++;
        getData(LOADING_MORE, page);
        mAdapter.isLoadingMore = true;
    }

    @Override
    public void onRefresh() {
        mAdapter.isLoadingMore = false;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        page = 0;
        getData(PULL_REFRESH, page);
    }

}
