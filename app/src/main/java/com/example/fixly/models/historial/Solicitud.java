package com.example.fixly.models.historial;

import com.google.gson.annotations.SerializedName;

public class Solicitud {
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

    public int getId() { return id; }
    public int getServicioId() { return servicioId; }
    public String getNombreServicio() { return nombreServicio; }
    public int getEstadoId() { return estadoId; }
    public String getEstadoSolicitud() { return estadoSolicitud; }

    public String getDescripcion() { return descripcion; }
}