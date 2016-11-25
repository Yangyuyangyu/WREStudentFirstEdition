package cn.waycube.wrjy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.model.AllGroup;
import cn.waycube.wrjy.model.ClassListe;
import cn.waycube.wrjy.wheelview.ArrayWheelAdapter;
import cn.waycube.wrjy.wheelview.OnWheelScrollListener;
import cn.waycube.wrjy.wheelview.WheelView;

/**
 * Created by Administrator on 2016/5/7.
 */
public class SubjectAdapter extends AbstractDialog {
    private RelativeLayout enter;
    private List<ClassListe> allGroups;
    private int choose;
    private WheelView wheelView;

    public SubjectAdapter(Context context, List<ClassListe> data, String content, View.OnClickListener onClickListener) {
        super(context);
        this.allGroups = data;
        setContentView(R.layout.subjectwheeldialog_layout);
        enter = (RelativeLayout) findViewById(R.id.sexdialog_rl);
        wheelView = (WheelView) findViewById(R.id.sexdialog_wheelview);
        String[] str = new String[allGroups.size()];
        for (int i = 0; i < allGroups.size(); i++) {
            str[i] = allGroups.get(i).getName();
        }


        ArrayWheelAdapter adapter = new ArrayWheelAdapter(context, str);
        wheelView.setViewAdapter(adapter);
        wheelView.setVisibleItems(7);
        wheelView.setCyclic(false);
        wheelView.addScrollingListener(scrollListener);
        wheelView.setCurrentItem(find(str, content));

        enter.setOnClickListener(onClickListener);
    }


    public void Dismiss() {
        dismiss();
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            choose = wheel.getCurrentItem();
        }
    };

    public int GetText() {
        return choose;
    }

    public static int find(String[] arr, String str) {
        boolean flag = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str)) {
                flag = true;
                return i;
            }
        }
        if (flag == false) {
            return -1;
        }
        return -1;
    }
}