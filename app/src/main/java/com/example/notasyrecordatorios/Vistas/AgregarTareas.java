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

import java.text.DecimalFormat;
import java.util.Calendar;


public class AgregarTareas extends AppCompatActivity {
    EditText titulo, descripcion;
    Button btnAgregar;
    TextView time;
    Button btnTime;
    Button btnDate;
    int currHour, currMinute, currSecond,currDay, currMonth, currYear;

    String tiempo="";
    String fecha="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tareas);

        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        btnAgregar = findViewById(R.id.btnEditarTarea);
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
                        tiempo=new DecimalFormat("00").format(i)+":"+new DecimalFormat("00").format(i1)+":00";
                        time.setText(tiempo);

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
                        fecha=i+"-"+(new DecimalFormat("00").format(i1+1))+"-"+new DecimalFormat("00").format(i2)+" ";
                        time.setText(fecha + tiempo);

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