package com.example.notasyrecordatorios.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.notasyrecordatorios.AdapterNotas;
import com.example.notasyrecordatorios.Database.DatabaseNotas;
import com.example.notasyrecordatorios.Database.DatabaseTareas;
import com.example.notasyrecordatorios.Modelo.Nota;
import com.example.notasyrecordatorios.Modelo.Tarea;
import com.example.notasyrecordatorios.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton fabAgregarTarea;
    AdapterNotas adapterNotas;
    List<Nota> listaNotas;
    List<Tarea> listaTareas;
    List<Object> lista;

    DatabaseNotas db;
    DatabaseTareas dbTareas;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        fab=findViewById(R.id.fab);
        fabAgregarTarea=findViewById(R.id.fabAgregarTareas);
        coordinatorLayout=findViewById(R.id.layout_main);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AgregarNotas.class);
                startActivity(intent);
            }
        });

        fabAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AgregarTareas.class);
                startActivity(intent);
            }
        });

        listaNotas = new ArrayList<>();
        listaTareas= new ArrayList<>();
        lista = new ArrayList<>();

        db = new DatabaseNotas(this);
        dbTareas = new DatabaseTareas(this);
        cargarTareas();
        cargarNotas();

        //System.out.println(listaNotas.size());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapterTareas = new AdapterTareas(this, MainActivity.this, listaTareas);
        adapterNotas = new AdapterNotas(this, MainActivity.this, lista);
        recyclerView.setAdapter(adapterNotas);

        ItemTouchHelper helper =  new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void cargarTareas() {
        Cursor cursor =  dbTareas.obtenerTodasLasTareas();
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                listaTareas.add(new Tarea(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3)));
                lista.add(new Tarea(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3)));
                System.out.println(cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" ");
            }

        }else{
            Toast.makeText(this, "No hay Tareas", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchview = (SearchView) searchItem.getActionView();
        searchview.setQueryHint("Search notes here");

        SearchView.OnQueryTextListener listener=new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterNotas.getFilter().filter(s);
                return true;
            }
        };
        searchview.setOnQueryTextListener(listener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.delete_all_notes){
            eliminarNotas();
        }else if(item.getItemId()==R.id.delete_all_Tareas){
            eliminarTareas();
        }

        return super.onOptionsItemSelected(item);
    }

    private void eliminarNotas() {
        DatabaseNotas db = new DatabaseNotas(MainActivity.this);
        db.eliminarNotas();
        recreate();
    }

    private void eliminarTareas() {
        DatabaseTareas db = new DatabaseTareas(MainActivity.this);
        db.eliminarTareas();
        recreate();
    }

    void cargarNotas(){
      Cursor cursor =  db.obtenerTodasLasNotas();
      if(cursor.getCount()!=0){
          while(cursor.moveToNext()){
              listaNotas.add(new Nota(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
              lista.add(new Nota(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
          }

      }else{
          Toast.makeText(this, "No hay notas", Toast.LENGTH_SHORT).show();
      }
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            Object item = adapterNotas.getList().get(pos);
            adapterNotas.removeItem(pos);
            Snackbar snack = Snackbar.make(coordinatorLayout,"Item deleted",Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterNotas.restoreItem(item,pos);
                    recyclerView.scrollToPosition(pos);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    if(!(event==DISMISS_EVENT_ACTION)){
                        if(item instanceof Nota) {
                            Nota nota =(Nota) item;
                            DatabaseNotas db = new DatabaseNotas(MainActivity.this);
                            db.deleteItem(nota.getId());
                        }else{
                            Tarea tarea =(Tarea) item;
                            DatabaseTareas db = new DatabaseTareas(MainActivity.this);
                            db.deleteItem(tarea.getId());
                        }
                    }
                }
            });

            snack.setActionTextColor(Color.BLUE);
            snack.show();
        }
    };


}