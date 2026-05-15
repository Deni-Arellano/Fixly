package com.example.fixly.models.calificar;

public class CalificarRequest {
    private float estrellas;
    private String comentario;

    public CalificarRequest(float estrellas, String comentario) {
        this.estrellas = estrellas;
        this.comentario = comentario;
    }

    public float getEstrellas() {
        return estrellas;
    }

    public String getComentario() {
        return comentario;
    }
}
