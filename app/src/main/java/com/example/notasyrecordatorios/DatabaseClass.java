package com.example.notasyrecordatorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class     DatabaseClass extends SQLiteOpenHelper {

    Context context;
    private static final String nombre = "NotasYRecordatorios";
    private static final int version = 1;

    private static final String nombreTabla = "notas";
    private static final String columnId = "id";
    private static final String columnTitulo = "titulo";
    private static final String columnDescripcion = "descripcion";

    public DatabaseClass(@Nullable Context context ) {
        super(context, nombre, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+ nombreTabla +" ("+
                columnId+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + columnTitulo + " TEXT,"
                + columnDescripcion + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVe, int newVe) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ nombreTabla);
        onCreate(sqLiteDatabase);
    }

    void agregarNota(String titulo, String descripcion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnTitulo, titulo);
        cv.put(columnDescripcion,descripcion);
        long resultValue = db.insert(nombreTabla, null, cv);
        if (resultValue == -1){
            Toast.makeText(context, "Los datos no se agregaron de forma exitosa", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Datos agregados exitosamente", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor obtenerTodasLasNotas(){
        String query = "SELECT * FROM "+ nombreTabla;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void eliminarNotas(){
        SQLiteDatabase db= this.getWritableDatabase();
        String query = "DELETE FROM "+nombreTabla;
        db.execSQL(query);
    }

    void ActualizarNota(String titulo, String descripcion, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnTitulo,titulo);
        cv.put(columnDescripcion,descripcion);

        long res= db.update(nombreTabla, cv,"id=?",new String[]{id});
        if (res==-1){
            Toast.makeText(context, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Elemento actualizado", Toast.LENGTH_SHORT).show();
        }

    }
}
