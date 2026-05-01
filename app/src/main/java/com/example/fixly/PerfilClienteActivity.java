package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixly.models.verPerfilCliente.PerfilCliente;
import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilClienteActivity extends BaseActivity {

    private TextView textoNombre, textoCorreo, textoTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.perfil_cliente);

        textoNombre = findViewById(R.id.texto_nombre_cliente);
        textoCorreo = findViewById(R.id.texto_correo_cliente);
        textoTelefono = findViewById(R.id.texto_telefono_cliente);

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPerfil();
    }

    private void cargarPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPerfilCliente(token).enqueue(new Callback<PerfilClienteResponse>() {
            @Override
            public void onResponse(Call<PerfilClienteResponse> call, Response<PerfilClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilCliente perfil = response.body().getDatos();
                    if (perfil != null) {
                        mostrarDatos(perfil);
                    } else {
                        Toast.makeText(PerfilClienteActivity.this, "Datos de perfil vacíos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PerfilClienteActivity.this, "Error al cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilClienteResponse> call, Throwable t) {
                Toast.makeText(PerfilClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilCliente perfil) {
        String nombreCompleto = perfil.getNombreCompleto() != null ? perfil.getNombreCompleto() : "Usuario sin nombre";

        textoNombre.setText(nombreCompleto);
        textoCorreo.setText(perfil.getCorreo() != null ? perfil.getCorreo() : "Sin correo");
        textoTelefono.setText(perfil.getTelefono() != null ? perfil.getTelefono() : "Sin teléfono");
    }
}