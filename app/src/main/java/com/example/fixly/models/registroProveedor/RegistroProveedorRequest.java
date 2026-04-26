package com.example.fixly.models.registroProveedor;

import com.example.fixly.models.registroCliente.RegistroRequest;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RegistroProveedorRequest {
    @SerializedName("usuario")
    private RegistroRequest usuario;

    @SerializedName("datos_prestador")
    private DatosPrestador datosPrestador;

    public RegistroProveedorRequest(RegistroRequest usuario, DatosPrestador datosPrestador) {
        this.usuario = usuario;
        this.datosPrestador = datosPrestador;
    }

    public static class DatosPrestador {
        private String descripcion;
        @SerializedName("categoria_id")
        private int categoriaId;
        private List<Integer> servicios;

        public DatosPrestador(String descripcion, int categoriaId, List<Integer> servicios) {
            this.descripcion = descripcion;
            this.categoriaId = categoriaId;
            this.servicios = servicios;
        }
    }
}
