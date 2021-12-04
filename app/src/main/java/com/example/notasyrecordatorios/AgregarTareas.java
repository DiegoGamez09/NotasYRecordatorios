package com.example.notasyrecordatorios;

import androidx.appcompat.app.AppCompatActivity;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class AgregarTareas extends AppCompatActivity {

    TextView time;
    Button btnTime;
    int currHour, currMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tareas);

        time=findViewById(R.id.time);
        btnTime=findViewById(R.id.btnPickTime);

        Calendar calendar = Calendar.getInstance();
        currHour = calendar.get(Calendar.HOUR);
        currMinute = calendar.get(Calendar.MINUTE);


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AgregarTareas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time.setText(i+":"+i1);
                    }
                }, currHour, currMinute, false);
                dialog.show();
            }
        });
    }
}