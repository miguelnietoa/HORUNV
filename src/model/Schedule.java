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

    public String getScheduleInfo() {
        String val = "";
        val = getStringFullDay() + " " + getIntegerHour(start)+":30 "+"-"+getIntegerHour(end)+":30";
        return val;
    }

    public String getStringFullDay() {
        String val = "";
        switch (day) {

            case 'L':
                val = "Lunes";
                break;

            case 'M':
                val = "Martes";
                break;

            case 'X':
                val = "Miercoles";
                break;

            case 'J':
                val = "Jueves";
                break;

            case 'V':
                val = "Viernes";
                break;

            case 'S':
                val = "Sabado";
                break;

        }
        return val;
    }

    public LinkedList<int[]> getIndices() {
        int columna = 0;
        LinkedList<int[]> indices = new LinkedList<>();
        switch (day) {

            case 'L':
                columna = 1;
                break;

            case 'M':
                columna = 2;
                break;

            case 'X':
                columna = 3;
                break;

            case 'J':
                columna = 4;
                break;

            case 'V':
                columna = 5;
                break;

            case 'S':
                columna = 6;
                break;

        }
        int st = getIntegerHour(start);
        int en = getIntegerHour(end);
        for (int i = st; i < en; i++) {
            int[] par = {(i - 6), columna};
            indices.add(par);
        }
        return indices;
    }

    public int getIntegerHour(int hour) {
        return hour/100;
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
