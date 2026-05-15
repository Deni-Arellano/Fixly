package com.example.fixly.models.historial;

import com.google.gson.annotations.SerializedName;

public class EditarSolicitudRequest {
    private String calle;
    @SerializedName("numero_exterior")
    private String numeroExterior;
    @SerializedName("numero_interior")
    private String numeroInterior;
    private String colonia;
    private String ciudad;
    private String estado;
    @SerializedName("codigo_postal")
    private String codigoPostal;
    private String referencia;
    private String descripcion;

    public EditarSolicitudRequest(String calle, String numeroExterior, String numeroInterior, String colonia, String ciudad, String estado, String codigoPostal, String referencia, String descripcion) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
        this.referencia = referencia;
        this.descripcion = descripcion;
    }
}
