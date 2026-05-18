package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fixly.adapters.UsuariosAdminAdapter;
import com.example.fixly.models.administrador.UpdateEstadoRequest;
import com.example.fixly.models.administrador.UsuarioAdmin;
import com.example.fixly.models.administrador.UsuariosAdminResponse;
import com.example.fixly.network.RetrofitClient;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdministradorUsuariosActivity extends BaseActivity implements UsuariosAdminAdapter.OnUserStatusChangeListener {

    private RecyclerView rvUsuarios;
    private UsuariosAdminAdapter adapter;
    private List<UsuarioAdmin> listaUsuarios = new ArrayList<>();
    private EditText etBuscarUsuario;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.activity_admin_usuarios);


        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuariosAdminAdapter(listaUsuarios, this);
        rvUsuarios.setAdapter(adapter);
        

        rvUsuarios.setVisibility(View.VISIBLE);
        findViewById(R.id.item_usuario_preview).setVisibility(View.GONE);


        cargarUsuarios();


        etBuscarUsuario = findViewById(R.id.etBuscarUsuario);
        if (etBuscarUsuario != null) {
            etBuscarUsuario.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }

                    searchRunnable = () -> {
                        String query = s.toString().trim();
                        if (query.isEmpty()) {
                            cargarUsuarios();
                        } else {
                            buscarUsuarios(query);
                        }
                    };


                    searchHandler.postDelayed(searchRunnable, 500);
                }
            });
        }

        // Botón agregar
        View btnAgregarProveedor = findViewById(R.id.btnAgregarUsuario);
        if (btnAgregarProveedor != null) {
            btnAgregarProveedor.setOnClickListener(v -> {
                startActivity(new Intent(AdministradorUsuariosActivity.this, RegistroProveedorActivity.class));
            });
        }
    }

    private void cargarUsuarios() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().getUsuariosAdmin(token).enqueue(new Callback<UsuariosAdminResponse>() {
            @Override
            public void onResponse(Call<UsuariosAdminResponse> call, Response<UsuariosAdminResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios = response.body().getDatos();
                    adapter.setUsuarios(listaUsuarios);
                } else {
                    Toast.makeText(AdministradorUsuariosActivity.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuariosAdminResponse> call, Throwable t) {
                Toast.makeText(AdministradorUsuariosActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarUsuarios(String query) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().buscarUsuariosAdmin(token, query).enqueue(new Callback<UsuariosAdminResponse>() {
            @Override
            public void onResponse(Call<UsuariosAdminResponse> call, Response<UsuariosAdminResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios = response.body().getDatos();
                    if (listaUsuarios != null) {
                        adapter.setUsuarios(listaUsuarios);
                    } else {
                        adapter.setUsuarios(new ArrayList<>());
                    }
                } else {
                    adapter.setUsuarios(new ArrayList<>());
                    String errorMessage = "Error en la búsqueda";
                    if (response.code() == 404) {
                        try {
                            if (response.errorBody() != null) {
                                org.json.JSONObject errorObj = new org.json.JSONObject(response.errorBody().string());
                                errorMessage = errorObj.optString("mensaje", "Usuario no encontrado");
                            } else {
                                errorMessage = "Usuario no encontrado";
                            }
                        } catch (Exception e) {
                            errorMessage = "Usuario no encontrado";
                        }
                    }
                    Toast.makeText(AdministradorUsuariosActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuariosAdminResponse> call, Throwable t) {
                Toast.makeText(AdministradorUsuariosActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStatusChange(UsuarioAdmin usuario, boolean isChecked) {

        int nuevoEstadoId = isChecked ? 1 : 2;
        
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        UpdateEstadoRequest request = new UpdateEstadoRequest(nuevoEstadoId);

        RetrofitClient.getApiService().updateEstadoUsuario(token, usuario.getId(), request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdministradorUsuariosActivity.this, "Estado actualizado", Toast.LENGTH_SHORT).show();

                    cargarUsuarios();
                } else {
                    Toast.makeText(AdministradorUsuariosActivity.this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();

                    cargarUsuarios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AdministradorUsuariosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            }
        });
    }
}
