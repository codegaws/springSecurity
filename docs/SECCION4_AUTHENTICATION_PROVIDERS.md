# 🛡️ SECCION4 AUTHENTICATION PROVIDER🛡️

## 📝 Clase 31 - QUE SON LOS AUTHENTICATION PROVIDERS 🔒 🔒 🔑🔑

¡Buena pregunta! El concepto de **Authentication Provider** es FUNDAMENTAL en **Spring Security**. Aquí va una
explicación sencilla:

---

## ¿Qué es un Authentication Provider?

Un **Authentication Provider** es un **componente** de Spring Security encargado de **autenticar** (validar) los datos
que un usuario envía al hacer login (usuario + contraseña, tokens, etc.).

Piénsalo como una “estrategia” que sabe cómo validar credenciales.

---

### 🎯 **¿Por qué existen?**

- Spring Security es muy flexible: no siempre autenticas solo con usuario + password, ¡podrías usar tokens, LDAP, OAuth,
  etc.!
- Cada fuente o mecanismo de autenticación tiene su **AuthenticationProvider** responsable.

---

### 🧩 **Algunos tipos de Providers comunes:**

- **DaoAuthenticationProvider**
    - El más usado.
    - Usa tu `UserDetailsService` + `PasswordEncoder` para validar usuario y password contra la BD.
- **JwtAuthenticationProvider**
    - Usado para tokens JWT.
- **LdapAuthenticationProvider**
    - Autentica usuarios contra un servidor LDAP.
- **CustomAuthenticationProvider**
    - Puedes crear el tuyo si tienes un método especial de validación.

---

### 🔗 **¿Cómo se conecta todo?**

- Cuando un usuario intenta login, Spring Security pasa los datos a una **lista de AuthenticationProviders**.
- Cada uno “intenta” autenticar al usuario.
- Si uno dice “¡válido!”: autenticación exitosa.
- Si *ninguno* puede: login fallido.

---

### 📝 **Ejemplo con DaoAuthenticationProvider (el clásico)**

Supón que tienes esto en tu configuración:

```java

@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(customerUserDetails);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
}
```

- Aquí configuras el **provider** para usar tus clases personalizadas.
- Así Spring sabe **cómo** validar los usuarios y contra qué fuente de datos.

---

### 🏗️ **¿Dónde “vive” este provider?**

- Se lo agregas a tu configuración de seguridad (SecurityConfig).
- Spring puede tener una o varias estrategias de autenticación.

---

## **🚦 En resumen:**

- Los **Authentication Providers** son componentes que validan los intentos de autenticación.
- El más común usa tus usuarios en la base de datos (`DaoAuthenticationProvider`).
- Son súper útiles cuando tienes múltiples formas de login o seguridad avanzada.

---

¿Quieres que te muestre cómo configurar varios `AuthenticationProvider` juntos?
¿O cómo hacer uno personalizado para validar códigos, tokens u otra cosa?

## 📝 Clase 32 - IMPLEMENTANDO NUESTRO AUTHENTICATIONPROVIDER PERSONALIZADO PARTE 1 🔒 🔒 🔑🔑

- Guardamos nuestro CustomerUserDetails que implementa UserDetailsService para cargar los datos del usuario desde la
  base de datos:

```java

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
```

- ### En SecurityConfig

```java

@Bean
PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();//esta deprecado es fake solo es para pruebas solo es de momento, COMO NO TIENE CONSTRUCTOR POR ESO SE LE AGREGA EL METODO getInstance
}

```

- ### En MyAuthenticationProvider se crea nuestro AuthenticationProvider personalizado:

```java
import com.george.springsecurity.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Component
@AllArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;

    @Override

    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var username = authentication.getName();
        final var pwd = authentication.getCredentials().toString();

        final var customerFromDb = this.customerRepository.findByEmail(username);
        final var customer = customerFromDb.orElseThrow(() -> new BadCredentialsException("Ivalid credentials!!"));
        final var customerPwd = customer.getPassword();

        if (passwordEncoder.matches(pwd, customerPwd)) {
            final var authorities = Collections.singletonList(new SimpleGrantedAuthority(customer.getRole()));
            return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

```

