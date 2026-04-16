package com.example.fixly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroProveedorActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private LinearLayout containerServicios;
    private Map<String, String[]> datosServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_proveedor);

        // Inicializar vistas
        spinnerCategoria = findViewById(R.id.spinner_registro_proveedor_categoria);
        containerServicios = findViewById(R.id.container_servicios_checkboxes);
        Button btnRegistrarme = findViewById(R.id.button_regiistro_proveedor_registrarme);
        View main = findViewById(R.id.main);
        View topContainer = findViewById(R.id.top_container);
        View bottomContainer = findViewById(R.id.bottom_container);

        // Configurar Insets
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            if (topContainer != null) {
                topContainer.setPadding(topContainer.getPaddingLeft(), systemBars.top,
                        topContainer.getPaddingRight(), topContainer.getPaddingBottom());
            }
            if (bottomContainer != null) {
                bottomContainer.setPadding(bottomContainer.getPaddingLeft(), bottomContainer.getPaddingTop(),
                        bottomContainer.getPaddingRight(), systemBars.bottom);
            }
            return insets;
        });

        // 1. Preparar datos de ejemplo (Esto vendrá de tu BD después)
        prepararDatosEjemplo();

        // 2. Configurar el Spinner de Categorías
        List<String> categorias = new ArrayList<>(datosServicios.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // 3. Listener para cambios en la categoría
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = categorias.get(position);
                actualizarServicios(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Botón registrar
        if (btnRegistrarme != null) {
            btnRegistrarme.setOnClickListener(view -> {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroProveedorActivity.this, SolicitudesProveedorActivity.class);
                startActivity(intent);
            });
        }
    }

    private void prepararDatosEjemplo() {
        datosServicios = new HashMap<>();
        datosServicios.put("Jardinería", new String[]{
                "Corte de césped", "Poda de árboles", "Mantenimiento de jardín",
                "Diseño de jardín", "Sistema de riego", "Control de plagas"
        });
        datosServicios.put("Plomería", new String[]{
                "Fugas de agua", "Instalación de sanitarios", "Destape de drenajes", "Reparación de boilers"
        });
        datosServicios.put("Electricidad", new String[]{
                "Cortocircuitos", "Instalación de lámparas", "Cableado general", "Tableros eléctricos"
        });
    }

    private void actualizarServicios(String categoria) {
        if (containerServicios == null) return;
        
        containerServicios.removeAllViews(); // Limpiar checkboxes anteriores

        String[] servicios = datosServicios.get(categoria);
        if (servicios != null) {
            for (String servicio : servicios) {
                CheckBox cb = new CheckBox(this);
                cb.setText(servicio);
                cb.setTextColor(getResources().getColor(android.R.color.white));
                containerServicios.addView(cb);
            }
        }
    }
}
