package model;

import java.util.LinkedList;

public class PossibleSchedule {
    private int codigoEstudiante;
    private int consecutivo;
    private String nombre;
    private LinkedList<Course> courses;

    public PossibleSchedule(int codigoEstudiante, int consecutivo, String nombre) {
        this.codigoEstudiante = codigoEstudiante;
        this.consecutivo = consecutivo;
        this.nombre = nombre;
    }

    public int getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public String getNombre() {
        return nombre;
    }

    public LinkedList<Course> getCourses() {
        return courses;
    }

    public void setCourses(LinkedList<Course> courses) {
        this.courses = courses;
    }
}
