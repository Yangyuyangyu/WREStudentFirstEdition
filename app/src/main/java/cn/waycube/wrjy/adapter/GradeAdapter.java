package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.model.Grade;

/**
 * Created by Administrator on 2016/4/13.
 */
public class GradeAdapter extends MyBaseAdapter {
    public GradeAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grade, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //适配数据
        Grade grade = (Grade) mData.get(position);
        //名称
        viewHolder.tvName.setText(grade.getName());
        //分数
        viewHolder.tvScore.setText(grade.getScore()+"分");

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_score)
        TextView tvScore;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
