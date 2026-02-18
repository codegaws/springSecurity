package com.george.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.exceptionHandling(e ->
                e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(ADMIN_RESOURCES).hasAuthority(AUTH_WRITE)
                        .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)
                        .anyRequest().permitAll());
        http.oauth2ResourceServer(oauth ->
                oauth.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
                        .requestMatchers(USER_RESOURCES).hasRole(ROLE_USER)
                        .anyRequest().permitAll());
        return http.build();
    }

    private static final String[] USER_RESOURCES = {"/loans/**", "/balance/**"};
    private static final String[] ADMIN_RESOURCES = {"/accounts/**", "/cards/**"};
    private static final String AUTH_WRITE = "write";
    private static final String AUTH_READ = "read";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final String LOGIN_RESOURCE = "/login";
}
