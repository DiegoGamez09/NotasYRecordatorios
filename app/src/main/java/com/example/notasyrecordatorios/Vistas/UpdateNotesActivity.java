package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
    String video;
    ImageView vistaImagen;
    VideoView vistaVideo;
    int position=1;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        titulo=findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnEditar = findViewById(R.id.btnEditarTarea);
        vistaImagen=findViewById(R.id.imgImagen);
        vistaVideo=findViewById(R.id.VistaVideo);

        Intent intent=getIntent();

        titulo.setText(intent.getStringExtra("titulo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        id=intent.getStringExtra("id");
        imagen=intent.getStringExtra("imagen");
        video = intent.getStringExtra("video");
        if(!video.equals("")) {
            Uri uri = Uri.parse(imagen);
            Uri uriVideo = Uri.parse(video);
            vistaImagen.setImageURI(uri);
            vistaVideo.setVideoURI(uriVideo);
            vistaVideo.start();
            System.out.println(uri.toString());
            System.out.println(uriVideo.toString());
        }

        if(mediaController==null){
            mediaController=new MediaController(UpdateNotesActivity.this);
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