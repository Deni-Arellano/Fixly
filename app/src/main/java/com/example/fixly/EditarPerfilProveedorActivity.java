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

import com.example.fixly.models.catalogos.Servicio;
import com.example.fixly.models.catalogos.ServiciosResponse;
import com.example.fixly.models.editarPerfilProveedor.EditarPerfilProveedorRequest;
import com.example.fixly.models.registroProveedor.RegistroProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;
import com.example.fixly.models.verPerfilProveedor.ServicioPerfil;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilProveedorActivity extends BaseActivity {

    private EditText editNombre, editApellidoP, editApellidoM, editTelefono, editDescripcion;
    private LinearLayout contenedorServicios;
    private String fotoPerfilActual = null;
    private final String nombreRegex = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ' .-]+$";
    private final String telefonoRegex = "^[0-9]{10,15}$";

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


        findViewById(R.id.boton_guardar).setOnClickListener(v -> {
            validarYGuardar();
        });


        findViewById(R.id.boton_cancelar).setOnClickListener(v -> {
            finish();
        });

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
            fotoPerfilActual = datos.getUsuario().getFotoPerfil();
        }

        if (datos.getPrestador() != null) {
            editDescripcion.setText(datos.getPrestador().getDescripcion());


            if (datos.getPrestador().getServicios() != null && !datos.getPrestador().getServicios().isEmpty()) {
                int categoriaId = datos.getPrestador().getServicios().get(0).getIdCategoria();

                Set<Integer> serviciosActualesIds = new HashSet<>();
                for (ServicioPerfil s : datos.getPrestador().getServicios()) {
                    serviciosActualesIds.add(s.getId());
                }

                cargarTodosLosServiciosDeCategoria(categoriaId, serviciosActualesIds);
            }
        }
    }

    private void cargarTodosLosServiciosDeCategoria(int categoriaId, Set<Integer> serviciosActualesIds) {
        RetrofitClient.getApiService().getServiciosPorCategoria(categoriaId).enqueue(new Callback<ServiciosResponse>() {
            @Override
            public void onResponse(Call<ServiciosResponse> call, Response<ServiciosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contenedorServicios.removeAllViews();
                    for (Servicio servicio : response.body().getDatos()) {
                        CheckBox cb = new CheckBox(EditarPerfilProveedorActivity.this);
                        cb.setText(servicio.getNombre());
                        cb.setTag(servicio.getId());

                        // aqui ando marcando el servicio que tine el proveedor
                        if (serviciosActualesIds.contains(servicio.getId())) {
                            cb.setChecked(true);
                        }

                        cb.setTextColor(Color.BLACK);
                        int colorAzul = ContextCompat.getColor(EditarPerfilProveedorActivity.this, R.color.azul_fixly);
                        cb.setButtonTintList(ColorStateList.valueOf(colorAzul));
                        contenedorServicios.addView(cb);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiciosResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilProveedorActivity.this, "Error al cargar catálogo de servicios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarYGuardar() {
        String nombre = editNombre.getText().toString().trim();
        String apellidoP = editApellidoP.getText().toString().trim();
        String apellidoM = editApellidoM.getText().toString().trim();
        String telefono = editTelefono.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || apellidoP.isEmpty() || telefono.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean banderitaError = false;

        if (nombre.length() < 2 || nombre.length() > 100 || !nombre.matches(nombreRegex)) {
            editNombre.setError("Nombre inválido (Mín. 2 letras)");
            if (!nombre.matches(nombreRegex)) editNombre.setError("Solo se permiten letras y acentos");
            banderitaError = true;
        }

        if (apellidoP.length() < 2 || apellidoP.length() > 100 || !apellidoP.matches(nombreRegex)) {
            editApellidoP.setError("Apellido inválido");
            if (!apellidoP.matches(nombreRegex)) editApellidoP.setError("Solo se permiten letras y acentos");
            banderitaError = true;
        }

        if (!apellidoM.isEmpty() && (!apellidoM.matches(nombreRegex) || apellidoM.length() < 2)) {
            editApellidoM.setError("Apellido materno inválido");
            banderitaError = true;
        }

        if (!telefono.matches(telefonoRegex)) {
            editTelefono.setError("El teléfono debe contener entre 10 y 15 dígitos");
            banderitaError = true;
        }

        if (banderitaError) {
            Toast.makeText(this, "Errores de validación.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> serviciosSeleccionados = new ArrayList<>();
        for (int i = 0; i < contenedorServicios.getChildCount(); i++) {
            View v = contenedorServicios.getChildAt(i);
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

        EditarPerfilProveedorRequest.UsuarioEdit usuarioEdit = new EditarPerfilProveedorRequest.UsuarioEdit(fotoPerfilActual, nombre, apellidoP, apellidoM, telefono);
        EditarPerfilProveedorRequest.DatosPrestadorEdit prestadorEdit = new EditarPerfilProveedorRequest.DatosPrestadorEdit(descripcion, serviciosSeleccionados);
        EditarPerfilProveedorRequest request = new EditarPerfilProveedorRequest(usuarioEdit, prestadorEdit);

        enviarActualizacion(request);
    }

    private void enviarActualizacion(EditarPerfilProveedorRequest request) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().editarPerfilProveedor(token, request).enqueue(new Callback<RegistroProveedorResponse>() {
            @Override
            public void onResponse(Call<RegistroProveedorResponse> call, Response<RegistroProveedorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditarPerfilProveedorActivity.this, response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroProveedorResponse errorRes = new Gson().fromJson(errorJson, RegistroProveedorResponse.class);

                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(EditarPerfilProveedorActivity.this, "Errores de validación.", Toast.LENGTH_SHORT).show();
                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al actualizar perfil";
                            Toast.makeText(EditarPerfilProveedorActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilProveedorActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroProveedorResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilProveedorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        String msgLower = mensaje.toLowerCase();

        if (campo.equals("usuario")) {
            if (msgLower.contains("teléfono") || msgLower.contains("10 y 15 dígitos")) {
                editTelefono.setError(mensaje);
            } else if (msgLower.contains("apellido paterno") || (msgLower.contains("apellido") && msgLower.contains("paterno"))) {
                editApellidoP.setError(mensaje);
            } else if (msgLower.contains("apellido materno") || (msgLower.contains("apellido") && msgLower.contains("materno"))) {
                editApellidoM.setError(mensaje);
            } else if (msgLower.contains("nombre") && !msgLower.contains("apellido")) {
                editNombre.setError(mensaje);
            } else if (msgLower.contains("letras") || msgLower.contains("espacios")) {
                editNombre.setError(mensaje);
            }
        } else if (campo.equals("datos_prestador")) {
            if (msgLower.contains("descripción") || msgLower.contains("obligatoria")) {
                editDescripcion.setError(mensaje);
            }
        }
    }
}
