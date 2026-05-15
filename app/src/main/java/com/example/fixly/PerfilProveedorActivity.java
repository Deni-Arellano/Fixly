package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixly.adapters.ComentarioAdapter;
import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.PrestadorPerfil;
import com.example.fixly.models.verPerfilProveedor.ServicioPerfil;
import com.example.fixly.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilProveedorActivity extends BaseActivity {

    private TextView textoNombre, textoTelefono, textoDescripcion, textoServicios;
    private TextView textoPromedioEstrellas, textoTotalComentarios;
    private ImageView imagenProveedor;
    private RatingBar ratingBar;
    private RecyclerView recyclerComentarios;
    private ComentarioAdapter comentarioAdapter;
    private int idProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.perfil_proveedor);


        textoNombre = findViewById(R.id.texto_nombre_proveedor);
        textoTelefono = findViewById(R.id.texto_telefono_proveedor);
        textoDescripcion = findViewById(R.id.texto_descripcion_proveedor);
        textoServicios = findViewById(R.id.texto_servicios_proveedor);
        

        textoPromedioEstrellas = findViewById(R.id.texto_promedio_estrellas);
        textoTotalComentarios = findViewById(R.id.texto_total_comentarios);
        imagenProveedor = findViewById(R.id.imagen_proveedor);
        ratingBar = findViewById(R.id.rating_bar);
        recyclerComentarios = findViewById(R.id.recycler_comentarios);

        if (recyclerComentarios != null) {
            recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
            comentarioAdapter = new ComentarioAdapter(new ArrayList<>());
            recyclerComentarios.setAdapter(comentarioAdapter);
        }


        idProveedor = getIntent().getIntExtra("id_proveedor", -1);

        // SOLICITAR SERVICIO
        View btnSolicitarServicio = findViewById(R.id.boton_solicitar_servicio);
        if (btnSolicitarServicio != null) {
            btnSolicitarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PerfilProveedorActivity.this, SolicitarServicioActivity.class);
                    intent.putExtra("id_proveedor", idProveedor);
                    startActivity(intent);
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---
        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilProveedorActivity.this, InicioActivity.class);
                startActivity(intent);
            });
        }

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilProveedorActivity.this, HistorialSolicitudesActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilProveedorActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
            });
        }

        if (idProveedor != -1) {
            cargarPerfil();
        } else {
            Toast.makeText(this, "ID de proveedor no válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");


        RetrofitClient.getApiService().verPrestador(token, idProveedor).enqueue(new Callback<PerfilProveedorResponse>() {
            @Override
            public void onResponse(Call<PerfilProveedorResponse> call, Response<PerfilProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarDatos(response.body().getDatos());
                } else {
                    Toast.makeText(PerfilProveedorActivity.this, "Error al obtener datos del prestador", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilProveedorResponse> call, Throwable t) {
                Toast.makeText(PerfilProveedorActivity.this, "Fallo en la comunicación con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilProveedorResponse.DatosProveedor datos) {
        if (datos == null) return;


        if (datos.getUsuario() != null) {
            if (textoNombre != null) textoNombre.setText(datos.getUsuario().getNombreCompleto());
            if (textoTelefono != null) textoTelefono.setText(datos.getUsuario().getTelefono());
        }


        if (datos.getPrestador() != null) {
            PrestadorPerfil prestador = datos.getPrestador();
            if (textoDescripcion != null) textoDescripcion.setText(prestador.getDescripcion());


            if (prestador.getCalificaciones() != null) {
                double promedio = prestador.getCalificaciones().getPromedio();
                if (ratingBar != null) ratingBar.setRating((float) promedio);
                if (textoPromedioEstrellas != null) {
                    textoPromedioEstrellas.setText(String.format(Locale.getDefault(), "%.1f", promedio));
                }

                if (prestador.getCalificaciones().getComentarios() != null) {
                    int total = prestador.getCalificaciones().getComentarios().size();
                    if (textoTotalComentarios != null) {
                        textoTotalComentarios.setText(String.format(Locale.getDefault(), "(%d comentarios)", total));
                    }


                    comentarioAdapter = new ComentarioAdapter(prestador.getCalificaciones().getComentarios());
                    if (recyclerComentarios != null) {
                        recyclerComentarios.setAdapter(comentarioAdapter);
                    }
                }
            }


            if (prestador.getServicios() != null && textoServicios != null) {
                StringBuilder sb = new StringBuilder();
                for (ServicioPerfil s : prestador.getServicios()) {
                    sb.append("• ").append(s.getNombre()).append("\n");
                }
                textoServicios.setText(sb.toString().trim());
            }
        }
    }
}
