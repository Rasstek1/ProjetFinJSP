package com.martin.projetfinal_jsp.models;

import com.martin.projetfinal_jsp.data.AuthenticationDataContext;
import com.martin.projetfinal_jsp.models.Client;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MyUserManager implements UserDetailsService {

    private final AuthenticationDataContext authenticationdatacontext;
    private final PasswordEncoder passwordEncoder;

    public MyUserManager(AuthenticationDataContext authenticationdatacontext, PasswordEncoder passwordEncoder) {
        this.authenticationdatacontext = authenticationdatacontext;
        this.passwordEncoder = passwordEncoder;
    }


    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDetails user = this.authenticationdatacontext.loadUserByUsername(username);
            return user;
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void createUser(Client client) {
        if (this.authenticationdatacontext.userExists(client.getUsername())) {
            throw new IncorrectResultSizeDataAccessException("Username already exists", 1);
        }
        if (client.getAuthorities().isEmpty()) {
            throw new IncorrectResultSizeDataAccessException("User must have at least one role", 1);
        }
        String encodedPassword = this.passwordEncoder.encode(client.getPassword());
        this.authenticationdatacontext.createUser(client, encodedPassword);
    }

    public void deleteUser(String username) {
        this.authenticationdatacontext.deleteUser(username);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Récupérer l'utilisateur
        UserDetails userDetails = authenticationdatacontext.loadUserByUsername(username);

        if (userDetails instanceof Client) {
            Client client = (Client) userDetails;

            // Vérifier si l'ancien mot de passe correspond
            if (passwordEncoder.matches(oldPassword, client.getMotPasse())) {
                // Si l'ancien mot de passe correspond, mettez à jour le mot de passe
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                client.setMotPasse(encodedNewPassword); // Mettre à jour le mot de passe dans votre objet Client
                authenticationdatacontext.updateUserPassword(username, oldPassword, encodedNewPassword);
                // Mettre à jour le mot de passe dans la base de données en utilisant votre méthode updateUserPassword
            } else {
                throw new BadCredentialsException("Ancien mot de passe incorrect");
            }
        }
    }



    public boolean userExists(String username) {
        return this.authenticationdatacontext.userExists(username);
    }
}
