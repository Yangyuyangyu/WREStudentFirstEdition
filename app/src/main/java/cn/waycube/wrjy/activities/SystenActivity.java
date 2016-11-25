package cn.waycube.wrjy.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

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
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class SystenActivity extends BaseActivity {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_time)
    TextView tvTime;
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
     * 获取社团管理制度
     */
    private void getData() {
        String url = UrlConfig.Team.ID_RULE + "groupId=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        data= (JSONObject) jsonObject.get("data");
                        initData();

                        break;
                    default:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("网络错误！");
            }
        });
        //设置取消取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("getCode");
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 配置数据
     */
    private void initData() {
        //标题栏
        if (!data.getString("title").equals(""))
            tvName.setText(data.getString("title"));
        //时间
        if (!data.getString("time").equals(""))
            tvTime.setText(data.getString("time"));
        //内容
        if (!data.getString("detail").equals("")){
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
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_system);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

}
