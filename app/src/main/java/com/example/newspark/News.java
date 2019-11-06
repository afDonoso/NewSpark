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
    private int image;

    /**
     * Autor de la noticia.
     */
    private String authorName;

    /**
     * Porcentaje de parcialidad;
     */
    private int parcialityPercentage;

    /**
     * Artículo de la noticia
     */
    private int article;

    /**
     * Fuente de la noticia
     */
    private String source;

    /**
     * Crea una nueva noticia con la información ingresada por parámetro.
     * @param title Título de la noticia. title != "" && != null.
     * @param date Fecha de la noticia. date != null.
     * @param image Imagen de la noticia. image != null && != "".
     * @param authorName Autor de la noticia. authorName != null && != "".
     * @param parcialityPercentage Porcentaje de parcialidad. parcialityPercentage >= 0 && <= 100.
     */
    public News(String title, String date, int image, String authorName, int parcialityPercentage, int article, String source) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.authorName = authorName;
        this.parcialityPercentage = parcialityPercentage;
        this.article = article;
        this.source = source;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getParcialityPercentage() {
        return parcialityPercentage;
    }

    public void setParcialityPercentage(int parcialityPercentage) {
        this.parcialityPercentage = parcialityPercentage;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
