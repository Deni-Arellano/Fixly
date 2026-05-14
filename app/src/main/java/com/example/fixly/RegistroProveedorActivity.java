package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.fixly.models.registroCliente.RegistroRequest;
import com.example.fixly.models.registroProveedor.RegistroProveedorRequest;
import com.example.fixly.models.registroProveedor.RegistroProveedorResponse;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroProveedorActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private LinearLayout containerServicios;
    private EditText inputNombre, inputApellidoP, inputApellidoM, inputCorreo, inputPassword, inputRepetirPassword, inputTelefono, inputDescripcion;
    private List<Categoria> listaCategorias = new ArrayList<>();
    private final String nombreRegex = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ' .-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_proveedor);

        inputNombre = findViewById(R.id.edit_nombre_registro_proveedor);
        inputApellidoP = findViewById(R.id.edit_apellido_p_registro_proveedor);
        inputApellidoM = findViewById(R.id.edit_apellido_m_registro_proveedor);
        inputCorreo = findViewById(R.id.edittext_regiistro_proveedor_correo);
        inputPassword = findViewById(R.id.edittext_regiistro_proveedor_contraseña);
        inputRepetirPassword = findViewById(R.id.edittext_regiistro_proveedor_repite_contraseña);
        inputTelefono = findViewById(R.id.edittext_regiistro_proveedor_telefono);
        inputDescripcion = findViewById(R.id.edittext_regiistro_proveedor_descripcion);
        
        spinnerCategoria = findViewById(R.id.spinner_registro_proveedor_categoria);
        containerServicios = findViewById(R.id.container_servicios_checkboxes);
        Button btnRegistrarme = findViewById(R.id.button_regiistro_proveedor_registrarme);
        
        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

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

        if (btnRegistrarme != null) {
            btnRegistrarme.setOnClickListener(view -> {
                validarYRegistrar();
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
                }
            }

            @Override
            public void onFailure(Call<ServiciosResponse> call, Throwable t) {}
        });
    }

    private void validarYRegistrar() {
        String nombre = inputNombre.getText().toString().trim();
        String apellidoP = inputApellidoP.getText().toString().trim();
        String apellidoM = inputApellidoM.getText().toString().trim();
        String correo = inputCorreo.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String repetirPassword = inputRepetirPassword.getText().toString().trim();
        String telefono = inputTelefono.getText().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || apellidoP.isEmpty() || correo.isEmpty() || password.isEmpty() || telefono.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean banderaError = false;

        if (nombre.length() < 2) {
            inputNombre.setError("El nombre debe tener al menos 2 caracteres");
            banderaError = true;
        } else if (nombre.length() > 100) {
            inputNombre.setError("El nombre no puede superar los 100 caracteres");
            banderaError = true;
        } else if (!nombre.matches(nombreRegex)) {
            inputNombre.setError("Solo se permiten letras, espacios, acentos, ñ, guion, apóstrofe y punto");
            banderaError = true;
        }

        if (apellidoP.length() < 2) {
            inputApellidoP.setError("El apellido paterno debe tener al menos 2 caracteres");
            banderaError = true;
        } else if (apellidoP.length() > 100) {
            inputApellidoP.setError("El apellido paterno no puede superar los 100 caracteres");
            banderaError = true;
        } else if (!apellidoP.matches(nombreRegex)) {
            inputApellidoP.setError("Solo se permiten letras, espacios, acentos, ñ, guion, apóstrofe y punto");
            banderaError = true;
        }

        if (!apellidoM.isEmpty()) {
            if (apellidoM.length() < 2) {
                inputApellidoM.setError("El apellido materno debe tener al menos 2 caracteres");
                banderaError = true;
            } else if (apellidoM.length() > 100) {
                inputApellidoM.setError("El apellido materno no puede superar los 100 caracteres");
                banderaError = true;
            } else if (!apellidoM.matches(nombreRegex)) {
                inputApellidoM.setError("Solo se permiten letras, espacios, acentos, ñ, guion, apóstrofe y punto");
                banderaError = true;
            }
        }

        if (!password.equals(repetirPassword)) {
            inputRepetirPassword.setError("Las contraseñas no coinciden");
            banderaError = true;
        }

        if (banderaError) {
            Toast.makeText(this, "Errores de validación.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> serviciosSeleccionados = new ArrayList<>();
        for (int i = 0; i < containerServicios.getChildCount(); i++) {
            View v = containerServicios.getChildAt(i);
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    serviciosSeleccionados.add((Integer) cb.getTag());
                }
            }
        }

        if (serviciosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Debes seleccionar al menos un servicio", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria cat = (Categoria) spinnerCategoria.getSelectedItem();
        if (cat == null) {
            Toast.makeText(this, "Selecciona una categoría válida", Toast.LENGTH_SHORT).show();
            return;
        }
        
        RegistroRequest usuario = new RegistroRequest(nombre, apellidoP, apellidoM, correo, password, telefono);
        RegistroProveedorRequest.DatosPrestador datosPrestador = new RegistroProveedorRequest.DatosPrestador(descripcion, cat.getId(), serviciosSeleccionados);
        RegistroProveedorRequest request = new RegistroProveedorRequest(usuario, datosPrestador);

        enviarRegistro(request);
    }

    private void enviarRegistro(RegistroProveedorRequest request) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().registrarPrestador(token, request).enqueue(new Callback<RegistroProveedorResponse>() {
            @Override
            public void onResponse(Call<RegistroProveedorResponse> call, Response<RegistroProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistroProveedorActivity.this, response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistroProveedorActivity.this, AdministradorUsuariosActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroProveedorResponse errorRes = new Gson().fromJson(errorJson, RegistroProveedorResponse.class);
                        
                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(RegistroProveedorActivity.this, "Errores de validación.", Toast.LENGTH_SHORT).show();
                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error en el registro";
                            Toast.makeText(RegistroProveedorActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegistroProveedorActivity.this, "Error al procesar registro", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroProveedorResponse> call, Throwable t) {
                Toast.makeText(RegistroProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "nombre":
                inputNombre.setError(mensaje);
                inputNombre.requestFocus();
                break;
            case "apellido_paterno":
                inputApellidoP.setError(mensaje);
                inputApellidoP.requestFocus();
                break;
            case "apellido_materno":
                inputApellidoM.setError(mensaje);
                inputApellidoM.requestFocus();
                break;
            case "correo":
                inputCorreo.setError(mensaje);
                inputCorreo.requestFocus();
                break;
            case "telefono":
                inputTelefono.setError(mensaje);
                inputTelefono.requestFocus();
                break;
            case "descripcion":
                inputDescripcion.setError(mensaje);
                inputDescripcion.requestFocus();
                break;
            case "password":
                inputPassword.setError(mensaje);
                inputPassword.requestFocus();
                break;
        }
    }
}
