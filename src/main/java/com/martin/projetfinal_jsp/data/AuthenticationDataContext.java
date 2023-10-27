package com.martin.projetfinal_jsp.data;


import com.martin.projetfinal_jsp.models.Client;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.martin.projetfinal_jsp.models.Client;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthenticationDataContext {
    private JdbcTemplate jdbcTemplate;

    // Définition des requêtes SQL
    private final String insertUserQuery = "INSERT INTO Clients(Courriel, Mot_Passe, Nom, Prenom, Adresse, Telephone) VALUES(?, ?, ?, ?, ?, ?)";
    private final String insertAuthQuery = "INSERT INTO Roles(Courriel, Role) VALUES(?, ?)";
    private final String selectPasswordQuery = "SELECT Mot_Passe FROM Clients WHERE Courriel=?";
    private final String selectAuthQuery = "SELECT role FROM Roles WHERE Courriel=?";
    private final String deleteUserAuthoritiesQuery = "DELETE FROM Roles WHERE Courriel=?";
    private final String deleteUserQuery = "DELETE FROM Clients WHERE Courriel=?";
    private final String changePasswordQuery = "UPDATE Clients SET Mot_Passe=? WHERE Courriel=? AND Mot_Passe=?";
    private final String userExistsQuery = "SELECT COUNT(*) FROM Clients WHERE Courriel=?";


    public AuthenticationDataContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Les méthodes utilitaires privées
    private String getPassword(String courriel) {
        return this.jdbcTemplate.queryForObject(selectPasswordQuery, String.class, courriel);
    }

    private List<GrantedAuthority> getAuthorities(String courriel) {
        List<GrantedAuthority> grantedAuthorities = jdbcTemplate.query(selectAuthQuery, new String[]{courriel}, new RoleMapper());
        return grantedAuthorities;
    }

    private class RoleMapper implements RowMapper<GrantedAuthority> {
        @Override
        public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SimpleGrantedAuthority(rs.getString("role"));
        }
    }

    // La redéfinition des méthodes héritées

    public UserDetails loadUserByUsername(String courriel) throws UsernameNotFoundException {
        // Elle retourne un objet composé d'un courriel, mot de passe et liste de rôles
        try {
            // Lire le mot de passe de l'utilisateur
            String password = this.getPassword(courriel);
            if (password == null) throw new UsernameNotFoundException("Utilisateur non trouvé");

            // Lire les rôles associés à l'utilisateur
            List<GrantedAuthority> grantedAuthorities = this.getAuthorities(courriel);
            return new Client(courriel, password, grantedAuthorities);

        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void createUser(UserDetails user, String encodedPassword) {
        // Insérer un utilisateur
        this.jdbcTemplate.update(
                insertUserQuery,
                ((Client) user).getCourriel(),
                encodedPassword,
                ((Client) user).getNom(),
                ((Client) user).getPrenom(),
                ((Client) user).getAdresse(),
                ((Client) user).getTelephone()
        );

        // Insérer les rôles de l'utilisateur
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            this.jdbcTemplate.update(insertAuthQuery, ((Client) user).getCourriel(), auth.getAuthority());
        }
    }


    public boolean userExists(String courriel) {
        int count = this.jdbcTemplate.queryForObject(userExistsQuery, Integer.class, courriel);
        return (count > 0);
    }

    public void deleteUser(String courriel) {
        // Supprimer les rôles de l'utilisateur
        this.jdbcTemplate.update(deleteUserAuthoritiesQuery, courriel);
        // Supprimer l'utilisateur
        this.jdbcTemplate.update(deleteUserQuery, courriel);
    }

    public void changePassword(String courriel, String oldPassword, String newPassword) {
        this.jdbcTemplate.update(changePasswordQuery, newPassword, courriel, oldPassword);
    }
}
