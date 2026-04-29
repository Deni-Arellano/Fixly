package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.ServicioPerfil;
import com.example.fixly.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilProveedorActivity extends BaseActivity {

    private EditText editNombre, editApellidoP, editApellidoM, editTelefono, editDescripcion;
    private LinearLayout contenedorServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.editar_perfil_proveedor);


        editNombre = findViewById(R.id.edit_nombre);
        editApellidoP = findViewById(R.id.edit_apellido_p);
        editApellidoM = findViewById(R.id.edit_apellido_m);
        editTelefono = findViewById(R.id.edit_telefono);
        editDescripcion = findViewById(R.id.edit_descripcion);
        contenedorServicios = findViewById(R.id.contenedor_servicios);

        cargarDatosPerfil();



        // --- NAVEGACIÓN INFERIOR ---
        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> {
                Intent intent = new Intent(EditarPerfilProveedorActivity.this, SolicitudesProveedorActivity.class);
                startActivity(intent);
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(EditarPerfilProveedorActivity.this, MiPerfilProveedorActivity.class);
                startActivity(intent);
            });
        }

        // --- HEADER ---
        View btnSalir = findViewById(R.id.imageView);
        if (btnSalir != null) {
            btnSalir.setOnClickListener(v -> {
                Intent intent = new Intent(EditarPerfilProveedorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void cargarDatosPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPerfilProveedor(token).enqueue(new Callback<PerfilProveedorResponse>() {
            @Override
            public void onResponse(Call<PerfilProveedorResponse> call, Response<PerfilProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cargarCampos(response.body().getDatos());
                } else {
                    Toast.makeText(EditarPerfilProveedorActivity.this, "Error al cargar datos del perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilProveedorResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCampos(PerfilProveedorResponse.DatosProveedor datos) {
        if (datos == null) return;


        if (datos.getUsuario() != null) {
            editNombre.setText(datos.getUsuario().getNombre());
            editApellidoP.setText(datos.getUsuario().getApellidoPaterno());
            editApellidoM.setText(datos.getUsuario().getApellidoMaterno());
            editTelefono.setText(datos.getUsuario().getTelefono());
        }


        if (datos.getPrestador() != null) {
            editDescripcion.setText(datos.getPrestador().getDescripcion());


            contenedorServicios.removeAllViews();
            if (datos.getPrestador().getServicios() != null) {
                for (ServicioPerfil servicio : datos.getPrestador().getServicios()) {
                    CheckBox cb = new CheckBox(this);
                    cb.setText(servicio.getNombre());
                    cb.setChecked(true);
                    cb.setEnabled(true);

                    cb.setTextColor(Color.BLACK);

                    int colorAzul = ContextCompat.getColor(this, R.color.azul_fixly);
                    cb.setButtonTintList(ColorStateList.valueOf(colorAzul));

                    contenedorServicios.addView(cb);
                }
            }
        }
    }
}
