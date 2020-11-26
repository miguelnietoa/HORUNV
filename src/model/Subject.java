package model;

import java.util.HashMap;
import java.util.LinkedList;

public class Subject {
    private String code;
    private String name;
    private int credits;
    private HashMap<Integer, Course> courses;
    private LinkedList<Professor> professors;
    public Subject(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.courses = new HashMap<>();
        this.professors = new LinkedList<>();
    }
    public void addProfessor(Professor professor){
        if (!this.professors.contains(professor)) {
            this.professors.add(professor);
        }
    }

    public LinkedList<Professor> getProfessors() {
        return professors;
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

    public HashMap<Integer, Course> getCourses() {
        return courses;
    }
}
