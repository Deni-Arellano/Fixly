package com.example.fixly.models.administrador;

import com.google.gson.annotations.SerializedName;

public class UsuarioAdmin {
    private int id;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    private String correo;
    private String telefono;

    @SerializedName("rol_id")
    private int rolId;

    @SerializedName("estado_id")
    private int estadoId;

    private String rol;
    private String estado;

    // Getters
    public int getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public int getRolId() { return rolId; }
    public int getEstadoId() { return estadoId; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }
}
