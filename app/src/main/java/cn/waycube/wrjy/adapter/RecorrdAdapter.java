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
import cn.waycube.wrjy.activities.CourseDetailsActivity;
import cn.waycube.wrjy.activities.ReportActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.listenter.AdapterListenter;
import cn.waycube.wrjy.model.Recorrd;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class RecorrdAdapter extends MyBaseAdapter {


    public RecorrdAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_record, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();


        }
        //适配参数

        final Recorrd recorrd = (Recorrd) mData.get(position);
        //课程图片
        ImageLoader.getInstance().displayImage(recorrd.getImg(), viewHolder.imageView1);
        //课程名
        if (recorrd.getType() == 1) {
            //大课
            viewHolder.tvName.setText(recorrd.getName());
            viewHolder.tvSize.setText("(大课)");
        } else {
            //小课
            viewHolder.tvName.setText(recorrd.getName());
            viewHolder.tvSize.setText("(小课)");
        }
        //上课时间
        viewHolder.tvTime.setText(recorrd.getClass_time());


        /*
         *打开课程详情
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, recorrd.getCid());
                mContext.startActivity(intent);
            }
        });


        /*
         *查看课程记录
         */
        viewHolder.tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReportActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, recorrd.getId());
                intent.putExtra(KeyUtil.KEY_TWO, recorrd.getType());
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
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_size)
        TextView tvSize;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_check)
        TextView tvCheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
