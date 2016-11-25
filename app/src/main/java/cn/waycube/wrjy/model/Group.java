package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Group {
    /**
     * 加入社团申请的审核状态，0待审核，1通过，2拒绝
     */
    private int status;
    /**
     * 社团id
     */
    private int group_id;

    /**
     * 社团
     */
    private GroupInfo groupInfo;

    private int id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        //equal默认比较的是hashcode，这里我们要改成默认比较id

        return this.id == ((Group) o).getId();
    }
}
