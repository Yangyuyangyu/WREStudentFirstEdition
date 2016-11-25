package cn.waycube.wrjy.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ProjectActivity extends BaseActivity {


    @Bind(R.id.wb_data)
    WebView wbData;
    /**
     * 社团id
     */
    private int id;

    private JSONObject data;


    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);
        getData();
    }

    /**
     * 获取社团建设
     */
    private void getData() {
        String url = UrlConfig.Team.ID_PLAN + "groupId=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        data = (JSONObject) jsonObject.get("data");
                        initData();
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

    /**
     * 配置课程规划
     */
    private void initData() {
        //内容
        if (data.getString("plan") != null) {
            String content = data.getString("plan");
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
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_project);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }
}
