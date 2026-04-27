package com.example.fixly.models.verPerfilCliente;

import com.google.gson.annotations.SerializedName;

public class PerfilClienteResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private PerfilCliente datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }
    public String getMensaje() { return mensaje; }
    public PerfilCliente getDatos() { return datos; }
}
