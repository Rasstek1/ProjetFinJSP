package com.martin.projetfinal_jsp.config;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.data.AuthenticationDataContext;
import com.martin.projetfinal_jsp.models.MyAuthenticationProvider;
import com.martin.projetfinal_jsp.models.MyUserManager;
import com.martin.projetfinal_jsp.models.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;



@Configuration
@EnableWebSecurity
public class ProjetFinalBeansConfiguration {
    // Configuration des règles d'accès aux routes
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((authorize) ->
                        authorize.requestMatchers(new AntPathRequestMatcher("/home/admin")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/home/usager")).hasRole("USER")
                                .anyRequest().permitAll())
                .formLogin((form) -> form.loginPage("/login/login")) // Page de login personnalisée
                .logout(log -> log.logoutSuccessUrl("/home/accueil")
                        .invalidateHttpSession(false)
                        .clearAuthentication(true)) // Configuration de la déconnexion
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.accessDeniedPage("/home/accessdenied")) // Page d'erreur en cas d'accès non autorisé
                .securityContext((securityContext) -> securityContext.requireExplicitSave(true));

        return http.build();
    }

    // Configuration du contexte de sécurité
    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // Configuration de l'encodeur de mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration du template JDBC
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Configuration des données d'authentification
    @Bean
    public AuthenticationDataContext authenticationDataContext(JdbcTemplate jdbcTemplate) {
        return new AuthenticationDataContext(jdbcTemplate);
    }

    // Configuration du gestionnaire d'utilisateurs

    @Bean(name = "userManagerBean")
    public MyUserManager userManagerBean(AuthenticationDataContext authenticationDataContext, PasswordEncoder passwordEncoder) {
        MyUserManager usermanager = new MyUserManager(authenticationDataContext, passwordEncoder);

        // Création de l'utilisateur admin s'il n'existe pas déjà
        if (usermanager.userExists("admin")) { //jai enlever le! car ca essayais de creer un user admin a chaque fois et causais une erreur de UserExists!
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            Client client= new Client("admin@email.com", "Ad", "Admin", "123 Admin Street", "819-666-8888", "Password", authorities);

            usermanager.createUser(client);
        }

        return usermanager;
    }



    // Configuration du fournisseur d'authentification
    @Bean
    MyAuthenticationProvider authenticationProvider(@Qualifier("myUserManager") MyUserManager uManager) {
        return new MyAuthenticationProvider(uManager);
    }

    // Configuration de la base de données pour les chalets
    @Bean
    public ChaletDbContext chaletDbContext(JdbcTemplate jdbcTemplate) {
        return new ChaletDbContext(jdbcTemplate);
    }
}
