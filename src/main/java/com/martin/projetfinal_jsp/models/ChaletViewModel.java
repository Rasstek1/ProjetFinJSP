package com.martin.projetfinal_jsp.models;
import java.math.BigDecimal;

public class ChaletViewModel {
    private int numChalet;
    private int nombreChambres;
    private String description;
    private BigDecimal prix;

    private String photo;

    public ChaletViewModel(int numChalet, int nombreChambres, String description, BigDecimal prix, String photo) {
        this.numChalet = numChalet;
        this.nombreChambres = nombreChambres;
        this.description = description;
        this.prix = prix;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // Constructeurs
    public ChaletViewModel() {
    }


}
