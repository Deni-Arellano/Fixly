package com.example.fixly.models.administrador;

import java.util.List;

public class UsuariosAdminResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private List<UsuarioAdmin> datos;

    public int getCodigo() { return codigo; }
    public String getMensaje() { return mensaje; }
    public List<UsuarioAdmin> getDatos() { return datos; }
}
