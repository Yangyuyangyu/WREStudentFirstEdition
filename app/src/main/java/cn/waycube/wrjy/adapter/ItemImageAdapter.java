package cn.waycube.wrjy.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Mr-fang on 2016/2/21.
 */
public class ItemImageAdapter extends PagerAdapter{

    /**
     * fragment集合
     */
    private List<View> view;

    public ItemImageAdapter( List<View> view) {
        this.view = view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(view.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        container.addView(view.get(position), 0);//添加页卡
        return view.get(position);
    }

    @Override
    public int getCount() {
        return view.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

}
