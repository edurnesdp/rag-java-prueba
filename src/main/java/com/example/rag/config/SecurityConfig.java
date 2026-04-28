package com.example.rag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    // Configuración de la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva la protección CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ask", "/status").authenticated() // Protege estos endpoints
                .anyRequest().permitAll() // Permite acceso público a otros endpoints
            )
            .httpBasic(httpBasic -> {}); // Habilita autenticación básica
        return http.build();
    }

    // Codificador de contraseñas para almacenar contraseñas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de usuarios en memoria para pruebas
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("password")) // Contraseña codificada
            .roles("USER") // Rol asignado
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("admin")) // Contraseña codificada
            .roles("ADMIN") // Rol asignado
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}