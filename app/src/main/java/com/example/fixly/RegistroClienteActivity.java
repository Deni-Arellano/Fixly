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
                    String errorMsg = "Error en el registro";
                    if (response.code() == 409) {
                        errorMsg = "El correo ya está registrado";
                    }
                    Toast.makeText(RegistroClienteActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Toast.makeText(RegistroClienteActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
