package cn.waycube.wrjy.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.ItemImageAdapter;
import cn.waycube.wrjy.adapter.ViewPagerFixed;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/5/7.
 */
public class ImageActivity extends BaseActivity {


    @Bind(R.id.view_pager)
    ViewPagerFixed viewPager;
    /**
     * 图片显示
     */
    private ItemImageAdapter mAdapter;

    /**
     * 图片地址
     */
    private String images;


    @Override
    protected void aadListenter() {


    }

    @Override
    protected void initVariables() {
        //设置图片
        images = getIntent().getStringExtra(KeyUtil.KEY_ONE);

        if (!images.equals("")) {
            List<String> str = new ArrayList<>();
            String[] as = images.split(",");
            for (int i = 0; i < as.length; i++) {
                str.add(as[i]);
            }
            //添加图片
            List<View> views = new ArrayList<>();
            for (int i = 0; i < str.size(); i++) {
                View imageView = LayoutInflater.from(this).inflate(R.layout.item_image, null);
                ImageView imag = (ImageView) imageView.findViewById(R.id.images);
                ImageLoader.getInstance().displayImage(str.get(i), imag);
                views.add(imageView);
            }
            mAdapter = new ItemImageAdapter(views);
            viewPager.setAdapter(mAdapter);


        }

    }

    ActionBar actionBar; //声明ActionBar

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_image);
        actionBar = getActionBar(); //得<span></span>到ActionBar
        actionBar.hide(); //隐藏ActionBar
    }

}
