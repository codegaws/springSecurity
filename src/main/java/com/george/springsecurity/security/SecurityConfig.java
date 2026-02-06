package com.george.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");//-> Resumen: Es básicamente un "alias" o "nombre de variable" para que tanto backend como frontend sepan cómo referirse al token CSRF. con el que vamos a trabajar en el front end por convencion se usa _csrf
        http.authorizeHttpRequests(auth ->
                        //auth.requestMatchers("/loans", "/balance", "/accounts", "/cards")
                        auth
                                .requestMatchers("/loans").hasAuthority("VIEW_LOANS")
                                .requestMatchers("/balance").hasAuthority("VIEW_BALANCE")
                                .requestMatchers("/cards").hasAuthority("VIEW_CARDS")
                                //.requestMatchers("/accounts").hasAnyAuthority("VIEW_ACCOUNT","VIEW_CARDS")
                                .anyRequest()
                                .permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        http.cors(cors -> corsConfigurationSource());
        http.csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/welcome", "/about_us")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();//esta deprecado es fake solo es para pruebas solo es de momento, COMO NO TIENE CONSTRUCTOR POR ESO SE LE AGREGA EL METODO getInstance
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        //config.setAllowedOrigins(List.of("http://localhost:4200/"));//-> aqui se define que pagina esta permitida
        config.setAllowedOrigins(List.of("*"));//-> esto quiere decir que cualquier pagina esta permitida
        //config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);//-> esto quiere decir que cualquier endpoint esta permitido
        return source;
    }
}
