package com.example.fixly.models.calificar;

public class CalificacionStatusResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private DatosStatus datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }
    public String getMensaje() { return mensaje; }
    public DatosStatus getDatos() { return datos; }

    public static class DatosStatus {
        private boolean calificada;
        public boolean isCalificada() { return calificada; }
    }
}
