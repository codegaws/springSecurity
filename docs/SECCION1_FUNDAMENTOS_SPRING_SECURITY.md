# 🛡️ SECCION1_FUNDAMENTOS_SPRING_SECURITY 🛡️

## 📝 Clase 1 - Conoce el flujo de autenticacion spring secutiry 🔒 🔒 🔑🔑

![image](img/1.png)

## 📝 Clase 2 - ¿Que es la seguridad en la aplicacion web y porque usar spring security?🔒 🔒 🔑🔑

#### Introducción a Spring Security

![image](img/img.png)

## 📝 Clase 3 -Creando nuestro proyecto con spring 🔒 🔒 🔑🔑

- Se configuro el POM.XML 📦 📦  para agregar las dependencias necesarias de Spring Security y Spring Web.

## 📝 Clase 4 - Configuracion por defecto del proyecto de Spring 🔒 🔒 🔑🔑

- Se crea un controlador para hacer una prueba

- Se agrega spring security

```java
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

![image](img/img1.png)

```java
@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping
    public Map<String, String> welcome() {
        return Collections.singletonMap("message", "Welcome to Spring Security!");
    }

    /*
    public Map<String, String> welcome() {
        Map<String, String> map = new HashMap<>();
        map.put("message", "Welcome to Spring Security!");
        return map;
    }
     */

}

```

![image](img/img2.png)

---
## 📝 Clase 5 - Configurando usuarios de nuestra aplicacion 🔒 🔒 🔑🔑

https://docs.spring.io/spring-boot/appendix/application-properties/index.html

```java
spring.security.user.name=debugger
spring.security.user.password=ideas
spring.security.user.roles={ROLE_VIEWER} //aqui cuando quieres mas de un rol se usa {}

```
## 📝 Clase 6 - introduccion a filtros en springboot 🔒 🔒 🔑🔑

![image](img/1.png)

#### Resumen de filtros en springboo secutiry
# 🔐 Filtros más Comunes e Importantes de Spring Security

## 🎯 Filtros Principales

### 1. **UsernamePasswordAuthenticationFilter**
- Procesa peticiones de autenticación con usuario y contraseña.
- Captura credenciales del formulario de login (por defecto en `/login`).
- Genera un `Authentication` token y lo pasa al `AuthenticationManager`.

### 2. **BasicAuthenticationFilter**
- Maneja autenticación HTTP Basic.
- Lee credenciales codificadas en Base64 del header `Authorization`.
- Útil para APIs REST y clientes que no usan sesiones.

### 3. **BearerTokenAuthenticationFilter**
- Procesa tokens JWT o OAuth2.
- Extrae el token del header `Authorization: Bearer <token>`.
- Valida y autentica usando el token proporcionado.

### 4. **SecurityContextPersistenceFilter**
- Carga y guarda el `SecurityContext` entre peticiones.
- Usa `SecurityContextRepository` para persistir el contexto (normalmente en sesión HTTP).

### 5. **ExceptionTranslationFilter**
- Maneja excepciones de seguridad (`AuthenticationException`, `AccessDeniedException`).
- Redirige al login si no está autenticado.
- Devuelve error 403 si no tiene permisos.

### 6. **AuthorizationFilter** (antes `FilterSecurityInterceptor`)
- Verifica si el usuario tiene permisos para acceder al recurso.
- Toma decisiones de autorización basadas en roles y permisos.

### 7. **CorsFilter**
- Maneja configuración de CORS (Cross-Origin Resource Sharing).
- Permite o bloquea peticiones desde otros dominios.

### 8. **CsrfFilter**
- Protege contra ataques CSRF (Cross-Site Request Forgery).
- Valida tokens CSRF en peticiones POST, PUT, DELETE.

### 9. **LogoutFilter**
- Procesa peticiones de cierre de sesión.
- Limpia el contexto de seguridad y la sesión.

### 10. **SessionManagementFilter**
- Gestiona la creación y validación de sesiones.
- Previene fijación de sesión y controla concurrencia de sesiones.

---

## 📊 Orden de Ejecución (simplificado)

1. `SecurityContextPersistenceFilter`
2. `LogoutFilter`
3. `UsernamePasswordAuthenticationFilter` / `BasicAuthenticationFilter` / `BearerTokenAuthenticationFilter`
4. `SessionManagementFilter`
5. `ExceptionTranslationFilter`
6. `AuthorizationFilter`

---

## 💡 Notas

- Puedes agregar filtros personalizados con `addFilterBefore()` o `addFilterAfter()`.
- La cadena de filtros se configura en la clase de configuración de seguridad.
- Spring Security 6+ usa `AuthorizationFilter` en lugar de `FilterSecurityInterceptor`.

## 📝 Clase 8 - CREANDO NUEVOS ENDPOINTS 🔒 🔒 🔑🔑

- se crean los endpoint en el controlador

![image](img/img3.png)

- Ejemplo :

 ```java
 @RestController
@RequestMapping("/about_us")
public class AboutUsController {

    @GetMapping
    public Map<String, String> about() {
        //business logic
        return Collections.singletonMap("msj", "about");
    }
 ```

## 📝 Clase 9 - DANDO SEGURIDAD CON PERMITALL Y AUTENTICATHED 🔒 🔒 🔑🔑

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        // auth.anyRequest().authenticated())//aqui requiere autenticacion para cualquier solicitud
                        auth.anyRequest().permitAll())//aqui permite el acceso sin autenticacion
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

#### EXPLICACION CODIGO 📝 📝 :

# 🔐 Explicación de `@Configuration` y `@Bean`

## 🏗️ `@Configuration`
- ✅ Marca la clase como **fuente de configuración de Spring**.
- 🔍 Spring escanea esta clase al iniciar y registra los métodos anotados con `@Bean`.
- 💬 Es como decirle a Spring: "aquí hay configuraciones personalizadas".

## 🫘 `@Bean`
- ✅ Indica que el método produce un **objeto administrado por Spring** (bean).
- 🔄 Spring llama al método, guarda el objeto retornado y lo inyecta donde se necesite.
- 🛡️ En este caso, Spring usa el `SecurityFilterChain` para configurar la seguridad.

---

## 📋 Detalle del Código 🌱🌱

### 🔓 Autorización de Peticiones

```java
http.authorizeHttpRequests(auth ->
    auth.anyRequest().permitAll())
```

- 🌐 `anyRequest()`: cualquier petición HTTP (GET, POST, etc.).
- ✅ `permitAll()`: permite acceso sin autenticación.
- 🔒 Si cambias a `authenticated()`, **todas** las peticiones requerirán login.

---

### 📝 Formulario de Login

```java
.formLogin(Customizer.withDefaults())
```

- 🖥️ Habilita un formulario de login automático en `/login`.
- ⚙️ `withDefaults()`: usa configuración por defecto de Spring Security.
- ↪️ Si el usuario intenta acceder a un recurso protegido, lo redirige al formulario.

---

### 🔑 Autenticación HTTP Basic

```java
.httpBasic(Customizer.withDefaults())
```

- 📡 Permite autenticación usando header `Authorization: Basic <credenciales>`.
- 🔌 Útil para clientes REST o APIs.
- ⚙️ `withDefaults()`: usa configuración estándar.

---

### 🏗️ Construcción de la Cadena de Filtros

```java
return http.build();
```

- 🔨 `build()` crea y retorna el `SecurityFilterChain` configurado.
- 🔄 Spring lo registra y lo aplica a todas las peticiones HTTP.

---

## 📌 Resumen

- ⚙️ `@Configuration` + `@Bean` permite personalizar la configuración de seguridad.
- 🔓 `permitAll()` permite acceso sin autenticación.
- 📝 `formLogin()` habilita formulario de login.
- 🔑 `httpBasic()` permite autenticación básica HTTP.
- ✨ Spring aplica esta configuración automáticamente a tu aplicación.
```
### detalle sobre @BEAN
El `@Bean` **crea un objeto personalizado** que Spring administra como si fuera suyo.

## 🔍 Diferencia Clave

### ❌ Sin `@Bean`
- Spring solo administra sus propios objetos (como `@Component`, `@Service`, `@Repository`).
- No puedes configurar manualmente cómo se crea un objeto.

### ✅ Con `@Bean`
- **Tú defines** cómo se crea el objeto (con tu lógica personalizada).
- Spring toma ese objeto y lo administra (lo guarda en su contenedor).
- Luego lo puede inyectar donde se necesite.

---

## 📦 En tu caso

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Tu configuración personalizada
    http.authorizeHttpRequests(...)
        .formLogin(...)
        .httpBasic(...);
    return http.build(); // Devuelves el objeto configurado
}
```

- `SecurityFilterChain` **no es un objeto estándar de Spring**.
- **Tú lo configuras** con tus reglas de seguridad.
- Spring lo toma y lo aplica automáticamente a todas las peticiones HTTP.

---

## 💡 Resumen

`@Bean` = "Spring, toma este objeto que **yo** creé y adminístralo como si fuera tuyo".

Es una forma de decirle a Spring: "usa **mi** configuración personalizada en lugar de la por defecto".

## 📝 Clase 10 - PROTEGIENDO ENDPOINTS CON REQUESTMATCHERS🔒 🔒 🔑🔑

```java
@Configuration
public class SecurityConfig {
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth ->
                    // auth.anyRequest().authenticated())//aqui requiere autenticacion para cualquier solicitud
                    //auth.anyRequest().permitAll())//aqui permite el acceso sin autenticacion
            auth.requestMatchers("/loans","/balance","/accounts","/cards"))//aqui protege el endpoint loans y cuando pongo ** protege todo lo que esta seguido de loans
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());
    return http.build();
    }
}
```
---
## 📝 Clase 11 - github 🔒 🔒 🔑🔑

- Da algo de informacion sobre el uso de GitHub.

