package com.martin.projetfinal_jsp.models;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.util.Date;

public class Reservation {
    private int numReservation;
    private int numChalet;
    private String telephone;
    private String nomClient;
    private String courriel;
    @Column// precision = 10, scale = 2 Ces valeurs doivent correspondre à celles de votre colonne DECIMAL dans la base de données.
    private BigDecimal prix;
    private int duree;
    private Date dateLocation;

    // Constructeurs

    public Reservation(int numReservation, int numChalet, String telephone, String nomClient, String courriel, BigDecimal prix, int duree, Date dateLocation) {
        this.numReservation = numReservation;
        this.numChalet = numChalet;
        this.telephone = telephone;
        this.nomClient = nomClient;
        this.courriel = courriel;
        this.prix = prix;
        this.duree = duree;
        this.dateLocation = dateLocation;
    }

    // Getters et Setters
    public int getNumReservation() {
        return numReservation;
    }

    public void setNumReservation(int numReservation) {
        this.numReservation = numReservation;
    }

    public int getNumChalet() {
        return numChalet;
    }

    public void setNumChalet(int numChalet) {
        this.numChalet = numChalet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateLocation() {
        return dateLocation;
    }

    public void setDateLocation(Date dateLocation) {
        this.dateLocation = dateLocation;
    }

    // Constructeurs
    public Reservation() {
    }

}
