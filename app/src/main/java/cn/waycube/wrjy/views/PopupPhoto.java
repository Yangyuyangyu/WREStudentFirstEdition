package cn.waycube.wrjy.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.listenter.OnPopupListenter;


/**
 * Created by Mr-fang on 2016/1/24.
 */
public class PopupPhoto extends PopupWindow implements View.OnClickListener{



    private OnPopupListenter listenter;


    private View conentView;

    private static final int TAKE_PICTURE = 0x000001;


    public PopupPhoto(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_comment_photo, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
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
        //         设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopWinAnimation);

        TextView cancel = (TextView) conentView.findViewById(R.id.tv_cancel);
        TextView camera = (TextView) conentView.findViewById(R.id.tv_popupwindows_camera);
        TextView photo = (TextView) conentView.findViewById(R.id.tv_popupwindows_Photo);
        cancel.setOnClickListener(this);
        camera.setOnClickListener(this);
        photo.setOnClickListener(this);


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
//            this.showAsDropDown(parent, 0, 0);
            this.showAtLocation(parent, Gravity.RIGHT, 0, 0);

        } else {
            this.dismiss();
        }
    }

    /**
     * 添加事件外派
     */

    public void setListenter(OnPopupListenter listenter){
        this.listenter=listenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
            //打开相机
            case R.id.tv_popupwindows_camera:
                listenter.popupListener(1);
                this.dismiss();
                break;
            //打开相册
            case R.id.tv_popupwindows_Photo:
                listenter.popupListener(2);
                this.dismiss();
                break;
        }
    }
}