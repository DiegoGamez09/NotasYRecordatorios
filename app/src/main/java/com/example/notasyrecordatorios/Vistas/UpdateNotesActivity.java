package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notasyrecordatorios.Database.DatabaseNotas;
import com.example.notasyrecordatorios.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class  UpdateNotesActivity extends AppCompatActivity {

    EditText titulo, descripcion;
    Button btnEditar;
    String id;
    String imagen;
    ImageView vistaImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        titulo=findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnEditar = findViewById(R.id.btnEditarTarea);
        vistaImagen=findViewById(R.id.imgImagen);

        Intent intent=getIntent();

        titulo.setText(intent.getStringExtra("titulo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        id=intent.getStringExtra("id");
        imagen=intent.getStringExtra("imagen");
        Uri uri = Uri.parse(imagen);
        vistaImagen.setImageURI(uri);
        System.out.println(uri.toString());

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseNotas db = new DatabaseNotas(UpdateNotesActivity.this);

                    db.ActualizarNota(titulo.getText().toString(), descripcion.getText().toString(), id, imagen, "");
                    Intent i = new Intent(UpdateNotesActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "Ambos campos deben ser llenados", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}