package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fixly.models.cambiarPassword.CambiarPasswordRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContrasenaActivity extends BaseActivity {

    private EditText editContrasenaActual, editNuevaContrasena, editConfirmarContrasena;
    private Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.cambiar_contrasena);

        editContrasenaActual = findViewById(R.id.edit_contrasena_actual);
        editNuevaContrasena = findViewById(R.id.edit_nueva_contrasena);
        editConfirmarContrasena = findViewById(R.id.edit_confirmar_contrasena);
        btnGuardar = findViewById(R.id.boton_guardar_cambiar_contrasena);
        btnCancelar = findViewById(R.id.boton_cancelar_cambiar_contrasena);

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarContrasena();
                }
            });
        }

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
                    Intent intent = new Intent(CambiarContrasenaActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CambiarContrasenaActivity.this, InicioActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CambiarContrasenaActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void cambiarContrasena() {
        String actual = editContrasenaActual.getText().toString().trim();
        String nueva = editNuevaContrasena.getText().toString().trim();
        String confirmacion = editConfirmarContrasena.getText().toString().trim();

        if (actual.isEmpty() || nueva.isEmpty() || confirmacion.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nueva.equals(confirmacion)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        CambiarPasswordRequest request = new CambiarPasswordRequest(actual, nueva, confirmacion);

        RetrofitClient.getApiService().cambiarPassword(token, request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getEstatus()) || response.body().getCodigo() == 200) {
                        Toast.makeText(CambiarContrasenaActivity.this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CambiarContrasenaActivity.this, response.body().getMensaje() != null ? response.body().getMensaje() : "Error al cambiar contraseña", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);
                        
                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(CambiarContrasenaActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al cambiar contraseña";
                            Toast.makeText(CambiarContrasenaActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CambiarContrasenaActivity.this, "Error al procesar validación", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(CambiarContrasenaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "contrasena_actual":
                editContrasenaActual.setError(mensaje);
                break;
            case "contrasena_nueva":
                editNuevaContrasena.setError(mensaje);
                break;
            case "contrasena_confirmacion":
                editConfirmarContrasena.setError(mensaje);
                break;
        }
    }
}
