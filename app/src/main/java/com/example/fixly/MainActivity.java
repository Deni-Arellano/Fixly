package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixly.models.login.LoginRequest;
import com.example.fixly.models.login.LoginResponse;
import com.example.fixly.network.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int originalTopPadding;
    private int originalBottomPadding;
    private EditText inputCorreo, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

        inputCorreo = findViewById(R.id.input_correo_login);
        inputPassword = findViewById(R.id.input_contraseña_login);

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

        // Probar conexión al backend al iniciar
        probarConexionBackend();

        Button btnIngresar = findViewById(R.id.boton_ingresar_login);
        btnIngresar.setOnClickListener(view -> {
            String correo = inputCorreo.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginUsuario(correo, password);
            }
        });

        TextView tvRegistrarse = findViewById(R.id.textview_registrarse_login);
        tvRegistrarse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegistroClienteActivity.class);
            startActivity(intent);
        });
    }

    private void loginUsuario(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        
        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    String mensaje = loginResponse.getMensaje() != null ? loginResponse.getMensaje() : "Bienvenido";
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                    if (loginResponse.getDatos() != null) {
                        guardarDatosSesion(loginResponse.getDatos());

                        int idRol = loginResponse.getDatos().getIdRol();
                        Intent intent;

                        switch (idRol) {
                            case 1:
                                intent = new Intent(MainActivity.this, AdministradorUsuariosActivity.class);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, SolicitudesProveedorActivity.class);
                                break;
                            case 3:
                                intent = new Intent(MainActivity.this, InicioActivity.class);
                                break;
                            default:
                                intent = new Intent(MainActivity.this, InicioActivity.class);
                                break;
                        }
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarDatosSesion(LoginResponse.Datos datos) {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("id_usuario", datos.getIdUsuario());
        editor.putInt("id_rol", datos.getIdRol());
        editor.putString("access_token", datos.getAccessToken());
        editor.apply();
    }

    private void probarConexionBackend() {
        RetrofitClient.getApiService().checkStatus().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Backend conectado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });
    }
}
