package cn.waycube.wrjy.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class CourseWeek {

    /**
     * 星期中的某一天，1到7分别代表星期一到星期天
     */
    private String week;

    /**
     *课程
     */
    private List<Course> course;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }
}
