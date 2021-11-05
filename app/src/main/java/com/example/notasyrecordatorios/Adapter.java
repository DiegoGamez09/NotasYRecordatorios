package com.example.notasyrecordatorios;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    Context context;
    Activity activity;
    List<Model> listaNotas;

    public Adapter(Context context, Activity activity, List<Model> listaNotas) {
        this.context = context;
        this.activity = activity;
        this.listaNotas = listaNotas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titulo.setText(listaNotas.get(position).getTitulo());
        holder.descripcion.setText(listaNotas.get(position).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion;
        RelativeLayout contenedor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.title);
            descripcion = itemView.findViewById(R.id.description);
            contenedor = itemView.findViewById(R.id.note_layout);
        }
    }
}
