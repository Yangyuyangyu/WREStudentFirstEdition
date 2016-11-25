package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.AssociationDetailsActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.RecommendGroup;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/18.
 */
public class TeamAdapter extends MyBaseAdapter {


    public TeamAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_team, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * 适配数据
         */
        final RecommendGroup recommendGroup = (RecommendGroup) mData.get(position);
        //图片
        ImageLoader.getInstance().displayImage( recommendGroup.getImg(), viewHolder.ivAvatar);
        //社团名称
        viewHolder.tvName.setText(recommendGroup.getName());
        //社团简介
        viewHolder.tvBrief.setText(recommendGroup.getBrief());
        //管理员名称
        viewHolder.tvAdminName.setText(recommendGroup.getAdmin_name());
        /**
         * 设置Item点击事件
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AssociationDetailsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, recommendGroup.getId());
                mContext.startActivity(intent);
            }
        });

        notifyLoadingMore(position);

        return convertView;
    }




    /**
     * 设置加载更多监听器
     *
     * @param listener
     */

    public void setOnLoadingMoreListener(AdapterListenter listener) {
        this.listener = listener;
    }

    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_admin_name)
        TextView tvAdminName;
        @Bind(R.id.tv_brief)
        TextView tvBrief;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
