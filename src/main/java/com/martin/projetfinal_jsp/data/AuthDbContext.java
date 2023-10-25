package com.martin.projetfinal_jsp.data;

import com.martin.projetfinal_jsp.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDbContext {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthDbContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveClient(Client client) {
        // Insertion du client dans la table 'Clients'
        String sqlClient = "INSERT INTO Clients (Courriel, Nom, Prenom, Adresse, Telephone, Mot_Passe) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlClient, client.getCourriel(), client.getNom(), client.getPrenom(), client.getAdresse(), client.getTelephone(), client.getMotPasse());

        // Insertion du rôle dans la table 'Roles'
        String sqlRole = "INSERT INTO Roles (courriel, role) VALUES (?, ?)";
        jdbcTemplate.update(sqlRole, client.getCourriel(), "USER");
    }

}
