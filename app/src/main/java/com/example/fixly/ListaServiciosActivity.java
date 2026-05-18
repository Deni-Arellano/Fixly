package com.example.fixly;

import android.os.Bundle;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fixly.adapters.ProveedoresAdapter;
import com.example.fixly.models.proveedores.ProveedorCategoria;
import com.example.fixly.models.proveedores.ProveedoresCategoriaResponse;
import com.example.fixly.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaServiciosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.lista_servicios);


        int idServicio = getIntent().getIntExtra("id_servicio", -1);
        String nombreServicio = getIntent().getStringExtra("nombre_servicio");

        TextView tvTitulo = findViewById(R.id.titulo_servicio);
        if (tvTitulo != null && nombreServicio != null) {
            tvTitulo.setText("¿Necesitas servicio de " + nombreServicio + "?");
        }


        RecyclerView recyclerView = findViewById(R.id.lista_proveedores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProveedoresAdapter adapter = new ProveedoresAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);


        View tarjeta = findViewById(R.id.item_proveedor);
        if (tarjeta != null) {
            tarjeta.setVisibility(View.GONE);
        }

        if (idServicio != -1) {
            SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
            String token = "Bearer " + preferences.getString("access_token", "");

            RetrofitClient.getApiService().getProveedoresPorCategoria(token, idServicio).enqueue(new Callback<ProveedoresCategoriaResponse>() {
                @Override
                public void onResponse(Call<ProveedoresCategoriaResponse> call, Response<ProveedoresCategoriaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ProveedorCategoria> proveedores = response.body().getDatos();
                        if (proveedores != null) {
                            adapter.setProveedores(proveedores);
                        }
                    } else {
                        adapter.setProveedores(new ArrayList<>());
                        String errorMessage = "Error al cargar proveedores";
                        if (response.code() == 404) {
                            try {
                                if (response.errorBody() != null) {
                                    org.json.JSONObject errorObj = new org.json.JSONObject(response.errorBody().string());
                                    errorMessage = errorObj.optString("mensaje", "No hay prestadores disponibles");
                                } else {
                                    errorMessage = "No hay prestadores disponibles";
                                }
                            } catch (Exception e) {
                                errorMessage = "No hay prestadores disponibles";
                            }
                        }
                        Toast.makeText(ListaServiciosActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProveedoresCategoriaResponse> call, Throwable t) {
                    Toast.makeText(ListaServiciosActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
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
