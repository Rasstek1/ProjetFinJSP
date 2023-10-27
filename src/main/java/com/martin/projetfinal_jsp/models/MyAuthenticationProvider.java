package com.martin.projetfinal_jsp.models;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class MyAuthenticationProvider implements AuthenticationProvider {

    private MyUserManager userDetailsManager;

    public MyAuthenticationProvider(MyUserManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public void setUserDetailsManager(MyUserManager uDetailsManager) {
        this.userDetailsManager = uDetailsManager;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String courriel = authentication.getName(); // Utilisez courriel au lieu de username
        String password = (String) authentication.getCredentials();
        // Récupérer le client de la base de données
        Client client = (Client) this.userDetailsManager.loadUserByUsername(courriel); // Utilisez Client au lieu de Usager
        // Vérifier l'existence du client
        if (client == null) {
            throw new UsernameNotFoundException("Le client est inexistant");
        }
        // En utilisant la méthode matches de la classe BCryptPasswordEncoder
        if (!this.userDetailsManager.getPasswordEncoder().matches(password, client.getMotPasse())) {
            throw new BadCredentialsException("Le mot de passe est incorrect.");
        }
        // Récupérer les rôles du client
        Collection<GrantedAuthority> authorities = client.getAuthorities(); // Utilisez getAuthorities
        if (authorities == null || authorities.isEmpty()) {
            throw new BadCredentialsException("Erreur d'autorisation");
        }
        // La création du token (le jeton) d'authentification
        authentication = new UsernamePasswordAuthenticationToken(client, password, authorities);
        return authentication;
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
