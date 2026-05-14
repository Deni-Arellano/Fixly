package com.example.fixly.models.proveedores;

import java.util.List;

public class ProveedoresCategoriaResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private List<ProveedorCategoria> datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }
    public String getMensaje() { return mensaje; }
    public List<ProveedorCategoria> getDatos() { return datos; }
}
