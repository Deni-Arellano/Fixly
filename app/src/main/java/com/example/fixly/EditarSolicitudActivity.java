package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditarSolicitudActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.editar_solicitud);

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, InicioActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
