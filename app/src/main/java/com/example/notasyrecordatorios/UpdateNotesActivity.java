package com.example.notasyrecordatorios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNotesActivity extends AppCompatActivity {

    EditText titulo, descripcion;
    Button btnEditar;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        titulo=findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnEditar = findViewById(R.id.btnEditar);

        Intent intent=getIntent();

        titulo.setText(intent.getStringExtra("titulo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        id=intent.getStringExtra("id");

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);

                    db.ActualizarNota(titulo.getText().toString(), descripcion.getText().toString(), id);
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