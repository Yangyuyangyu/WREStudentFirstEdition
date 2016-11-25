package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Message {
    /**
     * id
     */
    private int id;
    /**
     * 内容
     */
    private String content;
    /**
     * 时间
     */
    private String time;

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
