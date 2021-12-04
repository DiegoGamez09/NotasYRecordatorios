package com.example.notasyrecordatorios;

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

import java.util.Calendar;


public class AgregarTareas extends AppCompatActivity {
    EditText titulo, descripcion;
    Button btnAgregar;
    TextView time;
    Button btnTime;
    Button btnDate;
    int currHour, currMinute, currSecond,currDay, currMonth, currYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tareas);

        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnAgregar = findViewById(R.id.btnAgregar);
        time=findViewById(R.id.time);
        btnTime=findViewById(R.id.btnPickTime);
        btnDate=findViewById(R.id.btnPickDate);

        Calendar calendar = Calendar.getInstance();
        currHour = calendar.get(Calendar.HOUR);
        currMinute = calendar.get(Calendar.MINUTE);
        currSecond = calendar.get(Calendar.SECOND);
        currDay=calendar.get(Calendar.DATE);
        currMonth=calendar.get(Calendar.MONTH);
        currYear=calendar.get(Calendar.YEAR);


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AgregarTareas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time.setText(i+":"+i1+":00");
                    }
                }, currHour, currMinute, false);
                dialog.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AgregarTareas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        time.setText(i+"-"+i1+"-"+i2+" "+time.getText().toString());
                    }
                }, currYear,currMonth,currDay);
                dialog.show();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descripcion.getText().toString())){
                    DatabaseTareas db = new DatabaseTareas(AgregarTareas.this);
                    System.out.println(time.getText().toString());
                    db.agregarTarea(titulo.getText().toString(),descripcion.getText().toString(),time.getText().toString());

                    Intent intent = new Intent(AgregarTareas.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(AgregarTareas.this,"Ambos campos deben de ser llenados",Toast.LENGTH_LONG);
                }
            }
        });
    }
}