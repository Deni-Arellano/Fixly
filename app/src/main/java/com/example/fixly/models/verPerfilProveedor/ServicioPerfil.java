package com.example.fixly.models.verPerfilProveedor;

import com.google.gson.annotations.SerializedName;

public class ServicioPerfil {
    private int id;
    private String nombre;
    private String descripcion;
    @SerializedName("categoria_id")
    private int idCategoria;

    public int getId() { return id; }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getIdCategoria() { return idCategoria; }
}
