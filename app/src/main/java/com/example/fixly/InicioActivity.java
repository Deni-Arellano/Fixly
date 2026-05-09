package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioActivity extends BaseActivity {

    private TextView txtHolaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.inicio);

        txtHolaUsuario = findViewById(R.id.txt_hola_usuario);

        obtenerNombreUsuario();

        navegacionBotonServicio(R.id.boton_plomeria_inicio, 1, "Plomería");
        navegacionBotonServicio(R.id.boton_electricidad_inicio, 2, "Electricidad");
        navegacionBotonServicio(R.id.boton_jardineria_inicio, 3, "Jardinería");
        navegacionBotonServicio(R.id.boton_limpieza_inicio, 4, "Limpieza");
        navegacionBotonServicio(R.id.boton_pintura_inicio, 5, "Pintura");
        navegacionBotonServicio(R.id.boton_cerrajeria_inicio, 6, "Cerrajería");
        navegacionBotonServicio(R.id.boton_mecanica_inicio, 7, "Mecánica");
        navegacionBotonServicio(R.id.boton_instalacion_inicio, 8, "Instalación");

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
                // Ya estamos en Inicio
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
    }

    private void obtenerNombreUsuario() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = preferences.getString("access_token", null);

        if (token != null) {
            String authHeader = "Bearer " + token;
            RetrofitClient.getApiService().verPerfilCliente(authHeader).enqueue(new Callback<PerfilClienteResponse>() {
                @Override
                public void onResponse(Call<PerfilClienteResponse> call, Response<PerfilClienteResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                        String nombre = response.body().getDatos().getNombre();
                        txtHolaUsuario.setText("¡Hola " + nombre + "!");
                    }
                }

                @Override
                public void onFailure(Call<PerfilClienteResponse> call, Throwable t) {
                }
            });
        }
    }

    private void navegacionBotonServicio(int idBoton, int idServicio, String nombreServicio) {
        View boton = findViewById(idBoton);
        if (boton != null) {
            boton.setOnClickListener(v -> {
                Intent intent = new Intent(InicioActivity.this, ListaServiciosActivity.class);
                intent.putExtra("id_servicio", idServicio);
                intent.putExtra("nombre_servicio", nombreServicio);
                startActivity(intent);
            });
        }
    }
}
