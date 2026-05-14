package com.example.fixly.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fixly.PerfilProveedorActivity;
import com.example.fixly.R;
import com.example.fixly.models.proveedores.ProveedorCategoria;
import java.util.List;

public class ProveedoresAdapter extends RecyclerView.Adapter<ProveedoresAdapter.ProveedorViewHolder> {

    private List<ProveedorCategoria> proveedores;
    private Context context;

    public ProveedoresAdapter(List<ProveedorCategoria> proveedores, Context context) {
        this.proveedores = proveedores;
        this.context = context;
    }

    public void setProveedores(List<ProveedorCategoria> proveedores) {
        this.proveedores = proveedores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proveedor, parent, false);
        return new ProveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        ProveedorCategoria proveedor = proveedores.get(position);
        holder.txtNombre.setText(proveedor.getNombreCompleto());
        if (proveedor.getDescripcion() != null && !proveedor.getDescripcion().isEmpty()) {
            holder.txtDescripcion.setText(proveedor.getDescripcion());
        } else {
            holder.txtDescripcion.setText("Sin descripción");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PerfilProveedorActivity.class);
            // PASAMOS EL ID DEL PRESTADOR COMO ENTERO
            intent.putExtra("id_prestador", proveedor.getIdPrestador());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return proveedores == null ? 0 : proveedores.size();
    }

    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDescripcion;
        ImageView imgPerfil;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.texto_nombre_proveedor);
            txtDescripcion = itemView.findViewById(R.id.texto_descripcion_proveedor);
            imgPerfil = itemView.findViewById(R.id.imagen_proveedor);
        }
    }
}
