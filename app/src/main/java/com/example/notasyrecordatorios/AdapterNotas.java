package com.example.notasyrecordatorios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notasyrecordatorios.Modelo.Nota;
import com.example.notasyrecordatorios.Modelo.Tarea;
import com.example.notasyrecordatorios.Vistas.UpdateNotesActivity;
import com.example.notasyrecordatorios.Vistas.UpdateTareasActivity;

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
        //GradientDrawable gr =(GradientDrawable) holder.contenedor.getBackground();
        if(lista.get(i) instanceof Nota) {
            Nota nota =(Nota) lista.get(i);
            holder.titulo.setText(nota.getTitulo());
            holder.descripcion.setText(nota.getDescripcion());
            holder.carview.setBackgroundColor(Color.parseColor("#7d9ccf"));
            //gr.setColor(Color.parseColor(nota.getColor()));

            //gr.setColor(Color.BLUE);
            holder.contenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateNotesActivity.class);
                    intent.putExtra("titulo", nota.getTitulo());
                    intent.putExtra("descripcion", nota.getDescripcion());
                    intent.putExtra("id", nota.getId());
                    intent.putExtra("imagen", nota.getImagen());
                    Toast.makeText(context, "URI: " + nota.getImagen(), Toast.LENGTH_LONG).show();
                    activity.startActivity(intent);

                }
            });
        }else{
            Tarea tarea =(Tarea) lista.get(i);
            holder.titulo.setText(tarea.getTitulo());
            holder.descripcion.setText(tarea.getDescripcion());
            holder.fecha.setText("Finaliza el: "+ tarea.getFecha());
            holder.carview.setBackgroundColor(Color.parseColor("#eec859"));
            //gr.setColor(Color.YELLOW);
            holder.contenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateTareasActivity.class);
                    intent.putExtra("titulo", tarea.getTitulo());
                    intent.putExtra("descripcion", tarea.getDescripcion());
                    intent.putExtra("fecha", tarea.getFecha());
                    intent.putExtra("id", tarea.getId());
                    //Toast.makeText(context, "index: " + i, Toast.LENGTH_SHORT).show();
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
                    }else{
                        Tarea tarea =(Tarea) item;
                        if(tarea.getTitulo().toLowerCase().contains(filtroPadre)){
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
        TextView titulo, descripcion, fecha;
        RelativeLayout contenedor;
        CardView carview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.title);
            descripcion = itemView.findViewById(R.id.description);
            contenedor = itemView.findViewById(R.id.note_layout);
            fecha = itemView.findViewById(R.id.fecha);
            carview = itemView.findViewById(R.id.cardview);
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
