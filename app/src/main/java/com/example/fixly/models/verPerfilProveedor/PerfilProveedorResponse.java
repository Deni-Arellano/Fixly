package com.example.fixly.models.verPerfilProveedor;

import com.google.gson.annotations.SerializedName;

public class PerfilProveedorResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private DatosProveedor datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }

    public String getMensaje() { return mensaje; }

    public DatosProveedor getDatos() { return datos; }

    public static class DatosProveedor {
        private UsuarioPerfil usuario;
        private PrestadorPerfil prestador;

        public UsuarioPerfil getUsuario() { return usuario; }
        public PrestadorPerfil getPrestador() { return prestador; }
    }
}
