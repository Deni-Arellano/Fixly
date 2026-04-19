package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SolicitudesProveedorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.menu_solicitudes_proveedor);

        // Buscamos el botón de 'Ver solicitud' dentro del layout historial_solicitudes
        View btnVerSolicitud = findViewById(R.id.boton_guardar_cambiar_contrasena);
        if (btnVerSolicitud != null) {
            btnVerSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SolicitudesProveedorActivity.this, VerSolicitudProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        btnInicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Ya estamos en Inicio
            }
        });


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SolicitudesProveedorActivity.this, MiPerfilProveedorActivity.class);
                startActivity(intent);
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SolicitudesProveedorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
