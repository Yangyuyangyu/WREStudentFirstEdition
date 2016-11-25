package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/21.
 */
public class News {

    /**
     * 社团动态id
     */
    private int id;
    /**
     * 动态标题
     */
    private String name;
    /**
     * 动态简介
     */
    private String brief;
    /**
     * 动态图片
     */
    private String img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
