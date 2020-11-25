package model;

import java.util.LinkedList;

public class Schedule {
    private char day;
    private int start;
    private int end;
    private String classroom;

    public Schedule(char day, int start, int end, String classroom) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.classroom = classroom;
    }

    public LinkedList<Integer[]> getIndices() {


        return null;
    }

    public char getDay() {
        return day;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getClassroom() {
        return classroom;
    }
}
