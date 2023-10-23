package com.martin.projetfinal_jsp.config;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ProjetFinalBeansConfiguration {

    // a) Méthode retournant un objet de type "JdbcTemplate"
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /// b) Méthode retournant un objet de type "ChaletDbContext"
    @Bean
    public ChaletDbContext chaletDbContext(JdbcTemplate jdbcTemplate) {
        return new ChaletDbContext(jdbcTemplate);
    }

}