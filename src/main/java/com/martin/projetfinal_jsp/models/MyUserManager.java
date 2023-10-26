package com.martin.projetfinal_jsp.models;

import com.martin.projetfinal_jsp.data.AuthenticationDataContext;
import com.martin.projetfinal_jsp.models.Client;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        this.authenticationdatacontext.changePassword(username, newPassword, oldPassword);
    }

    public boolean userExists(String username) {
        return this.authenticationdatacontext.userExists(username);
    }
}
