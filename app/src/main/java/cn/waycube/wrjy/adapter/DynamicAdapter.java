package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.AssociationDynamicActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.News;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DynamicAdapter extends MyBaseAdapter {
    public DynamicAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dynamic, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 适配数据
         */
        final News news = (News) mData.get(position);
        //设置图片
        ImageLoader.getInstance().displayImage( news.getImg(), viewHolder.imageView1);
        //设置名称
        viewHolder.tvName.setText(news.getName());
        //设置简介
        viewHolder.tvBrief.setText(news.getBrief());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AssociationDynamicActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, news.getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.imageView1)
        RoundedImageView imageView1;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_brief)
        TextView tvBrief;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
