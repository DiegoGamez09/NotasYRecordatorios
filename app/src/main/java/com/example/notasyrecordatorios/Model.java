package com.example.notasyrecordatorios;

public class  Model {
    String titulo;
    String descripcion;
    String id;

    public Model(String id, String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id = id;
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
}
