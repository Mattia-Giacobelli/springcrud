package com.example.springcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springcrud.repositories.IUserRepository;

@Configuration
public class ApplicationConfiguration {

    private final IUserRepository userRepo;

    public ApplicationConfiguration(IUserRepository userRepo) {

        this.userRepo = userRepo;

    }

    // UserDetailsService: Viene chiamato dall'AuthenticationProvider per andare sul
    // database e
    // recuperare l'oggetto User associato all'email inserita.
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not foud"));

    }

    // BCryptPasswordEncoder: Viene usato per prendere la password digitata
    // dall'utente,
    // criptarla e confrontarla con quella (già criptata) trovata nel database.
    @Bean
    BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    // L'AuthenticationManager viene configurato una sola volta all'inizio
    // per coordinare tutti i processi di autenticazione dell'applicazione.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }

    // AuthenticationProvider: Viene interrogato per gestire l'intera procedura di
    // verifica.

    // verifica se l'email è unica
    // DaoAuthenticationProvider è il vero e proprio "motore di verifica" (o il
    // detective) di Spring Security.
    // questo componente è specializzato nell'autenticare gli utenti recuperandoli
    // all'interno del database.

    // questo è un componente che ritorna un DaoAuthenticationProvider
    // che contiene lo username e la password

    // Quando proviamo a loggarci, il Provider passa a questo archivista l'email
    // digitata e cerca di recuperare lo User dal database".
    // L'archivista esegue la query e torna con l'oggetto User completo (che
    // contiene l'email,
    // il ruolo e la password criptata).

    @Bean
    AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;

    }

}
