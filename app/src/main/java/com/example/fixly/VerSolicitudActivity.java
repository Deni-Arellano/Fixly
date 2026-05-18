package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fixly.models.historial.DetalleSolicitudResponse;
import com.example.fixly.models.calificar.CalificacionStatusResponse;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerSolicitudActivity extends BaseActivity {

    private TextView txtPrestador, txtTelefono, txtDireccion, txtReferencia;
    private TextView txtDescripcion, txtEstado, txtTipo, txtFecha;
    private ImageView imagenPrestador;
    private View btnEditar, btnCancelar, btnCalificar;

    private int idSolicitud = -1;
    private String nombreProveedor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_solicitud);

        txtPrestador = findViewById(R.id.texto_nombre_prestador_ver_solicitud);
        txtTelefono = findViewById(R.id.texto_telefono_prestador_ver_solicitud);
        txtDireccion = findViewById(R.id.direccion_ver_solicitud);
        txtReferencia = findViewById(R.id.referencia_ver_solicitud);
        txtDescripcion = findViewById(R.id.descripcion_ver_solicitud);
        txtEstado = findViewById(R.id.estado_ver_solicitud);
        txtTipo = findViewById(R.id.tipo_ver_solicitud);
        txtFecha = findViewById(R.id.fecha_ver_solicitud);
        imagenPrestador = findViewById(R.id.imagen_prestador_ver_solicitud);

        btnEditar = findViewById(R.id.boton_editar_solicitud);
        btnCancelar = findViewById(R.id.boton_cancelar_solicitud);
        btnCalificar = findViewById(R.id.boton_calificar_proveedor);

        idSolicitud = getIntent().getIntExtra("id_solicitud", -1);

        if (idSolicitud != -1) {
            cargarDetalles();
        } else {
            Toast.makeText(this, "ID de solicitud no válido", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (btnEditar != null) {
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudActivity.this, EditarSolicitudActivity.class);
                    intent.putExtra("id_solicitud", idSolicitud);
                    startActivity(intent);
                }
            });
        }

        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelarSolicitud();
                }
            });
        }

        if (btnCalificar != null) {
            btnCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerSolicitudActivity.this, CalificarServicioActivity.class);
                    intent.putExtra("id_solicitud", idSolicitud);
                    intent.putExtra("nombre_proveedor", nombreProveedor);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(VerSolicitudActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
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
                    Toast.makeText(VerSolicitudActivity.this, "Error al cargar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetalleSolicitudResponse> call, Throwable t) {
                Toast.makeText(VerSolicitudActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelarSolicitud() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().cancelarSolicitud(token, idSolicitud).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerSolicitudActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    cargarDetalles();
                } else {
                    Toast.makeText(VerSolicitudActivity.this, "Error al cancelar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(VerSolicitudActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(DetalleSolicitudResponse.DetalleSolicitud datos) {
        nombreProveedor = datos.getNombrePrestador() != null ? datos.getNombrePrestador() : "Sin asignar";
        if (txtPrestador != null) txtPrestador.setText(nombreProveedor);
        if (txtTelefono != null) txtTelefono.setText(datos.getTelefono() != null ? datos.getTelefono() : "Sin teléfono");
        if (txtDireccion != null) txtDireccion.setText(datos.getDireccion());
        if (txtReferencia != null) txtReferencia.setText(datos.getReferencia());
        if (txtDescripcion != null) txtDescripcion.setText(datos.getDescripcion());
        if (txtEstado != null) txtEstado.setText(datos.getEstadoSolicitud());
        if (txtTipo != null) txtTipo.setText(datos.getNombreServicio());
        if (txtFecha != null) txtFecha.setText(datos.getCreadoEn());

        if (datos.getFotoPerfil() != null && !datos.getFotoPerfil().isEmpty()) {
            if (imagenPrestador != null) {
                Glide.with(this)
                     .load(datos.getFotoPerfil())
                     .placeholder(R.drawable.ic_usuario_placeholder)
                     .into(imagenPrestador);
            }
        }

        if (btnEditar != null) btnEditar.setVisibility(View.GONE);
        if (btnCancelar != null) btnCancelar.setVisibility(View.GONE);
        if (btnCalificar != null) btnCalificar.setVisibility(View.GONE);

        if ("Pendiente".equalsIgnoreCase(datos.getEstadoSolicitud())) {
            if (btnEditar != null) btnEditar.setVisibility(View.VISIBLE);
            if (btnCancelar != null) btnCancelar.setVisibility(View.VISIBLE);
        } else if ("Finalizada".equalsIgnoreCase(datos.getEstadoSolicitud())) {
            verificarSiEstaCalificada();
        }
    }

    private void verificarSiEstaCalificada() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verificarCalificacion(token, idSolicitud).enqueue(new Callback<CalificacionStatusResponse>() {
            @Override
            public void onResponse(Call<CalificacionStatusResponse> call, Response<CalificacionStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    boolean calificada = response.body().getDatos().isCalificada();
                    if (!calificada) {
                        if (btnCalificar != null) btnCalificar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CalificacionStatusResponse> call, Throwable t) {

            }
        });
    }
}
