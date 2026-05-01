package com.example.fixly;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class ListaServiciosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.lista_servicios);

        // para traerme el id y el titulo del servicio seleccionado y mostrar el servicio en la pantalla
        int idServicio = getIntent().getIntExtra("id_servicio", -1);
        String nombreServicio = getIntent().getStringExtra("nombre_servicio");

        TextView tvTitulo = findViewById(R.id.titulo_servicio);
        if (tvTitulo != null && nombreServicio != null) {
            tvTitulo.setText("¿Necesitas servicio de " + nombreServicio + "?");
        }

        // Reference the RecyclerView defined in lista_servicios.xml
        RecyclerView recyclerView = findViewById(R.id.lista_proveedores);

        // SELECCION DEL PERFIL DEL PROVVEDOR
        View tarjeta = findViewById(R.id.item_proveedor);

        if (tarjeta != null) {
            tarjeta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListaServiciosActivity.this, PerfilProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListaServiciosActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(ListaServiciosActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListaServiciosActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
