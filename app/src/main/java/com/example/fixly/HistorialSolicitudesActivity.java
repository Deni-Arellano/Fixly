package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HistorialSolicitudesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.historial_solicitudes);

        // --- CONEXIÓN DE LAS TARJETAS ---
        
        // Buscamos el botón de 'Ver solicitud' dentro del layout historial_solicitudes
               View btnVerSolicitud = findViewById(R.id.boton_guardar_cambiar_contrasena);
        if (btnVerSolicitud != null) {
            btnVerSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistorialSolicitudesActivity.this, VerSolicitudActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                // Ya estamos en Historial
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(HistorialSolicitudesActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(HistorialSolicitudesActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            });
        }

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        if (btnSalir != null) {
            btnSalir.setOnClickListener(v -> {
                Intent intent = new Intent(HistorialSolicitudesActivity.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }
}
