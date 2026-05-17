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

import com.bumptech.glide.Glide;

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

public class MiPerfilProveedorActivity extends BaseActivity {

    private TextView textoNombre, textoCorreo, textoTelefono, textoDescripcion, textoServicios;
    private TextView textoPromedioEstrellas, textoTotalComentarios;
    private ImageView imagenProveedor;
    private RatingBar ratingBar;
    private RecyclerView recyclerComentarios;
    private ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.ver_mi_perfil_proveedor);

        textoNombre = findViewById(R.id.texto_nombre_proveedor);
        textoCorreo = findViewById(R.id.texto_correo_proveedor);
        textoTelefono = findViewById(R.id.texto_telefono_proveedor);
        textoDescripcion = findViewById(R.id.texto_descripcion_proveedor);
        textoServicios = findViewById(R.id.texto_servicios_proveedor);
        textoPromedioEstrellas = findViewById(R.id.texto_promedio_estrellas);
        textoTotalComentarios = findViewById(R.id.texto_total_comentarios);
        imagenProveedor = findViewById(R.id.imagen_proveedor);
        ratingBar = findViewById(R.id.rating_bar);
        recyclerComentarios = findViewById(R.id.recycler_comentarios);

        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentarioAdapter = new ComentarioAdapter(new ArrayList<>());
        recyclerComentarios.setAdapter(comentarioAdapter);

        View btnCambiarContrasena = findViewById(R.id.boton_password);
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, CambiarContrasenaProveedorActivity.class);
                startActivity(intent);
            }
        });

        View btnEditarPerfil = findViewById(R.id.boton_editar);
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilProveedorActivity.this, EditarPerfilProveedorActivity.class);
                startActivity(intent);
            }
        });

        // --- NAVEGACIÓN INFERIOR ---

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MiPerfilProveedorActivity.this, SolicitudesProveedorActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ya estamos en Perfil
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPerfil();
    }

    private void cargarPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPerfilProveedor(token).enqueue(new Callback<PerfilProveedorResponse>() {
            @Override
            public void onResponse(Call<PerfilProveedorResponse> call, Response<PerfilProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarDatos(response.body().getDatos());
                } else {
                    Toast.makeText(MiPerfilProveedorActivity.this, "Error al cargar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilProveedorResponse> call, Throwable t) {
                Toast.makeText(MiPerfilProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilProveedorResponse.DatosProveedor datos) {
        if (datos == null) return;

        if (datos.getUsuario() != null) {
            textoNombre.setText(datos.getUsuario().getNombreCompleto());
            textoCorreo.setText(datos.getUsuario().getCorreo());
            textoTelefono.setText(datos.getUsuario().getTelefono());

            String fotoPerfil = datos.getUsuario().getFotoPerfil();
            if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
                Glide.with(this)
                     .load(fotoPerfil)
                     .placeholder(R.drawable.ic_usuario_placeholder)
                     .into(imagenProveedor);
            }
        }

        if (datos.getPrestador() != null) {
            PrestadorPerfil prestador = datos.getPrestador();
            textoDescripcion.setText(prestador.getDescripcion());

            if (prestador.getCalificaciones() != null) {
                double promedio = prestador.getCalificaciones().getPromedio();
                ratingBar.setRating((float) promedio);
                textoPromedioEstrellas.setText(String.format(Locale.getDefault(), "%.1f", promedio));
                
                if (prestador.getCalificaciones().getComentarios() != null) {
                    int total = prestador.getCalificaciones().getComentarios().size();
                    textoTotalComentarios.setText(String.format(Locale.getDefault(), "(%d comentarios)", total));
                    
                    comentarioAdapter = new ComentarioAdapter(prestador.getCalificaciones().getComentarios());
                    recyclerComentarios.setAdapter(comentarioAdapter);
                } else {
                    textoTotalComentarios.setText("(0 comentarios)");
                }
            }

            if (prestador.getServicios() != null) {
                StringBuilder sb = new StringBuilder();
                for (ServicioPerfil s : prestador.getServicios()) {
                    sb.append("• ").append(s.getNombre()).append("\n");
                }
                textoServicios.setText(sb.toString().trim());
            }
        }
    }
}
