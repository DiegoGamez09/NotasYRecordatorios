package com.example.notasyrecordatorios.Modelo;

public class Nota {
    String titulo;
    String descripcion;
    String id;
    String imagen;
    String video;



    public Nota(String id, String titulo, String descripcion, String imagen, String video) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id = id;
        this.imagen=imagen;
        this.video=video;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
