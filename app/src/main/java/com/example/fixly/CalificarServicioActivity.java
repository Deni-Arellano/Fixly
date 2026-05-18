package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixly.models.calificar.CalificarRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalificarServicioActivity extends BaseActivity {

    private TextView txtNombreProveedor;
    private RatingBar ratingBar;
    private EditText inputComentario;
    private Button btnEnviar;

    private int idSolicitud = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.activity_calificar_servicio);

        txtNombreProveedor = findViewById(R.id.texto_nombre_proveedor);
        ratingBar = findViewById(R.id.rating_calificacion);
        inputComentario = findViewById(R.id.input_comentario);
        btnEnviar = findViewById(R.id.boton_enviar_calificacion);

        idSolicitud = getIntent().getIntExtra("id_solicitud", -1);
        String nombreProveedor = getIntent().getStringExtra("nombre_proveedor");

        if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
            if (txtNombreProveedor != null) {
                txtNombreProveedor.setText(nombreProveedor);
            }
        }

        if (idSolicitud == -1) {
            Toast.makeText(this, "ID de solicitud no válido", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (btnEnviar != null) {
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enviarCalificacion();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---
        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                Intent intent = new Intent(CalificarServicioActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(CalificarServicioActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(CalificarServicioActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            });
        }
    }

    private void enviarCalificacion() {
        if (ratingBar == null || inputComentario == null || btnEnviar == null) return;

        float estrellas = ratingBar.getRating();
        if (estrellas == 0) {
            Toast.makeText(this, "Por favor selecciona una calificación de estrellas", Toast.LENGTH_SHORT).show();
            return;
        }

        String comentario = inputComentario.getText().toString().trim();

        CalificarRequest request = new CalificarRequest(estrellas, comentario);

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        btnEnviar.setEnabled(false);

        RetrofitClient.getApiService().calificarServicio(token, idSolicitud, request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CalificarServicioActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CalificarServicioActivity.this, VerSolicitudActivity.class);
                    intent.putExtra("id_solicitud", idSolicitud);
                    startActivity(intent);
                    finish();
                } else {
                    btnEnviar.setEnabled(true);
                    Toast.makeText(CalificarServicioActivity.this, "Error al enviar la calificación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                btnEnviar.setEnabled(true);
                Toast.makeText(CalificarServicioActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
