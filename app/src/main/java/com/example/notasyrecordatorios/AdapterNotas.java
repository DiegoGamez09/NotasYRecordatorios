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
    List<Nota> nuevaLista;

    public AdapterNotas(Context context, Activity activity, List<Nota> listaNotas) {
        this.context = context;
        this.activity = activity;
        this.listaNotas = listaNotas;
        nuevaLista=new ArrayList<>(listaNotas);
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

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Nota> filteredList=new ArrayList<>();
            if (constraint ==null || constraint.length()==0){
                filteredList.addAll(nuevaLista);
            }
            else{
                String filtroPadre=constraint.toString().toLowerCase().trim();
                for (Nota item:nuevaLista){
                    if(item.getTitulo().toLowerCase().contains(filtroPadre)){
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaNotas.clear();
            listaNotas.addAll((List)results.values);
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

    public List<Nota> getList(){
        return listaNotas;
    }

    public void removeItem(int position){
        listaNotas.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Nota item, int position){
        listaNotas.add(position,item);
        notifyItemInserted(position);
    }
}