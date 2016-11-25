package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Comment {


    private String id;
    /**
     * 学生名称
     */
    private String name;
    /**
     * 头像
     */
    private String head;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private  String time;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        return this.id.equals(((Comment) o).getId());
    }
}
