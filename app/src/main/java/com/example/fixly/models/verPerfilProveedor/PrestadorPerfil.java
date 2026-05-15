package com.example.fixly.models.verPerfilProveedor;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PrestadorPerfil {
    @SerializedName("id_prestador")
    private int idPrestador;
    private String descripcion;
    private List<ServicioPerfil> servicios;
    private CalificacionesPerfil calificaciones;

    public int getIdPrestador() { return idPrestador; }
    public String getDescripcion() { return descripcion; }

    public List<ServicioPerfil> getServicios() { return servicios; }
    public CalificacionesPerfil getCalificaciones() { return calificaciones; }

    public static class CalificacionesPerfil {
        private double promedio;
        private List<ComentarioPerfil> comentarios;

        public double getPromedio() { return promedio; }
        public List<ComentarioPerfil> getComentarios() { return comentarios; }
    }

    public static class ComentarioPerfil {
        private int estrellas;
        private String comentario;

        public int getEstrellas() { return estrellas; }
        public String getComentario() { return comentario; }
    }
}
