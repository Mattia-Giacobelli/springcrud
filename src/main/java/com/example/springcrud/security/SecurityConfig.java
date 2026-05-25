package com.example.springcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {

        this.authenticationProvider = authenticationProvider;

    }

    // Security filter chain
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Autorizzazzioni per le rotte
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/home/**").hasRole("ADMIN")
                        .requestMatchers("/employees/**").hasRole("ADMIN")
                        .requestMatchers("/projects", "/projects/**").hasRole("ADMIN")
                        .requestMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                // indicare il componente per l'autenticazione
                .authenticationProvider(authenticationProvider)

                // Ricezione richiesta di login
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(roleBasedSuccessHandler())
                        .failureUrl("/login?error") // Se la compilzaione e errata rimanda al login con parametro error
                )

                // la rotta logut in post cancella la sessione e il relativo csrf
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login?logout") // parametro logout per eventuali messaggi front
                )

                // Gestisce le eccezzioni e rimanda alla pagina di errore
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"));

        // Costruisce un oggetto con le regole precedenti e lo passa a spring per la
        // gestione della sicurezza
        return http.build();

    }

    // Verifica dei permessi e redirect in base al ruolo solo se passa
    // l'autenticazione
    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {

        return (request, response, authentication) -> {

            var auths = authentication.getAuthorities(); // Prende tutti i ruoli

            System.out.println("--- RUOLI TROVATI PER L'UTENTE AUTENTICATO ---");
            auths.forEach(a -> System.out.println("Ruolo letto: " + a.getAuthority()));
            System.out.println("----------------------------------------------");

            boolean isAdmin = auths.stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
            boolean isUser = auths.stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_USER"));

            if (isAdmin) {

                response.sendRedirect("/home");

            } else if (isUser) {

                response.sendRedirect("/user/profile");

            } else {

                response.sendRedirect("/login");

            }

        };

    }

}
