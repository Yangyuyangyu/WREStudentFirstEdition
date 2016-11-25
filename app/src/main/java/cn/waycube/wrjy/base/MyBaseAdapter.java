package cn.waycube.wrjy.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.waycube.wrjy.listenter.AdapterListenter;


/**
 * Created by Mr-fang on 2016/1/13.
 */
public abstract class MyBaseAdapter extends BaseAdapter{

    protected AdapterListenter listener;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<? extends Object> mData;
    //加载更多的开关，默认为false，当为true标识正在请求加载更多
    public boolean isLoadingMore = false;

    public MyBaseAdapter( Context context,List<? extends Object> data ){

        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);


    /**
     * 触发加载更多的时间
     *
     * @param position
     */
    protected void notifyLoadingMore(int position) {
        //加载更多触发
        if (mData.size() >= 6 && position == mData.size() - 2 && isLoadingMore == false) {
            if (listener != null)
                listener.adapterListenter();
        }
    }

}

