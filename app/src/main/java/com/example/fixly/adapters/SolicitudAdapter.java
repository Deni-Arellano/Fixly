package com.example.fixly.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixly.R;
import com.example.fixly.models.historial.Solicitud;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder> {

    private List<Solicitud> solicitudes;
    private Context context;

    public SolicitudAdapter(List<Solicitud> solicitudes, Context context) {
        this.solicitudes = solicitudes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitud solicitud = solicitudes.get(position);
        holder.tvServicio.setText(solicitud.getNombreServicio());
        holder.tvDescripcion.setText(solicitud.getDescripcion());
        holder.tvEstado.setText(solicitud.getEstadoSolicitud());

        int colorFondo;
        switch (solicitud.getEstadoId()) {
            case 1:
                colorFondo = Color.parseColor("#FFE7C1"); // Amarillito
                break;
            case 2:
                colorFondo = Color.parseColor("#E3EDFB"); // Azulito
                break;
            case 3:
                colorFondo = Color.parseColor("#E0E0E0"); // Gris
                break;
            case 4:
                colorFondo = Color.parseColor("#FFCDD2"); // Rojito
                break;
            default:
                colorFondo = Color.LTGRAY;
                break;
        }
        holder.cardEstado.setCardBackgroundColor(colorFondo);

        holder.btnVerSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences("fixly_sesion", Context.MODE_PRIVATE);
                int idRol = prefs.getInt("id_rol", 3);
                Intent intent;
                if (idRol == 2) {
                    intent = new Intent(context, com.example.fixly.VerSolicitudProveedorActivity.class);
                } else {
                    intent = new Intent(context, com.example.fixly.VerSolicitudActivity.class);
                }
                intent.putExtra("id_solicitud", solicitud.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServicio, tvDescripcion, tvEstado;
        MaterialCardView cardEstado;
        Button btnVerSolicitud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServicio = itemView.findViewById(R.id.texto_tipo_servicio_item_solicitud);
            tvDescripcion = itemView.findViewById(R.id.texto_descripcion_item_solicitud);
            tvEstado = itemView.findViewById(R.id.texto_estado_item_solicitud);
            cardEstado = itemView.findViewById(R.id.card_estado_solicitud);
            btnVerSolicitud = itemView.findViewById(R.id.boton_ver_solicitud);
        }
    }
}