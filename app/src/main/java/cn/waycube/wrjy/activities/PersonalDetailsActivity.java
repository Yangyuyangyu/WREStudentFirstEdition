package cn.waycube.wrjy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.dialog.BirthDayDialog;
import cn.waycube.wrjy.dialog.SexWheelDialog;
import cn.waycube.wrjy.global.MyApplication;
import cn.waycube.wrjy.global.UrlConfig;
import cn.waycube.wrjy.listenter.OnPopupListenter;
import cn.waycube.wrjy.model.User;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.UploadUtil;
import cn.waycube.wrjy.utils.Utils;
import cn.waycube.wrjy.views.PopupPhoto;

/**
 * Created by Administrator on 2016/4/13.
 */
public class PersonalDetailsActivity extends BaseActivity implements OnPopupListenter, UploadUtil.OnUploadProcessListener {
    @Bind(R.id.tv_complete)
    TextView tvComplete;
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @Bind(R.id.ll_sex)
    LinearLayout llSex;
    @Bind(R.id.ll_date)
    LinearLayout llDate;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.tv_tel)
    TextView tvTel;
    @Bind(R.id.ll_modification)
    LinearLayout llModification;


    /**
     * 修改头像选择的popup
     */
    private PopupPhoto popupPhoto;


    /**
     * 选择年月日
     */
    private BirthDayDialog birthDayDialog;
    /**
     * 选择性别
     */
    private SexWheelDialog sexWheelDialog;
    /**
     * 加入社团对话框
     */
    private SweetAlertDialog pDialog, Dialog;

    /**
     * 头像处理
     */
    public static final int SET_HEADIMG_CAMERA = 1;// 拍照
    public static final int SET_HEADIMG_GALLERY = 2;// 从相册中选择
    public static final int SET_HEADIMG_CROP = 3;// 裁剪结果
    //    private BitmapUtils bitmapUtils;
    /* 头像名称 */
    public static final String PHOTO_FILE_NAME = "headimg_temp.jpg";
    private File head_file = new File(Environment.getExternalStorageDirectory() + "/WR_student/image/"
            + PHOTO_FILE_NAME); //自己设置存放位置
    /* 头像路径 */
    public static final String HEADIMGPATH = Environment.getExternalStorageDirectory() + "/WR_student/image/"
            + "head_image.jpg";


    @Override
    protected void aadListenter() {
        llSex.setOnClickListener(this);
        llDate.setOnClickListener(this);
        llModification.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //选择性别
            case R.id.ll_sex:
                ChooseSex();
                break;
            //打开年月日选择
            case R.id.ll_date:
                setData();
                break;
            //修改头像
            case R.id.ll_modification:
                modification();
                break;
            //上传修改信息
            case R.id.tv_complete:
                conplete();
                break;


            default:
                return;
        }

    }

    /**
     * 修改个人信息
     */
    private void conplete() {
        //名字为空
        if (etName.getText().length() == 0) {
            CommonUtils.showToast("请输入姓名");
            return;
        } else if (etAddress.getText().length() == 0) {
            CommonUtils.showToast("请输入正确的地址");
            return;
        } else {


            Dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            Dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            Dialog.setTitleText("正在修改信息...");

            Dialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, UrlConfig.Set.EDIT_INFO, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //请求成功
                    JSONObject jsonObject = JSON.parseObject(s);
                    int responseCode = jsonObject.getIntValue("code");
                    switch (responseCode) {
                        case 0:
                            if (jsonObject != null && jsonObject.getString("data") != null)
                                MyApplication.mUser = JSON.parseObject(jsonObject.getString("data"), User.class);
                            Dialog.dismiss();
                            CommonUtils.showToast("修改成功");
                            finish();
                            break;
                        default:
                            Dialog.dismiss();
                            CommonUtils.showToast(jsonObject.getString("msg"));
                            return;
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Dialog.dismiss();
                    CommonUtils.showToast("网络错误！！");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(MyApplication.mUser.getId()));
                    map.put("head_img", imgAddress);
                    map.put("name", etName.getText().toString());
                    map.put("birthday", tvDate.getText().toString());
                    map.put("home", etAddress.getText().toString());
                    if (tvSex.getText().toString().equals("男"))
                        map.put("sex", String.valueOf(1));
                    if (tvSex.getText().toString().equals("女"))
                        map.put("sex", String.valueOf(2));
                    CommonUtils.tag(map.toString());
                    return map;
                }
            };
            setTmie(request);
            request.setTag("add");
            MyApplication.getHttpQueue().add(request);
        }

    }

    /**
     * 修改头像
     */
    private void modification() {

        //影藏软件盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(
                        PersonalDetailsActivity.this
                                .getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

        popupPhoto = new PopupPhoto(this);
        popupPhoto.setListenter(this);
        popupPhoto.showPopupWindow(this, llModification);
    }

    /**
     * 处理年月日
     */
    private void setData() {
        if (birthDayDialog == null) {
            String[] strings;
            if (MyApplication.mUser.getBirthday().equals("")) {
                strings = null;

            } else {
                strings = Utils.convertStrToArray(MyApplication.mUser.getBirthday());
            }
            birthDayDialog = new BirthDayDialog(this, strings, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!Utils.StringIsNull(birthDayDialog.GetText())) {
                        tvDate.setText(birthDayDialog.GetText());
                    } else {
                        tvDate.setText(MyApplication.mUser.getBirthday());
                    }
                    birthDayDialog.dismiss();
                }
            });

        }
        birthDayDialog.show();
    }

    /**
     * 选择性别
     */
    private void ChooseSex() {
        final String[] sex = {
                "女",
                "男"
        };
        String Str;
        if (MyApplication.mUser.getSex() == 1) {
            Str = "男";
        } else {
            Str = "女";
        }
        if (sexWheelDialog == null)
            sexWheelDialog = new SexWheelDialog(this, sex, Str, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.StringIsNull(sexWheelDialog.GetText())) {
                        tvSex.setText(sexWheelDialog.GetText());
                    }
                    sexWheelDialog.dismiss();
                }
            });
        sexWheelDialog.show();

    }

    @Override
    protected void initVariables() {
        //初始化用户数据
        setUser();


    }

    /**
     * 修改个人信息
     */
    private void setUser() {
        //设置头像
        if (!MyApplication.mUser.getBirthday().equals("")) {
            ImageLoader.getInstance().displayImage(MyApplication.mUser.getHead(), ivAvatar);
        } else
            //设置头像
            ivAvatar.setImageResource(R.mipmap.no_loading);

        //设置名字
        if (!MyApplication.mUser.getName().equals(""))
            etName.setText(MyApplication.mUser.getName());
        //设置性别
        if (MyApplication.mUser.getSex() == 1) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
        //设置出生年月日
        if (!MyApplication.mUser.getBirthday().equals(""))
            tvDate.setText(MyApplication.mUser.getBirthday());
        //设置电话号码
        if (!MyApplication.mUser.getPhone().equals(""))
            tvTel.setText(MyApplication.mUser.getPhone());
        //设置地址
        if (!MyApplication.mUser.getAddr().equals(""))
            etAddress.setText(MyApplication.mUser.getAddr());
    }

    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_personal_details);
    }

    @Override
    public void popupListener(int tag) {
        switch (tag) {
            //打开相机
            case 1:
                camera();
                break;
            //打开相册
            case 2:
                gallery();
                break;
            default:
                return;
        }
    }

    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (head_file.exists()) {
                head_file.delete();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(head_file));
            startActivityForResult(intent, SET_HEADIMG_CAMERA);
        }
    }

    public boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /*
   * 从相册获取
   */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SET_HEADIMG_GALLERY);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);// 黑边
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        // 图片格式
        intent.putExtra("outputFormat", "JPG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        File f = new File(HEADIMGPATH);
        if (f.exists()) {
            f.delete();
        }
        Uri uritempFile = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/WR_student/image/"
                + "head_image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        startActivityForResult(intent, SET_HEADIMG_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 图库
            case SET_HEADIMG_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        crop(uri);
                    }
                }
                break;
            // 相机
            case SET_HEADIMG_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        try {
                            // tempFile = new File(FileUtils.getCacheImageDir()+
                            // File.separator + PHOTO_FILE_NAME);
                            crop(Uri.fromFile(head_file));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showToast("sd卡不存在");
                    }
                }
                break;
            // 裁剪
            case SET_HEADIMG_CROP:
                /*
                 * 当用户剪切完头像后
                 */
                if (resultCode == RESULT_OK) {
                    //上传头像
                    ivAvatar.setImageURI(Uri.parse(HEADIMGPATH));

                    uploadHeadImage(HEADIMGPATH);
                }
                break;
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage(String headimgpath) {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在加载图片...");

        pDialog.show();
        String fileKey = "pic";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<>();
        params.put("orderId", "11111");
        uploadUtil.uploadFile(headimgpath, fileKey, UrlConfig.Set.IMG, params);


    }


    private String imgAddress = "";

    @Override
    public void onUploadDone(int responseCode, String message) {

        pDialog.dismiss();

        //由于服务器返回的值一定是json数据，所以把返回值封装为jsonObject格式
        JSONObject jsonObject = JSON.parseObject(message);
        int code = jsonObject.getIntValue("code");
        switch (code) {
            //成功
            case 0:
                //设置服务器地址
                imgAddress = jsonObject.getString("data");
                break;
            default:
                CommonUtils.showToast(jsonObject.getString("msg"));
                return;
        }


    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {
    }


}
