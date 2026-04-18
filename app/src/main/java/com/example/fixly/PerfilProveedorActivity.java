package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PerfilProveedorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.perfil_proveedor);

        // SOLICITAR SERVICIO

        View btnSolicitarServicio = findViewById(R.id.boton_solicitar_servicio);
        btnSolicitarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilProveedorActivity.this, SolicitarServicioActivity.class);
                startActivity(intent);
            }
        });


        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilProveedorActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            }
        });

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilProveedorActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilProveedorActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilProveedorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
