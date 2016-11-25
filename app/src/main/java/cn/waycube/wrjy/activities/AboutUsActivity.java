package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.Utils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.tv_version_code)
    TextView tvVersionCode;
    @Bind(R.id.wb_data)
    WebView wbData;


    /**
     * 加入社团对话框
     */
    private SweetAlertDialog Dialog;

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        tvVersionCode.setText("版本：" + Utils.getAPPVersionCodeFromAPP(this));
        //获取关于我们
        getAboutUs();
    }

    /**
     * 获取关于我们的消息
     */
    private void getAboutUs() {
        Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        Dialog.setTitleText("正在加载...");

        Dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, UrlConfig.Set.ABOUT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Dialog.dismiss();
                        JSONObject jsonObject1= (JSONObject) jsonObject.get("data");
                        CommonUtils.tag(jsonObject1.toJSONString());


                        if(!jsonObject1.getString("detail").equals("")){
                            String content = (String) jsonObject1.get("detail");
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
        setContentView(R.layout.activity_about_us);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("getCode");

    }

}
