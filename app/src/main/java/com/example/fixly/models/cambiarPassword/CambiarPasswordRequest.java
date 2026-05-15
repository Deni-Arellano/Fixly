package com.example.fixly.models.cambiarPassword;

import com.google.gson.annotations.SerializedName;

public class CambiarPasswordRequest {
    @SerializedName("contrasena_actual")
    private String contrasenaActual;

    @SerializedName("contrasena_nueva")
    private String contrasenaNueva;

    @SerializedName("contrasena_confirmacion")
    private String contrasenaConfirmacion;

    public CambiarPasswordRequest(String contrasenaActual, String contrasenaNueva, String contrasenaConfirmacion) {
        this.contrasenaActual = contrasenaActual;
        this.contrasenaNueva = contrasenaNueva;
        this.contrasenaConfirmacion = contrasenaConfirmacion;
    }

    public String getContrasenaActual() {
        return contrasenaActual;
    }

    public void setContrasenaActual(String contrasenaActual) {
        this.contrasenaActual = contrasenaActual;
    }

    public String getContrasenaNueva() {
        return contrasenaNueva;
    }

    public void setContrasenaNueva(String contrasenaNueva) {
        this.contrasenaNueva = contrasenaNueva;
    }

    public String getContrasenaConfirmacion() {
        return contrasenaConfirmacion;
    }

    public void setContrasenaConfirmacion(String contrasenaConfirmacion) {
        this.contrasenaConfirmacion = contrasenaConfirmacion;
    }
}
