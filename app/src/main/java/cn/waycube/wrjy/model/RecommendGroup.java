package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/21.
 */
public class RecommendGroup {
    /**
     *社团简介
     */
    private String brief;
    /**
     *社团ID
     */
    private int id;
    /**
     *社团名称
     */
    private String name;
    /**
     * 所属机构名称
     */
    private String agency_name;
    /**
     * 图片
     */
    private String img;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 管理员名称
     */
    private String admin_name;

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

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
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

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    @Override
    public boolean equals(Object o) {

        //equal默认比较的是hashcode，这里我们要改成默认比较id

        return this.id == ((RecommendGroup) o).getId();
    }
}
