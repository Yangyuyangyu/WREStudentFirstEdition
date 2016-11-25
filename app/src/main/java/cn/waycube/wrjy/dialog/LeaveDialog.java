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
 * Created by Administrator on 2016/4/19.
 */
public class LeaveDialog extends AbstractDialog {
    private RelativeLayout enter;
    private LinearLayout ll;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView time;
    private WheelView min;
    private WheelView sec;
    private LayoutInflater inflater = null;


    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    private int mMin = 0;
    private int mSec = 0;


    View view = null;

    String birthday;

    public LeaveDialog(Context context, String[] str, View.OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.dialog_leave);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mYear = Integer.valueOf(str[0]);
        mMonth = (Integer.valueOf(str[1])) - 1;
        mDay = Integer.valueOf(str[2]);
        mMin = Integer.valueOf(str[3]);
        mSec = Integer.valueOf(str[4]);
        birthday =( mYear + "-" + ((mMonth + 1 < 10) ? "0" + (mMonth + 1) : (mMonth + 1) )+ "-" + ((mDay < 10) ? "0" + (mDay) : (mDay) )+ " " + ((mMin < 10) ? "0" + (mMin) : (mMin)) + ":" + ((mSec < 10) ? "0" +( mSec) :( mSec))).toString();
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

        view = inflater.inflate(R.layout.leave_wheel, null);

        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(getContext(), mYear, norYear + 10);
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
        day.setCyclic(true);
        day.addScrollingListener(scrollListener);

        min = (WheelView) view.findViewById(R.id.min);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(getContext(), 0, 24, "%02d");
        numericWheelAdapter3.setLabel("时");
        min.setViewAdapter(numericWheelAdapter3);
        min.setCyclic(true);
        min.addScrollingListener(scrollListener);

        sec = (WheelView) view.findViewById(R.id.sec);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(getContext(), 0, 60, "%02d");
        numericWheelAdapter4.setLabel("分");
        sec.setViewAdapter(numericWheelAdapter4);
        sec.setCyclic(true);
        sec.addScrollingListener(scrollListener);


        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);
//		time.setVisibleItems(7);
        min.setVisibleItems(7);
        sec.setVisibleItems(7);

        year.setCurrentItem(curYear - mYear);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        min.setCurrentItem(mMin);
        sec.setCurrentItem(mSec);

        return view;

    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + mYear;//年
            int n_month = month.getCurrentItem() + 1;//月

            initDay(n_year, n_month);

            birthday = new StringBuilder().append((year.getCurrentItem() + mYear)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1)).append(" ").append(((min.getCurrentItem() + 1) < 10) ? "0" + (min.getCurrentItem()) : (min.getCurrentItem())).append(":").append(((sec.getCurrentItem() + 1) < 10) ? "0" + (sec.getCurrentItem()) : (sec.getCurrentItem())).toString();
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