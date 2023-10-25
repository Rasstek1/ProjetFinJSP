package com.martin.projetfinal_jsp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class BeansSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        //org.springframework.security.crypto.password.PasswordEncoder
        BCryptPasswordEncoder bCryptEnc= new BCryptPasswordEncoder();
        return bCryptEnc;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/home/admin")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/home/usager")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/accueil")).permitAll()

                            .anyRequest().permitAll();
                })
                .formLogin(form -> form
                        .loginPage("/connexion") // Définir la page de connexion personnalisée
                        .failureUrl("/connexion?error=true") // URL de redirection en cas d'échec de connexion
                        .successForwardUrl("/accueil") // URL de redirection après une connexion réussie
                        .permitAll()
                )

                // Activer la protection CSRF
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                });

        return http.build();
    }



    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}



   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/chalets/**")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/chalets/listeChalets")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/chalets/changementMotDePasse")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/chalets/logout")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/chalets/accueil")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/chalets/connexion")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/chalets/inscription")).permitAll()


                            .anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/chalets/connexion")
                        .failureUrl("/chalets/connexion?error=true")
                        .successForwardUrl("/chalets/accueil")
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/chalets/logout").permitAll())
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                });

        return http.build();
    }*/