package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import cn.waycube.wrjy.adapter.ReportRankingAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.ReportRanking;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.views.NoScrollListView;
import cn.waycube.wrjy.views.RoundProgressBar;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ReportRankingActivity extends BaseActivity {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.iv_my_avatar)
    RoundedImageView ivMyAvatar;
    @Bind(R.id.tv_my_name)
    TextView tvMyName;
    @Bind(R.id.tv_my_order)
    TextView tvMyOrder;
    @Bind(R.id.roundProgressBar3)
    RoundProgressBar roundProgressBar3;
    @Bind(R.id.lv_ranking)
    NoScrollListView lvRanking;
    @Bind(R.id.ll_my_grade)
    LinearLayout llMyGrade;
    /**
     * 加载对话框
     */
    private SweetAlertDialog Dialog;

    /**
     * 课程id
     */
    private int id;


    /**
     * 数据
     */
    private JSONObject data;

    private ReportRankingAdapter mAdapter;

    private List<ReportRanking> mData = new ArrayList<>();

    @Override
    protected void aadListenter() {
        llMyGrade.setOnClickListener(this);
        lvRanking.setFocusable(false);
    }

    @Override
    protected void initVariables() {

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);


        mAdapter = new ReportRankingAdapter(this, mData);
        lvRanking.setAdapter(mAdapter);

        getRanking();
    }

    /**
     * 获取成绩排名
     */
    private void getRanking() {
        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");

        Dialog.show();

        String url = UrlConfig.Course.ID_SCORE + "courseId=" + id + "&studentId=" + MyApplication.mUser.getId();
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
                        CommonUtils.tag(data.toJSONString());
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
     * 配置数据
     */
    private void initData() {

        //适配数据
        if (!data.getString("course").equals("")) {
            //标题栏
            JSONObject jsonObject = (JSONObject) data.get("course");
            tvName.setText(jsonObject.getString("name"));
        }

        if (data.getJSONArray("my").size()>0) {

            JSONObject jsonObject = (JSONObject) data.getJSONArray("my").get(0);
            CommonUtils.tag(jsonObject.toJSONString());

            //设置头像
            ImageLoader.getInstance().displayImage(jsonObject.getString("head"), ivAvatar);
            ImageLoader.getInstance().displayImage(jsonObject.getString("head"), ivMyAvatar);
            //名次
            tvOrder.setText("第  " + jsonObject.getString("order") + "  名");
            tvMyOrder.setText("第  " + jsonObject.getString("order") + "  名");
            //成绩
            tvScore.setText(jsonObject.getString("score") + "分");

            tvMyName.setText(MyApplication.mUser.getName());

            roundProgressBar3.setMax(100);
            roundProgressBar3.setProgress((int) (jsonObject.getFloat("score") * 10));


        }

        if (!data.getString("rank").equals("")) {
            List<ReportRanking> reportRankings = JSON.parseArray(data.getString("rank"), ReportRanking.class);
            mData.remove(reportRankings);
            mData.addAll(reportRankings);
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开详情
            case R.id.ll_my_grade:
                Intent intent1 = new Intent(this, GradeActivity.class);
                intent1.putExtra(KeyUtil.KEY_ONE, id);
                startActivity(intent1);
                break;
            default:
                return;

        }

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_report_ranking);

    }

    @Override
    public void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

}
