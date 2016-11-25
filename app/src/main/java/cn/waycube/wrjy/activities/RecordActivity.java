package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

/**
 * Created by Administrator on 2016/5/10.
 */
public class RecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AdapterListenter{
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout Refresh;

    private RecorrdAdapter mAdapter;
    private List<Recorrd> mData = new ArrayList<>();

    private int page = 0;

    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;


    @Override
    protected void aadListenter() {
        Refresh.setOnRefreshListener(this);
        mAdapter.setOnLoadingMoreListener(this);
    }

    @Override
    protected void initVariables() {

        mAdapter = new RecorrdAdapter(this, mData);
        lvList.setAdapter(mAdapter);
        Refresh.setColorSchemeResources(R.color.green_color);


        getData(PULL_REFRESH,page);

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
            mAdapter.isLoadingMore=false;
        } else {
            mAdapter.isLoadingMore = false;
        }


        String url = UrlConfig.Course.ID_RECORD + "id=" + MyApplication.mUser.getId()+"&page="+pages;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        if (type == PULL_REFRESH)
                            Dialog.dismiss();

                        List<Recorrd> recorrds = JSON.parseArray(jsonObject.getString("data"), Recorrd.class);
                        mData.remove(recorrds);
                        mData.addAll(recorrds);
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        if (type == PULL_REFRESH)
                            Dialog.dismiss();

                        if (type == LOADING_MORE)
                            CommonUtils.showToast("没有可加载的数据了。");
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
        setContentView(R.layout.activity_record);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

    /**
     * 下来刷新
     */
    @Override
    public void onRefresh() {
        mAdapter.isLoadingMore = false;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        page = 0;
        getData(PULL_REFRESH, page);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void adapterListenter() {
        page++;
        getData(LOADING_MORE, page);
        mAdapter.isLoadingMore = true;
    }
}
