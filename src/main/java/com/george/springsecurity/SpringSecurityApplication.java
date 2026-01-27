package com.george.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableWebSecurity// ya no es necesario ponerla a partir de Spring Security 5.7 viene por defecto.
public class SpringSecurityApplication {//implements CommandLineRunner

    /*@Autowired
    PasswordEncoder passwordEncoder;*/

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    //esto lo agregue para que pueda ver el password encriptado en la consola
    /*@Override
    public void run(String... args) throws Exception {
        System.out.println(passwordEncoder.encode("to_be_encoded"));
    }*/
}
