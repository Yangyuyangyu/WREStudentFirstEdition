package cn.waycube.wrjy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.TeamAdapter;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.model.RecommendGroup;

/**
 * Created by Administrator on 2016/4/18.
 */
public class AssociationFragment extends BaseFragment {


    @Bind(R.id.lv_list)
    ListView lvList;
    private TeamAdapter mAdapter;
    private List<RecommendGroup> mData = new ArrayList<>();

    private String data;


    public static  AssociationFragment  getString(String data) {
        AssociationFragment instance = new AssociationFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_association, container, false);
    }

    @Override
    protected void initVariables() {

        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }

        JSONObject jsonObject=JSON.parseObject(data);
        List<RecommendGroup> recommendGroup = JSON.parseArray(jsonObject.getString("groups"), RecommendGroup.class);
        mData.remove(recommendGroup);
        mData.addAll(recommendGroup);
        mAdapter = new TeamAdapter(getActivity(), mData);
        lvList.setAdapter(mAdapter);
    }

    @Override
    protected void addListener() {

    }

}
