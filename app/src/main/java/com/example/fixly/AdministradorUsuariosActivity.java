package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdministradorUsuariosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge(R.layout.activity_admin_usuarios);

        View btnAgregarProveedor = findViewById(R.id.btnAgregarUsuario);
        btnAgregarProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministradorUsuariosActivity.this, RegistroProveedorActivity.class);
                startActivity(intent);
            }
        });

    }
}
