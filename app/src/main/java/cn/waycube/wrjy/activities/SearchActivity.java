package cn.waycube.wrjy.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.OrganizationAdapter;
import cn.waycube.wrjy.adapter.TeamAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.listenter.OnPopupListenter;
import cn.waycube.wrjy.model.Club;
import cn.waycube.wrjy.model.RecommendGroup;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.views.ClearEditText;
import cn.waycube.wrjy.views.PopupCatalogue;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SearchActivity extends BaseActivity implements OnPopupListenter, SwipeRefreshLayout.OnRefreshListener, AdapterListenter {


    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_popup_window)
    TextView tvPopupWindow;
    @Bind(R.id.et_findcircle_find)
    ClearEditText etFindcircleFind;
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.ll_recommend)
    LinearLayout llRecommend;
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.refresh)
    SwipeRefreshLayout Refresh;
    /**
     * 社团
     */
    private TeamAdapter mAdapter;
    private List<RecommendGroup> mData = new ArrayList<>();

    /**
     * 机构适配器
     */
    private OrganizationAdapter mAdapter2;
    private List<Club> mData2 = new ArrayList<>();

    /**
     * 默认收索社团列表
     */
    private int SeaechType = 3;

    /**
     * 加载第几页
     */
    private int page = 0;

    //初始化popupWindow
    private PopupCatalogue popupCatalogue;

    @Override
    protected void aadListenter() {

        Refresh.setOnRefreshListener(this);
        tvPopupWindow.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        mAdapter.setOnLoadingMoreListener(this);
        mAdapter2.setOnLoadingMoreListener(this);

        /*
         *用户点击收索的处理事件
         */
        etFindcircleFind.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //影藏软件盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    page = 0;
                    search(PULL_REFRESH, SeaechType, etFindcircleFind.getText().toString());
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //悬着机构，学校，社团按钮
            case R.id.tv_popup_window:
                popupCatalogue.showPopupWindow(this, llTitle);
                popupCatalogue.setOnPopupListenter(this);
                break;
            //点击取消关闭页面
            case R.id.tv_back:
                finish();
                break;

            default:
                return;
        }
    }

    @Override
    protected void initVariables() {
        //城市
        if (MyApplication.city != null)
            tvCity.setText(MyApplication.city);

        //下拉刷新颜色
        Refresh.setColorSchemeResources(R.color.green_color);

        //不自动弹出如阿键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        mAdapter = new TeamAdapter(this, mData);
        mAdapter2 = new OrganizationAdapter(this, mData2);


        //获取推存社团
        getTeam(PULL_REFRESH, SeaechType);


        //初始化选择社团，机构学校的popupWindow
        popupCatalogue = new PopupCatalogue(this);

    }

    /**
     * 获取推存的社团
     */
    private void getTeam(int refresh, final int type) {
        //显示推存
        llRecommend.setVisibility(View.VISIBLE);

        if (refresh == PULL_REFRESH) {
            mData.clear();
            mData2.clear();
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            Refresh.setRefreshing(false);
            mAdapter2.isLoadingMore = false;
            mAdapter.isLoadingMore = false;
        } else {
            mAdapter2.isLoadingMore = false;
            mAdapter.isLoadingMore = false;
        }


        String url = UrlConfig.Team.RECOMMEND_TEAM + "type=" + type + "&page=" + page;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    //获取数据成功
                    case 0:
                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                        //推荐的是社团
                        if (type == 3) {
                            List<RecommendGroup> recommendGroups = JSON.parseArray(jsonObject1.getString("list"), RecommendGroup.class);
                            mData.remove(recommendGroups);
                            mData.addAll(recommendGroups);
                            lvList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            //获取的是机构或者是学校
                            List<Club> clubs = JSON.parseArray(jsonObject1.getString("list"), Club.class);
                            mData2.remove(clubs);
                            mData2.addAll(clubs);
                            lvList.setAdapter(mAdapter2);
                            mAdapter2.notifyDataSetChanged();
                        }

                        break;
                    default:
                        return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
        setContentView(R.layout.activity_search);
    }

    /*
     *查看用户选择机构，学校还是社团
     * @param tag
     */
    @Override
    public void popupListener(int tag) {

        //学校
        if (tag == 0) {
            tvPopupWindow.setText("学校");
            page = 0;
            SeaechType = 1;
            etFindcircleFind.setText("");
            getTeam(PULL_REFRESH, SeaechType);
        }
        //社团
        if (tag == 1) {
            SeaechType = 3;
            page = 0;
            etFindcircleFind.setText("");
            tvPopupWindow.setText("社团");
            getTeam(PULL_REFRESH, SeaechType);
        }
        //机构
        if (tag == 2) {
            SeaechType = 2;
            page = 0;
            etFindcircleFind.setText("");
            tvPopupWindow.setText("机构");
            getTeam(PULL_REFRESH, SeaechType);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCodes");
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

    /**
     * 收索
     */
    private void search(int refresh, final int type, String name) {

        //影藏推存
        llRecommend.setVisibility(View.GONE);
        mAdapter.isLoadingMore = false;
        mAdapter2.isLoadingMore = false;

        if (refresh == PULL_REFRESH) {
            mData.clear();
            mData2.clear();
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            Refresh.setRefreshing(false);
        }

        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urls = UrlConfig.Team.RECOMMEND_TEAM + "type=" + type + "&name=" + name + "&page=" + page;
        StringRequest request = new StringRequest(Request.Method.GET, urls, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        //社团
                        if (SeaechType == 3) {
                            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                            List<RecommendGroup> recommendGroups = JSON.parseArray(jsonObject1.getString("list"), RecommendGroup.class);
                            if (page == 0)
                                mData.clear();
                            mData.remove(recommendGroups);
                            mData.addAll(recommendGroups);
                            lvList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                            //学校和机构
                            List<Club> clubs = JSON.parseArray(jsonObject1.getString("list"), Club.class);
                            if (page == 0)
                                mData2.clear();
                            mData2.remove(clubs);
                            mData2.addAll(clubs);
                            lvList.setAdapter(mAdapter2);
                            mAdapter2.notifyDataSetChanged();
                        }
                        break;

                    default:
                        //搜索出错
                        mData.clear();
                        mData2.clear();
                        mAdapter.notifyDataSetChanged();
                        mAdapter2.notifyDataSetChanged();
                        CommonUtils.showToast("没有收索当相关内容！");
                        return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("网络错误！");
            }
        });
        setTmie(request);
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCodes");
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    public void onRefresh() {

        //表示获取推荐
        if (etFindcircleFind.getText().length() == 0) {
            mAdapter.isLoadingMore = false;
            mAdapter2.isLoadingMore = false;
            mData.clear();
            mData2.clear();
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            page = 0;
            getTeam(PULL_REFRESH, SeaechType);
        } else {
            //表示获取搜索
            mAdapter.isLoadingMore = false;
            mAdapter2.isLoadingMore = false;
            mData.clear();
            mData2.clear();
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            page = 0;
            search(PULL_REFRESH, SeaechType, etFindcircleFind.getText().toString());
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void adapterListenter() {
        page++;
        //表示获取推荐
        if (etFindcircleFind.getText().length() == 0) {
            getTeam(LOADING_MORE, SeaechType);
            mAdapter.isLoadingMore = true;
            mAdapter2.isLoadingMore = true;
        } else {
            //表示获取搜索

            search(LOADING_MORE, SeaechType, etFindcircleFind.getText().toString());
            mAdapter.isLoadingMore = true;
            mAdapter2.isLoadingMore = true;
        }
    }

}
