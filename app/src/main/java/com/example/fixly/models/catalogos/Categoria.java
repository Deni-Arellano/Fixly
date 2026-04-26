package com.example.fixly.models.catalogos;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;

    public int getId() { return id; }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return nombre; }
}
