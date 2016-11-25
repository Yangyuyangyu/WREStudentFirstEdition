package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Rank {
    /**
     * id
     */
    private int group_id;

    /**
     * 社团图片
     */
    private String img;
    /**
     * 机构名称
     */
    private String agency;
    /**
     * 社团名称
     */
    private String group;
    /**
     * 名次
     */
    private String order;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        return this.group_id == ((Rank) o).getGroup_id();
    }
}
