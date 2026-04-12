package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroClienteActivity extends AppCompatActivity {

    private int originalTopPadding;
    private int originalBottomPadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_cliente);

        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

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
        if (btnRegistrarme != null) {
            btnRegistrarme.setOnClickListener(view -> {
                Intent intent = new Intent(RegistroClienteActivity.this, RegistroProveedorActivity.class);
                startActivity(intent);
            });
        }
    }
}
