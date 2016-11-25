package cn.waycube.wrjy.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.global.MyApplication;

/**
 * Created by Mr-fang on 2016/1/13.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 标志 下拉刷新
     */
    protected final static int PULL_REFRESH = 1;
    /**
     * 标志 上拉刷新
     */
    protected final static int LOADING_MORE = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //管理activity
        MyApplication.getInstance().setActivity(this);
        super.onCreate(savedInstanceState);

        //初始化布局
        initLayout();
        //添加ButterKife框架
        ButterKnife.bind(this);
        //初始化变量
        initVariables();
        //添加监听时间
        aadListenter();

    }



//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }


    protected abstract void aadListenter();

    protected abstract void initVariables();

    protected abstract void initLayout();

    @Override
    public void onClick(View v) {

    }

    public void onBack(View view) {
        finish();
    }


    /**
     * 隐藏软键盘
     *
     * @param view
     */
    protected void hideSoftInputFromWindow(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected  void setTmie(StringRequest request){
        request.setRetryPolicy( new DefaultRetryPolicy( 20*1000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                0,//默认最大尝试次数
                1.0f ) );


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}

