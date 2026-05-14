package com.example.fixly.models.administrador;

import com.google.gson.annotations.SerializedName;

public class UpdateEstadoRequest {
    @SerializedName("estado_id")
    private int estadoId;

    public UpdateEstadoRequest(int estadoId) {
        this.estadoId = estadoId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }
}
