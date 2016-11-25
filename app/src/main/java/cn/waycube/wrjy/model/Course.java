package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/25.
 */
public class Course {

    /**
     * id
     */
    private int id;
    /**
     * 课程名称
     */
    private String name;
    /**
     *课程图片
     */
    private String img;
    /**
     *上课时间
     */
    private String class_time;
    /**
     * 课程状态，0老师未签到，1待开始上课，2待点名，3待提交上课报告，4课程结束；
     * 当课程类型为小课时状态为：0待老师确认，1待上课，2待开始上课，3待提交报告，4待学生确认，5课程结束
     */
    private int status;
    /**
     *1表示大课，2表示小课
     */
    private int type;

    /**
     * 学生是否确认上课，0未确认，1已确认
     */
    private int confirm;

    /**
     * 是否可以查看课程报告，0否，1是
     */
    private int report;

    /**
     * 上课老师是否请假，0未请假，1已请假
     */
    private int teacher_leave;

    /**
     * 课程id
     */
    private  int cid;

    /**
     * 学生是否已评价课程，0未评价，1已评价
     */
    private int is_comment;

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public int getTeacher_leave() {
        return teacher_leave;
    }

    public void setTeacher_leave(int teacher_leave) {
        this.teacher_leave = teacher_leave;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }



    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getClass_time() {
        return class_time;
    }

    public void setClass_time(String class_time) {
        this.class_time = class_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {

        //equal默认比较的是hashcode，这里我们要改成默认比较id

        return this.id == ((Course) o).getId();
    }
}
