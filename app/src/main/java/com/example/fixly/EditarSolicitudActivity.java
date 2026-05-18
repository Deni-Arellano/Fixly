package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.Map;

import com.example.fixly.models.historial.DetalleSolicitudResponse;
import com.example.fixly.models.historial.EditarSolicitudRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarSolicitudActivity extends BaseActivity {

    private EditText editCalle, editNumExt, editNumInt, editColonia, editCiudad, editCp, editReferencia, editDescripcion;
    private Spinner spinnerEstado;
    private View btnGuardar, btnCancelar;
    private int idSolicitud = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.editar_solicitud);

        editCalle = findViewById(R.id.edit_calle_editar_solicitud);
        editNumExt = findViewById(R.id.edit_num_ext_editar_solicitud);
        editNumInt = findViewById(R.id.edit_num_int_editar_solicitud);
        editColonia = findViewById(R.id.edit_colonia_editar_solicitud);
        editCiudad = findViewById(R.id.edit_ciudad_editar_solicitud);
        spinnerEstado = findViewById(R.id.spinner_estado_editar_solicitud);
        editCp = findViewById(R.id.edit_cp_editar_solicitud);
        editReferencia = findViewById(R.id.edit_referencia_editar_solicitud);
        editDescripcion = findViewById(R.id.edit_descripcion_editar_solicitud);

        btnGuardar = findViewById(R.id.btn_guardar_edicion_solicitud);
        btnCancelar = findViewById(R.id.btn_cancelar_edicion_solicitud);

        idSolicitud = getIntent().getIntExtra("id_solicitud", -1);
        if (idSolicitud != -1) {
            cargarDetalles();
        } else {
            Toast.makeText(this, "ID de solicitud no válido", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarCambios();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---
        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, InicioActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarSolicitudActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void cargarDetalles() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verDetalleSolicitud(token, idSolicitud).enqueue(new Callback<DetalleSolicitudResponse>() {
            @Override
            public void onResponse(Call<DetalleSolicitudResponse> call, Response<DetalleSolicitudResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    DetalleSolicitudResponse.DetalleSolicitud datos = response.body().getDatos();

                    if (editDescripcion != null && datos.getDescripcion() != null) {
                        editDescripcion.setText(datos.getDescripcion());
                    }
                    if (editReferencia != null && datos.getReferencia() != null) {
                        editReferencia.setText(datos.getReferencia());
                    }
                    if (editCalle != null && datos.getCalle() != null) editCalle.setText(datos.getCalle());
                    if (editNumExt != null && datos.getNumeroExterior() != null) editNumExt.setText(datos.getNumeroExterior());
                    if (editNumInt != null && datos.getNumeroInterior() != null) editNumInt.setText(datos.getNumeroInterior());
                    if (editColonia != null && datos.getColonia() != null) editColonia.setText(datos.getColonia());
                    if (editCiudad != null && datos.getCiudad() != null) editCiudad.setText(datos.getCiudad());
                    if (editCp != null && datos.getCodigoPostal() != null) editCp.setText(datos.getCodigoPostal());
                    
                    if (spinnerEstado != null && datos.getEstado() != null) {
                        for (int i = 0; i < spinnerEstado.getCount(); i++) {
                            if (spinnerEstado.getItemAtPosition(i).toString().equalsIgnoreCase(datos.getEstado())) {
                                spinnerEstado.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetalleSolicitudResponse> call, Throwable t) {
                Toast.makeText(EditarSolicitudActivity.this, "Error de conexión al cargar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        String calle = editCalle.getText().toString().trim();
        String numExt = editNumExt.getText().toString().trim();
        String numInt = editNumInt.getText().toString().trim();
        String colonia = editColonia.getText().toString().trim();
        String ciudad = editCiudad.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem().toString();
        String cp = editCp.getText().toString().trim();
        String referencia = editReferencia.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();

        if (calle.isEmpty() || numExt.isEmpty() || colonia.isEmpty() || ciudad.isEmpty() || cp.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        EditarSolicitudRequest request = new EditarSolicitudRequest(
                calle, numExt, numInt, colonia, ciudad, estado, cp, referencia, descripcion
        );

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().editarSolicitud(token, idSolicitud, request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditarSolicitudActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditarSolicitudActivity.this, VerSolicitudActivity.class);
                    intent.putExtra("id_solicitud", idSolicitud);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);

                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(EditarSolicitudActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al actualizar la solicitud";
                            Toast.makeText(EditarSolicitudActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditarSolicitudActivity.this, "Error al procesar validación", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(EditarSolicitudActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "calle":
                if (editCalle != null) editCalle.setError(mensaje);
                break;
            case "numero_exterior":
                if (editNumExt != null) editNumExt.setError(mensaje);
                break;
            case "numero_interior":
                if (editNumInt != null) editNumInt.setError(mensaje);
                break;
            case "colonia":
                if (editColonia != null) editColonia.setError(mensaje);
                break;
            case "ciudad":
                if (editCiudad != null) editCiudad.setError(mensaje);
                break;
            case "estado":
                if (spinnerEstado != null && spinnerEstado.getSelectedView() instanceof TextView) {
                    ((TextView) spinnerEstado.getSelectedView()).setError(mensaje);
                } else {
                    Toast.makeText(this, "Estado: " + mensaje, Toast.LENGTH_SHORT).show();
                }
                break;
            case "codigo_postal":
                if (editCp != null) editCp.setError(mensaje);
                break;
            case "referencia":
                if (editReferencia != null) editReferencia.setError(mensaje);
                break;
            case "descripcion":
                if (editDescripcion != null) editDescripcion.setError(mensaje);
                break;
        }
    }
}
