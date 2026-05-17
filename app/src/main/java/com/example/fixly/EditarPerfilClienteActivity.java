package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.bumptech.glide.Glide;
import com.example.fixly.models.editarPerfilCliente.EditarPerfilClienteRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.models.verPerfilCliente.PerfilCliente;
import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilClienteActivity extends BaseActivity {

    private EditText editNombre, editApellidoPaterno, editApellidoMaterno, editTelefono;
    private ImageView imagenPerfil;
    private String fotoPerfilActual = null;
    private Uri uriFotoNueva = null;

    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    uriFotoNueva = uri;
                    imagenPerfil.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.editar_perfil_cliente);

        editNombre = findViewById(R.id.edit_nombre_editar_perfil_cliente);
        editApellidoPaterno = findViewById(R.id.edit_apellido_p_editar_perfil_cliente);
        editApellidoMaterno = findViewById(R.id.edit_apellido_m_editar_perfil_cliente);
        editTelefono = findViewById(R.id.edit_telefono_editar_perfil_cliente);
        imagenPerfil = findViewById(R.id.imagen_editar_perfil_cliente);
        View fabCambiarFoto = findViewById(R.id.fab_cambiar_foto);

        View.OnClickListener listenerFoto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagenLauncher.launch("image/*");
            }
        };

        if (imagenPerfil != null) {
            imagenPerfil.setOnClickListener(listenerFoto);
        }
        if (fabCambiarFoto != null) {
            fabCambiarFoto.setOnClickListener(listenerFoto);
        }

        cargarPerfil();

        Button btnGuardar = findViewById(R.id.boton_guardar_editar_perfil_cliente);
        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarPerfil();
                }
            });
        }

        View btnCancelar = findViewById(R.id.boton_cancelar_guardar_editar_perfil_cliente);
        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // --- NAVEGACIÓN INFERIOR ---

        View btnHistorial = findViewById(R.id.historial_barra_navegacion);
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, HistorialSolicitudesActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnInicio = findViewById(R.id.home_barra_navegacion);
        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, InicioActivity.class);
                    startActivity(intent);
                }
            });
        }

        View btnPerfil = findViewById(R.id.perfil_barra_navegacion);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditarPerfilClienteActivity.this, PerfilClienteActivity.class);
                    startActivity(intent);
                }
            });
        }


    }

    private void cargarPerfil() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        RetrofitClient.getApiService().verPerfilCliente(token).enqueue(new Callback<PerfilClienteResponse>() {
            @Override
            public void onResponse(Call<PerfilClienteResponse> call, Response<PerfilClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilCliente datos = response.body().getDatos();
                    if (datos != null) {
                        mostrarDatos(datos);
                    }
                } else {
                    Toast.makeText(EditarPerfilClienteActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilClienteResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(PerfilCliente datos) {
        editNombre.setText(datos.getNombre());
        editApellidoPaterno.setText(datos.getApellidoPaterno());
        editApellidoMaterno.setText(datos.getApellidoMaterno());
        editTelefono.setText(datos.getTelefono());
        fotoPerfilActual = datos.getFotoPerfil();
        
        if (fotoPerfilActual != null && !fotoPerfilActual.isEmpty()) {
            Glide.with(this)
                 .load(fotoPerfilActual)
                 .placeholder(R.drawable.ic_usuario_placeholder)
                 .into(imagenPerfil);
        }
    }

    private void actualizarPerfil() {
        String nombre = editNombre.getText().toString().trim();
        String apellidoP = editApellidoPaterno.getText().toString().trim();
        String apellidoM = editApellidoMaterno.getText().toString().trim();
        String telefono = editTelefono.getText().toString().trim();


        EditarPerfilClienteRequest requestJson = new EditarPerfilClienteRequest(
                fotoPerfilActual,
                nombre,
                apellidoP,
                apellidoM,
                telefono
        );

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        String token = "Bearer " + preferences.getString("access_token", "");

        Callback<RegistroResponse> callbackCompartido = new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditarPerfilClienteActivity.this, response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);

                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(EditarPerfilClienteActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error al actualizar perfil";
                            Toast.makeText(EditarPerfilClienteActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilClienteActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        };

        if (uriFotoNueva != null) {
            // Opción 1: Enviar Multipart
            String usuarioJson = new Gson().toJson(requestJson);
            RequestBody usuarioBody = RequestBody.create(MediaType.parse("application/json"), usuarioJson);

            File archivoFoto = getFileFromUri(uriFotoNueva);
            MultipartBody.Part fotoPart = null;
            if (archivoFoto != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), archivoFoto);
                fotoPart = MultipartBody.Part.createFormData("imagen", archivoFoto.getName(), requestFile);
            }

            RetrofitClient.getApiService().editarPerfilConFoto(token, fotoPart, usuarioBody).enqueue(callbackCompartido);
        } else {
            // No hay foto nueva: Enviar JSON
            RetrofitClient.getApiService().editarPerfil(token, requestJson).enqueue(callbackCompartido);
        }
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "nombre":
                editNombre.setError(mensaje);
                break;
            case "apellido_paterno":
                editApellidoPaterno.setError(mensaje);
                break;
            case "apellido_materno":
                editApellidoMaterno.setError(mensaje);
                break;
            case "telefono":
                editTelefono.setError(mensaje);
                break;
        }
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), "perfil_temp.jpg");
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}