package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerSolicitudActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_solicitud);

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerSolicitudActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            }
        });

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerSolicitudActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerSolicitudActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerSolicitudActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
