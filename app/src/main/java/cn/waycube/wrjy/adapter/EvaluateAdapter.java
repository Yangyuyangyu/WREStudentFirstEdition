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
import cn.waycube.wrjy.model.Comment;

/**
 * Created by Administrator on 2016/4/18.
 */
public class EvaluateAdapter extends MyBaseAdapter {
    public EvaluateAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_evaluate, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 适配数据
         */
        Comment comment = (Comment) mData.get(position);
        //头像
        ImageLoader.getInstance().displayImage(comment.getHead(), viewHolder.ivAvatar);
        //名字
        viewHolder.tvName.setText(comment.getName());
        //时间
        viewHolder.tvTime.setText(comment.getTime());
        //内容
        viewHolder.tvContent.setText(comment.getContent());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
