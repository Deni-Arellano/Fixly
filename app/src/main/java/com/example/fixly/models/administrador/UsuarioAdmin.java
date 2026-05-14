package com.example.fixly.models.administrador;

import com.google.gson.annotations.SerializedName;

public class UsuarioAdmin {
    private int id;
    private String nombre;
    @SerializedName("apellido_paterno")
    private String apellidoPaterno;
    @SerializedName("apellido_materno")
    private String apellidoMaterno;
    private String correo;
    private String telefono;
    private String rol;
    private int estatus; // 1 para activo, 0 para inactivo

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
    public int getEstatus() { return estatus; }
    public void setEstatus(int estatus) { this.estatus = estatus; }
}
