package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ClassListe {

    /**
     * 科目id
     */
    private int id;

    /**
     * 科目名称
     */
    private String name;
    /**
     *科目简介
     */
    private String intro;
    /**
     * 科目图片
     */
    private String img;
    /**
     * 换班状态
     */
    private int status;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
