package com.example.fixly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fixly.R;
import com.example.fixly.models.verPerfilProveedor.PrestadorPerfil;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {
    private List<PrestadorPerfil.ComentarioPerfil> comentarios;

    public ComentarioAdapter(List<PrestadorPerfil.ComentarioPerfil> comentarios) {
        this.comentarios = comentarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrestadorPerfil.ComentarioPerfil comentario = comentarios.get(position);
        holder.textoContenido.setText(comentario.getComentario());
        holder.textoNombre.setText("Cliente (Valoración: " + comentario.getEstrellas() + "★)");
    }

    @Override
    public int getItemCount() {
        return comentarios != null ? comentarios.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoNombre, textoContenido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoNombre = itemView.findViewById(R.id.texto_nombre_comentario);
            textoContenido = itemView.findViewById(R.id.texto_contenido_comentario);
        }
    }
}
