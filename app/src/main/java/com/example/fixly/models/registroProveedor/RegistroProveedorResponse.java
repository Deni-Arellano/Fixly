package com.example.fixly.models.registroProveedor;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class RegistroProveedorResponse {
    private int codigo;
    private String estatus;
    private String mensaje;
    private Datos datos;
    private Map<String, String> errores;

    public int getCodigo() { return codigo; }
    public String getMensaje() { return mensaje; }
    public Datos getDatos() { return datos; }
    public Map<String, String> getErrores() { return errores; }

    public static class Datos {
        private int id;

        public int getId() { return id; }
    }
}
