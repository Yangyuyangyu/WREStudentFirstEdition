package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.AssociationRankingAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.GroupRank;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.NoScrollListView;

/**
 * Created by Administrator on 2016/4/13.
 */
public class AssociationRankingActivity extends BaseActivity {


    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.lv_ranking)
    NoScrollListView lvRanking;
    private AssociationRankingAdapter mAdapter;
    private List<GroupRank> mData = new ArrayList<>();


    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;

    /**
     * 社团的id
     */
    private int id;

    /**
     * 数据
     */
    private JSONObject data;


    @Override
    protected void aadListenter() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {


            default:
                return;
        }

    }

    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);


        mAdapter = new AssociationRankingAdapter(this, mData);
        lvRanking.setAdapter(mAdapter);
        lvRanking.setFocusable(false);

        /**
         * 获取社团排名
         */
        getRanking();
    }

    /**
     * 根据社团id获取社团排名
     */
    private void getRanking() {

        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");

        Dialog.show();

        String url = UrlConfig.Ranking.GROUP_RANK + "groupId=" + id + "&studentId=" + MyApplication.mUser.getId();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();

                        data = (JSONObject) jsonObject.get("data");
                        initData();
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
     * 适配数据
     */
    private void initData() {
        //头像
        if (!data.getString("mine").equals("")) {
            JSONObject jsonObject = (JSONObject) data.get("mine");
            //头像
            ImageLoader.getInstance().displayImage(jsonObject.getString("head"), ivAvatar);
            //排名
            tvOrder.setText("第  " + jsonObject.getString("order") + "  名");
            //成绩
            tvScore.setText(jsonObject.getString("score") + "分");
            //标题
            tvName.setText(jsonObject.getString("group_name"));
        }

        List<GroupRank> groupRanks = JSON.parseArray(data.getString("list"), GroupRank.class);
        CommonUtils.tag(data.toJSONString());
        mData.remove(groupRanks);
        mData.addAll(groupRanks);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_association_ranking);
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }
}
