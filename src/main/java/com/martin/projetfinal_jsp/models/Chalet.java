package com.martin.projetfinal_jsp.models;

import java.util.List;

public class Chalet {
    private int numChalet;
    private int nombreChambres;
    private String description;
    private double prix;
    private List<String> listePhotos;


    public Chalet(int numChalet, int nombreChambres, String description, double prix, List<String> listePhotos) {
        this.numChalet = numChalet;
        this.nombreChambres = nombreChambres;
        this.description = description;
        this.prix = prix;
        this.listePhotos = listePhotos;
    }

    // Getters et Setters
    public int getNumChalet() {
        return numChalet;
    }

    public void setNumChalet(int numChalet) {
        this.numChalet = numChalet;
    }

    public int getNombreChambres() {
        return nombreChambres;
    }

    public void setNombreChambres(int nombreChambres) {
        this.nombreChambres = nombreChambres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<String> getListePhotos() {
        return listePhotos;
    }

    public void setListePhotos(List<String> listePhotos) {
        this.listePhotos = listePhotos;
    }

    // Constructeurs
    public Chalet() {
    }


}
