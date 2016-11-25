package cn.waycube.wrjy.model;

/**
 * Created by Administrator on 2016/4/20.
 */
public class User {

    /**
     * 学生的ID
     */
    private int id;

    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String head;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别
     */
    private int sex;
    /**
     * 新消息数量
     */
    private int notice_unread_num;

    /**
     * 所在地址
     */
    private String addr;

    /**
     * 客服电话
     */
    private String customer_service;


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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getNotice_unread_num() {
        return notice_unread_num;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setNotice_unread_num(int notice_unread_num) {
        this.notice_unread_num = notice_unread_num;

    }

    public String getCustomer_service() {

        return customer_service;
    }

    public void setCustomer_service(String customer_service) {
        this.customer_service = customer_service;
    }
}
