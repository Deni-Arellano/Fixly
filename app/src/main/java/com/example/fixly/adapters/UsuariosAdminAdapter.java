package com.example.fixly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fixly.R;
import com.example.fixly.models.administrador.UsuarioAdmin;
import java.util.List;

public class UsuariosAdminAdapter extends RecyclerView.Adapter<UsuariosAdminAdapter.UsuarioViewHolder> {

    private List<UsuarioAdmin> usuarios;

    public UsuariosAdminAdapter(List<UsuarioAdmin> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_admin, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioAdmin usuario = usuarios.get(position);
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno();
        holder.tvNombre.setText(nombreCompleto);
        holder.tvEmail.setText(usuario.getCorreo());
        holder.tvTelefono.setText(usuario.getTelefono());
        holder.tvRol.setText(usuario.getRol());
        
        holder.swEstado.setChecked(usuario.getEstatus() == 1);
        holder.tvEstadoBadge.setText(usuario.getEstatus() == 1 ? "Activo" : "Inactivo");
    }

    @Override
    public int getItemCount() {
        return usuarios != null ? usuarios.size() : 0;
    }

    public void setUsuarios(List<UsuarioAdmin> usuarios) {
        this.usuarios = usuarios;
        notifyDataSetChanged();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEmail, tvTelefono, tvRol, tvEstadoBadge;
        SwitchCompat swEstado;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(holder itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreUsuario);
            tvEmail = itemView.findViewById(R.id.tvEmailUsuario);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoUsuario);
            tvRol = itemView.findViewById(R.id.tvRolUsuario);
            tvEstadoBadge = itemView.findViewById(R.id.tvEstadoBadge);
            swEstado = itemView.findViewById(R.id.swEstadoUsuario);
        }
    }
}
