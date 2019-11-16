package com.example.newspark;

import java.io.Serializable;

public class News implements Serializable {

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
    private String image;

    /**
     * Porcentaje de parcialidad;
     */
    private int parcialityPercentage;

    /**
     * Crea una nueva noticia con la información ingresada por parámetro.
     * @param title Título de la noticia. title != "" && != null.
     * @param date Fecha de la noticia. date != null.
     * @param image Imagen de la noticia. image != null && != "".
     * @param parcialityPercentage Porcentaje de parcialidad. parcialityPercentage >= 0 && <= 100.
     */
    public News(String title, String date, String image, int parcialityPercentage) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.parcialityPercentage = parcialityPercentage;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParcialityPercentage() {
        return parcialityPercentage;
    }

    public void setParcialityPercentage(int parcialityPercentage) {
        this.parcialityPercentage = parcialityPercentage;
    }
}
