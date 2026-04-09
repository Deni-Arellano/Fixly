package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int originalTopPadding;
    private int originalBottomPadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);


        originalTopPadding = topContainer.getPaddingTop();
        originalBottomPadding = bottomContainer.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );


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


        Button btnIngresar = findViewById(R.id.boton_login_ingresar);
        btnIngresar.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(intent);
        });


        TextView tvRegistrarse = findViewById(R.id.textview_login_registrarse);
        tvRegistrarse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegistroClienteActivity.class);
            startActivity(intent);
        });
    }
}