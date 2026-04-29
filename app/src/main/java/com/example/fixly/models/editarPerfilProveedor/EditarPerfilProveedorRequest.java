package com.example.fixly.models.editarPerfilProveedor;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EditarPerfilProveedorRequest {
    @SerializedName("usuario")
    private UsuarioEdit usuario;

    @SerializedName("datos_prestador")
    private DatosPrestadorEdit datosPrestador;

    public EditarPerfilProveedorRequest(UsuarioEdit usuario, DatosPrestadorEdit datosPrestador) {
        this.usuario = usuario;
        this.datosPrestador = datosPrestador;
    }

    public static class UsuarioEdit {
        @SerializedName("foto_perfil")
        private String fotoPerfil;
        private String nombre;
        @SerializedName("apellido_paterno")
        private String apellidoPaterno;
        @SerializedName("apellido_materno")
        private String apellidoMaterno;
        private String telefono;

        public UsuarioEdit(String fotoPerfil, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono) {
            this.fotoPerfil = fotoPerfil;
            this.nombre = nombre;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.telefono = telefono;
        }
    }

    public static class DatosPrestadorEdit {
        private String descripcion;
        private List<Integer> servicios;

        public DatosPrestadorEdit(String descripcion, List<Integer> servicios) {
            this.descripcion = descripcion;
            this.servicios = servicios;
        }
    }
}
