package com.example.fixly.models.solicitarServicio;

import com.google.gson.annotations.SerializedName;

public class SolicitarServicioRequest {
    @SerializedName("calle")
    private String calle;

    @SerializedName("numero_exterior")
    private String numeroExterior;

    @SerializedName("numero_interior")
    private String numeroInterior;

    @SerializedName("colonia")
    private String colonia;

    @SerializedName("ciudad")
    private String ciudad;

    @SerializedName("estado")
    private String estado;

    @SerializedName("codigo_postal")
    private String codigoPostal;

    @SerializedName("referencia")
    private String referencia;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("servicio_id")
    private int servicioId;

    @SerializedName("prestador_id")
    private int prestadorId;

    public SolicitarServicioRequest(String calle, String numeroExterior, String numeroInterior, String colonia, String ciudad, String estado, String codigoPostal, String referencia, String descripcion, int servicioId, int prestadorId) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
        this.referencia = referencia;
        this.descripcion = descripcion;
        this.servicioId = servicioId;
        this.prestadorId = prestadorId;
    }

    public String getCalle() { return calle; }
    public String getNumeroExterior() { return numeroExterior; }
    public String getNumeroInterior() { return numeroInterior; }
    public String getColonia() { return colonia; }
    public String getCiudad() { return ciudad; }
    public String getEstado() { return estado; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getReferencia() { return referencia; }
    public String getDescripcion() { return descripcion; }
    public int getServicioId() { return servicioId; }
    public int getPrestadorId() { return prestadorId; }
}
