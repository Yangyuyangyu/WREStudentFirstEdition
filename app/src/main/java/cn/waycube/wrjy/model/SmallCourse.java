package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SmallCourse {
    /**
     * id
     */
    private int id;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 课程图片
     */
    private String img;
    /**
     * 课程简介
     */
    private String brief;

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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }


    @Override
    public boolean equals(Object o) {
        return this.id == ((SmallCourse) o).getId();
    }
}
