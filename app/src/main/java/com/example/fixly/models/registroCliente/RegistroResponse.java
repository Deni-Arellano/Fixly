package com.example.fixly.models.registroCliente;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class RegistroResponse {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("estatus")
    private String estatus;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("datos")
    private Object datos;

    @SerializedName("paginacion")
    private Object paginacion;

    @SerializedName("errores")
    private Map<String, String> errores;

    public int getCodigo() { return codigo; }
    public String getEstatus() { return estatus; }
    public String getMensaje() { return mensaje; }
    public Object getDatos() { return datos; }
    public Object getPaginacion() { return paginacion; }
    public Map<String, String> getErrores() { return errores; }
}
