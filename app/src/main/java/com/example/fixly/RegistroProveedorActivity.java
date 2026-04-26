package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixly.models.catalogos.Categoria;
import com.example.fixly.models.catalogos.CategoriasResponse;
import com.example.fixly.models.catalogos.Servicio;
import com.example.fixly.models.catalogos.ServiciosResponse;
import com.example.fixly.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroProveedorActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private LinearLayout containerServicios;
    private List<Categoria> listaCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_proveedor);

        // Inicializar vistas
        spinnerCategoria = findViewById(R.id.spinner_registro_proveedor_categoria);
        containerServicios = findViewById(R.id.container_servicios_checkboxes);
        Button btnRegistrarme = findViewById(R.id.button_regiistro_proveedor_registrarme);
        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

        // Configurar Insets
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            if (topContainer != null) {
                topContainer.setPadding(topContainer.getPaddingLeft(), systemBars.top,
                        topContainer.getPaddingRight(), topContainer.getPaddingBottom());
            }
            if (bottomContainer != null) {
                bottomContainer.setPadding(bottomContainer.getPaddingLeft(), bottomContainer.getPaddingTop(),
                        bottomContainer.getPaddingRight(), systemBars.bottom);
            }
            return insets;
        });

        obtenerCategorias();

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria seleccionada = listaCategorias.get(position);
                cargarServicios(seleccionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Botón registrar
        if (btnRegistrarme != null) {
            btnRegistrarme.setOnClickListener(view -> {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroProveedorActivity.this, SolicitudesProveedorActivity.class);
                startActivity(intent);
            });
        }
    }

    private void obtenerCategorias() {
        RetrofitClient.getApiService().getCategorias().enqueue(new Callback<CategoriasResponse>() {
            @Override
            public void onResponse(Call<CategoriasResponse> call, Response<CategoriasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCategorias = response.body().getDatos();
                    ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(RegistroProveedorActivity.this,
                            android.R.layout.simple_spinner_item, listaCategorias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoriasResponse> call, Throwable t) {
                Toast.makeText(RegistroProveedorActivity.this, "Error al cargar categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarServicios(int categoriaId) {
        RetrofitClient.getApiService().getServiciosPorCategoria(categoriaId).enqueue(new Callback<ServiciosResponse>() {
            @Override
            public void onResponse(Call<ServiciosResponse> call, Response<ServiciosResponse> response) {
                containerServicios.removeAllViews();
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Servicio> servicios = response.body().getDatos();
                    for (Servicio servicio : servicios) {
                        CheckBox cb = new CheckBox(RegistroProveedorActivity.this);
                        cb.setText(servicio.getNombre());
                        cb.setTag(servicio.getId());
                        cb.setTextColor(getResources().getColor(android.R.color.white));
                        containerServicios.addView(cb);
                    }
                } else {
                    Toast.makeText(RegistroProveedorActivity.this, "Error al obtener servicios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiciosResponse> call, Throwable t) {
                Toast.makeText(RegistroProveedorActivity.this, "Error de red al cargar servicios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
