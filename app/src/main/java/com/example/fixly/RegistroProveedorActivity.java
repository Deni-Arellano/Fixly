package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroProveedorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_proveedor);

        Button btnRegistrarme = findViewById(R.id.button_regiistro_proveedor_registrarme);
        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroProveedorActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });
    }
}