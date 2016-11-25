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
import cn.waycube.wrjy.activities.TeamActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Club;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/12.
 */
public class OrganizationAdapter extends MyBaseAdapter {


    public OrganizationAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_organization, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 适配数据
         */
        final Club club = (Club) mData.get(position);
        //图片
        ImageLoader.getInstance().displayImage(club.getImg(), viewHolder.ivImage);
        //机构名称
        viewHolder.tvName.setText(club.getName());
        //老师人数
        viewHolder.tvUserNum.setText(club.getUserNum()+"位老师");
        //地址
        viewHolder.tvLocation.setText(club.getLocation());

        /**
         * 设置Item点击事件
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,TeamActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE,club.getId());
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
        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_userNum)
        TextView tvUserNum;
        @Bind(R.id.tv_location)
        TextView tvLocation;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
