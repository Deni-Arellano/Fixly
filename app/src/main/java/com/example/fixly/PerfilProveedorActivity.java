package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.ServicioPerfil;
import com.example.fixly.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilProveedorActivity extends BaseActivity {

    private TextView txtNombre, txtTelefono, txtDescripcion, txtServicios, txtSinComentarios;
    private RatingBar ratingBar;
    private ImageView imgPerfil;
    private RecyclerView rvComentarios;
    private int idPrestador;
    private ComentariosAdapter comentariosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.perfil_proveedor);


        txtNombre = findViewById(R.id.texto_nombre_proveedor);
        txtTelefono = findViewById(R.id.texto_telefono_proveedor);
        txtDescripcion = findViewById(R.id.texto_descripcion_proveedor);
        txtServicios = findViewById(R.id.texto_servicios_proveedor);
        txtSinComentarios = findViewById(R.id.texto_sin_comentarios);
        ratingBar = findViewById(R.id.rating_proveedor);
        imgPerfil = findViewById(R.id.imagen_proveedor);
        rvComentarios = findViewById(R.id.lista_comentarios);


        if (rvComentarios != null) {
            rvComentarios.setLayoutManager(new LinearLayoutManager(this));
            comentariosAdapter = new ComentariosAdapter(new ArrayList<>());
            rvComentarios.setAdapter(comentariosAdapter);
            rvComentarios.setNestedScrollingEnabled(false);
        }


        idPrestador = getIntent().getIntExtra("id_prestador", -1);
        
        if (idPrestador != -1) {
            cargarPerfilPrestador(idPrestador);
        } else {
            Toast.makeText(this, "ID de prestador no válido", Toast.LENGTH_SHORT).show();
        }


        View btnSolicitarServicio = findViewById(R.id.boton_solicitar_servicio);
        if (btnSolicitarServicio != null) {
            btnSolicitarServicio.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilProveedorActivity.this, SolicitarServicioActivity.class);
                intent.putExtra("id_prestador", idPrestador);
                startActivity(intent);
            });
        }

        configurarNavegacionInferior();
    }

    private void cargarPerfilPrestador(int id) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = preferences.getString("access_token", "");
        String authHeader = "Bearer " + token;

        RetrofitClient.getApiService().getPerfilPrestador(authHeader, id).enqueue(new Callback<PerfilProveedorResponse>() {
            @Override
            public void onResponse(Call<PerfilProveedorResponse> call, Response<PerfilProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarDatos(response.body().getDatos());
                } else {
                    Toast.makeText(PerfilProveedorActivity.this, "Error al obtener la información", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilProveedorResponse> call, Throwable t) {
                Toast.makeText(PerfilProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilProveedorResponse.DatosProveedor datos) {
        if (datos == null) return;

        if (datos.getUsuario() != null) {
            txtNombre.setText(datos.getUsuario().getNombreCompleto());
            txtTelefono.setText(datos.getUsuario().getTelefono());
        }

        if (datos.getPrestador() != null) {
            txtDescripcion.setText(datos.getPrestador().getDescripcion());
            

            if (datos.getPrestador().getCalificaciones() != null) {
                float promedio = (float) datos.getPrestador().getCalificaciones().getPromedio();
                ratingBar.setRating(promedio);
                
                List<String> comentarios = datos.getPrestador().getCalificaciones().getComentarios();
                if (comentarios != null && !comentarios.isEmpty()) {
                    txtSinComentarios.setVisibility(View.GONE);
                    comentariosAdapter.actualizarComentarios(comentarios);
                } else {
                    txtSinComentarios.setVisibility(View.VISIBLE);
                }
            }

            List<ServicioPerfil> servicios = datos.getPrestador().getServicios();
            if (servicios != null && !servicios.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ServicioPerfil s : servicios) {
                    sb.append("• ").append(s.getNombre()).append("\n");
                }
                txtServicios.setText(sb.toString().trim());
            }
        }
    }

    private void configurarNavegacionInferior() {
        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) btnHistorial.setOnClickListener(v -> startActivity(new Intent(this, HistorialSolicitudesActivity.class)));
        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) btnInicio.setOnClickListener(v -> startActivity(new Intent(this, InicioActivity.class)));
        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilClienteActivity.class)));
    }


    private static class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ViewHolder> {
        private List<String> lista;

        ComentariosAdapter(List<String> lista) { this.lista = lista; }

        void actualizarComentarios(List<String> nuevaLista) {
            this.lista = nuevaLista;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.contenido.setText(lista.get(position));
            holder.nombre.setText("Anónimo");
        }

        @Override
        public int getItemCount() { return lista.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView nombre, contenido;
            ViewHolder(View itemView) {
                super(itemView);
                nombre = itemView.findViewById(R.id.texto_nombre_comentario);
                contenido = itemView.findViewById(R.id.texto_contenido_comentario);
            }
        }
    }
}
