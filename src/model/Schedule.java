package model;

import java.util.Arrays;
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

    public LinkedList<int[]> getIndices() {
        int columna=0;
        LinkedList<int[]> indices= new LinkedList<>();
        switch (day) {

            case 'L':
                columna = 0;
                break;

            case 'M':
                columna = 1;
                break;

            case 'X':
                columna = 2;
                break;

            case 'J':
                columna = 3;
                break;

            case 'V':
                columna = 4;
                break;

            case 'S':
                columna = 5;
                break;

        }
        int st=getIntegerHour(start);
        int en=getIntegerHour(end);
        for (int i = st; i <en ; i++) {
            int[] par= {(i-6),columna};
            indices.add(par);
        }
        return indices;
    }

    public int getIntegerHour(int hour){
        String st= ""+hour;
        int i;
        if(st.length()==4){
            st=st.substring(0,2);
        }else{
            st=st.substring(0,1);
        }
        i=Integer.parseInt(st);
        return i;
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
