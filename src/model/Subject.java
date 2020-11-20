package model;

import java.util.HashMap;
import java.util.LinkedList;

public class Subject {
    private String code;
    private String name;
    private int credits;
    private HashMap<Integer, Course> courses;

    public Subject(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.courses = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setCourses(HashMap<Integer, Course> courses) {
        this.courses = courses;
    }
}
