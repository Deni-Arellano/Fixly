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
        private List<String> comentarios;

        public double getPromedio() { return promedio; }
        public List<String> getComentarios() { return comentarios; }
    }
}
