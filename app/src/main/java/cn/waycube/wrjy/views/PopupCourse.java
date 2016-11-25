package cn.waycube.wrjy.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.listenter.OnPopupListenter;

/**
 * Created by Administrator on 2016/4/15.
 */
public class PopupCourse extends PopupWindow {
    private View conentView;
    private OnPopupListenter listenter;

    public PopupCourse(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_windouw_course, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 4);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);

        LinearLayout linearLayout = (LinearLayout) conentView.findViewById(R.id.ll_leave);
        LinearLayout linearLayout1 = (LinearLayout) conentView.findViewById(R.id.ll_complaint);


        //请假
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenter.popupListener(0);
                PopupCourse.this.dismiss();
            }
        });

        //投诉
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenter.popupListener(1);
                PopupCourse.this.dismiss();
            }
        });


    }

    public void setOnPopupListenter(OnPopupListenter listener) {
        this.listenter = listener;
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(Activity context, View parent) {
        if (!this.isShowing()) {
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, w/2 +w/5, 0);
        } else {
            this.dismiss();
        }
    }


}

