package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixly.models.historial.DetalleSolicitudResponse;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerSolicitudProveedorActivity extends BaseActivity {

    private TextView txtTipo, txtDescripcion, txtDireccion, txtReferencia, txtEstado, txtFecha;
    private View btnAceptar, btnFinalizar;

    private int idSolicitud = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_solicitud_proveedor);

        txtTipo = findViewById(R.id.tipo_ver_solicitud_proveedor);
        txtDescripcion = findViewById(R.id.descripcion_ver_solicitud_proveedor);
        txtDireccion = findViewById(R.id.direccion_ver_solicitud_proveedor);
        txtReferencia = findViewById(R.id.referencia_ver_solicitud_proveedor);
        txtEstado = findViewById(R.id.estado_ver_solicitud_proveedor);
        txtFecha = findViewById(R.id.fecha_ver_solicitud_proveedor);

        btnAceptar = findViewById(R.id.boton_aceptar_solicitud);
        btnFinalizar = findViewById(R.id.boton_finalizar_servicio);

        idSolicitud = getIntent().getIntExtra("id_solicitud", -1);

        if (idSolicitud != -1) {
            cargarDetalles();
        } else {
            Toast.makeText(this, "ID de solicitud no válido", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (btnAceptar != null) {
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aceptarSolicitud();
                }
            });
        }

        if (btnFinalizar != null) {
            btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarSolicitud();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudProveedorActivity.this, SolicitudesProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudProveedorActivity.this, MiPerfilProveedorActivity.class);
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
                    mostrarDatos(response.body().getDatos());
                } else {
                    Toast.makeText(VerSolicitudProveedorActivity.this, "Error al cargar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetalleSolicitudResponse> call, Throwable t) {
                Toast.makeText(VerSolicitudProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(DetalleSolicitudResponse.DetalleSolicitud datos) {
        if (txtTipo != null) txtTipo.setText(datos.getNombreServicio());
        if (txtDescripcion != null) txtDescripcion.setText(datos.getDescripcion());
        if (txtDireccion != null) txtDireccion.setText(datos.getDireccion());
        if (txtReferencia != null) txtReferencia.setText(datos.getReferencia());
        if (txtEstado != null) txtEstado.setText(datos.getEstadoSolicitud());
        if (txtFecha != null) txtFecha.setText(datos.getCreadoEn());

        if (btnAceptar != null) btnAceptar.setVisibility(View.GONE);
        if (btnFinalizar != null) btnFinalizar.setVisibility(View.GONE);

        if ("Pendiente".equalsIgnoreCase(datos.getEstadoSolicitud())) {
            if (btnAceptar != null) btnAceptar.setVisibility(View.VISIBLE);
        } else if ("Aceptada".equalsIgnoreCase(datos.getEstadoSolicitud())) {
            if (btnFinalizar != null) btnFinalizar.setVisibility(View.VISIBLE);
        }
    }

    private void aceptarSolicitud() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().aceptarSolicitud(token, idSolicitud).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerSolicitudProveedorActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    cargarDetalles();
                } else {
                    Toast.makeText(VerSolicitudProveedorActivity.this, "Error al aceptar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(VerSolicitudProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizarSolicitud() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().finalizarSolicitud(token, idSolicitud).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerSolicitudProveedorActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    cargarDetalles();
                } else {
                    Toast.makeText(VerSolicitudProveedorActivity.this, "Error al finalizar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(VerSolicitudProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
