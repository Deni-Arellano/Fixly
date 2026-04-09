package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InicioActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.inicio);

        View btnPlomeria = findViewById(R.id.boton_inicio_plomeria);
        btnPlomeria.setOnClickListener(v -> {
            startActivity(new Intent(this, ListaServiciosActivity.class));
        });
    }
}