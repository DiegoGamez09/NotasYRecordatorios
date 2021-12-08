package com.example.notasyrecordatorios.Vistas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notasyrecordatorios.AlarmReceiver;
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
    int selectedHour, selectedMinute, selectedSecond, selectedDay, selectedMonth, selectedYear;

    String tiempo="";
    String fecha="";

    private PendingIntent pendingIntent;

    private AlarmManager alarmManager;

    private Calendar calendarAlarm;

    private static final String CHANNEL_ID = "RECORDATORIOS-TAREAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agregar_tareas);

        createNotificationChannel();

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
                        selectedHour=i;
                        selectedMinute=i1;
                        selectedSecond=0;

                        calendarAlarm = Calendar.getInstance();
                        calendarAlarm.set(Calendar.HOUR_OF_DAY, i);
                        calendarAlarm.set(Calendar.MINUTE, i1);
                        calendarAlarm.set(Calendar.SECOND, 0);
                        calendarAlarm.set(Calendar.MILLISECOND, 0);
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
                        selectedYear=i;
                        selectedMonth=i1;
                        selectedDay=i2;


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

                    setAlarm();


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

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendarAlarm.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();



    }


    private void mostrarNotificacion(Context context, Intent intent) {
        //createNotificationChannel(context,intent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Titulo recordatorio")
                .setContentText("Te recuerdo tarea pendiente")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, builder.build());

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


    }



}