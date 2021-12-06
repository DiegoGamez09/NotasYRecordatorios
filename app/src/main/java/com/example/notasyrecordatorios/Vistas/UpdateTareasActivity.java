package com.example.notasyrecordatorios.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notasyrecordatorios.Database.DatabaseTareas;
import com.example.notasyrecordatorios.R;

import java.util.Calendar;

public class UpdateTareasActivity extends AppCompatActivity {

    EditText titulo, descripcion;
    Button btnEditar;
    String id;
    TextView date;
    TextView time;
    Button btnTime;
    Button btnDate;
    int currHour, currMinute, currSecond,currDay, currMonth, currYear;

    String tiempo="";
    String fecha="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tareas);

        titulo=findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnEditar = findViewById(R.id.btnEditarTarea);
        date = findViewById(R.id.time);
        btnTime=findViewById(R.id.btnPickTime);
        btnDate=findViewById(R.id.btnPickDate);

        Calendar calendar = Calendar.getInstance();
        currHour = calendar.get(Calendar.HOUR);
        currMinute = calendar.get(Calendar.MINUTE);
        currSecond = calendar.get(Calendar.SECOND);
        currDay=calendar.get(Calendar.DATE);
        currMonth=calendar.get(Calendar.MONTH);
        currYear=calendar.get(Calendar.YEAR);

        Intent intent=getIntent();

        titulo.setText(intent.getStringExtra("titulo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        id=intent.getStringExtra("id");
        date.setText(intent.getStringExtra("fecha"));

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(UpdateTareasActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tiempo=i+":"+i1+":00";
                        date.setText(tiempo);

                    }
                }, currHour, currMinute, false);
                dialog.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UpdateTareasActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        fecha=i+"-"+(i1+1)+"-"+i2+" ";
                        date.setText(fecha + tiempo);

                    }
                }, currYear,currMonth,currDay);
                dialog.show();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseTareas db = new DatabaseTareas(UpdateTareasActivity.this);

                    db.ActualizarTarea(titulo.getText().toString(), descripcion.getText().toString(), id, date.getText().toString());
                    Intent i = new Intent(UpdateTareasActivity.this, MainActivity.class);
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