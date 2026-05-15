package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.ServicioPerfil;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.models.solicitarServicio.SolicitarServicioRequest;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import android.widget.RadioButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitarServicioActivity extends BaseActivity {

    private EditText inputCalle, inputNumExt, inputNumInt, inputColonia, inputCiudad;
    private EditText inputCp, inputReferencia, inputDescripcion;
    private Spinner spinnerEstado;
    private RadioGroup grupoServicios;
    private Button btnSolicitar;

    private int prestadorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.activity_solicitar_servicio);

        prestadorId = getIntent().getIntExtra("id_proveedor", -1);

        inputCalle = findViewById(R.id.input_calle);
        inputNumExt = findViewById(R.id.input_numero_exterior);
        inputNumInt = findViewById(R.id.input_numero_interior);
        inputColonia = findViewById(R.id.input_colonia);
        inputCiudad = findViewById(R.id.input_ciudad);
        spinnerEstado = findViewById(R.id.spinner_estado);
        inputCp = findViewById(R.id.input_cp);
        inputReferencia = findViewById(R.id.input_referencia);
        inputDescripcion = findViewById(R.id.input_descripcion_servicio);
        grupoServicios = findViewById(R.id.grupo_servicios);
        btnSolicitar = findViewById(R.id.boton_solicitar_servicio);

        if (prestadorId != -1) {
            cargarServicios(prestadorId);
        }

        if (btnSolicitar != null) {
            btnSolicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enviarSolicitud();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SolicitarServicioActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(SolicitarServicioActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }


        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SolicitarServicioActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void cargarServicios(int idProveedor) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPrestador(token, idProveedor).enqueue(new Callback<PerfilProveedorResponse>() {
            @Override
            public void onResponse(Call<PerfilProveedorResponse> call, Response<PerfilProveedorResponse> response) {
                grupoServicios.removeAllViews();
                if (response.isSuccessful() && response.body() != null) {
                    PerfilProveedorResponse.DatosProveedor datos = response.body().getDatos();
                    if (datos != null && datos.getPrestador() != null && datos.getPrestador().getServicios() != null) {
                        List<ServicioPerfil> servicios = datos.getPrestador().getServicios();
                        for (ServicioPerfil servicio : servicios) {
                            RadioButton rb = new RadioButton(SolicitarServicioActivity.this);
                            rb.setText(servicio.getNombre());
                            rb.setId(View.generateViewId());
                            rb.setTag(servicio.getId());
                            rb.setTextColor(getResources().getColor(android.R.color.black));
                            grupoServicios.addView(rb);
                        }
                    }
                } else {
                    Toast.makeText(SolicitarServicioActivity.this, "Error al cargar los servicios del proveedor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilProveedorResponse> call, Throwable t) {
                Toast.makeText(SolicitarServicioActivity.this, "Error de conexión al cargar servicios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarSolicitud() {
        if (prestadorId == -1) {
            Toast.makeText(this, "Error: No se seleccionó un prestador válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        String calle = inputCalle.getText().toString().trim();
        String numExt = inputNumExt.getText().toString().trim();
        String numInt = inputNumInt.getText().toString().trim();
        String colonia = inputColonia.getText().toString().trim();
        String ciudad = inputCiudad.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem() != null ? spinnerEstado.getSelectedItem().toString() : "";
        String cp = inputCp.getText().toString().trim();
        String referencia = inputReferencia.getText().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();

        int servicioId = 0;
        int checkedRadioId = grupoServicios.getCheckedRadioButtonId();
        if (checkedRadioId != -1) {
            RadioButton selectedRb = findViewById(checkedRadioId);
            if (selectedRb != null && selectedRb.getTag() != null) {
                servicioId = (int) selectedRb.getTag();
            }
        }

        if (servicioId == 0) {
            Toast.makeText(this, "Por favor selecciona un servicio", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        SolicitarServicioRequest request = new SolicitarServicioRequest(
                calle, numExt, numInt, colonia, ciudad, estado, cp, referencia, descripcion, servicioId, prestadorId
        );

        RetrofitClient.getApiService().solicitarServicio(token, request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getEstatus()) || response.body().getCodigo() == 200) {
                        Toast.makeText(SolicitarServicioActivity.this, "Solicitud creada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SolicitarServicioActivity.this, response.body().getMensaje() != null ? response.body().getMensaje() : "Error al solicitar servicio", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);

                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(SolicitarServicioActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al solicitar servicio";
                            Toast.makeText(SolicitarServicioActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SolicitarServicioActivity.this, "Error al procesar validación", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(SolicitarServicioActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "calle":
                inputCalle.setError(mensaje);
                break;
            case "numero_exterior":
                inputNumExt.setError(mensaje);
                break;
            case "numero_interior":
                inputNumInt.setError(mensaje);
                break;
            case "colonia":
                inputColonia.setError(mensaje);
                break;
            case "ciudad":
                inputCiudad.setError(mensaje);
                break;
            case "estado":
                if (spinnerEstado.getSelectedView() instanceof TextView) {
                    ((TextView) spinnerEstado.getSelectedView()).setError(mensaje);
                } else {
                    Toast.makeText(this, "Estado: " + mensaje, Toast.LENGTH_SHORT).show();
                }
                break;
            case "codigo_postal":
                inputCp.setError(mensaje);
                break;
            case "referencia":
                inputReferencia.setError(mensaje);
                break;
            case "descripcion":
                inputDescripcion.setError(mensaje);
                break;
            case "servicio_id":
            case "prestador_id":
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
