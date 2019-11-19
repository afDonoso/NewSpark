package com.example.newspark;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Calendar;

public class News implements Serializable, Comparable<News> {

    /**
     * Título de la noticia.
     */
    private String title;

    /**
     * Fecha de la noticia.
     */
    private String date;

    /**
     * Imagen de la noticia.
     */
    private Bitmap image;

    /**
     * Porcentaje de parcialidad;
     */
    private int parcialityPercentage;

    /**
     * Contenido del artículo.
     */
    private String contenido;

    /**
     * Url del sitio de la noticia.
     */
    private String url;

    /**
     * Crea una nueva noticia con la información ingresada por parámetro.
     * @param title Título de la noticia. title != "" && != null.
     * @param contenido Contenido del artículo. contenido != "" && != null.
     * @param url Url del sitio de la noticia. url != "" && != null.
     * @param date Fecha de la noticia. date != null.
     * @param image Imagen de la noticia. image != null && != "".
     * @param parcialityPercentage Porcentaje de parcialidad. parcialityPercentage >= 0 && <= 100.
     */
    public News(String title, String contenido, String url, String date, Bitmap image, int parcialityPercentage) {
        this.title = title;
        this.contenido = contenido;
        this.date = date;
        this.image = image;
        this.parcialityPercentage = parcialityPercentage;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getParcialityPercentage() {
        return parcialityPercentage;
    }

    public void setParcialityPercentage(int parcialityPercentage) {
        this.parcialityPercentage = parcialityPercentage;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(News news) {
        if (news.getDate().compareTo(this.getDate()) > 0) {
            return 1;
        } else if (news.getDate().compareTo(this.getDate()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
