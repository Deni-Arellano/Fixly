package com.example.fixly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificarSesion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarSesion();
    }

    protected void verificarSesion() {
        String className = this.getClass().getSimpleName();
        if (className.equals("MainActivity") || className.contains("Registro")) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        int idUsuario = preferences.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            irALogin();
        }
    }

    protected void irALogin() {
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finishAffinity();
    }

    protected void setupEdgeToEdge(int layoutId) {
        EdgeToEdge.enable(this);
        setContentView(layoutId);

        if (getWindow() != null) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.azul_fixly));
            ViewCompat.getWindowInsetsController(getWindow().getDecorView())
                    .setAppearanceLightStatusBars(false);
        }

        View header = findViewById(R.id.header_container);
        if (header != null) {
            ImageView btnSalir = header.findViewById(R.id.imageView);
            if (btnSalir != null) {
                btnSalir.setOnClickListener(v -> cerrarSesion());
            }
        }

        configurarPaddings(header);
    }

    private void configurarPaddings(View header) {
        View main = findViewById(R.id.main);
        View bottom = findViewById(R.id.bottom_container);
        if (main != null) {
            ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (header != null) {
                    header.setPadding(header.getPaddingLeft(), systemBars.top, header.getPaddingRight(), header.getPaddingBottom());
                    ViewGroup.LayoutParams params = header.getLayoutParams();
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics()) + systemBars.top;
                    header.setLayoutParams(params);
                }
                if (bottom != null) {
                    bottom.setPadding(bottom.getPaddingLeft(), bottom.getPaddingTop(), bottom.getPaddingRight(), systemBars.bottom);
                    ViewGroup.LayoutParams params = bottom.getLayoutParams();
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()) + systemBars.bottom;
                    bottom.setLayoutParams(params);
                }
                return insets;
            });
        }
    }

    protected void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("fixly_sesion", MODE_PRIVATE);
        preferences.edit().clear().apply();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        irALogin();
    }
}