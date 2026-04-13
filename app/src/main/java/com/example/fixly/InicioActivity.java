package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InicioActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.inicio);

        // --- SERVICIOS ---
        
        View btnPlomeria = findViewById(R.id.boton_plomeria_inicio);
        btnPlomeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnElectricidad = findViewById(R.id.boton_electricidad_inicio);
        btnElectricidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnJardineria = findViewById(R.id.boton_jardineria_inicio);
        btnJardineria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnLimpieza = findViewById(R.id.boton_limpieza_inicio);
        btnLimpieza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnPintura = findViewById(R.id.boton_pintura_inicio);
        btnPintura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnCerrajeria = findViewById(R.id.boton_cerrajeria_inicio);
        btnCerrajeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnMecanica = findViewById(R.id.boton_mecanica_inicio);
        btnMecanica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        View btnInstalacion = findViewById(R.id.boton_instalacion_inicio);
        btnInstalacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                startActivity(intent);
            }
        });

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            }
        });

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            }
        });

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
