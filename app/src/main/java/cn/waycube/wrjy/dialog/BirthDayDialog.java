package cn.waycube.wrjy.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Calendar;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.wheelview.NumericWheelAdapter;
import cn.waycube.wrjy.wheelview.OnWheelScrollListener;
import cn.waycube.wrjy.wheelview.WheelView;

/**
 * Created by Administrator on 2016/4/18.
 */
public class BirthDayDialog extends AbstractDialog {
    private RelativeLayout enter;
    private LinearLayout ll;
    private int mYear = 1950;
    private int mMonth = 1;
    private int mDay = 1;
    View view = null;
    private LayoutInflater inflater = null;

    boolean isMonthSetted = false, isDaySetted = false;
    private WheelView year;
    private WheelView month;
    private WheelView day;


    String birthday;

    public BirthDayDialog(Context context, String[] str, View.OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.birthdaydialog_layout);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (str != null && !str.equals("") && str.length > 0) {
            mYear = Integer.valueOf(str[0]);
            mMonth = (Integer.valueOf(str[1])) - 1;
            mDay = Integer.valueOf(str[2]);
        }
        enter = (RelativeLayout) findViewById(R.id.birthdaydialog_rl);
        enter.setOnClickListener(onClickListener);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll.addView(getDataPick());
    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
//		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
//		int curDate = c.get(Calendar.DATE);

        int curYear = mYear;
        int curMonth = mMonth + 1;
        int curDate = mDay;

        view = inflater.inflate(R.layout.wheel_date_picker, null);

        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(getContext(), 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(getContext(), 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        day.addScrollingListener(scrollListener);
        day.setCyclic(true);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;//年
            int n_month = month.getCurrentItem() + 1;//月

            initDay(n_year, n_month);

            birthday = new StringBuilder().append((year.getCurrentItem() + 1950)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1)).toString();
        }
    };

    /**
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getContext(), 1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    public String GetText() {
        return birthday;
    }

    public void DissMiss() {
        dismiss();
    }
}
