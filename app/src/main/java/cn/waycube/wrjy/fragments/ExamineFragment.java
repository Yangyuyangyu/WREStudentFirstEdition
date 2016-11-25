package cn.waycube.wrjy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseFragment;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ExamineFragment extends BaseFragment {


    @Bind(R.id.tv_detail)
    WebView tvDetail;
    //要适配的数据

    private  String data;

    public static ExamineFragment getString  (String data) {
        ExamineFragment instance = new ExamineFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_examine, container, false);
    }

    @Override
    protected void initVariables() {

        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }

        //适配数据
        JSONObject jsonObject= JSON.parseObject(data);
        if (!jsonObject.getString("detail").equals("")) {
            String content = jsonObject.getString("detail");
            Document doc_Dis = Jsoup.parse(content);
            Elements ele_Img = doc_Dis.getElementsByTag("img");
            if (ele_Img.size() != 0) {
                for (Element e_Img : ele_Img) {
                    e_Img.attr("style", "width:100%");
                }
            }
            String newHtmlContent = doc_Dis.toString();
            tvDetail.loadDataWithBaseURL(null, newHtmlContent, "text/html", "utf-8", null);

        }

    }

    @Override
    protected void addListener() {

    }

}
