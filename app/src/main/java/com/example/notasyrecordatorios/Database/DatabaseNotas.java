package com.example.notasyrecordatorios.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseNotas extends SQLiteOpenHelper {

    Context context;
    private static final String nombre = "notasreloaded";
    private static final int version = 1;

    private static final String nombreTabla = "notas";
    private static final String columnId = "id";
    private static final String columnTitulo = "titulo";
    private static final String columnDescripcion = "descripcion";
    private static final String columnImagen = "imagen";
    private static final String columnVideo = "video";

    public DatabaseNotas(@Nullable Context context ) {
        super(context, nombre, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+ nombreTabla +" ("+
                columnId+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + columnTitulo + " TEXT,"
                + columnDescripcion + " TEXT,"
                + columnImagen+" TEXT,"
                +columnVideo+" TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVe, int newVe) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ nombreTabla);
        onCreate(sqLiteDatabase);
    }

    public void agregarNota(String titulo, String descripcion, String imagen, String video){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnTitulo, titulo);
        cv.put(columnDescripcion,descripcion);
        cv.put(columnImagen,imagen);
        cv.put(columnVideo,video);
        long resultValue = db.insert(nombreTabla, null, cv);
        if (resultValue == -1){
            Toast.makeText(context, "Los datos no se agregaron de forma exitosa", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Datos agregados exitosamente", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor obtenerTodasLasNotas(){
        String query = "SELECT * FROM "+ nombreTabla;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void eliminarNotas(){
        SQLiteDatabase db= this.getWritableDatabase();
        String query = "DELETE FROM "+nombreTabla;
        db.execSQL(query);
    }

    public void ActualizarNota(String titulo, String descripcion, String id, String imagen, String video){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnTitulo,titulo);
        cv.put(columnDescripcion,descripcion);
        cv.put(columnImagen,imagen);
        cv.put(columnVideo,video);

        long res= db.update(nombreTabla, cv,"id=?",new String[]{id});
        if (res==-1){
            Toast.makeText(context, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Elemento actualizado", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteItem(String id){
        SQLiteDatabase database =  this.getWritableDatabase();
        long result = database.delete(nombreTabla, "id=?", new String[]{id});
        if(result==-1){
            Toast.makeText(context, "No se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}
