# 🛡️ SECCION2 USER DETAILS SERVICE🛡️

## 📝 Clase 13 - INTRODUCCION 🔒 🔒 🔑🔑

![image](img/img4.png)

![image](img/img5.png)

## 📝 Clase 14 - CREANDO USUARIOS EN MEMORIA 🔒 🔒 🔑🔑

### 📝 No vamos a trabajar con user y pass en application.properties
```java
# Configuracion cuando se trabaja en modo practicas en memoria de usuarios
#spring.security.user.name=debugger
#spring.security.user.password=ideas
#spring.security.user.roles={ROLE_VIEWER}
```

### 📝Dato adicional: 
- ¿por que usar var?

Sí, puedes usar `var` desde Java 10 en adelante (incluyendo Java 17). Cuando usas `var`, el compilador de Java infiere el tipo de la variable 
a partir del valor que se le asigna. En este caso, el método `User.withUsername(...).password(...).authorities(...).build()` retorna un objeto de 
tipo `UserDetails`, por lo que el compilador infiere que `var admin` y `var user` son de tipo `UserDetails`.

Ejemplo:

```java
@Bean
InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    var admin = User.withUsername("admin")
            .password("to_be_encode")
            .authorities("ADMIN")
            .build();

    var user = User.withUsername("user")
            .password("to_be_encode")
            .authorities("USER")
            .build();
    return new InMemoryUserDetailsManager(admin, user);
}
```

El tipo real de `admin` y `user` sigue siendo `UserDetails`. El compilador lo detecta automáticamente
por el valor de retorno del método `build()`.

## 📝 Clase 15 - PROBANDO IN MEMORY USER DETAILS 🔒 🔒 🔑🔑

cuando ponemos el usiario y contraseña correctos me sale un error por que no hemos configurado el password encoder

![image](img/img_2.png)

- Te muestra un error por que no se ha configurado el password encoder

![image](img/img_1.png)

- Solucion : configurar el password encoder cremaos primero un @Bean con una interfaz que esta deprecada pero funciona bien
- NoOpPasswordEncoder.getInstance() // OJO SOLO SIRVE PARA PRUEBAS NO PARA PRODUCCION

```java
 @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();//esta deprecado es fake solo es para pruebas solo es de momento
    }
```

## 📝 Clase 16-17 - CREANDO BASE DE DATOS 🔒 🔒 🔑🔑
- hemos creado documento YML de docker para una base de datos POSTGRESQL

```yml
version: '3.8'

services:

  db:
    image: postgres:15.2
    container_name: security_bank
    restart: always
    volumes:
      - ./db/sql/create_schema.sql:/docker-entrypoint-initdb.d/create_schema.sql
      - ./db/sql/data.sql:/docker-entrypoint-initdb.d/data.sql
    environment:
      - POSTGRES_DB=security_bank
      - POSTGRES_USER=alejandro
      - POSTGRES_PASSWORD=debuggeandoideas
    ports:
      - "5432:5432"
```
- schemas
```sql
create table users(
                      username varchar(50) not null primary key,
                      password varchar(500) not null,
                      enabled boolean not null
);

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (
                                                     username,
                                                     authority);
```
- data
```sql
insert into users (username, password, enabled) VALUES
                                                    ('admin', 'to_be_encoded', true),
                                                    ('user', 'to_be_encoded', true);

insert into authorities (username, authority) VALUES
                                                  ('admin', 'admin'),
                                                  ('user', 'user');
```
![image](img/img_4.png)

---
## 📝 Clase 18 - CONFIGURANDO CONEXION DE BASE DE DATOS CON SPRINGBOOT 🔒 🔒 🔑🔑

- Buscar JDBC driver en maven repository agregarlo a pomxml y spring jdbc

```xml
<!-- Source: https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.9</version>
    <scope>compile</scope>
</dependency>
```
- Aquitar la version que tengas de spring boot starter data jpa para evitar conflictos
```xml
<!-- Source: https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

- Configurar nuestro DATA SOURCE en application.properties

```properties
# Configuracion de la base de datos Postgresql
```xml

spring.datasource.url=jdbc:postgresql://localhost:5432/security_bank
spring.datasource.username=alejandro
spring.datasource.password=debuggeandoideas
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=5

```

## 📝 Clase 19 - IMPLEMENTANDO JDBCUSERDETAILSMANAGER🔒 🔒 🔑🔑

- Springboot tiene una base de datos por defecto para trabajar con jdbcUserDetailsManager
- Si queremos usar nuestra propia base de datos debemos crear un @Bean de tipo jdbcUserDetails 
- Si sale el error en dataSource agregamos la dependencia de spring boot starter jdbc
```java
@Bean
JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
}
```
- Considera que hay una condicion para implementar el jdbcUserDetailsManager debes usar el mismo usuario y password que estan en la base de datos

https://github.com/spring-projects/spring-security/blob/main/core/src/main/resources/org/springframework/security/core/userdetails/jdbc/users.ddl

![image](img/img_5.png)

- Si todo esta bien configurado ya podemos iniciar sesion con los usuarios que estan en la base de datos tal cual

![image](img/img_6.png)

- Entonces toca poder configurar nuestro propio password encoder con userdetails.

---

## 📝 Clase 20 - Implementando base de datos personalizada 🔒 🔑🔑

- Springboot tiene una base de datos por defecto para trabajar con jdbcUserDetailsManager
- Si queremos usar nuestra propia base de datos debemos crear un @Bean de tipo jdbcUserDetails 
- Cambiamos la base de datos y creamos una tabla customers

```sql
create table customers(
                          id bigserial primary key,
                          email varchar(50) not null,
                          pwd varchar(500) not null,
                          rol varchar(20) not null);

````

```sql
insert into customers (email, pwd, rol)
VALUES ('super_user@debuggeandoieas.com', 'to_be_encoded', 'admin'),
       ('basic_user@debuggeandoieas.com', 'to_be_encoded', 'user');
```
![image](img/img_7.png)

---
## 📝 Clase 21 - Implementando JPA para Customers🔒 🔑🔑
- agregamos lombok a pomxml
```xml
<!-- Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.42</version>
            <scope>provided</scope>
        </dependency>
```
- agregamos JPA a pomxml
```xml
<!-- Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

```

- agregamos entities -> CustomerEntity.java
- agregamos repositorio de la entidad  -> CustomerEntity.java

```java
@Entity
@Table(name = "customers")
@Data
public class CustomerEntity implements Serializable {//todo objeto que viaja por la red es bueno que se implemente serializable

    @Id
    private BigInteger id;
    private String email;
    @Column(name = "pwd")
    private String password;
    @Column(name = "rol")
    private String role;


}
```
----
## 📝 Clase 22 - 👤👤 Creando nuestra propia implementacion de UserDetailsService 🔒 🔑🔑

- Creamos nuestra propia implementacion de UserDetailsService llamado CurstomerUserDetails

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
#### Explicacion sobre el codigo 📝📝
## 🔍 Análisis detallado del flujo de `loadUserByUsername`

#### 🎯 **Contexto general**

Este método es el **corazón de la autenticación** en Spring Security. Se ejecuta **automáticamente** cuando un usuario intenta iniciar sesión.

---

## 📊 **Flujo completo paso a paso**

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    return this.customerRepository.findByEmail(username)  // 1️⃣
            .map(customer -> {                             // 2️⃣
                var authorities = List.of(new SimpleGrantedAuthority(customer.getRole())); // 3️⃣
                return new User(customer.getEmail(), customer.getPassword(), authorities); // 4️⃣
            }).orElseThrow(() -> new UsernameNotFoundException("user not found")); // 5️⃣
}
```

---

### 1️⃣ **`findByEmail(username)` - Búsqueda en BD**

| Acción | Descripción |
|--------|-------------|
| **Entrada** | `username` (String) - ejemplo: `"user@example.com"` |
| **Consulta SQL** | `SELECT * FROM customers WHERE email = 'user@example.com'` |
| **Retorno** | `Optional<CustomerEntity>` - puede estar vacío o contener el cliente |

```java
// Si existe el usuario en BD:
Optional<CustomerEntity> = Optional.of(CustomerEntity)

// Si NO existe:
Optional<CustomerEntity> = Optional.empty()
```

---

### 2️⃣ **`.map(customer -> {...})` - Stream sobre Optional**

#### 📌 **¿Qué es `.map()` en `Optional`?**

**NO es un stream tradicional**, es un método de `Optional<T>` que:

- **Si el Optional contiene un valor**: ejecuta la lambda y transforma el valor
- **Si el Optional está vacío**: no ejecuta nada y devuelve `Optional.empty()`

```java
// Ejemplo conceptual:
Optional<CustomerEntity> optCustomer = findByEmail("user@example.com");

if (optCustomer.isPresent()) {
    CustomerEntity customer = optCustomer.get(); // ✅ Extraído automáticamente por .map()
    // Ahora ejecuta la lambda con 'customer'
}
```

---

### 3️⃣ **`var authorities = List.of(...)` - Creación de autoridades**

```java
var authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
```

#### 🔹 **¿Qué valor toma `var`?**

El compilador infiere:

```java
List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("admin"));
```

#### 🔹 **¿Por qué es una lista?**

Porque Spring Security permite **múltiples roles/permisos por usuario**:

```java
// Ejemplo con múltiples roles:
List.of(
    new SimpleGrantedAuthority("ROLE_ADMIN"),
    new SimpleGrantedAuthority("ROLE_USER"),
    new SimpleGrantedAuthority("PERMISSION_READ")
);
```

#### 🔹 **¿Por qué hay un `new` dentro?**

Porque `SimpleGrantedAuthority` es una **clase concreta** que implementa `GrantedAuthority`:

```java
public class SimpleGrantedAuthority implements GrantedAuthority {
    private final String authority;
    
    public SimpleGrantedAuthority(String authority) {
        this.authority = authority;
    }
}
```

#### ✅ **Sí, `SimpleGrantedAuthority` siempre recibe un String**

Ese String representa el nombre del rol/permiso:

```java
new SimpleGrantedAuthority("admin")      // ✅ Correcto
new SimpleGrantedAuthority("ROLE_USER")  // ✅ Correcto
new SimpleGrantedAuthority(123)          // ❌ Error de compilación
```

---

### 4️⃣ **`new User(...)` - Construcción del UserDetails**

```java
return new User(customer.getEmail(), customer.getPassword(), authorities);
```

#### 🔹 **¿Por qué 3 parámetros?**

Este es el constructor de `org.springframework.security.core.userdetails.User`:

```java
public User(String username, String password, Collection<? extends GrantedAuthority> authorities)
```

| Parámetro | Valor en tu código | Descripción |
|-----------|-------------------|-------------|
| **1. username** | `customer.getEmail()` | Identificador único del usuario |
| **2. password** | `customer.getPassword()` | Contraseña (debe estar encriptada) |
| **3. authorities** | `authorities` | Lista de roles/permisos |

#### 📌 **Otros constructores disponibles**

```java
// Constructor con 6 parámetros (más control):
new User(
    username,
    password,
    enabled,           // true/false - cuenta activa
    accountNonExpired, // true/false
    credentialsNonExpired,
    accountNonLocked,
    authorities
);
```

---

### 5️⃣ **`.orElseThrow(...)` - Manejo de usuario no encontrado**

```java
.orElseThrow(() -> new UsernameNotFoundException("user not found"));
```

Si `findByEmail()` devolvió `Optional.empty()`:

- No se ejecuta `.map()`
- Se lanza `UsernameNotFoundException`
- Spring Security intercepta esta excepción y muestra error de login

---

## 🔄 **Diagrama de flujo completo**

```
┌─────────────────────────────────────┐
│ Usuario ingresa:                    │
│ - Email: admin@example.com          │
│ - Password: secreto123              │
└───────────────┬─────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│ Spring Security llama:              │
│ loadUserByUsername("admin@...com")  │
└───────────────┬─────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│ 1. findByEmail() consulta BD        │
│    SELECT * FROM customers          │
│    WHERE email = 'admin@...com'     │
└───────────────┬─────────────────────┘
                │┌────────┴────────┐
       │                 │
   ✅ Encontrado     ❌ No existe
       │                 │
       ▼                 ▼
┌─────────────────┐  ┌─────────────────────┐
│ 2. .map()       │  │ 5. .orElseThrow()   │
│ ejecuta lambda  │  │ lanza excepción     │
└────┬────────────┘  └─────────────────────┘
     │
     ▼
┌─────────────────────────────────────┐
│ 3. Crea authorities:                │
│    List.of(new SimpleGrantedAuthority│
│          ("admin"))                  │
└───────────────┬─────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│ 4. Retorna nuevo User:              │
│    username: admin@example.com      │
│    password: $2a$10$... (hash)      │
│    authorities: [admin]             │
└───────────────┬─────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│ Spring Security compara contraseñas │
│ usando PasswordEncoder              │
└─────────────────────────────────────┘
```

---

## 💡 **Ejemplo práctico con datos reales**

```java
// Entrada: 
loadUserByUsername("admin@debuggeandoideas.com")

// 1. Consulta BD retorna:
CustomerEntity {
    id: 1,
    email: "admin@debuggeandoideas.com",
    password: "$2a$10$abcd...",
    role: "admin"
}

// 2. .map() ejecuta lambda

// 3. Crea authorities:
List<SimpleGrantedAuthority> authorities = [
    SimpleGrantedAuthority(authority="admin")
]

// 4. Retorna User:
User {
    username: "admin@debuggeandoideas.com",
    password: "$2a$10$abcd...",
    authorities: [SimpleGrantedAuthority(authority="admin")],
    accountNonExpired: true,
    accountNonLocked: true,
    credentialsNonExpired: true,
    enabled: true
}
```

---

## 📋 **Resumen de conceptos clave**

| Concepto | Explicación |
|----------|-------------|
| **`.map()` en Optional** | Transforma el valor si existe, no es un stream tradicional |
| **`var authorities`** | Tipo inferido: `List<SimpleGrantedAuthority>` |
| **`new` dentro de `List.of()`** | Instancia una clase que implementa `GrantedAuthority` |
| **3 parámetros en `User`** | username, password, authorities (mínimo requerido) |
| **`SimpleGrantedAuthority(String)`** | ✅ Siempre recibe un String con el nombre del rol |

---
#### Nota 📝📝

#### **¿Por qué `@Service` agrega la clase al contenedor de Spring?**🧩

`@Service` es una **anotación de estereotipo** que marca la clase como un **componente administrado por Spring**. Cuando Spring Boot arranca:

1. **Escanea** el classpath buscando clases con anotaciones como `@Component`, `@Service`, `@Repository`, `@Controller`
2. **Crea una instancia** de `CustomerUserDetails` automáticamente
3. **La registra** como un **bean** en el contenedor de Spring
4. Spring puede **inyectarla** donde se necesite (por ejemplo, en la configuración de seguridad)

#### 🧩**¿Qué es el contenedor de Spring (Application Context)?**

Es un **registro central** donde Spring almacena y administra todos los objetos (beans) de tu aplicación:

- **Crea** los objetos automáticamente (no usas `new`)
- **Gestiona** sus dependencias (inyección automática)
- **Controla** su ciclo de vida (cuándo se crean, destruyen)
- **Resuelve** las relaciones entre beans (autowiring)

#### 🧩**Ejemplo práctico:**

```java
// Sin Spring (manual):
CustomerRepository repo = new CustomerRepository(); // ❌ Tú creas todo
CustomerUserDetails userDetails = new CustomerUserDetails(repo);

// Con Spring (automático):
@Service // ✅ Spring lo crea y lo gestiona
public class CustomerUserDetails {
    // Spring inyecta CustomerRepository automáticamente
    private final CustomerRepository customerRepository;
}
```

#### 🧩**Otras anotaciones de estereotipo:**

- `@Component` — componente genérico
- `@Service` — lógica de negocio (semánticamente más claro)
- `@Repository` — acceso a datos (BD)
- `@Controller` — controladores web

Todas hacen lo mismo técnicamente, pero `@Service` comunica mejor la **intención**
de que esta clase contiene lógica de servicio.

#### 🧩**Resumen:** `@Service` le dice a Spring *"Toma esta clase, créala automáticamente y 
ponla disponible para que otras clases la usen sin que yo tenga que instanciarla manualmente"*. 
El contenedor es donde Spring guarda todos esos objetos creados.
