package cn.waycube.wrjy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.wheelview.ArrayWheelAdapter;
import cn.waycube.wrjy.wheelview.OnWheelScrollListener;
import cn.waycube.wrjy.wheelview.WheelView;


/**
 * Created by Administrator on 2016/4/18.
 */
public class SexWheelDialog extends AbstractDialog {
    private RelativeLayout enter;
    private String[] sex;
    private String choose;
    private WheelView wheelView;

    public SexWheelDialog(Context context, String[] sex, String content, View.OnClickListener onClickListener) {
        super(context);
        this.sex = sex;
        setContentView(R.layout.sexwheeldialog_layout);
        enter = (RelativeLayout) findViewById(R.id.sexdialog_rl);
        wheelView = (WheelView) findViewById(R.id.sexdialog_wheelview);
        ArrayWheelAdapter adapter = new ArrayWheelAdapter(context, sex);
        wheelView.setViewAdapter(adapter);
        wheelView.setVisibleItems(7);
        wheelView.setCyclic(false);
        wheelView.addScrollingListener(scrollListener);
        wheelView.setCurrentItem(find(sex, content));

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
            choose = sex[wheel.getCurrentItem()];
        }
    };

    public String GetText() {
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
