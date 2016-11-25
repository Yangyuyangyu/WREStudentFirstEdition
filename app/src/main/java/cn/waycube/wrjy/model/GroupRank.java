package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/28.
 */
public class GroupRank {
    /**
     * id
     */
    private int id;
    /**
     * 学生头像
     */
    private String head;
    /**
     * 学生名字
     */
    private String name;

    /**
     *学生成绩
     */
    private float score;

    /**
     * 学生名称
     */
    private String my_order;

    public String getMy_order() {
        return my_order;
    }

    public void setMy_order(String my_order) {
        this.my_order = my_order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
