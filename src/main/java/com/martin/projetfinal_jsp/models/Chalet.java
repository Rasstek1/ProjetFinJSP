package com.martin.projetfinal_jsp.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Chalet {
    private int numChalet;
    private int nombreChambres;
    private String description;
    private BigDecimal prix;
    private List<String> listePhotos = new ArrayList<>();




    // Constructeurs
    public Chalet() {
    }

    public Chalet(int numChalet, int nombreChambres, String description, BigDecimal prix, List<String> listePhotos) {
        this.numChalet = numChalet;
        this.nombreChambres = nombreChambres;
        this.description = description;
        this.prix = (prix != null) ? prix : BigDecimal.ZERO;
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

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public List<String> getListePhotos() {
        return listePhotos;
    }

    public void setListePhotos(List<String> listePhotos) {
        this.listePhotos = listePhotos;
    }







}
