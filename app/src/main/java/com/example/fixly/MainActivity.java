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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixly.models.login.LoginRequest;
import com.example.fixly.models.login.LoginResponse;
import com.example.fixly.network.RetrofitClient;

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


        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        if (preferences.getInt("id_usuario", -1) != -1) {
            lanzarActividadPorRol(preferences.getInt("id_rol", 3));
            finish();
            return;
        }


        //para no regresar a otras pantallas desde el login
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

        inputCorreo = findViewById(R.id.input_correo_login);
        inputPassword = findViewById(R.id.input_contraseña_login);

        if (topContainer != null && bottomContainer != null) {
            originalTopPadding = topContainer.getPaddingTop();
            originalBottomPadding = bottomContainer.getPaddingBottom();
            ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                topContainer.setPadding(topContainer.getPaddingLeft(), originalTopPadding + systemBars.top, topContainer.getPaddingRight(), topContainer.getPaddingBottom());
                bottomContainer.setPadding(bottomContainer.getPaddingLeft(), bottomContainer.getPaddingTop(), bottomContainer.getPaddingRight(), originalBottomPadding + systemBars.bottom);
                return insets;
            });
        }

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
            startActivity(new Intent(MainActivity.this, RegistroClienteActivity.class));
        });
    }

    private void loginUsuario(String email, String password) {
        RetrofitClient.getApiService().login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if (res.getDatos() != null) {
                        guardarDatosSesion(res.getDatos());
                        lanzarActividadPorRol(res.getDatos().getIdRol());
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void lanzarActividadPorRol(int idRol) {
        Intent intent;
        switch (idRol) {
            case 1: intent = new Intent(this, AdministradorUsuariosActivity.class); break;
            case 2: intent = new Intent(this, SolicitudesProveedorActivity.class); break;
            default: intent = new Intent(this, InicioActivity.class); break;
        }
        startActivity(intent);
    }

    private void guardarDatosSesion(LoginResponse.Datos datos) {
        SharedPreferences.Editor editor = getSharedPreferences("fixly_sesion", MODE_PRIVATE).edit();
        editor.putInt("id_usuario", datos.getIdUsuario());
        editor.putInt("id_rol", datos.getIdRol());
        editor.putString("access_token", datos.getAccessToken());
        editor.apply();
    }

    private void probarConexionBackend() {
        RetrofitClient.getApiService().checkStatus().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
}
