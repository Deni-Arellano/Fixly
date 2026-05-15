package com.example.fixly.models.historial;

import com.google.gson.annotations.SerializedName;

public class DetalleSolicitudResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private DetalleSolicitud datos;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }
    public String getMensaje() { return mensaje; }
    public DetalleSolicitud getDatos() { return datos; }

    public static class DetalleSolicitud {
        private int id;
        @SerializedName("servicio_id")
        private int servicioId;
        @SerializedName("nombre_servicio")
        private String nombreServicio;
        @SerializedName("estado_id")
        private int estadoId;
        @SerializedName("estado_solicitud")
        private String estadoSolicitud;
        private String descripcion;
        private String direccion;
        private String referencia;
        @SerializedName("creado_en")
        private String creadoEn;
        @SerializedName("nombre_prestador")
        private String nombrePrestador;
        @SerializedName("foto_perfil")
        private String fotoPerfil;
        private String telefono;

        public int getId() { return id; }
        public int getServicioId() { return servicioId; }
        public String getNombreServicio() { return nombreServicio; }
        public int getEstadoId() { return estadoId; }
        public String getEstadoSolicitud() { return estadoSolicitud; }
        public String getDescripcion() { return descripcion; }
        public String getDireccion() { return direccion; }
        public String getReferencia() { return referencia; }
        public String getCreadoEn() { return creadoEn; }
        public String getNombrePrestador() { return nombrePrestador; }
        public String getFotoPerfil() { return fotoPerfil; }
        public String getTelefono() { return telefono; }
    }
}
