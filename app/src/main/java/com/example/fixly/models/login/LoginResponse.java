package com.example.fixly.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("datos")
    private Datos datos;

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Datos getDatos() {
        return datos;
    }

    public static class Datos {
        @SerializedName("id_usuario")
        private int idUsuario;

        @SerializedName("id_rol")
        private int idRol;

        @SerializedName("accessToken")
        private String accessToken;

        public int getIdUsuario() {
            return idUsuario;
        }

        public int getIdRol() {
            return idRol;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }
}
