package com.martin.projetfinal_jsp.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Client implements UserDetails {
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String courriel;
    private String motPasse;
    private final boolean status;
    private List<GrantedAuthority> roles;





    public Client(String nom, String prenom, String adresse, String telephone, String courriel, String motPasse, boolean status, List<GrantedAuthority> roles) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.courriel = courriel;
        this.motPasse = motPasse;
        this.status = status;
        this.roles = roles;
    }
    public Client(String courriel, String motPasse, List<GrantedAuthority> roles) {
        this.courriel = courriel;
        this.motPasse = motPasse;
        this.roles = roles;
        this.status = true;
    }

    public Client(String courriel, String motPasse) {
        this.courriel = courriel; // Utilisez le courriel comme username
        this.motPasse = motPasse; // Assurez-vous que le mot de passe est correctement défini
        this.status = true;
        this.roles = new ArrayList<>(); // Une liste vide d'autorités par défaut
    }
    public Client(String courriel, String nom, String prenom, String adresse, String telephone, String motPasse, List<GrantedAuthority> authorities) {
        this.courriel = courriel;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.motPasse = motPasse;
        this.status = true; // Vous pouvez initialiser le statut par défaut ici
        this.roles = authorities;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return motPasse;
    }

    @Override
    public String getUsername() {
        return courriel;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
