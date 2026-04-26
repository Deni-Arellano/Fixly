package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixly.models.registroCliente.RegistroRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroClienteActivity extends AppCompatActivity {

    private int originalTopPadding;
    private int originalBottomPadding;
    private EditText inputNombre, inputApellidoP, inputApellidoM, inputCorreo, inputPassword, inputRepetirPassword, inputTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_cliente);

        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

        inputNombre = findViewById(R.id.input_nombre_registro_cliente);
        inputApellidoP = findViewById(R.id.input_apellido_paterno_registro_cliente);
        inputApellidoM = findViewById(R.id.input_apellido_materno_registro_cliente);
        inputCorreo = findViewById(R.id.input_correo_registro_cliente);
        inputPassword = findViewById(R.id.input_contraseña_registro_cliente);
        inputRepetirPassword = findViewById(R.id.input_repite_contraseña_registro_cliente);
        inputTelefono = findViewById(R.id.input_telefono_registro_cliente);

        originalTopPadding = topContainer.getPaddingTop();
        originalBottomPadding = bottomContainer.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            topContainer.setPadding(
                    topContainer.getPaddingLeft(),
                    originalTopPadding + systemBars.top,
                    topContainer.getPaddingRight(),
                    topContainer.getPaddingBottom()
            );

            bottomContainer.setPadding(
                    bottomContainer.getPaddingLeft(),
                    bottomContainer.getPaddingTop(),
                    bottomContainer.getPaddingRight(),
                    originalBottomPadding + systemBars.bottom
            );

            return insets;
        });

        Button btnRegistrarme = findViewById(R.id.boton_registrarme_registro_cliente);

        btnRegistrarme.setOnClickListener(view -> {
            validarYRegistrar();
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

        if (nombre.isEmpty() || apellidoP.isEmpty() || correo.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repetirPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        RegistroRequest request = new RegistroRequest(nombre, apellidoP, apellidoM, correo, password, telefono);
        registrarUsuario(request);
    }

    private void registrarUsuario(RegistroRequest request) {
        RetrofitClient.getApiService().registrarCliente(request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegistroResponse res = response.body();
                    Toast.makeText(RegistroClienteActivity.this, res.getMensaje(), Toast.LENGTH_LONG).show();
                    
                    if (res.getCodigo() == 200) {
                        Intent intent = new Intent(RegistroClienteActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        RegistroResponse errorRes = new Gson().fromJson(errorJson, RegistroResponse.class);
                        
                        if (errorRes != null && errorRes.getCodigo() == 422 && errorRes.getErrores() != null) {
                            Toast.makeText(RegistroClienteActivity.this, errorRes.getMensaje(), Toast.LENGTH_LONG).show();

                            for (Map.Entry<String, String> entry : errorRes.getErrores().entrySet()) {
                                mostrarErrorEnInput(entry.getKey(), entry.getValue());
                            }
                        } else {
                            String msg = (errorRes != null) ? errorRes.getMensaje() : "Error en el registro";
                            Toast.makeText(RegistroClienteActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegistroClienteActivity.this, "Error al procesar validación", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(RegistroClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarErrorEnInput(String campo, String mensaje) {
        switch (campo) {
            case "correo":
                inputCorreo.setError(mensaje);
                break;
            case "telefono":
                inputTelefono.setError(mensaje);
                break;
            case "nombre":
                inputNombre.setError(mensaje);
                break;
            case "apellido_paterno":
                inputApellidoP.setError(mensaje);
                break;
            case "password":
                inputPassword.setError(mensaje);
                break;
        }
    }
}
