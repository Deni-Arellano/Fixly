package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ListaServiciosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_servicios);

        ImageButton plomero1 = findViewById(R.id.plomero1);
        plomero1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaServiciosActivity.this, PerfilProveedorActivity.class);
                startActivity(intent);
            }
        });
    }
}