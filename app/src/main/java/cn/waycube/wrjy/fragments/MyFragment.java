package cn.waycube.wrjy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.InviteActivity;
import cn.waycube.wrjy.activities.NewsActivity;
import cn.waycube.wrjy.activities.PersonalDetailsActivity;
import cn.waycube.wrjy.activities.RecordActivity;
import cn.waycube.wrjy.activities.SetActivity;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.global.MyApplication;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyFragment extends BaseFragment {
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_tel)
    TextView tvTel;
    @Bind(R.id.ll_set)
    LinearLayout llSet;
    @Bind(R.id.ll_record)
    LinearLayout llRecord;
    @Bind(R.id.ll_news)
    LinearLayout llNews;
    @Bind(R.id.ll_invite)
    LinearLayout llInvite;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.mipmap.my_bg);//通知栏所需颜色


        return inflater.inflate(R.layout.fragmnet_my, container, false);
    }

    @Override
    protected void initVariables() {


    }

    @Override
    public void onResume() {
        super.onResume();
//        //设置新消息数量
//        if (MyApplication.mUser.getNotice_unread_num() > -1)
//            tvNewsQuantity.setText(MyApplication.mUser.getNotice_unread_num() + "");
        //设置电话号码
        if (MyApplication.mUser.getPhone() != null)
            tvTel.setText("电话： " + MyApplication.mUser.getPhone());
        //设置名字
        if (MyApplication.mUser.getName() != null)
            tvName.setText(MyApplication.mUser.getName());
        //设置头像
        if (!MyApplication.mUser.getBirthday().equals(""))
            ImageLoader.getInstance().displayImage(MyApplication.mUser.getHead(), ivAvatar);
        else
            ivAvatar.setImageResource(R.mipmap.no_loading);
    }

    @Override
    protected void addListener() {
        llInvite.setOnClickListener(this);
        llNews.setOnClickListener(this);
        llSet.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        llRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开个人信息
            case R.id.iv_avatar:
                startActivity(new Intent(getActivity(), PersonalDetailsActivity.class));
                break;
            //打开邀请
            case R.id.ll_invite:
                startActivity(new Intent(getActivity(), InviteActivity.class));
                break;
            //打开设置
            case R.id.ll_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            //打开消息界面
            case R.id.ll_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            //打开上课历史
            case R.id.ll_record:
                startActivity(new Intent(getActivity(), RecordActivity.class));
                break;

            default:
                return;
        }
    }

}
