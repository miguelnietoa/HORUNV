package model;

import java.util.LinkedList;

public class Professor {
    private String id;
    private String fullname;
    private LinkedList<Course> courses;
    private boolean enable;

    public Professor(String id, String fullname) {
        this.id = id;
        this.fullname = fullname;
        this.courses = new LinkedList<>();
        this.enable = true;
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public LinkedList<Course> getCourses() {
        return courses;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
