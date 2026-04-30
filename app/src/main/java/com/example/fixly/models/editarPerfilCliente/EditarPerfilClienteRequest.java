package com.example.fixly.models.editarPerfilCliente;

import com.google.gson.annotations.SerializedName;

public class EditarPerfilClienteRequest {
    @SerializedName("foto_perfil")
    private String fotoPerfil;
    private String nombre;
    @SerializedName("apellido_paterno")
    private String apellidoPaterno;
    @SerializedName("apellido_materno")
    private String apellidoMaterno;
    private String telefono;

    public EditarPerfilClienteRequest(String fotoPerfil, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono) {
        this.fotoPerfil = fotoPerfil;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
    }
}
