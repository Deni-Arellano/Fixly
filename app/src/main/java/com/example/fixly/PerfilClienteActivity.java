package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PerfilClienteActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.perfil_cliente);

        View btnEditarPerfil = findViewById(R.id.boton_editar_perfil_cliente);
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilClienteActivity.this, EditarPerfilClienteActivity.class);
                startActivity(intent);
            }
        });

        View btnCambiarContrasena = findViewById(R.id.boton_cambiar_contraseña_perfil_cliente);
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilClienteActivity.this, CambiarContrasenaActivity.class);
                startActivity(intent);
            }
        });

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilClienteActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            }
        });

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilClienteActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ya estamos en Perfil
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilClienteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
