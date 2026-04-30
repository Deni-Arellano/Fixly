package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fixly.models.editarPerfilCliente.EditarPerfilClienteRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.models.verPerfilCliente.PerfilCliente;
import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilClienteActivity extends BaseActivity {

    private EditText editNombre, editApellidoPaterno, editApellidoMaterno, editTelefono;
    private ImageView imagenPerfil;
    private String fotoPerfilActual = null;

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

        Button btnGuardar = findViewById(R.id.boton_guardar_editar_perfil_cliente);
        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarPerfil();
                }
            });
        }

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
        fotoPerfilActual = datos.getFotoPerfil();
    }

    private void actualizarPerfil() {
        String nombre = editNombre.getText().toString().trim();
        String apellidoP = editApellidoPaterno.getText().toString().trim();
        String apellidoM = editApellidoMaterno.getText().toString().trim();
        String telefono = editTelefono.getText().toString().trim();


        EditarPerfilClienteRequest request = new EditarPerfilClienteRequest(
                fotoPerfilActual,
                nombre,
                apellidoP,
                apellidoM,
                telefono
        );

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().editarPerfil(token, request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditarPerfilClienteActivity.this, response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);

                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(EditarPerfilClienteActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al actualizar perfil";
                            Toast.makeText(EditarPerfilClienteActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilClienteActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "nombre":
                editNombre.setError(mensaje);
                break;
            case "apellido_paterno":
                editApellidoPaterno.setError(mensaje);
                break;
            case "apellido_materno":
                editApellidoMaterno.setError(mensaje);
                break;
            case "telefono":
                editTelefono.setError(mensaje);
                break;
        }
    }
}
