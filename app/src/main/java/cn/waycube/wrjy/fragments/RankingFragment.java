package cn.waycube.wrjy.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.RankingAdapter;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Rank;
import cn.waycube.wrjy.utils.CommonUtils;

/**
 * Created by Administrator on 2016/4/12.
 */
public class RankingFragment extends BaseFragment implements AdapterListenter, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.lv_ranking)
    ListView lvRanking;
    @Bind(R.id.ll_no_join)
    LinearLayout llNoJoin;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout Refresh;


    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;

    private RankingAdapter mAdapter;
    private List<Rank> mData = new ArrayList<>();

    /**
     * 显示页数
     */
    private int page = 0;



    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }


    @Override
    protected void initVariables() {

        mAdapter = new RankingAdapter(getActivity(), mData);
        mAdapter.setOnLoadingMoreListener(this);
        lvRanking.setAdapter(mAdapter);
        Refresh.setColorSchemeResources(R.color.green_color);



        //获取社团排名
        getRanking(PULL_REFRESH, page);
    }

    /**
     * 获取社团排名
     */
    private void getRanking(final int type, int pages) {



        if (type == PULL_REFRESH) {
            Dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog.setTitleText("正在加载...");
            Refresh.setRefreshing(false);
            Dialog.show();
            mAdapter.isLoadingMore=false;
        } else {
            mAdapter.isLoadingMore = false;
        }
        String url = UrlConfig.Ranking.ID_RANKING + "sid=" + MyApplication.mUser.getId() + "&page=" + pages;
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
                        List<Rank> ranks = JSON.parseArray(jsonObject.getString("data"), Rank.class);
                        mData.removeAll(ranks);
                        mData.addAll(ranks);
                        mAdapter.notifyDataSetChanged();
                        lvRanking.setVisibility(View.VISIBLE);
                        llNoJoin.setVisibility(View.GONE);
                        break;
                    default:
                        if (type == PULL_REFRESH){
                            Dialog.dismiss();
                            lvRanking.setVisibility(View.GONE);
                            llNoJoin.setVisibility(View.VISIBLE);
                        }
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
    protected void addListener() {
        Refresh.setOnRefreshListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*
     * 加载更多
     */
    @Override
    public void adapterListenter() {
        page++;
        getRanking(LOADING_MORE, page);
        mAdapter.isLoadingMore = true;
    }

    /*
     * 下来刷新
     */
    @Override
    public void onRefresh() {
        mAdapter.isLoadingMore = false;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        page = 0;
        getRanking(PULL_REFRESH, page);
    }

}
