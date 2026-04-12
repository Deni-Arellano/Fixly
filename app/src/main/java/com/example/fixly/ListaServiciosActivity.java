package com.example.fixly;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

public class ListaServiciosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEdgeToEdge(R.layout.lista_servicios);

        // Reference the RecyclerView defined in lista_servicios.xml
        RecyclerView recyclerView = findViewById(R.id.lista_proveedores);

        // TODO: Set up the RecyclerView with an adapter to show the list of providers
    }
}
