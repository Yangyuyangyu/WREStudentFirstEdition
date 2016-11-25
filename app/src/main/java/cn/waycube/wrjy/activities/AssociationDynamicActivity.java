package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AssociationDynamicActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_title1)
    TextView tvTitle1;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.wb_data)
    WebView wbData;
    /**
     * 社团动态id
     */
    private int id;

    private JSONObject data;


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



        getData();
    }


    /**
     * 获取社团动态详情
     */
    private void getData() {
        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");

        Dialog.show();
        String url = UrlConfig.Team.ID_NEWS_DETAIL + "newsId=" + id;
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

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_association_dynamic);
    }

    /**
     * 配置课程规划
     */
    private void initData() {

        //所属社团
        if (!data.getString("group_name").equals("")) {
            tvTitle.setText(data.getString("group_name"));
            tvTitle1.setText(data.getString("group_name"));
        }
        //时间
        if (!data.getString("time") .equals(""))
            tvTime.setText(data.getString("time"));
        //标题
        if (!data.getString("name").equals(""))
            tvName.setText(data.getString("name"));
        //设置显示的图片
        if (!data.getString("img").equals(""))
            ImageLoader.getInstance().displayImage(data.getString("img"), ivAvatar);

        //内容
        if (!data.getString("detail") .equals("")) {
            String content = data.getString("detail");
            Document doc_Dis = Jsoup.parse(content);
            Elements ele_Img = doc_Dis.getElementsByTag("img");
            if (ele_Img.size() != 0) {
                for (Element e_Img : ele_Img) {
                    e_Img.attr("style", "width:100%");
                }
            }
            String newHtmlContent = doc_Dis.toString();
            wbData.loadDataWithBaseURL(null, newHtmlContent, "text/html", "utf-8", null);


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

}
