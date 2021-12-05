package com.example.notasyrecordatorios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotas extends RecyclerView.Adapter<AdapterNotas.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<Nota> listaNotas;
    List<Tarea> listaTareas;
    List<Object> nuevaLista;
    List<Object> lista;

    public AdapterNotas(Context context, Activity activity, List<Object> lista) {
        this.context = context;
        this.activity = activity;
        this.lista = lista;
        nuevaLista=new ArrayList<>(lista);
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
        if(lista.get(i) instanceof Nota) {
            Nota nota =(Nota) lista.get(i);
            holder.titulo.setText(nota.getTitulo());
            holder.descripcion.setText(nota.getDescripcion());
            holder.contenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateNotesActivity.class);
                    intent.putExtra("titulo", nota.getTitulo());
                    intent.putExtra("descripcion", nota.getDescripcion());
                    intent.putExtra("id", nota.getId());
                    Toast.makeText(context, "index: " + i, Toast.LENGTH_SHORT).show();
                    activity.startActivity(intent);

                }
            });
        }else{
            Tarea tarea =(Tarea) lista.get(i);
            holder.titulo.setText(tarea.getTitulo());
            holder.descripcion.setText(tarea.getDescripcion());
            holder.contenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateNotesActivity.class);
                    intent.putExtra("titulo", tarea.getTitulo());
                    intent.putExtra("descripcion", tarea.getDescripcion());
                    intent.putExtra("id", tarea.getId());
                    Toast.makeText(context, "index: " + i, Toast.LENGTH_SHORT).show();
                    activity.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filteredList=new ArrayList<>();
            if (constraint ==null || constraint.length()==0){
                filteredList.addAll(nuevaLista);
            }
            else{
                String filtroPadre=constraint.toString().toLowerCase().trim();
                for (Object item:nuevaLista){
                    if(item instanceof Nota){
                        Nota nota =(Nota) item;
                        if(nota.getTitulo().toLowerCase().contains(filtroPadre)){
                            filteredList.add(item);
                        }
                    }

                }

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lista.clear();
            lista.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

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

    public List<Object> getList(){
        return lista;
    }

    public void removeItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Object item, int position){
        lista.add(position,item);
        notifyItemInserted(position);
    }
}
