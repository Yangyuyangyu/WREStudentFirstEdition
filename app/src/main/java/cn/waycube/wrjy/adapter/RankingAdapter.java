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
import cn.waycube.wrjy.activities.AssociationRankingActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Rank;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/12.
 */
public class RankingAdapter extends MyBaseAdapter {
    public RankingAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_ranking, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 配置数据
         */
        final Rank rank = (Rank) mData.get(position);
        //图片
        ImageLoader.getInstance().displayImage(rank.getImg(), viewHolder.imageView1);
        //机构名称
        viewHolder.tvAgency.setText(rank.getAgency());
        //社团名称
        viewHolder.tvGroup.setText(rank.getGroup());
        //设置排名
        if (!rank.getOrder().equals(""))
            viewHolder.tvOrder.setText("第" + rank.getOrder() + "名");
        else
            viewHolder.tvOrder.setText("");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rank.getOrder().equals("")) {
                    Intent intent = new Intent(mContext, AssociationRankingActivity.class);
                    intent.putExtra(KeyUtil.KEY_ONE, rank.getGroup_id());
                    mContext.startActivity(intent);

                } else
                    CommonUtils.showToast("目前还没有排名！");

            }
        });

        /*
         *加载更多
         */
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

    @Override
    protected void notifyLoadingMore(int position) {
        //加载更多触发
        if (mData.size() >= 8 && position == mData.size() - 2 && isLoadingMore == false) {
            if (listener != null)
                listener.adapterListenter();
        }
    }

    static class ViewHolder {
        @Bind(R.id.imageView1)
        RoundedImageView imageView1;
        @Bind(R.id.tv_agency)
        TextView tvAgency;
        @Bind(R.id.tv_group)
        TextView tvGroup;
        @Bind(R.id.tv_order)
        TextView tvOrder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
