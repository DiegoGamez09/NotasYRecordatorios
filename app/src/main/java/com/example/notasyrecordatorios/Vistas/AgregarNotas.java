package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notasyrecordatorios.Database.DatabaseNotas;
import com.example.notasyrecordatorios.R;

import java.io.File;

public class  AgregarNotas extends AppCompatActivity {

    EditText titulo, descripcion;
    Button btnAgregar, btnTomarFoto, btnTomarVideo, btnElegirImagen;
    ImageView vistaImagen;
    Boolean cargarimg=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_notas);

        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnAgregar = findViewById(R.id.btnEditarTarea);
        btnTomarFoto = findViewById(R.id.btnTomarImg);
        btnElegirImagen = findViewById(R.id.btnAgregarImg);
        vistaImagen = findViewById(R.id.imgImagen);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseNotas db = new DatabaseNotas(AgregarNotas.this);
                    db.agregarNota(titulo.getText().toString(),descripcion.getText().toString());

                    Intent intent = new Intent(AgregarNotas.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(AgregarNotas.this,"Ambos campos deben de ser llenados",Toast.LENGTH_LONG);
                }
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(titulo.getText()+".jpg");
                abrirCamara();

            }
        });

        btnElegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elegirImagen();
            }
        });

    }

    void elegirImagen(){
        cargarimg=true;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("images/");
        startActivityForResult(intent.createChooser(intent, "seleccionar imagen"),10);
    }

    void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 1);
        //}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            if(!cargarimg) {
                Bundle extras = data.getExtras();
                Bitmap img = (Bitmap) extras.get("data");
                vistaImagen.setImageBitmap(img);
            }else{
                Uri path = data.getData();
                vistaImagen.setImageURI(path);
            }
        }
    }


}