package cn.waycube.wrjy.global;

/**
 * Created by Administrator on 2016/4/20.
 */
public class UrlConfig {

    /**
     * 基础URL
     */
    public final static String BASE_URL = "http://211.149.226.242/";


    /**
     * 登录
     */
    public class User {
        //获取验证码
        public final static String USER_GET_CODE = BASE_URL + "Api/StudentApi/sendCode?mobile=";

        //注册
        public final static String REGISTER = BASE_URL + "Api/StudentApi/register";

        //登录
        public final static String LOGGING = BASE_URL + "Api/StudentApi/login?";

        //忘记密码
        public final static String FORGET = BASE_URL + "Api/StudentApi/findPwd";

        //创建百度云标签
        public final static  String TAG=BASE_URL+"Api/StudentApi/createTag";

        //根据id获取用户信息
        public final static  String USER_ID=BASE_URL+"Api/StudentApi/studentInfo?";
    }

    /**
     * 社团
     */
    public class Team {
        //根据用户获取已加入社团
        public final static String ID_GET_TEAM = BASE_URL + "Api/StudentApi/myGroup?";

        //获取推存社团列表
        public final static String RECOMMEND_TEAM = BASE_URL + "Api/StudentApi/group?";

        //根据id获取社团详情
        public final static String ID_TEAM = BASE_URL + "Api/StudentApi/groupDetail?";

        //根据id获取社团制度
        public final static String ID_RULE = BASE_URL + "Api/StudentApi/rule?";

        //根据id获取社团建设
        public final static String ID_BUILD = BASE_URL + "Api/StudentApi/groupBuild?";

        //根据id获取课程规划
        public final static String ID_PLAN = BASE_URL + "Api/StudentApi/coursePlan?";

        //根据id获取动态详情
        public final static String ID_NEWS_DETAIL = BASE_URL + "Api/StudentApi/newsDetail?";

        //加入社团
        public final static String ADD_GROUP = BASE_URL + "Api/StudentApi/joinGroup";

        //根据id获取机构详情
        public final static String ID_AGENCY = BASE_URL + "Api/StudentApi/agencyDetail?";

        //换班时查询可选择的社团
        public final static String ID_GONFIG = BASE_URL + "Api/StudentApi/allGroup?";

        //换班时选择社团后查询科目
        public final static String ID_SUBJECT=BASE_URL+"Api/StudentApi/getSubject?";

        //提交换课申请
        public final static String CHANGE_CLASS=BASE_URL+"Api/StudentApi/changeClass";



    }

    /**
     * 课程表
     */
    public class Course {
        //根据学生id获取课程表
        public final static String ID_COURSE = BASE_URL + "Api/StudentApi/courseList?";

        //根据课程id获取课程详情
        public final static String ID_COURSE_DETAILS = BASE_URL + "Api/StudentApi/courseInfo?";

        //请假
        public final static String LEAVE = BASE_URL + "Api/StudentApi/leave";

        //投诉
        public final static String COMPLAINT = BASE_URL + "Api/StudentApi/complaint";

        //查询学生加入社团的科目
        public final static String ID_SUBJECT = BASE_URL + "Api/StudentApi/mySubject?";

        //根据课程id获取课程报告
        public final static String ID_REPORT = BASE_URL + "Api/StudentApi/courseReport?";

        //预约小课
        public final static String PRECONTRACT = BASE_URL + "Api/StudentApi/appoint";

        //提交作业
        public final static String HOME_WORK = BASE_URL + "Api/StudentApi/homework";

        //确认上课
        public final static String COFIRM = BASE_URL + "Api/StudentApi/confirm";

        //评价课程
        public final static String COMMENT = BASE_URL + "Api/StudentApi/comment";

        //获取课程成绩排名
        public final static String ID_SCORE = BASE_URL + "Api/StudentApi/score?";

        //获取成绩详情
        public final static String ID_GRADE = BASE_URL + "Api/StudentApi/myScore?";

        //获取社团下的课程
        public final static  String ID_SUBJECTS=BASE_URL+"Api/StudentApi/getSubject?";

        //历史课程
        public final  static  String ID_RECORD=BASE_URL+"Api/StudentApi/finishedClass?";

        //查看对应社团的历史课程
        public final static  String  ID_CLUB_RECORD=BASE_URL+"Api/StudentApi/groupClass?";
    }


    /**
     * 社团排名
     */
    public class Ranking{
        //根据id获取社团排名
        public final static String ID_RANKING=BASE_URL+"Api/StudentApi/rank?";

        //获取某个社团的排名
        public final static String GROUP_RANK=BASE_URL+"Api/StudentApi/groupRank?";


    }

    /**
     * 设置
     */
    public class Set{
        //修改密码
        public final static String CHANGE=BASE_URL+"Api/StudentApi/editPwd";

        //获取消息
        public final  static String  NEWS=BASE_URL+"Api/StudentApi/message?";

        //上传头像
        public  final static String  IMG=BASE_URL+"Api/CommonApi/imgUpload";

        //修改个人信息
        public final static  String EDIT_INFO=BASE_URL+"Api/StudentApi/editInfo";

        //获取关于我们
        public final static  String ABOUT_US=BASE_URL+"Api/StudentApi/aboutUs";

        //邀请好友
        public final static  String  INVITE=BASE_URL+"Api/StudentApi/invite?";

        //提交意见
        public final static   String FEEDBACK=BASE_URL+"Api/StudentApi/feedback";
    }
}
