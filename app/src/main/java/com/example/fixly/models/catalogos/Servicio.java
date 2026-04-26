package com.example.fixly.models.catalogos;

import com.google.gson.annotations.SerializedName;

public class Servicio {
    private int id;
    @SerializedName("categoria_id")
    private int categoriaId;
    private String nombre;
    private String descripcion;

    public int getId() { return id; }
    public int getCategoriaId() { return categoriaId; }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
}
