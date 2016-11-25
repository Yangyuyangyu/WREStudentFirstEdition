package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/5/10.
 */
public class Recorrd {

    private int id;

    /**
     * 课程id
     */
    private int cid;

    /**
     * 课程名称
     */
    private String name;
    /**
     * 课程图片
     */
    private String img;
    /**
     * 上课时间
     */
    private String class_time;
    /**
     * 上课类型，1表示大课，2表示小课
     */
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Recorrd) o).getId();
    }
}
