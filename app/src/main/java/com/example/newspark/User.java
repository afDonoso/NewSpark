package com.example.newspark;

public class User {
    /**
     * Atributos
     */

    private String name;
    private String lastName;
    private String email;
    private String birthDate;

    User(String name, String lastName, String email, String birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
