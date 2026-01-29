package com.george.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

    /*@Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        // UserDetails admin = User.withUsername("admin") o solo usas var
        var admin = User.withUsername("admin")
                .password("to_be_encode")
                .authorities("ADMIN")
                .build();

        //UserDetails user = User.withUsername("user")
        var user = User.withUsername("user")
                .password("to_be_encode")
                .authorities("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }*/

    //Como ya he realizado una implementacion personalizada de UserDetailsService
    //ahora uso JdbcUserDetailsManager para obtener los detalles de los usuarios desde la base de datos
   /* @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

*/
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();//esta deprecado es fake solo es para pruebas solo es de momento, COMO NO TIENE CONSTRUCTOR POR ESO SE LE AGREGA EL METODO getInstance
    }

    /*
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    */

    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

    }

}
