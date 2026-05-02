package com.example.fixly.models.historial;

import java.util.List;

public class HistorialResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private List<Solicitud> datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }

    public String getMensaje() { return mensaje; }

    public List<Solicitud> getDatos() { return datos; }
}