package cn.waycube.wrjy.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Utils {

    /**
     * 返回当前程序版本名
     */
    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    public static String getAPPVersionCodeFromAPP(Context ctx) {
        int currentVersionCode = 0;
        String appVersionName = null;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
            e.printStackTrace();
        }
        return appVersionName;
    }

    //使用String的split 方法
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split("-"); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    /*
     * 判断String是否为null、""或者null字符串
	 */
    public static boolean StringIsNull(String v) {
        boolean flag = true;
        if (v != null) {
            v = v.trim();
            if (!"".equals(v) && !"null".equals(v)) {
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断是否是手机号
     *
     * @param phone 手机号码
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        //^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$以前的是：^1[3|4|5|7|8][0-9]\\d{4,8}$
        if (phone.length() < 11) {
            return false;
        }
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
        Matcher m = p.matcher(phone);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }




    /**
     * 将时间戳转换成秒
     *
     * @param str
     * @return
     */
    public static long formatDate(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeStart = 0;
        try {
            timeStart = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeStart;
    }



}
