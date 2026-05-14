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
    private OnUserStatusChangeListener listener;

    public interface OnUserStatusChangeListener {
        void onStatusChange(UsuarioAdmin usuario, boolean isChecked);
    }

    public UsuariosAdminAdapter(List<UsuarioAdmin> usuarios, OnUserStatusChangeListener listener) {
        this.usuarios = usuarios;
        this.listener = listener;
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
        
        holder.tvNombre.setText(usuario.getNombreCompleto());
        holder.tvEmail.setText(usuario.getCorreo());
        holder.tvTelefono.setText(usuario.getTelefono());
        holder.tvRol.setText(usuario.getRol());
        
        // Evitar que el listener se dispare al setear el valor inicial
        holder.swEstado.setOnCheckedChangeListener(null);
        holder.swEstado.setChecked(usuario.getEstadoId() == 1);
        holder.tvEstadoBadge.setText(usuario.getEstado());
        
        if (usuario.getEstadoId() == 1) {
             holder.tvEstadoBadge.setBackgroundResource(R.drawable.bg_estado_activo);
        } else {
             holder.tvEstadoBadge.setBackgroundResource(R.drawable.bg_estado_suspendido);
        }

        // Listener para el cambio de estado
        holder.swEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStatusChange(usuario, isChecked);
            }
        });
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
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreUsuario);
            tvEmail = itemView.findViewById(R.id.tvEmailUsuario);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoUsuario);
            tvRol = itemView.findViewById(R.id.tvRolUsuario);
            tvEstadoBadge = itemView.findViewById(R.id.tvEstadoBadge);
            swEstado = itemView.findViewById(R.id.swEstadoUsuario);
        }
    }
}
