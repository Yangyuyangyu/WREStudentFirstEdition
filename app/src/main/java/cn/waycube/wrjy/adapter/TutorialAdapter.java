package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import cn.waycube.wrjy.activities.PrecontractActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.SmallCourse;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/19.
 */
public class TutorialAdapter extends MyBaseAdapter {

    public TutorialAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tutorial, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 适配数据
         */
        final SmallCourse smallCourse = (SmallCourse) mData.get(position);
        //名称
        viewHolder.tvName.setText(smallCourse.getName());
        //图片
        ImageLoader.getInstance().displayImage(smallCourse.getImg(), viewHolder.imageView1);
        //简介
        viewHolder.tvBrief.setText(smallCourse.getBrief());

        /**
         * 预约小课
         */
        viewHolder.tvPrecontract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,PrecontractActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE,smallCourse.getId());
                mContext.startActivity(new Intent(intent));
                Log.e("预约小课----",smallCourse.getId()+"");
            }
        });


        /*
         *打开课程详情
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, smallCourse.getId());
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
        @Bind(R.id.tv_precontract)
        TextView tvPrecontract;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
