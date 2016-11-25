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
import cn.waycube.wrjy.activities.ChangeShiftsActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ClassListAdapter extends MyBaseAdapter {

    private int id;

    public ClassListAdapter(Context context, List<? extends Object> data,int id) {
        super(context, data);
        this.id=id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_class_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*
         *配置参数
         */
         final ClassListe classListe = (ClassListe) mData.get(position);
        //，名字
        viewHolder.tvName.setText(classListe.getName());
        //简介
        viewHolder.tvIntro.setText("简介:" + classListe.getIntro());
        //图片
        ImageLoader.getInstance().displayImage(classListe.getImg(), viewHolder.ivImage);
        //判断是否申请换班了
        if (classListe.getStatus() == -1 || classListe.getStatus() == 2) {
            viewHolder.tvAudit.setVisibility(View.GONE);
            viewHolder.tvChange.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvAudit.setVisibility(View.VISIBLE);
            viewHolder.tvChange.setVisibility(View.GONE);
        }
        /**
         * 申请换课
         */
        viewHolder.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ChangeShiftsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE,classListe.getId());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_audit)
        TextView tvAudit;
        @Bind(R.id.tv_change)
        TextView tvChange;
        @Bind(R.id.tv_intro)
        TextView tvIntro;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
