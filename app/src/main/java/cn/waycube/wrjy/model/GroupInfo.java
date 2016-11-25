package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/22.
 */
public class GroupInfo {
    /**
     * 简介
     */
    private String brief;
    /**
     * 社团id
     */
    private int id;
    /**
     * 社团名称
     */
    private String name;
    /**
     * 学生数量
     */
    private int studentNum;
    /**
     * 所属机构
     */
    private String agency_name;
    /**
     * 管理员
     */
    private String admins;
    /**
     * 图片
     */
    private String img;
    /**
     * 创建时间
     */
    private String create_time;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
