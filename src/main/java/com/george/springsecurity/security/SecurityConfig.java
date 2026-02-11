package com.george.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    RegisteredClientRepository clientRepository() {
        var client = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("debugueandoIdeas")
                .clientSecret("secret")
                .scope("read")
                .redirectUri("http://localhost:8080")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryRegisteredClientRepository(client);// muy porecido a InMemoryUserDetails- problema seria un cliente estatico no se puede mover en runtime
    }
}
