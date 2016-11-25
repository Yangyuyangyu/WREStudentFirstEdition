package cn.waycube.wrjy.global;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.waycube.wrjy.R;
import cn.waycube.wrjy.listenter.MyLocationListener;
import cn.waycube.wrjy.model.User;
import cn.waycube.wrjy.utils.CommonUtils;
import cn.waycube.wrjy.utils.ShareReferenceUtils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MyApplication extends Application {

    /**
     * 用户登录的参数 全局
     */

    public static User mUser;
    public static RequestQueue queue;


    /**
     * 管理activity
     */
    private Activity activity = null;
    private List<Activity> mList = new LinkedList<Activity>();
    private static MyApplication instance;

    /**
     * 定位的城市
     */
    public static String city;

    @Override
    public void onCreate() {
        super.onCreate();


        //管理activity
        instance = this;

        //初始化图片缓存
        initImageLoader();

        //初始化常用工具类
        CommonUtils.init(getApplicationContext());
        //初始化ShareReferenceUtils
        ShareReferenceUtils.init(getApplicationContext());

        queue = Volley.newRequestQueue(getApplicationContext());//使用全局上下文

    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "WR_student/image/");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null)//设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3) //线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300) //缓存的文件数量
                .defaultDisplayImageOptions(getDisplayImageOptions())
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    private DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.upload_ico)//设置图片在下载期间显示的图片
                .showImageOnLoading(R.mipmap.upload_ico)//设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.mipmap.failure_ico)//设置图片加载/解码过程中错误时候显示的图片
//                .delayBeforeLoading(1000)//下载前的延迟时间
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(false)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888) //设置图片的解码类型
                .displayer(new FadeInBitmapDisplayer(1500)) //是否图片加载好后渐入的动画时间
                .build();//构建完成
        return options;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //杀掉后台进程
//            System.exit(0);
        }
    }

    public void exitAll() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);   //返回值为0代表正常退出
        }
    }
}
