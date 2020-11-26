package model;

import java.util.LinkedList;

public class Course {
    private Subject subject;
    private int nrc;
    private char mode;
    private int totalStudents;
    private Professor professor;
    private LinkedList<Schedule> schedules;

    public Course(Subject subject, int nrc, char mode, int totalStudents, Professor professor) {
        this.subject = subject;
        this.nrc = nrc;
        this.mode = mode;
        this.totalStudents = totalStudents;
        this.professor = professor;
        this.subject.addProfessor(professor);

    }


    public LinkedList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(LinkedList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getNrc() {
        return nrc;
    }

    public char getMode() {
        return mode;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
