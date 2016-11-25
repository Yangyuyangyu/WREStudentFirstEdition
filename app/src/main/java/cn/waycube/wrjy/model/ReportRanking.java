package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ReportRanking {
    /**
     * 学生id
     */
    private int sid;
    /**
     * 学生姓名
     */
    private String sname;
    /**
     * 头像
     */
    private String head_img;
    /**
     * 成绩
     */
    private float score;
    /**
     * 排名
     */
    private  String my_order;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getMy_order() {
        return my_order;
    }

    public void setMy_order(String my_order) {
        this.my_order = my_order;
    }
}
