package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.model.GroupRank;
import cn.waycube.wrjy.model.ReportRanking;
import cn.waycube.wrjy.views.RoundProgressBar;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ReportRankingAdapter extends MyBaseAdapter{

    public ReportRankingAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_association_ranking, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * 适配数据
         */
        ReportRanking reportRanking = (ReportRanking) mData.get(position);
        //头像
        ImageLoader.getInstance().displayImage(reportRanking.getHead_img(), viewHolder.ivAvatar);
        //名称
        viewHolder.tvName.setText(reportRanking.getSname());
        //排名
        viewHolder.tvMyOrder.setText("第  " + reportRanking.getMy_order() + "  名");


        viewHolder.roundProgressBar3.setMax(100);
        viewHolder.roundProgressBar3.setProgress((int) (reportRanking.getScore()*10));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_my_order)
        TextView tvMyOrder;
        @Bind(R.id.roundProgressBar3)
        RoundProgressBar roundProgressBar3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
