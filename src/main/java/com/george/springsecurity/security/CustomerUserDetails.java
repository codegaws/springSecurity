package com.george.springsecurity.security;

import com.george.springsecurity.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //lo anoto como un service para que se agregue al contenedor de spring
@Transactional// Nos servira para hacer llamadas a la BD
@AllArgsConstructor// se crea el constructor y se inyecta
public class CustomerUserDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;//inyectamos como si fuera un autowired

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    var authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                }).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
