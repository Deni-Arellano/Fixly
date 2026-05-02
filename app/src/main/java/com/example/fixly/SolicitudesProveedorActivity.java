package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixly.adapters.SolicitudAdapter;
import com.example.fixly.models.historial.HistorialResponse;
import com.example.fixly.models.historial.Solicitud;
import com.example.fixly.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudesProveedorActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SolicitudAdapter adapter;
    private List<Solicitud> listaSolicitudes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.menu_solicitudes_proveedor);

        recyclerView = findViewById(R.id.recycler_historial_solicitudes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SolicitudAdapter(listaSolicitudes, this);
        recyclerView.setAdapter(adapter);

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ya estamos en Inicio
                }
            });
        }


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SolicitudesProveedorActivity.this, MiPerfilProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        obtenerHistorial();

    }

    private void obtenerHistorial() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().getHistorialSolicitudes(token).enqueue(new Callback<HistorialResponse>() {
            @Override
            public void onResponse(Call<HistorialResponse> call, Response<HistorialResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaSolicitudes.clear();
                    if (response.body().getDatos() != null) {
                        listaSolicitudes.addAll(response.body().getDatos());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SolicitudesProveedorActivity.this, "Error al obtener historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistorialResponse> call, Throwable t) {
                Toast.makeText(SolicitudesProveedorActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

