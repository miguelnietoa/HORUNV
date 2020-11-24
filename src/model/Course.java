package model;

public class Course {
    private Subject subject;
    private int nrc;
    private char mode;
    private int totalStudents;
    private Professor professor;

    public Course(Subject subject, int nrc, char mode, int totalStudents, Professor professor) {
        this.subject = subject;
        this.nrc = nrc;
        this.mode = mode;
        this.totalStudents = totalStudents;
        this.professor = professor;
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
