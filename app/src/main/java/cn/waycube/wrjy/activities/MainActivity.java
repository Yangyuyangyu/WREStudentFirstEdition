package cn.waycube.wrjy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import butterknife.Bind;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.base.BaseFragment;
import cn.waycube.wrjy.fragments.CourseFragment;
import cn.waycube.wrjy.fragments.MyFragment;
import cn.waycube.wrjy.fragments.RankingFragment;
import cn.waycube.wrjy.fragments.TeamFragment;
import cn.waycube.wrjy.global.MyApplication;

public class MainActivity extends BaseActivity {


    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.iv_team)
    ImageView ivTeam;
    @Bind(R.id.tv_team)
    TextView tvTeam;
    @Bind(R.id.ll_team)
    LinearLayout llTeam;
    @Bind(R.id.iv_course)
    ImageView ivCourse;
    @Bind(R.id.tv_course)
    TextView tvCourse;
    @Bind(R.id.ll_course)
    LinearLayout llCourse;
    @Bind(R.id.iv_ranking)
    ImageView ivRanking;
    @Bind(R.id.tv_ranking)
    TextView tvRanking;
    @Bind(R.id.ll_ranking)
    LinearLayout llRanking;
    @Bind(R.id.iv_my)
    ImageView ivMy;
    @Bind(R.id.tv_my)
    TextView tvMy;
    @Bind(R.id.ll_my)
    LinearLayout llMy;


    /**
     * 标记当前是那个Fragment
     */
    private final static String TEAM = "1", COURSE = "2", RANKING = "3", MY = "4";

    /**
     * Fragment
     */
    private BaseFragment teamFragment, courseFragment, rankingFragment, myFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //由于activity被回收后导致fragment重叠，这里防止重叠措施
        if (savedInstanceState != null) {
            teamFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TEAM);
            courseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(COURSE);
            rankingFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(RANKING);
            myFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(MY);

        }
        //默认界面
        showFrangment(TEAM);


    }


    /**
     * 显示某个fragment
     *
     * @param index
     */
    private void showFrangment(String index) {

        //隐藏所有Fragment
        hideAllFragments();
        //获取Fragment管理
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (index) {
            //社团
            case TEAM:
                if (teamFragment == null) {
                    teamFragment = new TeamFragment();
                    ft.add(R.id.fl_content, teamFragment, TEAM);
                } else {
                    ft.show(teamFragment);
                }
                changeBottomBtnStyle(0);
                break;
            //课程表
            case COURSE:
                if (courseFragment == null) {
                    courseFragment = new CourseFragment();
                    ft.add(R.id.fl_content, courseFragment, COURSE);
                } else {
                    ft.show(courseFragment);
                }
                changeBottomBtnStyle(1);
                break;
            //排名
            case RANKING:
                if (rankingFragment == null) {
                    rankingFragment = new RankingFragment();
                    ft.add(R.id.fl_content, rankingFragment, RANKING);
                } else {
                    ft.show(rankingFragment);
                }
                changeBottomBtnStyle(2);
                break;
            //我
            case MY:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    ft.add(R.id.fl_content, myFragment, MY);
                } else {
                    ft.show(myFragment);
                }
                changeBottomBtnStyle(3);
                break;
        }
        //提交事务
        ft.commit();
    }


    /**
     * 记录上次底部被点击按钮索引
     */
    private int pre_index = 0;

    /**
     * 修改对应的图标和颜色
     *
     * @param index
     */
    private void changeBottomBtnStyle(int index) {
        if (index < 0 || index > 3 || index == pre_index)
            return;

        ImageView[] ivs = {ivTeam, ivCourse, ivRanking, ivMy};
        TextView[] tvs = {tvTeam, tvCourse, tvRanking, tvMy};

        int[] ons = {R.mipmap.team_on, R.mipmap.course_on, R.mipmap.ranking_on, R.mipmap.my_on};
        int[] offs = {R.mipmap.team_close, R.mipmap.course_close, R.mipmap.ranking_close, R.mipmap.my_close};

        ivs[index].setImageResource(ons[index]);
        tvs[index].setTextColor(ContextCompat.getColor(this, R.color.green_color));
        ivs[pre_index].setImageResource(offs[pre_index]);
        tvs[pre_index].setTextColor(ContextCompat.getColor(this, R.color.gray_color));

        pre_index = index;

    }

    /**
     * 隐藏所有Fragment
     */
    private void hideAllFragments() {
        //获取fragment管理事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (teamFragment != null && teamFragment.isVisible())
            ft.hide(teamFragment);
        if (courseFragment != null && courseFragment.isVisible())
            ft.hide(courseFragment);
        if (rankingFragment != null && rankingFragment.isVisible())
            ft.hide(rankingFragment);
        if (myFragment != null && myFragment.isVisible())
            ft.hide(myFragment);
        //提交事务
        ft.commit();
    }

    @Override
    protected void aadListenter() {
        llCourse.setOnClickListener(this);
        llMy.setOnClickListener(this);
        llRanking.setOnClickListener(this);
        llTeam.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //社团
            case R.id.ll_team:
                showFrangment(TEAM);
                break;
            //课程表
            case R.id.ll_course:
                showFrangment(COURSE);
                break;
            //排名
            case R.id.ll_ranking:
                showFrangment(RANKING);
                break;
            //我
            case R.id.ll_my:
                showFrangment(MY);
                break;
            default:
                return;
        }
    }

    @Override
    protected void initVariables() {
        //百度推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "FGkadaSO3MWE1hqGuwV4Gz5UswcTH31X");
//        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "FGkadaSO3MWE1hqGuwV4Gz5UswcTH31X");
//        List<String> tag = new ArrayList<>();
//        tag.add(String.valueOf(6666666));
//        PushManager.setTags(getApplicationContext(), tag);


    }


    @Override
    protected void initLayout() {
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);
    }


    /*
       *返回按钮的点击事件
       */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击按钮时间
    private long exitTime = 0;

    /*
     *处理点击事件 8毫秒之内再次点击退出
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
