package com.example.fixly.models.verPerfilCliente;

import com.google.gson.annotations.SerializedName;

public class PerfilCliente {
    private int id;
    private String nombre;
    @SerializedName("apellido_paterno")
    private String apellidoPaterno;
    @SerializedName("apellido_materno")
    private String apellidoMaterno;
    @SerializedName("nombre_completo")
    private String nombreCompleto;
    private String correo;
    private String telefono;
    @SerializedName("foto_perfil")
    private String fotoPerfil;
    @SerializedName("rol_id")
    private int idRol;
    @SerializedName("estado_id")
    private int idEstado;
    @SerializedName("created_at")
    private String fechaCreacion;

    public int getId() { return id; }

    public String getNombre() { return nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public String getNombreCompleto() { return nombreCompleto; }

    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getFotoPerfil() { return fotoPerfil; }
    public int getIdRol() { return idRol; }
    public int getIdEstado() { return idEstado; }
    public String getFechaCreacion() { return fechaCreacion; }
}
