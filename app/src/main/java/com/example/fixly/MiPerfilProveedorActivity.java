package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MiPerfilProveedorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_mi_perfil_proveedor);

        View btnCambiarContrasena = findViewById(R.id.boton_password);
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, CambiarContrasenaProveedorActivity.class);
                startActivity(intent);
            }
        });

        View btnEditarPerfil = findViewById(R.id.boton_editar);
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, EditarPerfilProveedorActivity.class);
                startActivity(intent);
            }
        });

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        btnInicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, SolicitudesProveedorActivity.class);
                startActivity(intent);
            }
        });


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ya estamos en Inicio
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
