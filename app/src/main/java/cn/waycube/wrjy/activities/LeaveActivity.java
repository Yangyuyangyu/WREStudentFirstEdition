package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.dialog.LeaveDialog;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.OnPopupListenter;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.Utils;
import cn.waycube.wrjy.views.PopupLeave;

/**
 * Created by Administrator on 2016/4/15.
 */
public class LeaveActivity extends BaseActivity implements OnPopupListenter {

    @Bind(R.id.tv_leave)
    TextView tvLeave;
    @Bind(R.id.ll_leave)
    LinearLayout llLeave;
    @Bind(R.id.ll_leaves)
    LinearLayout llLeaves;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.ll_start_time)
    LinearLayout llStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.ll_end_time)
    LinearLayout llEndTime;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.et_data)
    EditText etData;


    View view = null;


    /**
     * 加载对话框
     */
    private SweetAlertDialog pDialog;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 备注
     */
    private String reason;

    /**
     * 请假类型,默认事假
     */
    private int type = 1;


    private PopupLeave popupLeave;
    /**
     * 请假时间的Dialog
     */
    private LeaveDialog leaveStartDialog, leaveEndDialog;

    /**
     * 课程id
     */
    private int id;

    /**
     * 当前时间
     */
    private String[] times;

    @Override
    protected void aadListenter() {
        llLeave.setOnClickListener(this);
        llStartTime.setOnClickListener(this);
        llEndTime.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //选择请假原因
            case R.id.ll_leave:
                leave();
                break;
            //选择开始时间
            case R.id.ll_start_time:
                startTime();
                break;
            //结束时间
            case R.id.ll_end_time:
                endTime();
                break;
            //提交请假
            case R.id.tv_submit:
                submit();
                break;
            default:
                return;
        }
    }

    /**
     * 提交请假处理
     */
    private void submit() {
        if (etData.getText().toString().length() == 0) {
            CommonUtils.showToast("请输入请假原因!");
            return;
        }
        if (startTime.equals(endTime)) {
            CommonUtils.showToast("请选择正确请假时间！");
            return;
        }


        if (Utils.formatDate(tvEndTime.getText().toString()) < Utils.formatDate(startTime) || Utils.formatDate(startTime) < Utils.formatDate(currentTime)) {
            CommonUtils.showToast("请选择正确的时间!");
            return;
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在提交申请...");

        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.LEAVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast("请假已提交！");
                        pDialog.dismiss();
                        finish();
                        break;
                    default:
                        pDialog.dismiss();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                CommonUtils.showToast("网络错误！！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("reason", etData.getText().toString());
                map.put("type", String.valueOf(type));
                map.put("start", startTime);
                map.put("end", endTime);
                map.put("student", String.valueOf(MyApplication.mUser.getId()));
                map.put("courseId", String.valueOf(id));
                return map;
            }
        };
        setTmie(request);
        request.setTag("leave");
        MyApplication.getHttpQueue().add(request);

    }

    /**
     * 设置请假结束时间
     */
    private void endTime() {
        if (leaveEndDialog == null)
            leaveEndDialog = new LeaveDialog(this, times, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.StringIsNull(leaveEndDialog.GetText())) {
                        tvEndTime.setText(leaveEndDialog.GetText());
                        endTime = leaveEndDialog.GetText();
                    }
                    leaveEndDialog.DissMiss();
                }
            });
        leaveEndDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = leaveEndDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        leaveEndDialog.getWindow().setAttributes(lp);
    }

    /**
     * 设置请假时间
     */
    private void startTime() {


        if (leaveStartDialog == null)
            leaveStartDialog = new LeaveDialog(this, times, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.StringIsNull(leaveStartDialog.GetText())) {
                        tvStartTime.setText(leaveStartDialog.GetText());
                        startTime = leaveStartDialog.GetText();
                    }
                    leaveStartDialog.DissMiss();
                }
            });
        leaveStartDialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = leaveStartDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        leaveStartDialog.getWindow().setAttributes(lp);
    }

    /**
     * 处理请假原因
     */
    private void leave() {
        popupLeave = new PopupLeave(this);
        popupLeave.setOnPopupListenter(this);
        popupLeave.showPopupWindow(this, llLeaves);

    }

    @Override
    protected void initVariables() {
        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);

        //设置当前时间
        setTime();

        //设置在家对话框


    }

    /**
     *当前时间
     */
    private String currentTime;

    /**
     * 设置当前时间
     */
    private void setTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        times = new String[]{
                String.valueOf(year),
                String.valueOf(month + 1),
                String.valueOf(day),
                String.valueOf(hour),
                String.valueOf(minute),
        };
        currentTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        startTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
//        tvEndTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
        endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;

    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_leave);

    }

    @Override
    public void popupListener(int tag) {
        //事假
        if (tag == 0) {
            tvLeave.setText("事假");
            type = 1;
        }
        //病假
        if (tag == 1) {
            tvLeave.setText("病假");
            type = 2;
        }
        //病假
        if (tag == 2) {
            tvLeave.setText("其他");
            type = 3;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里面注销
        MyApplication.getHttpQueue().cancelAll("leave");

    }
}
