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
import cn.waycube.wrjy.adapter.NewsAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Message;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.ShareReferenceUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class NewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterListenter {
    @Bind(R.id.lv_news)
    ListView lvNews;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout Refresh;

    /**
     * 加载框
     */
    private SweetAlertDialog Dialog;


    private NewsAdapter mAdapter;
    private List<Message> mData = new ArrayList<>();

    private int page = 0;

    @Override
    protected void aadListenter() {
        Refresh.setOnRefreshListener(this);
        mAdapter.setOnLoadingMoreListener(this);
    }

    @Override
    protected void initVariables() {
        mAdapter = new NewsAdapter(this, mData);
        lvNews.setAdapter(mAdapter);

        Refresh.setColorSchemeResources(R.color.green_color);
        //获取消息
        getNews(PULL_REFRESH, page);
    }

    /**
     * 获取消息
     */
    private void getNews(final int type, int pages) {

        if (ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID) != null && !ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID).equals("")) {


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
            String url = UrlConfig.Set.NEWS + "studentId=" + ShareReferenceUtils.getValue(KeyUtil.KEY_USER_ID) + "&page=" + pages;
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
                            List<Message> messages = JSON.parseArray(jsonObject.getString("data"), Message.class);
                            mData.remove(messages);
                            mData.addAll(messages);
                            mAdapter.notifyDataSetChanged();

                            //用户已经查看消息，把新消息数量改为0
                            MyApplication.mUser.setNotice_unread_num(0);

                            break;
                        default:
                            if (type == PULL_REFRESH)
                                Dialog.dismiss();
                            CommonUtils.showToast(jsonObject.getString("msg"));
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
        } else {
            CommonUtils.showToast("请先登录！");

        }
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_news);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mAdapter.isLoadingMore = false;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        page = 0;
        getNews(PULL_REFRESH, page);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void adapterListenter() {
        page++;
        getNews(LOADING_MORE, page);
        mAdapter.isLoadingMore = true;
    }
}
