package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerSolicitudActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_solicitud);

        View btnEditar = findViewById(R.id.boton_editar_solicitud);
        if (btnEditar != null) {
            //btnEditar.setVisibility(View.VISIBLE);
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudActivity.this, EditarSolicitudActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnCancelar = findViewById(R.id.boton_cancelar_solicitud);
        if (btnCancelar != null) {

        }

        View btnCalificar = findViewById(R.id.boton_calificar_proveedor);
        if (btnCalificar != null) {
            btnCalificar.setVisibility(View.VISIBLE);
            
            btnCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudActivity.this, CalificarServicio.class);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            });
        }

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        if (btnSalir != null) {
            btnSalir.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }
}
