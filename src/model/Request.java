package model;
import java.sql.Timestamp;

public class Request {
    private int codeStudentRequested;
    private int codeStudentHost;
    private Timestamp dateHour;
    private String fullNameStudent;
    public Request(int codeStudentRequested, int codeStudentHost, Timestamp dateHour) {
        this.codeStudentRequested = codeStudentRequested;
        this.codeStudentHost = codeStudentHost;
        this.dateHour = dateHour;
        fullNameStudent = "";
    }

    public String getFullNameStudent() {
        return fullNameStudent;
    }

    public void setFullNameStudent(String fullNameStudent) {
        this.fullNameStudent = fullNameStudent;
    }

    public int getCodeStudentRequested() {
        return codeStudentRequested;
    }

    public int getCodeStudentHost() {
        return codeStudentHost;
    }

    public Timestamp getDateHour() {
        return dateHour;
    }
}
