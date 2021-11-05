package com.example.notasyrecordatorios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> listaNotas;

    DatabaseClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AgregarNotas.class);
                startActivity(intent);
            }
        });

        listaNotas = new ArrayList<>();

        db = new DatabaseClass(this);
        cargarNotas();
        //System.out.println(listaNotas.size());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, MainActivity.this, listaNotas);
        recyclerView.setAdapter(adapter);
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
                adapter.getFilter().filter(s);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void eliminarNotas() {
        DatabaseClass db = new DatabaseClass(MainActivity.this);
        db.eliminarNotas();
        recreate();
    }

    void cargarNotas(){
      Cursor cursor =  db.obtenerTodasLasNotas();
      if(cursor.getCount()!=0){
          while(cursor.moveToNext()){
              listaNotas.add(new Model(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
          }

      }else{
          Toast.makeText(this, "No hay notas", Toast.LENGTH_SHORT).show();
      }
    }


}