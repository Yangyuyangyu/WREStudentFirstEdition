package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Message;

/**
 * Created by Administrator on 2016/4/13.
 */
public class NewsAdapter extends MyBaseAdapter {
    public NewsAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_news, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        /**
         * 适配数据
         */
        Message message= (Message) mData.get(position);
        //时间
        viewHolder.tvTime.setText(message.getTime());
        //内容
        viewHolder.tvContent.setText(message.getContent());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 设置加载更多监听器
     *
     * @param listener
     */

    public void setOnLoadingMoreListener(AdapterListenter listener) {
        this.listener = listener;
    }

    @Override
    protected void notifyLoadingMore(int position) {
        //加载更多触发
        if (mData.size() >= 8 && position == mData.size() - 2 && isLoadingMore == false) {
            if (listener != null)
                listener.adapterListenter();
        }
    }
}
