package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.global.MyApplication;

/**
 * Created by Administrator on 2016/4/14.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_logging)
    TextView tvLogging;
    @Bind(R.id.tv_forget)
    TextView tvForget;

    @Override
    protected void aadListenter() {
        tvForget.setOnClickListener(this);
        tvLogging.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //打开登录页面
            case R.id.tv_logging:
                startActivity(new Intent(this, LoggingActivity.class));
                break;
            //打开忘记密码
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            //打开注册界面
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            default:
                return;
        }
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);
    }

}
