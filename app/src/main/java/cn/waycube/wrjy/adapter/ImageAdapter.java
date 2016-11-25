package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.listenter.AdapterListenter;

/**
 * Created by Administrator on 2016/5/7.
 */
public class ImageAdapter extends MyBaseAdapter {

    private AdapterListenter listenter;
    public ImageAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_imag, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String image= (String) mData.get(position);
        //适配数据
        ImageLoader.getInstance().displayImage(image,viewHolder.ivImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            listenter.adapterListenter();
            }
        });


        return convertView;
    }


    public void setlisetnter( AdapterListenter listenter){
        this.listenter=listenter;
    }

    static class ViewHolder {
        @Bind(R.id.iv_image)
        ImageView ivImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
