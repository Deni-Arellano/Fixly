package com.example.fixly.models.catalogos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ServiciosResponse {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("datos")
    private List<Servicio> datos;

    public int getCodigo() { return codigo; }
    public String getMensaje() { return mensaje; }
    public List<Servicio> getDatos() { return datos; }
}
