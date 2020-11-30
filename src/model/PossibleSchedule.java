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
        courses = new LinkedList<>();
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

    public int calcTotalCredits() {
        if (courses.size() == 0) {
            return 0;
        } else {
            return courses.stream().map(course -> course.getSubject().getCredits())
                    .reduce(Integer::sum).orElse(0);
        }


    }
}
