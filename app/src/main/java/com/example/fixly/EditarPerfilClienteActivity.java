package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fixly.models.verPerfilCliente.PerfilCliente;
import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilClienteActivity extends BaseActivity {

    private EditText editNombre, editApellidoPaterno, editApellidoMaterno, editTelefono;
    private ImageView imagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.editar_perfil_cliente);

        editNombre = findViewById(R.id.edit_nombre_editar_perfil_cliente);
        editApellidoPaterno = findViewById(R.id.edit_apellido_p_editar_perfil_cliente);
        editApellidoMaterno = findViewById(R.id.edit_apellido_m_editar_perfil_cliente);
        editTelefono = findViewById(R.id.edit_telefono_editar_perfil_cliente);
        imagenPerfil = findViewById(R.id.imagen_editar_perfil_cliente);

        cargarPerfil();

        View btnCancelar = findViewById(R.id.boton_cancelar_guardar_editar_perfil_cliente);
        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, InicioActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- HEADER ---

        View btnSalir = findViewById(R.id.imageView);
        if (btnSalir != null) {
            btnSalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void cargarPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPerfilCliente(token).enqueue(new Callback<PerfilClienteResponse>() {
            @Override
            public void onResponse(Call<PerfilClienteResponse> call, Response<PerfilClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilCliente datos = response.body().getDatos();
                    if (datos != null) {
                        mostrarDatos(datos);
                    }
                } else {
                    Toast.makeText(EditarPerfilClienteActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilClienteResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilCliente datos) {
        editNombre.setText(datos.getNombre());
        editApellidoPaterno.setText(datos.getApellidoPaterno());
        editApellidoMaterno.setText(datos.getApellidoMaterno());
        editTelefono.setText(datos.getTelefono());

    }
}
