package com.example.notasyrecordatorios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        int i=position;
        holder.titulo.setText(listaNotas.get(position).getTitulo());
        holder.descripcion.setText(listaNotas.get(position).getDescripcion());

        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdateNotesActivity.class);
                intent.putExtra("titulo", listaNotas.get(i).getTitulo());
                intent.putExtra("descripcion", listaNotas.get(i).getDescripcion());
                intent.putExtra("id", listaNotas.get(i).getId());
                Toast.makeText(context, "index: "+i, Toast.LENGTH_SHORT).show();
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
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
