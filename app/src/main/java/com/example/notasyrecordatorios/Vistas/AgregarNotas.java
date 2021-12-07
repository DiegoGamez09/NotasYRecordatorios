package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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

    static final int REQUEST_VIDEO_CAPTURE = 10;
    int position=1;
    EditText titulo, descripcion;
    Button btnAgregar, btnTomarFoto, btnTomarVideo, btnElegirImagen, btnElegirVideo;
    ImageView vistaImagen;
    VideoView vistaVideo;
    Boolean cargarimg=false;
    String rutaGlobal ="";
    String rutaGlobalVideo="";
    MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_notas);

        if(ContextCompat.checkSelfPermission(AgregarNotas.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AgregarNotas.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AgregarNotas.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }




        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnAgregar = findViewById(R.id.btnEditarTarea);
        btnTomarFoto = findViewById(R.id.btnTomarImg);
        btnElegirImagen = findViewById(R.id.btnAgregarImg);
        btnTomarVideo = findViewById(R.id.btnTomarVideo);
        btnElegirVideo = findViewById(R.id.btnAgregarVideo);
        vistaImagen = findViewById(R.id.imgImagen);
        vistaVideo = findViewById(R.id.VistaVideo);

        if(mediaController==null){
            mediaController=new MediaController(AgregarNotas.this);
            mediaController.setAnchorView(vistaVideo);
            vistaVideo.setMediaController(mediaController);
        }

        vistaVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                vistaVideo.seekTo(position);
                if(position==0){
                    vistaVideo.start();
                }
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(vistaVideo);
                    }
                });
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseNotas db = new DatabaseNotas(AgregarNotas.this);
                    db.agregarNota(titulo.getText().toString(),descripcion.getText().toString(), rutaGlobal, rutaGlobalVideo);

                    Intent intent = new Intent(AgregarNotas.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(AgregarNotas.this,"Ambos campos deben de ser llenados",Toast.LENGTH_LONG);
                }
            }
        });

        btnTomarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(titulo.getText()+ ".mp4");
                abrirCamaraVideo();
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

        btnElegirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarVideoGaleria();
            }
        });



    }

    private void tomarVideoGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("images/");
        startActivityForResult(intent,200);
    }


    void elegirImagen(){
        //cargarimg=true;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("images/");
        startActivityForResult(intent,100);
    }

    private void tomarVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    void abrirCamara(){
       // cargarimg=false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
        startActivityForResult(intent, 1);
        //}
    }

    void abrirCamaraVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File video=null;
        try {
            video = crearVideo();
        }catch (Exception e){

        }

        if(video!=null){
            Uri uri = FileProvider.getUriForFile(AgregarNotas.this, "com.example.notasyrecordatorios", video);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        //if(intent.resolveActivity(getPackageManager())!=null){
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
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

    private File crearVideo() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String imgFileName = "video "+ timestamp;

        File storageFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File videoFile = File.createTempFile(imgFileName, ".mp4", storageFile);
        rutaGlobalVideo = videoFile.getAbsolutePath();
        System.out.println(rutaGlobalVideo);
        return videoFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){

            Uri uri = Uri.parse(rutaGlobal);
            vistaImagen.setImageURI(uri);



        }else if(requestCode==100 && resultCode==RESULT_OK){

            Uri path = data.getData();
            rutaGlobal=getRealPathFromUri(path);
            vistaImagen.setImageURI(path);
            Toast.makeText(AgregarNotas.this,rutaGlobal, Toast.LENGTH_LONG).show();
        }else if(requestCode==REQUEST_VIDEO_CAPTURE && resultCode==RESULT_OK){
            Uri uri = Uri.parse(rutaGlobalVideo);
            vistaVideo.setVideoURI(uri);
            vistaVideo.start();

        }else if(requestCode==200 && resultCode==RESULT_OK){
            Uri path = data.getData();
            rutaGlobalVideo=getRealPathFromUri(path);
            vistaVideo.setVideoURI(path);
            vistaVideo.start();
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




}