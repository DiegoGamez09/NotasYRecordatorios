package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notasyrecordatorios.Database.DatabaseNotas;
import com.example.notasyrecordatorios.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class  AgregarNotas extends AppCompatActivity {

    EditText titulo, descripcion;
    Button btnAgregar, btnTomarFoto, btnTomarVideo, btnElegirImagen;
    ImageView vistaImagen;
    Boolean cargarimg=false;
    String rutaGlobal ="";

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
                    db.agregarNota(titulo.getText().toString(),descripcion.getText().toString(), rutaGlobal, "");

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
        //cargarimg=true;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("images/");
        startActivityForResult(intent,100);
    }

    void abrirCamara(){
       // cargarimg=false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
        File foto=null;
        try {
            foto = crearFoto();
       }catch (Exception e){

        }

        if(foto!=null){
            Uri uri = FileProvider.getUriForFile(AgregarNotas.this, "com.example.notasyrecordatorios", foto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        //if(intent.resolveActivity(getPackageManager())!=null){

        //}
    }

    private File crearFoto() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String imgFileName = "imagen "+ timestamp;

        File storageFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fotoFile = File.createTempFile(imgFileName, ".jpg", storageFile);
        rutaGlobal = fotoFile.getAbsolutePath();
        return fotoFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){

 /*           if(data!=null){
                Bitmap img = (Bitmap) data.getExtras().get("data");
                vistaImagen.setImageBitmap(img);
                Uri uri = getImageUri(getApplicationContext(), img);
                File file = new File(getRealPathFromUri(uri));
                rutaGlobal = getPathFromUri(uri);
            }*/
            Uri uri = Uri.parse(rutaGlobal);
            vistaImagen.setImageURI(uri);



        }else if(requestCode==100 && resultCode==RESULT_OK){

            Uri path = data.getData();
            rutaGlobal=getRealPathFromUri(path);
            vistaImagen.setImageURI(path);
            Toast.makeText(AgregarNotas.this,rutaGlobal, Toast.LENGTH_LONG).show();
        }

    }

    private String getRealPathFromUri(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }


}