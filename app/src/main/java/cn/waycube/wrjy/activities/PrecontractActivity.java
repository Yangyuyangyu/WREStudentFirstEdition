package cn.waycube.wrjy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.adapter.HistoryAdapter;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.dialog.LeaveDialog;
import cn.waycube.wrjy.fragments.CourseFragment;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.HistoryListener;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.KeyUtil;
import cn.waycube.wrjy.utils.RecordSQLiteOpenHelper;
import cn.waycube.wrjy.utils.ShareReferenceUtils;
import cn.waycube.wrjy.utils.Utils;

/**
 * Created by Administrator on 2016/4/15.
 */
public class PrecontractActivity extends BaseActivity implements HistoryListener {
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_time)
    LinearLayout llTime;
    @Bind(R.id.et_data)
    EditText etData;
    @Bind(R.id.lv_history)
    ListView lvHistory;
    @Bind(R.id.ll_history)
    LinearLayout llHistory;

    /**
     * 申请上课时间的Dialog
     */
    private LeaveDialog precontractDialog;

    /**
     * 当前时间
     */
    private String[] times;

    /**
     * 用户选择的上课时间
     */
    private String precontractTime = null;

    /**
     * 加载框
     */
    private SweetAlertDialog pDialog;

    /**
     * 预约上课的id
     */
    private int id;

    /**
     * 数据存储
     */
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);

    @Override
    protected void aadListenter() {
        llTime.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        adapter.setNameListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //选择上课时间
            case R.id.ll_time:
                setTime();
                break;
            //提交预约
            case R.id.tv_submit:
                submit();
                break;
            default:
                return;
        }
    }

    private void submit() {
        //用户没有选择时间
        if (precontractTime == null) {
            CommonUtils.showToast("请选择上课时间！");
            return;
        }
        //用户没有输入上课地址
        if (etData.getText().length() == 0) {
            CommonUtils.showToast("请输入上课地址！");
            return;
        }
        //提交预约上课申请
        submitData();
    }

    /**
     * 提交上课申请
     */
    private void submitData() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在提交申请...");

        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Course.PRECONTRACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功
                JSONObject jsonObject = JSON.parseObject(s);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        pDialog.dismiss();

                        //通知课程表刷新状态
                        CourseFragment.isRefresh = true;

                        //申请成功保存历史
                        JSONArray data = new JSONArray();
                        if (history.size() > 0)
                            for (int i = 0; i < history.size(); i++)
                                data.add(history.get(i));
                        data.add(etData.getText().toString());
                        ShareReferenceUtils.putValue(KeyUtil.KEY_HISTORY, data.toJSONString());


                        CommonUtils.showToast("申请成功，等待老师审核！");
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
                map.put("courseId", String.valueOf(id));
                map.put("studentId", String.valueOf(MyApplication.mUser.getId()));
                map.put("time", precontractTime);
                map.put("place", etData.getText().toString());
                return map;
            }
        };
        setTmie(request);
        request.setTag("add");
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 设置请假时间
     */
    private void setTime() {
        if (precontractDialog == null)
            precontractDialog = new LeaveDialog(this, times, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.StringIsNull(precontractDialog.GetText())) {
                        tvTime.setText(precontractDialog.GetText());
                        precontractTime = precontractDialog.GetText();
                    }
                    precontractDialog.DissMiss();
                }
            });
        precontractDialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = precontractDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        precontractDialog.getWindow().setAttributes(lp);
    }

    /**
     * 设置当前时间
     */
    private void setTimes() {

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
//        tvTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);

    }

    /**
     * 历史记录
     */
    private List<String> history = new ArrayList<>();

    private HistoryAdapter adapter;

    @Override
    protected void initVariables() {
        //设置当前时间
        setTimes();

        id = getIntent().getIntExtra(KeyUtil.KEY_ONE, 0);

        initHistory();

    }

    /**
     * 设置历史搜索记录
     */
    private void initHistory() {
        String data = ShareReferenceUtils.getValue(KeyUtil.KEY_HISTORY);
        if (data != null) {
            JSONArray jsonArray1 = JSON.parseArray(data);
            for (int i = 0; i < jsonArray1.size(); i++)
                history.add(jsonArray1.get(i).toString());

            adapter = new HistoryAdapter(this, history);
            lvHistory.setAdapter(adapter);

        } else {
            adapter = new HistoryAdapter(this, history);
        }
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_precontract);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束Activity时队列里 面注销
        MyApplication.getHttpQueue().cancelAll("add");

    }

    /**
     * 用户点击历史输入记录
     *
     * @param name
     */
    @Override
    public void setString(String name) {
        etData.setText(name);
    }
}
