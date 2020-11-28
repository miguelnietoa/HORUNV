package model;

public class PossibleSchedule {
    private int codigoEstudiante;
    private int consecutivo;
    private String nombre;

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
}
