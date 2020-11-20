package model;

public class Course {
    private Subject subject;
    private int nrc;
    private char mode;
    private int totalStudents;

    public Course(Subject subject, int nrc, char mode, int totalStudents) {
        this.subject = subject;
        this.nrc = nrc;
        this.mode = mode;
        this.totalStudents = totalStudents;
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
}
