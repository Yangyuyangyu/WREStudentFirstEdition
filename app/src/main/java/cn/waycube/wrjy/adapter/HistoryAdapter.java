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
import cn.waycube.wrjy.listenter.HistoryListener;

/**
 * Created by Administrator on 2016/6/7.
 */
public class HistoryAdapter extends MyBaseAdapter {


    private HistoryListener listener;
    public HistoryAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_history, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvHistory.setText(mData.get(position).toString());


        //点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            listener.setString(mData.get(position).toString());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_history)
        TextView tvHistory;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void setNameListener(HistoryListener listener){
        this.listener=listener;

    }
}
