package cn.waycube.wrjy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.EvaluateAdapter;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.model.Comment;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.views.NoScrollListView;

/**
 * Created by Administrator on 2016/4/15.
 */
public class EvaluateFragment extends BaseFragment {

    @Bind(R.id.lv_list)
    NoScrollListView lvList;


    private EvaluateAdapter mAdapter;
    private List<Comment> mData = new ArrayList<>();


    //要适配的数据
    private  String data;

    public static EvaluateFragment getString(String data) {
        EvaluateFragment instance = new EvaluateFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        instance.setArguments(args);
        return instance;

    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evaluate, container, false);
    }

    @Override
    protected void initVariables() {

        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }

        mAdapter = new EvaluateAdapter(getActivity(), mData);
        lvList.setAdapter(mAdapter);
        JSONObject jsonObject=JSON.parseObject(data);
        if (!jsonObject.getString("commentList").equals("")) {
            CommonUtils.tag(jsonObject.getString("commentList"));
            List<Comment> comments = JSON.parseArray(jsonObject.getString("commentList"), Comment.class);
            mData.removeAll(comments);
            mData.addAll(comments);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void addListener() {


    }

}
