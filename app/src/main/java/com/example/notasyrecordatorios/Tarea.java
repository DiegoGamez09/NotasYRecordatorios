package com.example.notasyrecordatorios;

public class Tarea {
    String titulo;
    String descripcion;
    String id;
    String fecha;

    public Tarea(String titulo, String descripcion, String id, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id = id;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
