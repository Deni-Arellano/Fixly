package com.example.fixly.models.catalogos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoriasResponse {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("datos")
    private List<Categoria> datos;

    public int getCodigo() { return codigo; }
    public String getMensaje() { return mensaje; }
    public List<Categoria> getDatos() { return datos; }
}
