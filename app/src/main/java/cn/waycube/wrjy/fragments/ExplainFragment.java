package cn.waycube.wrjy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseFragment;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ExplainFragment extends BaseFragment {


    @Bind(R.id.tv_fit_crowd)
    TextView tvFitCrowd;
    @Bind(R.id.tv_goal)
    TextView tvGoal;
    @Bind(R.id.tv_quit_rule)
    TextView tvQuitRule;
    @Bind(R.id.tv_join_rule)
    TextView tvJoinRule;

    //要适配的数据
    private String data;

    public static ExplainFragment  getString(String data) {
        ExplainFragment instance = new ExplainFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        instance.setArguments(args);
        return instance;

    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explain, container, false);
    }

    @Override
    protected void initVariables() {

        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }
        //适配数据
        //适学人群
        JSONObject jsonObject= JSON.parseObject(data);
        if (!jsonObject.getString("fit_crowd").equals(""))
            tvFitCrowd.setText(jsonObject.getString("fit_crowd"));
        //教学目标
        if (!jsonObject.getString("goal").equals(""))
            tvGoal.setText(jsonObject.getString("goal"));
        //退班规则
        if (!jsonObject.getString("quit_rule").equals(""))
            tvQuitRule.setText(jsonObject.getString("quit_rule"));
        //插班规则
        if (!jsonObject.getString("join_rule").equals(""))
            tvJoinRule.setText(jsonObject.getString("join_rule"));

    }

    @Override
    protected void addListener() {

    }

}
