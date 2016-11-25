package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.fragments.CourseFragment;
import cn.waycube.wrjy.fragments.TeamFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.DataCleanManager;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.ShareReferenceUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class SetActivity extends BaseActivity {
    @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @Bind(R.id.ll_feedback)
    LinearLayout llFeedback;
    @Bind(R.id.ll_change_password)
    LinearLayout llChangePassword;
    @Bind(R.id.ll_dial)
    LinearLayout llDial;
    @Bind(R.id.ll_clear)
    LinearLayout llClear;
    @Bind(R.id.ll_quit)
    LinearLayout llQuit;
    @Bind(R.id.tv_size)
    TextView tvSize;

    /**
     * 提示用户是否退出登录
     */
    private SweetAlertDialog dialog;


    //图片缓存路径
    private static String path = "/sdcard/WR_student/image/";
    private static File file;

    @Override
    protected void aadListenter() {
        llAboutUs.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);
        llDial.setOnClickListener(this);
        llClear.setOnClickListener(this);
        llQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开关于我们
            case R.id.ll_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            //打开意见反馈
            case R.id.ll_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            //打开修改密码
            case R.id.ll_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            //拨号
            case R.id.ll_dial:
                Dial();
                break;
            //清理缓存
            case R.id.ll_clear:
                Clear();
                break;
            //退出登录
            case R.id.ll_quit:
                Quit();
                break;
            default:
                return;
        }
    }

    /**
     * 退出登录
     */
    private void Quit() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示！")
                .setContentText("是否退出登录")
                .setCancelText("否")
                .setConfirmText("是")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //关闭所有fragment
                        MyApplication.getInstance().exit();
                        //删除推送标签
                        List<String> tag = new ArrayList<>();
                        tag.add(String.valueOf(MyApplication.mUser.getId()));
                        PushManager.delTags(getApplicationContext(), tag);
                        //进入主页要刷新
                        TeamFragment.isRefresh=true;
                        CourseFragment.isRefresh=true;

                        //删除用户id
                        ShareReferenceUtils.putValue(KeyUtil.KEY_USER_ID,"");
                        MyApplication.mUser=null;

                        //设置课表
                        CourseFragment.date="8";

                        CommonUtils.showToast("退出成功!");
                        startActivity(new Intent(SetActivity.this, LoginActivity.class));
                    }
                });
        dialog.show();
    }

    /**
     * 清理缓存
     */
    private void Clear() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText("是否清理缓存?")
                .setConfirmText("确认")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance

                        DataCleanManager.deleteFolderFile(path, true);
                        tvSize.setText(String.valueOf(DataCleanManager.getFormatSizes(DataCleanManager.getFolderSize(file))));

                        sDialog.setTitleText("清理成功!")
                                .setContentText("没有缓存了！")
                                .setConfirmText("关闭")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();

    }

    /**
     * 进行联系客服的拨号处理
     */
    private void Dial() {
        Intent intent = new Intent(Intent.ACTION_DIAL);


        Uri data = Uri.parse("tel:" + MyApplication.mUser.getCustomer_service());

        intent.setData(data);

        startActivity(intent);
    }

    @Override
    protected void initVariables() {
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        file = new File(path);
        tvSize.setText(String.valueOf(DataCleanManager.getFormatSizes(DataCleanManager.getFolderSize(file))));
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_set);
    }
}
