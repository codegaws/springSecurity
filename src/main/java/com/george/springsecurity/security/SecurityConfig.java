package com.george.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        // auth.anyRequest().authenticated())//aqui requiere autenticacion para cualquier solicitud
                        //auth.anyRequest().permitAll())//aqui permite el acceso sin autenticacion
                        auth.requestMatchers("/loans", "/balance", "/accounts", "/cards")
                                .authenticated()
                                .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
