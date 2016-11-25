package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Club {
    /**
     * id
     */
    private int id;
    /**
     * 机构名称
     */
    private String name;

    /**
     * 1机构，2学校
     */
    private String type;
    /**
     * 机构图片
     */
    private String img;

    /**
     * 机构地址
     */
    private String location;
    /**
     * 简介
     */
    private String brief;
    /**
     * 特点
     */
    private String feature;

    /**
     * 老师数量
     */
    private String userNum;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Club) o).getId();
    }
}
