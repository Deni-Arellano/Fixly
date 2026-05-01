package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerSolicitudProveedorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_solicitud_proveedor);

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudProveedorActivity.this, SolicitudesProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudProveedorActivity.this, MiPerfilProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
