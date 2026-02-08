# 🛡️ 🌐🔒 Spring Security — JSON WEB TOKEN (JWT) 🔐🔐🔑🔑

## 📝 Clase 53 - INTRODUCCION A LOS JWTs 👤👤🕵️‍♂🕵️‍♂🔑 🔑 

- JWT se divide en HEADER PAYLOAD y SIGNATURE
- HEADER: contiene el tipo de token y el algoritmo de cifrado
- PAYLOAD: contiene la información del usuario y las reclamaciones (claims)
- SIGNATURE: se genera a partir del HEADER y el PAYLOAD utilizando una clave secreta
- JWT se utiliza para autenticar y autorizar a los usuarios en aplicaciones web
- JWT es un estándar abierto (RFC 7519) que define un formato compacto y autónomo para transmitir información entre partes como un objeto JSON
- JWT se puede utilizar en aplicaciones web, móviles y de escritorio para autenticar a los usuarios y autorizar el acceso a recursos protegidos
- JWT es una alternativa a las sesiones tradicionales y
- permite una mayor escalabilidad y flexibilidad en la gestión de la autenticación y autorización de los usuarios

# 📊 JWT — Resumen en Cuadros

## 🔐 **1. Características Técnicas de JWT**

| Aspecto | Descripción |
|---------|-------------|
| **Algoritmos de cifrado** | HMAC, RSA, ECDSA |
| **Almacenamiento en cliente** | `localStorage`, `cookies`, `sessionStorage` |
| **Transmisión** | Header `Authorization: Bearer <token>` en cada request |
| **Arquitectura** | Stateless (sin estado) - servidor NO guarda sesiones |
| **Ventajas** | ✅ Escalabilidad<br>✅ Mejor rendimiento<br>✅ Distribuible entre microservicios |

---

## 👥 **2. Modelos de Control de Acceso con JWT**

| Modelo | ¿Qué controla? | Ejemplo en JWT |
|--------|----------------|----------------|
| **Role-Based (RBAC)** | Acceso por **roles** | `"roles": ["ADMIN", "USER"]` |
| **Permission-Based** | Acceso por **permisos** específicos | `"permissions": ["READ", "WRITE", "DELETE"]` |
| **Claims-Based** | Acceso por **atributos/reclamaciones** | `"department": "IT", "level": "senior"` |
| **Token-Based** | Validez del **token mismo** | Verificación de firma + expiración |

---

## 🌐 **3. Integraciones de JWT con Protocolos de Autenticación**

| Protocolo | ¿Qué hace? | Rol de JWT |
|-----------|------------|------------|
| **OAuth 2.0** | Autorización delegada (login con Google/GitHub) | JWT usado como **Access Token** |
| **OpenID Connect** | Capa de identidad sobre OAuth 2.0 | JWT usado como **ID Token** |
| **SAML** | SSO empresarial (XML-based) | JWT puede reemplazar tokens SAML |
| **LDAP** | Autenticación contra directorio (ej: empresa) | JWT generado tras validar credenciales LDAP |
| **Active Directory** | Autenticación Windows/empresarial | JWT creado tras autenticación AD |
| **SSO** | Un login → múltiples aplicaciones | JWT compartido entre aplicaciones |

---

## 🔑 **4. Tipos de Tokens JWT**

| Tipo de Token | Propósito | Duración típica | ¿Se renueva? |
|---------------|-----------|-----------------|--------------|
| **Access Token** | Autenticar requests API | 15min - 1h | ❌ (expira rápido) |
| **Refresh Token** | Renovar Access Token sin login | 7 días - 30 días | ✅ (puede generar nuevos Access) |
| **ID Token** (OpenID) | Información de identidad del usuario | Similar a Access | ❌ |
| **Revocation Token** | Invalidar tokens antes de expiración | N/A | ➖ (lista negra en servidor) |

---

## 🔒 **5. Métodos de Autenticación Avanzados con JWT**

| Método | ¿Qué valida? | Ejemplo |
|--------|--------------|---------|
| **MFA (Multi-Factor)** | Token + código SMS/email/app | JWT generado SOLO tras 2º factor |
| **Biométrica** | Huella/Face ID + JWT | App móvil valida biometría → genera JWT |
| **Certificados digitales** | Certificado X.509 + JWT | Usado en ambientes empresariales |
| **Clave Pública/Privada** | Firma asimétrica (RSA/ECDSA) | JWT firmado con clave privada, validado con pública |

---

## 🎯 **6. Flujo Completo: Access + Refresh Tokens**

```
┌─────────────┐                 ┌──────────────────┐
│   CLIENTE   │                 │     SERVIDOR     │
└──────┬──────┘                 └────────┬─────────┘
       │                                 │
       │ 1. POST /login (user+pass)      │
       │ ──────────────────────────────► │
       │                                 │ 2. Valida credenciales
       │ 3. Respuesta:                   │
       │    { "accessToken": "...",      │
       │      "refreshToken": "..." }    │
       │ ◄───��────────────────────────── │
       │                                 │
       │ ═════ REQUESTS NORMALES ═══════│
       │                                 │
       │ 4. GET /api/data                │
       │    Authorization: Bearer <access>│
       │ ──────────────────────────────► │ ✅ Token válido
       │                                 │
       │ ═════ DESPUÉS DE 15 MIN ════════│
       │                                 │
       │ 5. GET /api/data                │
       │    Authorization: Bearer <access>│
       │ ──────────────────────────────► │ ❌ Token expirado
       │                                 │
       │ 6. Error 401 Unauthorized       │
       │ ◄────────────────────────────── │
       │                                 │
       │ 7. POST /auth/refresh           │
       │    { "refreshToken": "..." }    │
       │ ──────────────────────────────► │ 8. Valida refresh token
       │                                 │
       │ 9. Nuevo accessToken            │
       │ ◄────────────────────────────── │
       │                                 │
       │ 10. Reintentar con nuevo token  │
       │ ──────────────────────────────► │ ✅
```

---

## 📌 **7. Resumen Ultra-Compacto**

| Categoría | Características clave |
|-----------|----------------------|
| **Seguridad** | HMAC, RSA, ECDSA |
| **Almacenamiento** | localStorage, cookies |
| **Arquitectura** | Stateless, escalable |
| **Control de acceso** | Roles, permisos, claims |
| **Tokens** | Access (corto) + Refresh (largo) |
| **Protocolos** | OAuth2, OpenID, SAML, LDAP, AD |
| **Autenticación avanzada** | MFA, biometría, certificados |

---

## 💡 **Lo que DEBES recordar para entrevistas**

```
JWT = Header + Payload + Signature

✅ Stateless (servidor no guarda sesión)
✅ Self-contained (token contiene toda la info)
✅ Firmado (integridad verificable)
❌ NO cifrado por defecto (usa HTTPS siempre)
❌ NO puede revocarse fácilmente (usar refresh tokens)
```
**Regla de oro**: Access Token corto (15min) + Refresh Token largo (7 días) = Balance perfecto entre seguridad y UX.


![img](img/img_33.png)

---

![img](img/img_34.png)


---

![img](img/img_35.png)

---
## 📝 Clase 54 - AÑADIENDO LIBRERIAS PARA TRABAJAR CON JWT 👤👤️‍♂🕵️‍♂🔑 🔑 
- Se agregan 3 dependencias para trabajar con JWT en Spring Boot, todas de la biblioteca `io.jsonwebtoken` (JJWT):

```xml
   <!-- Source: https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Source: https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Source: https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.5</version>
        </dependency>
        
   ```

## 📝 Clase 55 -JWT USER DETAILS 👤👤️‍♂🕵️‍♂🔑 🔑 
- Creamos un JwtUserDetailService que implementa UserDetailsService para cargar los detalles del usuario desde la base de datos y convertirlos en un objeto UserDetails que Spring Security pueda usar para la autenticación y autorización basada en JWT.

```java
@Service
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    final var authorities = customer.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList();
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                }).orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        
    }
}


```
### ✅ Explicación del método `loadUserByUsername`Este método busca un usuario por su email y lo transforma en un `UserDetails` que Spring Security entiende.

---

#### 🧭 Flujo general1. 🔎 Busca el cliente por email en el repositorio.2. 🧩 Si existe, convierte sus roles en `GrantedAuthority`.3. 👤 Crea un objeto `User` de Spring Security.4. ❌ Si no existe, lanza una excepción.

---

### 🔁 ¿Qué transforman los dos `map`?

| Lugar | `map` | Entrada | Salida |
|---|---|---|---|
| `Optional.map(...)` | Optional | `Customer` | `UserDetails` |
| `Stream.map(...)` | Stream | `Role` | `SimpleGrantedAuthority` |

---

### 🧠 Detalle de cada `map`

####1️⃣ `Optional.map(...)`✅ **Transforma un `Customer` en un `UserDetails`**Se ejecuta solo si el cliente existe.

---

####2️⃣ `Stream.map(...)`✅ **Transforma cada `Role` en `SimpleGrantedAuthority`**Se usa para crear la lista de permisos que necesita Spring Security.

---

### 📌 Ejemplo conceptual (mismo escenario)
- Email: `alice@mail.com`- Roles: `ROLE_USER`, `ROLE_ADMIN`**Resultado final**:Un `UserDetails` con email, contraseña cifrada y dos autoridades.

---
# 🔐 Explicación Detallada: `JwtUserDetailService`

---

## 🎯 ¿Qué hace este servicio?

Este servicio actúa como **puente** entre tu base de datos y Spring Security. Busca un usuario por email y lo convierte en un formato que Spring Security entiende (`UserDetails`).

---

## 🧩 Anatomía del Método `loadUserByUsername`

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.customerRepository.findByEmail(username)  // 1️⃣
            .map(customer -> {                             // 2️⃣
                final var authorities = customer.getRoles()
                        .stream()                          // 3️⃣
                        .map(role -> new SimpleGrantedAuthority(role.getName())) // 4️⃣
                        .toList();
                return new User(customer.getEmail(), customer.getPassword(), authorities); // 5️⃣
            }).orElseThrow(() -> new UsernameNotFoundException("User not exists")); // 6️⃣
}
```

---

## 🔍 Análisis Paso a Paso

| Paso | Código | ¿Qué hace? |
|------|--------|------------|
| **1️⃣** | `findByEmail(username)` | 🔎 Busca en la BD un cliente con ese email |
| **2️⃣** | `Optional.map(customer -> {...})` | 🔄 Si existe, transforma `Customer` → `UserDetails` |
| **3️⃣** | `customer.getRoles().stream()` | 📋 Convierte la lista de roles en un Stream |
| **4️⃣** | `.map(role -> new SimpleGrantedAuthority(...))` | 🔐 Transforma cada `Role` → `SimpleGrantedAuthority` |
| **5️⃣** | `new User(email, password, authorities)` | 👤 Crea el objeto `UserDetails` de Spring Security |
| **6️⃣** | `.orElseThrow(...)` | ❌ Si no existe, lanza excepción |

---

## 🎭 Los Dos `map()`: ¿Qué Transforman?

### 📦 **MAP #1: `Optional.map()`** → Transforma el Contenedor

```java
Optional<Customer> ---> Optional<UserDetails>
```

| **Antes del map** | **Después del map** |
|-------------------|---------------------|
| `Optional<Customer>` | `Optional<UserDetails>` |
| Objeto de tu BD | Objeto que Spring Security entiende |

#### 🧪 Ejemplo con tu escenario:

```java
// ANTES del map:
Optional<Customer> cliente = Optional.of(
    new Customer("alice@mail.com", "$2a$10...", [ROLE_USER, ROLE_ADMIN])
)

// DESPUÉS del map:
Optional<UserDetails> usuario = Optional.of(
    new User("alice@mail.com", "$2a$10...", [SimpleGrantedAuthority("ROLE_USER"), ...])
)
```

---

### 📋 **MAP #2: `Stream.map()`** → Transforma Cada Elemento

```java
Stream<Role> ---> Stream<SimpleGrantedAuthority>
```

| **Antes del map** | **Después del map** |
|-------------------|---------------------|
| `Stream<Role>` | `Stream<SimpleGrantedAuthority>` |
| Tus entidades de BD | Autoridades de Spring Security |

#### 🧪 Ejemplo con tu escenario:

```java
// ANTES del map:
Stream<Role> roles = Stream.of(
    new Role("ROLE_USER"),
    new Role("ROLE_ADMIN")
)

// DESPUÉS del map:
Stream<SimpleGrantedAuthority> authorities = Stream.of(
    new SimpleGrantedAuthority("ROLE_USER"),
    new SimpleGrantedAuthority("ROLE_ADMIN")
)
```

---

## 🎨 Diagrama de Flujo Completo

```
📧 Email: "alice@mail.com"
        │
        ▼
   🔍 customerRepository.findByEmail()
        │
        ├─── ✅ ENCONTRADO
        │         │
        │         ▼│    📦 Optional<Customer>
        │         │
        │         ▼ (Optional.map)
        │    🔄 Transformación
        │         │
        │         ├─── 📋 getRoles() → [Role, Role, ...]
        │         │         │
        │         │         ▼ (Stream.map)
        │         │    🔐 [SimpleGrantedAuthority, ...]
        │         │
        │         ▼
        │    👤 new User(email, password, authorities)
        │         │
        │         ▼
        │    ✅ UserDetails
        │
        └─── ❌ NO ENCONTRADO
                  │
                  ▼🚫 UsernameNotFoundException
```

---

## 🧮 Ejemplo Completo con Datos Reales

### 🗄️ **Datos en la Base de Datos:**

```
CUSTOMER TABLE:
+----+------------------+-----------------+
| id | email            | password        |
+----+------------------+-----------------+
| 1  | alice@mail.com   | $2a$10abc...    |
+----+------------------+-----------------+

ROLE TABLE:
+----+-------------+
| id | name        |
+----+-------------+
| 1  | ROLE_USER   |
| 2  | ROLE_ADMIN  |
+----+-------------+

CUSTOMER_ROLES:
+-------------+---------+
| customer_id | role_id |
+-------------+---------+
| 1           | 1       |
| 1           | 2       |
+-------------+---------+
```

### 🔄 **Proceso de Transformación:**

```java
// 1️⃣ findByEmail("alice@mail.com") retorna:
Optional<Customer> {
    email: "alice@mail.com",
    password: "$2a$10abc...",
    roles: [
        Role{name: "ROLE_USER"},
        Role{name: "ROLE_ADMIN"}
    ]
}

// 2️⃣ Stream.map() transforma roles:
[Role{ROLE_USER}, Role{ROLE_ADMIN}]
           ↓
[SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")]

// 3️⃣ Optional.map() crea UserDetails:
User {
    username: "alice@mail.com",
    password: "$2a$10abc...",
    authorities: [
        SimpleGrantedAuthority("ROLE_USER"),
        SimpleGrantedAuthority("ROLE_ADMIN")
    ],
    enabled: true,
    accountNonExpired: true,
    credentialsNonExpired: true,
    accountNonLocked: true
}
```

---

## 💡 Conceptos Clave para Entender `map()`

### 🎯 **`Optional.map()`**
- **No modifica** el Optional original
- **Solo se ejecuta** si el Optional contiene un valor
- **Retorna** un nuevo Optional con el valor transformado

### 🎯 **`Stream.map()`**
- **Transforma cada elemento** del Stream
- **Es una operación intermedia** (lazy)
- **No modifica** la colección original

---

## 📚 Métodos Relacionados que Podrías Necesitar

| Método | ¿Cuándo usarlo? | Ejemplo |
|--------|-----------------|---------|
| `Optional.flatMap()` | Cuando la función retorna otro Optional | `.flatMap(c -> repository.findAddress(c))` |
| `Optional.orElse()` | Valor por defecto si está vacío | `.orElse(guestUser)` |
| `Optional.ifPresent()` | Ejecutar acción solo si existe | `.ifPresent(user -> log.info(user))` |
| `Stream.filter()` | Filtrar elementos antes de transformar | `.filter(role -> role.isActive())` |
| `Stream.collect()` | Convertir Stream a colección | `.collect(Collectors.toSet())` |

---

## 🎓 Resumen para Estudiantes

### ✅ **Lo que DEBES recordar:**

```
🔑 loadUserByUsername() hace 3 cosas:
   1. Busca usuario por email (BD → Optional<Customer>)
   2. Transforma roles (Role → SimpleGrantedAuthority)
   3. Crea UserDetails (Customer → User)

🔄 Dos transformaciones map():
   - Optional.map() → cambia el tipo del contenedor
   - Stream.map() → cambia cada elemento de la colección

❌ Si no existe usuario → lanza UsernameNotFoundException
```

---

## 🚀 Mejora Adicional (Bonus)

Si quisieras agregar **más información al token JWT** (como nombre completo), podrías extender así:

```java
return new User(
    customer.getEmail(),
    customer.getPassword(),
    authorities
) {
    @Override
    public String toString() {
        return customer.getFullName() + " (" + customer.getEmail() + ")";
    }
};
```

# ¿QUE HACE SimpleGrantedAuthority? INFORMACION DE CHAT CLAUDE 🛡️🛡️
# 🔐 Explicación Detallada: `SimpleGrantedAuthority`

---

## 🎯 ¿Qué es `SimpleGrantedAuthority`?

`SimpleGrantedAuthority` es una **clase de Spring Security** que representa un **permiso o rol** que tiene un usuario en el sistema.

---

## 📚 Definición Técnica

```java
public final class SimpleGrantedAuthority implements GrantedAuthority {
    private final String role;

    public SimpleGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
```

---

## 🧩 Componentes de `SimpleGrantedAuthority`

| Componente | Tipo | Descripción |
|------------|------|-------------|
| **Implementa** | `GrantedAuthority` | Interfaz de Spring Security |
| **Atributo** | `String role` | El nombre del rol/permiso |
| **Método clave** | `getAuthority()` | Retorna el nombre del rol |
| **Propósito** | Autorización | Define QUÉ puede hacer el usuario |

---

## 🔄 ¿Qué Transforma el `.map()`?

### 📋 Transformación: `Role` → `SimpleGrantedAuthority`

```java
customer.getRoles().stream()
    .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().name()))
```

| **ANTES del `.map()`** | **DESPUÉS del `.map()`** |
|------------------------|--------------------------|
| `Stream<Role>` | `Stream<SimpleGrantedAuthority>` |
| Entidades de tu BD | Objetos de Spring Security |

---

## 🎨 Diagrama de Transformación

```
📦 Customer
   [...]
            [...]
   📋 Stream<Role> [
   [...]
            [...]
   🔐 Stream<SimpleGrantedAuthority> [
   [...]
```

---

## 🧪 Ejemplo Completo con Datos Reales

### 🗄️ **1. Datos en la Base de Datos**

```sql
-- Tabla CUSTOMER
+----+------------------+-----------------+
| id | email            | password        |
+----+------------------+-----------------+
| 1  | alice@mail.com   | $2a$10abc...    |
+----+------------------+-----------------+

-- Tabla ROLE
+----+-------------+
| id | name        |
+----+-------------+
| 1  | ROLE_USER   |
| 2  | ROLE_ADMIN  |
+----+-------------+

-- Tabla CUSTOMER_ROLES (relación Many-to-Many)// OJO NO ES ASI NO HAY TABLA INTERMEDIA ES EJEMPLO DE CHAT
+-------------+---------+
| customer_id | role_id |
+-------------+---------+
| 1           | 1       |
| 1           | 2       |
+-------------+---------+
```

---

### 🔄 **2. Proceso de Transformación Paso a Paso**

```java
// PASO 1: Buscar el customer en la BD
Customer customer = customerRepository.findByEmail("alice@mail.com");

// customer.getRoles() retorna:
List<Role> roles = [
    Role{id=1, roleEnum=ROLE_USER},
    Role{id=2, roleEnum=ROLE_ADMIN}
];

// PASO 2: Convertir a Stream
Stream<Role> roleStream = roles.stream();
// [Role(ROLE_USER), Role(ROLE_ADMIN)]

// PASO 3: Aplicar .map() - AQUÍ OCURRE LA TRANSFORMACIÓN
Stream<SimpleGrantedAuthority> authStream = roleStream.map(role ->
    new SimpleGrantedAuthority(role.getRoleEnum().name())
);
// [SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")]

// PASO 4: Colectar en una lista
List<SimpleGrantedAuthority> authorities = authStream.collect(Collectors.toList());

// Resultado final:
[
    SimpleGrantedAuthority { authority: "ROLE_USER" },
    SimpleGrantedAuthority { authority: "ROLE_ADMIN" }
]
```

---

## 🎯 ¿Para Qué Sirve `SimpleGrantedAuthority`?

### 📋 **Función Principal: Autorización**

`SimpleGrantedAuthority` le dice a **Spring Security**:

```
✅ "Este usuario tiene el rol ROLE_ADMIN"
✅ "Por lo tanto, puede acceder a endpoints protegidos con @PreAuthorize('ROLE_ADMIN')"
```

---

## 🔐 Uso en Spring Security

### ✅ **Caso 1: Protección de Endpoints**

```java
@RestController
@RequestMapping("/api")
public class AdminController {

    // Solo usuarios con ROLE_ADMIN pueden acceder
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")  // ← Aquí se usa SimpleGrantedAuthority
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // Solo usuarios con ROLE_USER pueden acceder
    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public UserProfile getProfile() {
        return profileService.getCurrentUser();
    }

    // Usuarios con cualquiera de estos roles pueden acceder
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Dashboard getDashboard() {
        return dashboardService.getData();
    }
}
```

---

### ✅ **Caso 2: Validación Manual en Código**

```java
@Service
public class AuthorizationService {

    public boolean hasRole(Authentication auth, String roleName) {
        return auth.getAuthorities().stream()
            .anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ROLE_" + roleName)
            );
    }

    public boolean canDeleteUser(Authentication auth) {
        return hasRole(auth, "ADMIN") || hasRole(auth, "SUPER_ADMIN");
    }
}
```

---

## 🧠 Diferencia entre `Role` (Tu Entidad) y `SimpleGrantedAuthority`

| Aspecto | `Role` (Tu BD) | `SimpleGrantedAuthority` (Spring) |
|---------|----------------|-----------------------------------|
| **Propósito** | 💾 Persistencia en BD | 🔐 Autorización en tiempo real |
| **Paquete** | Tu proyecto | `org.springframework.security` |
| **Atributos** | `id`, `roleEnum`, `permissions`, etc. | Solo `authority` (String) |
| **Uso** | CRUD, relaciones JPA | Decisiones de acceso en Spring Security |
| **Creación** | JPA/Hibernate | Manualmente en `UserDetailsService` |

---

## 🎨 Diagrama de Arquitectura

```
┌───────────────────────────────────────────────┐
│            BASE DE DATOS                      │
│                                               │
│  Customer ←─── Many-to-Many ───→ Role        │
│  [alice@mail.com]         [ROLE_USER]        │
│                           [ROLE_ADMIN]        │
└───────────────────────────────────────────────┘
                    [...]
┌───────────────────────────────────────────────┐
│        CAPA DE SERVICIO (JwtUserDetailService)│
│                                               │
│  Role[] → Stream<Role>                        │
│        [...]
│  SimpleGrantedAuthority[]                     │
└───────────────────────────────────────────────┘
                    [...]
┌───────────────────────────────────────────────┐
│         SPRING SECURITY CONTEXT               │
│                                               │
│  UserDetails {                                │
│    username: "alice@mail.com"                 │
│    authorities: [                             │
│      SimpleGrantedAuthority("ROLE_USER"),     │
│      SimpleGrantedAuthority("ROLE_ADMIN")     │
│    ]                                          │
│  }                                            │
└───────────────────────────────────────────────┘
                    [...]
┌───────────────────────────────────────────────┐
│           ENDPOINT PROTEGIDO                  │
│                                               │
│  @PreAuthorize("hasRole('ADMIN')")            │
│  public void adminOnlyAction() {              │
│    // Spring compara contra authorities       │
│  }                                            │
└───────────────────────────────────────────────┘
```

---

## 💻 Código Completo de Tu Escenario

```java
@Service
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
            .map(customer -> {
                // 🔄 TRANSFORMACIÓN: Role → SimpleGrantedAuthority
                var authorities = customer.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().name()))
                    .collect(Collectors.toList());

                // 👤 Crear UserDetails de Spring Security
                return new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    authorities  // ← Aquí se usan los SimpleGrantedAuthority
                );
            })
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
```

---

## 🎯 ¿Qué Se Guarda Exactamente?

### 📦 **En el Objeto `User` de Spring Security:**

```java
User springUser = new User(
    "alice@mail.com",                              // username
    "$2a$10abc...",                                // password (encriptado)
    [                                              // authorities
        SimpleGrantedAuthority("ROLE_USER"),
        SimpleGrantedAuthority("ROLE_ADMIN")
    ]
);
```

### 🔍 **Estructura Interna:**

```java
// Dentro del objeto User
private String username = "alice@mail.com";
private String password = "$2a$10abc...";
private Set<GrantedAuthority> authorities = Set.of(
    new SimpleGrantedAuthority("ROLE_USER"),
    new SimpleGrantedAuthority("ROLE_ADMIN")
);
```

---

## 🚀 Flujo Completo de Autenticación y Autorización

```
1️⃣ USUARIO SE AUTENTICA
   POST /api/auth/login
   Body: { "email": "alice@mail.com", "password": "123456" }
        [...]
2️⃣ JwtUserDetailService.loadUserByUsername("alice@mail.com")
        [...]
3️⃣ TRANSFORMACIÓN DE ROLES
   [Role(ROLE_USER), Role(ROLE_ADMIN)]
        ↓ .map()
   [SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")]
        [...]
4️⃣ SPRING SECURITY GUARDA EL UserDetails
   SecurityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken(
            userDetails,  // ← Contiene los SimpleGrantedAuthority
            null,
            userDetails.getAuthorities()
        )
   )
        [...]
5️⃣ USUARIO INTENTA ACCEDER A ENDPOINT PROTEGIDO
   GET /api/admin/users
   @PreAuthorize("hasRole('ADMIN')")
        [...]
6️⃣ SPRING SECURITY VALIDA
   ✅ ¿El usuario tiene SimpleGrantedAuthority("ROLE_ADMIN")?
   ✅ SÍ → Permite acceso
   ❌ NO → Retorna 403 Forbidden
```

---

## 🎓 Resumen Visual

```
📦 Role (Tu Entidad JPA)
   ↓ .map()
🔐 SimpleGrantedAuthority (Spring Security)
   [...]
   ✅ "hasRole('ADMIN')" en @PreAuthorize
   ↓ compara
   ✅ SimpleGrantedAuthority("ROLE_ADMIN")
   ↓ resultado
   ✅ Acceso concedido / ❌ 403 Forbidden
```

---

## 💡 Para Recordar

```
✅ SimpleGrantedAuthority = Wrapper de Spring Security para roles
✅ Contiene un String con el nombre del rol (ej: "ROLE_ADMIN")
✅ Se usa en UserDetails.getAuthorities()
✅ Spring Security lo compara con @PreAuthorize, @Secured, etc.
✅ La transformación Role → SimpleGrantedAuthority conecta tu BD con Spring Security
✅ SIN SimpleGrantedAuthority, Spring Security NO puede verificar permisos
```

---

## 🔑 Concepto Clave

```
🎯 SimpleGrantedAuthority es el "lenguaje" que Spring Security entiende

Tu código:
Role{id=1, roleEnum=ROLE_ADMIN} ← Tu objeto de BD

Spring Security necesita:
SimpleGrantedAuthority("ROLE_ADMIN") ← Objeto de Spring Security

.map() hace la traducción entre ambos mundos
```
---
## 📝 Clase 56 -Configurando Payload(claims) de nuestro usuario JWT 👤👤️‍♂🕵️‍♂🔑 🔑 

## INTRODUCION A QUE ES PAYLOAD(claims) DE JWT
¡Excelente pregunta! El **PAYLOAD** es una parte fundamental de los **JWT (JSON Web Tokens)** 
que se usan en Spring Security para autenticación y autorización.

## Estructura de un JWT

Un JWT tiene **3 partes** separadas por puntos (`.`):

```
HEADER.PAYLOAD.SIGNATURE
```

### Ejemplo real:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

## ¿Qué es el PAYLOAD?

El **PAYLOAD** es la **segunda parte** del JWT y contiene la información útil (los datos). Cuando lo decodificas 
(es Base64, no está encriptado), obtienes un JSON:

```json
{
  "sub": "1234567890",           // Subject: identificador del usuario
  "name": "John Doe",            // Nombre del usuario
  "email": "john@example.com",   // Email
  "roles": ["USER", "ADMIN"],    // Roles del usuario
  "iat": 1516239022,             // Issued At: cuándo se creó
  "exp": 1516242622              // Expiration: cuándo expira
}
```

## ¿Qué son los CLAIMS (reclamaciones)?

Los **claims** son los pares clave-valor dentro del payload. Hay 3 tipos:

### 1. **Registered Claims** (Claims estándar)
```json
{
  "iss": "https://mi-api.com",    // Issuer: quién emitió el token
  "sub": "user123",                // Subject: de quién es el token
  "aud": "mi-aplicacion",          // Audience: para quién es el token
  "exp": 1735689600,               // Expiration: fecha de expiración
  "iat": 1735603200,               // Issued At: cuándo se creó
  "nbf": 1735603200                // Not Before: no válido antes de...
}
```

### 2. **Public Claims** (Claims públicos registrados)
```json
{
  "name": "María García",
  "email": "maria@example.com"
}
```

### 3. **Private Claims** (Claims personalizados)
```json
{
  "userId": 12345,
  "department": "IT",
  "roles": ["ADMIN", "USER"],
  "permissions": ["read", "write", "delete"]
}
```

## Ejemplo en Spring Security

```java
// Crear un JWT con claims personalizados
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    
    // Agregar claims personalizados al PAYLOAD
    claims.put("email", userDetails.getUsername());
    claims.put("roles", userDetails.getAuthorities());
    claims.put("userId", 12345);
    claims.put("department", "IT");
    
    return Jwts.builder()
        .setClaims(claims)                          // PAYLOAD con claims
        .setSubject(userDetails.getUsername())      // Subject claim
        .setIssuedAt(new Date())                    // Issued At claim
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // SIGNATURE
        .compact();
}
```

## Extraer información del PAYLOAD

```java
// Leer claims del JWT
public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);  // Extrae el "sub"
}

public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);  // Extrae el "exp"
}

public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);  // Extrae todo el PAYLOAD
    return claimsResolver.apply(claims);
}

private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();  // ← Aquí está el PAYLOAD completo
}

// Extraer claim personalizado
public String extractEmail(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("email", String.class);
}

public List<String> extractRoles(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("roles", List.class);
}
```

## Ejemplo completo en Spring Security

```java
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    // Generar token con claims
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("roles", user.getRoles());
        
        return createToken(extraClaims, user.getUsername());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)              // PAYLOAD: claims personalizados
            .setSubject(subject)            // PAYLOAD: subject
            .setIssuedAt(new Date())        // PAYLOAD: iat
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // PAYLOAD: exp
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // SIGNATURE
            .compact();
    }
    
    // Validar token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
```

## Visualización

```
┌─────────────────────────────────────────────────────┐
│                      JWT TOKEN                       │
├─────────────────────────────────────────────────────┤
│ HEADER                                              │
│ { "alg": "HS256", "typ": "JWT" }                   │
├─────────────────────────────────────────────────────┤
│ PAYLOAD (Claims) ← AQUÍ ESTÁ LA INFO DEL USUARIO   │
│ {                                                   │
│   "sub": "usuario123",         (Subject)           │
│   "email": "user@example.com", (Custom)            │
│   "roles": ["ADMIN", "USER"],  (Custom)            │
│   "iat": 1735603200,           (Issued At)         │
│   "exp": 1735689600            (Expiration)        │
│ }                                                   │
├─────────────────────────────────────────────────────┤
│ SIGNATURE                                           │
│ HMACSHA256(base64(header) + "." + base64(payload)) │
└─────────────────────────────────────────────────────┘
```

## Resumen

- **PAYLOAD** = La parte del JWT que contiene los datos
- **CLAIMS** = Los pares clave-valor dentro del payload
- **No está encriptado** (solo Base64), así que no pongas información sensible como contraseñas
- **La SIGNATURE protege la integridad** del payload (detecta si fue modificado)

---
# CODIGO DE LA CLASE 56

```java
@Service
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
}

```
# 🔐 Explicación Detallada: `JWTService`

---

## 🎯 ¿Qué hace este servicio?

Este servicio es el **desencriptador de tokens JWT**. Toma un token JWT (que es como un sobre cerrado con información) y extrae los datos que contiene de forma segura.

---

## 🧩 Anatomía Completa del Servicio

```java
@Service
public class JWTService {
    // ⏱️ Constante: Tiempo de validez (5 horas)
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    
    // 🔑 Constante: Clave secreta para firmar/validar tokens
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    // 🔓 Método privado: Extrae TODOS los claims del token
    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🎯 Método público: Extrae UN claim específico usando una función
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
}
```

---

## 📊 Constantes del Servicio

| Constante | Valor | ¿Para qué sirve? | Formato |
|-----------|-------|------------------|---------|
| `JWT_TOKEN_VALIDITY` | `18000` | ⏱️ Duración del token (5 horas en segundos) | `Long` |
| `JWT_SECRET` | `"jxgEQe..."` | 🔑 Clave para firmar y validar tokens | `String` |

### 🧮 Cálculo de `JWT_TOKEN_VALIDITY`:
```
5 horas × 60 minutos × 60 segundos = 18,000 segundos
```

---

## 🔍 Análisis de los Dos Métodos

### 🔓 **MÉTODO #1: `getAllClaimsFromToken()`**

#### 📋 **Propósito:**
Desencripta el token y extrae **TODOS** los claims (información) que contiene.

#### 🎯 **Características:**
- **Visibilidad:** `private` (solo lo usa esta clase)
- **Retorna:** `Claims` (objeto con toda la info del token)
- **Validación:** Si el token es inválido → lanza excepción

#### 📦 **Proceso Paso a Paso:**

```java
private Claims getAllClaimsFromToken(String token) {
    // 1️⃣ Convierte el secreto en clave criptográfica
    final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    
    // 2️⃣ Construye el parser del token
    return Jwts.parserBuilder()
            .setSigningKey(key)      // 3️⃣ Configura la clave de validación
            .build()                  // 4️⃣ Construye el parser
            .parseClaimsJws(token)    // 5️⃣ Valida y parsea el token
            .getBody();               // 6️⃣ Extrae el cuerpo (claims)
}
```

| Paso | Código | ¿Qué hace? |
|------|--------|------------|
| **1️⃣** | `Keys.hmacShaKeyFor(...)` | 🔑 Convierte el string secreto en clave HMAC-SHA |
| **2️⃣** | `Jwts.parserBuilder()` | 🏗️ Crea el constructor del parser |
| **3️⃣** | `.setSigningKey(key)` | 🔐 Configura la clave para validar la firma |
| **4️⃣** | `.build()` | ✅ Construye el parser configurado |
| **5️⃣** | `.parseClaimsJws(token)` | 🔍 Valida firma y parsea el token |
| **6️⃣** | `.getBody()` | 📦 Extrae el payload (claims) |

---

### 🎯 **MÉTODO #2: `getClaimsFromToken()`**

#### 📋 **Propósito:**
Extrae **UN DATO ESPECÍFICO** del token usando una función personalizada.

#### 🎯 **Características:**
- **Visibilidad:** `public` (otros servicios pueden usarlo)
- **Genérico:** `<T>` puede retornar cualquier tipo
- **Flexible:** Usa `Function<Claims, T>` para extraer lo que necesites

#### 📦 **Proceso Paso a Paso:**

```java
public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
    // 1️⃣ Obtiene TODOS los claims del token
    final var claims = this.getAllClaimsFromToken(token);
    
    // 2️⃣ Aplica la función para extraer el claim específico
    return claimsResolver.apply(claims);
}
```

| Paso | Código | ¿Qué hace? |
|------|--------|------------|
| **1️⃣** | `getAllClaimsFromToken(token)` | 📦 Obtiene todos los claims |
| **2️⃣** | `claimsResolver.apply(claims)` | 🎯 Extrae el dato específico según la función |

# 🧩 Explicación Detallada: `Function<Claims, T>` en Java

---

## 🎯 ¿Qué es `Function<Claims, T>`?

`Function` es una **interfaz funcional** de Java 8 que representa una función que:
- 📥 **Recibe** un parámetro de tipo `Claims`
- 📤 **Retorna** un valor de tipo `T` (genérico)

---

## 📚 Anatomía de `Function<Input, Output>`

```java
Function<Claims, T> claimsResolver
         ↓       ↓
      ENTRADA  SALIDA
     (Claims)   (T)
```

| Parte | Significado |
|-------|-------------|
| `Function` | 🧩 Interfaz funcional de `java.util.function` |
| `<Claims, T>` | 📦 Tipos genéricos: entrada y salida |
| `claimsResolver` | 🏷️ Nombre de la variable |

---

## 🔍 La Interfaz `Function` por Dentro

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);  // Método abstracto único
}
```

### 📋 Traducido a tu caso:

```java
Function<Claims, T> claimsResolver
    ↓
T apply(Claims claims) {
    // Extrae algo del objeto Claims
    return ...;
}
```

---

## 🎨 Diagrama del Flujo

```
🎫 TOKEN
    ↓
getAllClaimsFromToken(token)
    ↓
📦 Claims {
    sub: "alice@mail.com",
    exp: 1735689600,
    iat: 1735671600
}
    ↓
claimsResolver.apply(claims)  ← 🎯 Function se ejecuta aquí
    ↓
🎯 RESULTADO (tipo T)
```

---

## 💡 ¿Cómo se Usa en tu Método?

```java
public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
    final var claims = this.getAllClaimsFromToken(token);  // 1️⃣ Obtiene todos los claims
    return claimsResolver.apply(claims);                    // 2️⃣ Aplica la función
}
```

### 🧪 Ejemplo Real:

```java
// Cuando llamas al método desde otro lugar:
String email = jwtService.getClaimsFromToken(token, Claims::getSubject);
                                                      ↑
                                            Esto es el Function
```

---

## 🎯 Tres Formas de Pasar un `Function`

### ✅ **Forma 1: Reference Method (Referencia a Método)**

```java
// 📧 Extraer el subject (email)
String email = getClaimsFromToken(token, Claims::getSubject);
                                          ↑
                        Esto es: claims -> claims.getSubject()
```

### ✅ **Forma 2: Lambda Expression**

```java
// 🔐 Extraer un claim personalizado
List<String> roles = getClaimsFromToken(token, 
    claims -> claims.get("roles", List.class)
);
```

### ✅ **Forma 3: Implementación Explícita** (antigua)

```java
List<String> roles = getClaimsFromToken(token, new Function<Claims, List<String>>() {
    @Override
    public List<String> apply(Claims claims) {
        return claims.get("roles", List.class);
    }
});
```

---

## 📊 Tabla Comparativa de las 3 Formas

| Forma | Sintaxis | Cuándo usarla |
|-------|----------|---------------|
| **Method Reference** | `Claims::getSubject` | ✅ Cuando usas un método existente |
| **Lambda** | `claims -> claims.get("roles")` | ✅ Cuando necesitas lógica personalizada |
| **Clase Anónima** | `new Function<>() {...}` | ⚠️ Código antiguo (antes de Java 8) |

---

## 🧪 Ejemplos Prácticos Completos

### 📧 **Ejemplo 1: Extraer el Email (Subject)**

```java
// En tu servicio de autenticación:
public String getUserEmail(String token) {
    return jwtService.getClaimsFromToken(token, Claims::getSubject);
    //                                           ↑
    //                    Function<Claims, String>
}

// Resultado: "alice@mail.com"
```

#### 🔍 **Desglose:**

```java
Claims::getSubject
    ↓ equivale a:
claims -> claims.getSubject()
    ↓ equivale a:
new Function<Claims, String>() {
    public String apply(Claims claims) {
        return claims.getSubject();
    }
}
```

---

### ⏱️ **Ejemplo 2: Extraer la Fecha de Expiración**

```java
public Date getExpirationDate(String token) {
    return jwtService.getClaimsFromToken(token, Claims::getExpiration);
    //                                           ↑
    //                    Function<Claims, Date>
}

// Resultado: Tue Jan 01 00:00:00 UTC 2025
```

---

### 🔐 **Ejemplo 3: Extraer Claim Personalizado (Roles)**

```java
public List<String> getUserRoles(String token) {
    return jwtService.getClaimsFromToken(token, 
        claims -> claims.get("roles", List.class)
        //  ↑
        //  Function<Claims, List<String>>
    );
}

// Resultado: ["ROLE_USER", "ROLE_ADMIN"]
```

---

### 🆔 **Ejemplo 4: Extraer el ID del Token**

```java
public String getTokenId(String token) {
    return jwtService.getClaimsFromToken(token, Claims::getId);
    //                                           ↑
    //                    Function<Claims, String>
}

// Resultado: "550e8400-e29b-41d4-a716-446655440000"
```

---

## 🎓 ¿Por Qué Usar `Function` en Lugar de Métodos Separados?

### ❌ **Sin Function (Código Repetitivo):**

```java
// Tendrías que crear un método para cada claim:
public String getSubjectFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
}

public Date getExpirationFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
}

public String getIdFromToken(String token) {
    return getAllClaimsFromToken(token).getId();
}
// ... 10 métodos más para cada claim
```

### ✅ **Con Function (Flexible y Reutilizable):**

```java
// Un solo método que hace TODO:
public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
    final var claims = this.getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
}

// Úsalo para cualquier claim:
getClaimsFromToken(token, Claims::getSubject);    // String
getClaimsFromToken(token, Claims::getExpiration); // Date
getClaimsFromToken(token, Claims::getId);         // String
getClaimsFromToken(token, c -> c.get("roles"));   // Object
```

---

## 🧠 Cuadro Explicativo del Genérico `<T>`

```java
public <T> T getClaimsFromToken(...)
       ↓   ↓
   Declaración │
             Retorno
```

| Parte | Significado | Ejemplo |
|-------|-------------|---------|
| `<T>` antes del retorno | 📢 Declaración del tipo genérico | Indica que `T` es un tipo |
| `T` como retorno | 📤 El método retorna tipo `T` | Puede ser `String`, `Date`, `List`, etc. |
| `Function<Claims, T>` | 🎯 La función retorna tipo `T` | `T` se deduce del `claimsResolver` |

---

## 🔄 Flujo Completo con Ejemplo Real

```
1️⃣ LLAMADA AL MÉTODO:
   String email = getClaimsFromToken(token, Claims::getSubject);
                                             ↑
                              Function<Claims, String>

2️⃣ DENTRO DEL MÉTODO:
   public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
       // T se deduce como String
       final var claims = getAllClaimsFromToken(token);
       return claimsResolver.apply(claims);  // Llama a Claims::getSubject
   }

3️⃣ EJECUCIÓN DE LA FUNCIÓN:
   claims.getSubject()  // Retorna "alice@mail.com"

4️⃣ RESULTADO:
   email = "alice@mail.com"
```

---

## 📚 Interfaces Funcionales Relacionadas

| Interfaz | Estructura | Cuándo usarla | Ejemplo |
|----------|------------|---------------|---------|
| `Function<T, R>` | `T → R` | Transformar entrada en salida | `Claims::getSubject` |
| `Predicate<T>` | `T → boolean` | Validar/filtrar | `claims -> claims.getExpiration().after(new Date())` |
| `Consumer<T>` | `T → void` | Procesar sin retornar | `claims -> log.info(claims.getSubject())` |
| `Supplier<T>` | `() → T` | Generar valor sin entrada | `() -> new Date()` |

---

## 🎯 Métodos Comunes de `Claims` que Puedes Usar

```java
Claims claims = getAllClaimsFromToken(token);

// 📋 Métodos estándar:
claims.getSubject()      // 👤 "alice@mail.com"
claims.getExpiration()   // ⏱️ Date
claims.getIssuedAt()     // 📅 Date
claims.getId()           // 🆔 String
claims.getIssuer()       // 🏢 String
claims.getAudience()     // 👥 String

// 🎨 Métodos personalizados:
claims.get("roles")               // Object (raw)
claims.get("roles", List.class)   // List<String>
claims.get("userId", Integer.class) // Integer
```

---

## 🚀 Implementación Completa Recomendada

```java
@Service
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    // 🔓 Método privado base
    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🎯 Método genérico flexible
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // ⏱️ Métodos de conveniencia (usan Function internamente)
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public List<String> getRolesFromToken(String token) {
        return getClaimsFromToken(token, claims -> claims.get("roles", List.class));
    }
}
```

---

## 💡 Casos de Uso Adicionales

### 🔍 **Extraer Múltiples Claims en un Objeto:**

```java
public UserInfo extractUserInfo(String token) {
    return getClaimsFromToken(token, claims -> new UserInfo(
        claims.getSubject(),
        claims.get("name", String.class),
        claims.get("roles", List.class)
    ));
}
```

### ⚡ **Validación Personalizada:**

```java
public boolean isTokenValid(String token, String expectedEmail) {
    return getClaimsFromToken(token, claims -> 
        claims.getSubject().equals(expectedEmail) && 
        claims.getExpiration().after(new Date())
    );
}
```

---

## ✅ Resumen Visual

```
📦 Function<Claims, T>
   ↓
┌──────────────────────────────────────┐
│  🎯 Representa una función que:      │
│  ➤ Recibe: Claims                    │
│  ➤ Retorna: Tipo T (genérico)       │
└──────────────────────────────────────┘
   ↓
📝 Tres formas de escribirla:
   ├─ Claims::getSubject (Method Reference)
   ├─ claims -> claims.getSubject() (Lambda)
   └─ new Function<>() {...} (Clase Anónima)
   ↓
✅ Ventajas:
   ➤ Código flexible y reutilizable
   ➤ Evita duplicar métodos
   ➤ Permite extraer cualquier claim
```

---

## 🎓 Para Recordar:

```
✅ Function<A, B> = Función que transforma A en B
✅ Claims::getSubject = Atajo para: claims -> claims.getSubject()
✅ apply() = Método que ejecuta la función
✅ <T> = Tipo genérico que se deduce automáticamente
✅ claimsResolver = Variable que guarda la función
```
---

## 🎨 Diagrama de Flujo Completo

```
🎫 TOKEN JWT
   "eyJhbGciOiJIUzI1NiIs..."
        │
        ▼
   🔓 getAllClaimsFromToken()
        │
        ├─── 1️⃣ JWT_SECRET → 🔑 Clave HMAC-SHA
        │
        ├─── 2️⃣ Jwts.parserBuilder()
        │         │
        │         ├─── setSigningKey(key)
        │         ├─── build()
        │         └─── parseClaimsJws(token)
        │
        ├─── ✅ VÁLIDO
        │      │
        │      ▼
        │   📦 Claims {
        │       sub: "alice@mail.com",
        │       exp: 1735689600,
        │       iat: 1735671600,
        │       roles: ["ROLE_USER", "ROLE_ADMIN"]
        │   }
        │
        ├─── ❌ INVÁLIDO
        │      │
        │      ▼
        │   🚫 JwtException
        │
        ▼
   🎯 getClaimsFromToken(token, claimsResolver)
        │
        ├─── 📦 Obtiene todos los claims
        │
        └─── 🎯 Aplica función específica
               │
               ├─── Claims::getSubject → "alice@mail.com"
               ├─── Claims::getExpiration → Date
               └─── Claims::get("roles") → List<String>
```

---

## 🧪 Ejemplos Prácticos con Datos Reales

### 📝 **Ejemplo 1: Estructura de un Token JWT**

```
🎫 TOKEN JWT (codificado):
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhbGljZUBtYWlsLmNvbSIsImV4cCI6MTczNTY4OTYwMCwiaWF0IjoxNzM1NjcxNjAwLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl19.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

┌─────────────┬──────────────────────┬────────────────┐
│   HEADER    │       PAYLOAD        │   SIGNATURE    │
│  (Algoritmo)│   (Claims/Datos)     │  (Validación)  │
└─────────────┴──────────────────────┴────────────────┘
```

### 📦 **Ejemplo 2: Claims Decodificados**

```json
{
  "sub": "alice@mail.com",           // 👤 Usuario
  "exp": 1735689600,                 // ⏱️ Expiración (timestamp)
  "iat": 1735671600,                 // 📅 Fecha de emisión
  "roles": ["ROLE_USER", "ROLE_ADMIN"] // 🔐 Roles
}
```

---

## 💻 Ejemplos de Uso en Otros Servicios

### 🎯 **Caso 1: Extraer el Email del Usuario**

```java
// En otro servicio (por ejemplo, AuthService)
@Autowired
private JWTService jwtService;

public String getUserEmail(String token) {
    // 📧 Extrae el "subject" (email) del token
    return jwtService.getClaimsFromToken(token, Claims::getSubject);
}

// Resultado: "alice@mail.com"
```

### 🎯 **Caso 2: Extraer la Fecha de Expiración**

```java
public Date getExpirationDate(String token) {
    // ⏱️ Extrae la fecha de expiración
    return jwtService.getClaimsFromToken(token, Claims::getExpiration);
}

// Resultado: Tue Jan 01 00:00:00 UTC 2025
```

### 🎯 **Caso 3: Extraer un Claim Personalizado**

```java
public List<String> getUserRoles(String token) {
    // 🔐 Extrae un claim personalizado
    return jwtService.getClaimsFromToken(
        token, 
        claims -> claims.get("roles", List.class)
    );
}

// Resultado: ["ROLE_USER", "ROLE_ADMIN"]
```

### 🎯 **Caso 4: Validar si el Token Expiró**

```java
public boolean isTokenExpired(String token) {
    Date expiration = jwtService.getClaimsFromToken(token, Claims::getExpiration);
    // ⏰ Compara la fecha de expiración con la actual
    return expiration.before(new Date());
}

// Resultado: true (expiró) o false (aún válido)
```

---

## 🧠 Cuadro Comparativo de los Métodos

| Aspecto | `getAllClaimsFromToken()` | `getClaimsFromToken()` |
|---------|---------------------------|------------------------|
| **Visibilidad** | 🔒 `private` | 🌐 `public` |
| **Propósito** | Extrae **todos** los claims | Extrae **un** claim específico |
| **Retorno** | `Claims` (objeto completo) | `<T>` (tipo genérico) |
| **Uso directo** | ❌ No (método interno) | ✅ Sí (desde otros servicios) |
| **Validación** | ✅ Valida firma del token | ✅ Reutiliza validación del método privado |
| **Flexibilidad** | ⚠️ Devuelve todo | 🎯 Personalizable con `Function` |

---

## 🔄 ¿Por Qué Dos Métodos en Lugar de Uno?

### 🎯 **Principio de Responsabilidad Única:**

```
🔓 getAllClaimsFromToken()
   ↓ Responsabilidad: Validar y desencriptar el token
   
🎯 getClaimsFromToken()
   ↓ Responsabilidad: Extraer datos específicos de forma flexible
```

### 📚 **Ventajas del Diseño:**

| Ventaja | Descripción |
|---------|-------------|
| **🔄 Reutilización** | `getAllClaimsFromToken()` se usa internamente múltiples veces |
| **🎯 Flexibilidad** | `getClaimsFromToken()` permite extraer cualquier claim sin duplicar código |
| **🔒 Encapsulación** | La lógica de validación está oculta (método privado) |
| **🧹 Clean Code** | Cada método tiene una responsabilidad clara |

---

## 🛡️ Validaciones que Realiza `parseClaimsJws()`

Cuando llamas a `parseClaimsJws(token)`, JJWT valida automáticamente:

| Validación | ¿Qué verifica? | Excepción si falla |
|------------|----------------|-------------------|
| **🔐 Firma** | ¿El token fue firmado con `JWT_SECRET`? | `SignatureException` |
| **⏱️ Expiración** | ¿El token ya expiró? | `ExpiredJwtException` |
| **📅 Not Before** | ¿El token ya es válido? | `PrematureJwtException` |
| **📝 Formato** | ¿El token tiene formato correcto? | `MalformedJwtException` |

---

## 🎓 Conceptos Clave para Estudiantes

### 📚 **Claims:** ¿Qué son?

```
🎯 Claims = Afirmaciones sobre el usuario guardadas en el token

Tipos comunes:
- sub (subject): 👤 Identificador del usuario
- exp (expiration): ⏱️ Cuándo expira el token
- iat (issued at): 📅 Cuándo se creó el token
- custom claims: 🎨 Datos personalizados (roles, permisos, etc.)
```

### 🔑 **HMAC-SHA:** ¿Qué es?

```
🔐 HMAC-SHA = Algoritmo de firma criptográfica

Componentes:
- HMAC: Hash-based Message Authentication Code
- SHA: Secure Hash Algorithm

Función:
✅ Garantiza que el token NO fue modificado
✅ Solo quien tiene JWT_SECRET puede crear tokens válidos
```

### 🎯 **Function<Claims, T>:** ¿Qué es?

```
🧩 Function = Interfaz funcional de Java 8

Estructura:
Function<INPUT, OUTPUT>

En este caso:
Function<Claims, T>
   ↓        ↓
  INPUT   OUTPUT
(Claims) (Cualquier tipo)

Ejemplo:
Claims::getSubject → Function<Claims, String>
```

---

## 📊 Flujo Completo de Validación de Token

```
1️⃣ CLIENTE
   ↓ Envía request con header:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIs...

2️⃣ FILTRO DE SEGURIDAD
   ↓ Extrae el token del header

3️⃣ JWTService.getClaimsFromToken()
   ↓ Llama internamente a:
   
4️⃣ JWTService.getAllClaimsFromToken()
   ├─── 🔑 Convierte JWT_SECRET en clave
   ├─── 🔍 Valida la firma
   ├─── ⏱️ Verifica que no expiró
   └─── 📦 Extrae los claims

5️⃣ SI VÁLIDO ✅
   ↓ Retorna los claims
   ↓ Spring Security autentica al usuario
   ↓ Permite acceso al recurso

6️⃣ SI INVÁLIDO ❌
   ↓ Lanza JwtException
   ↓ Retorna 401 Unauthorized
```

---

## 🚀 Métodos Adicionales que Podrías Añadir

```java
// ⏰ Validar si el token expiró
public boolean isTokenExpired(String token) {
    Date expiration = getClaimsFromToken(token, Claims::getExpiration);
    return expiration.before(new Date());
}

// 👤 Obtener el username del token
public String getUsernameFromToken(String token) {
    return getClaimsFromToken(token, Claims::getSubject);
}

// 🆔 Obtener el ID del token
public String getIdFromToken(String token) {
    return getClaimsFromToken(token, Claims::getId);
}

// 🔐 Obtener roles del token
public List<String> getRolesFromToken(String token) {
    return getClaimsFromToken(token, claims -> claims.get("roles", List.class));
}
```

---

## ✅ Resumen Visual

```
🔐 JWTService tiene DOS métodos:

┌────────────────────────────────────────┐
│  🔓 getAllClaimsFromToken()            │
│  ➤ Privado                             │
│  ➤ Valida y desencripta el token       │
│  ➤ Retorna TODOS los claims           │
└────────────────────────────────────────┘
                 ↓ usa
┌────────────────────────────────────────┐
│  🎯 getClaimsFromToken()               │
│  ➤ Público                             │
│  ➤ Extrae UN claim específico          │
│  ➤ Usa Function<Claims, T> para        │
│     flexibilidad                       │
└────────────────────────────────────────┘

📌 Flujo típico:
Token → getAllClaims → Valida → getClaim → Dato específico
```

---

## 🎯 Para Recordar:

```
✅ getAllClaimsFromToken() = Abre el sobre (desencripta)
✅ getClaimsFromToken() = Lee un dato específico del sobre
✅ Claims = Información guardada en el token
✅ JWT_SECRET = Llave para abrir/validar el sobre
✅ Function<Claims, T> = Extractor personalizable
```
---

## 📝 Clase 57 - Configurando el tiempo de caducidad a nuestro JWT 👤👤🕵️‍♂🕵️‍♂🔑 🔑 

# 🔍 Explicación Detallada: Métodos de Extracción de Claims

---

## 🎯 Método #1: `getClaimsFromToken()` - El Método Genérico Flexible

### 📋 Firma del Método

```java
public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver)
```

---

## 🧩 Desglose Completo del Método Genérico

### 📊 Tabla de Componentes

| Componente | Tipo | Descripción |
|------------|------|-------------|
| `<T>` | Declaración de genérico | 📢 Define que `T` es un tipo variable |
| `T` (retorno) | Tipo genérico | 📤 El método retorna tipo `T` |
| `String token` | Parámetro 1 | 🎫 Token JWT a procesar |
| `Function<Claims, T> claimsResolver` | Parámetro 2 | 🎯 Función que extrae el claim específico |

---

## 🔄 Flujo de Ejecución Paso a Paso

```java
public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
    // 1️⃣ Extrae TODOS los claims del token
    final var claims = this.getAllClaimsFromToken(token);
    
    // 2️⃣ Aplica la función personalizada para extraer UN claim específico
    return claimsResolver.apply(claims);
}
```

### 📊 Tabla del Flujo

| Paso | Acción | Entrada | Salida |
|------|--------|---------|--------|
| **1️⃣** | `getAllClaimsFromToken(token)` | Token JWT (String) | `Claims` (objeto completo) |
| **2️⃣** | `claimsResolver.apply(claims)` | `Claims` | Tipo `T` (dato específico) |

---

## 🎨 Diagrama Visual del Flujo

```
🎫 TOKEN JWT"eyJhbGciOiJIUzI1NiIs..."
        ↓
   [getAllClaimsFromToken(token)]
        ↓
   📦 Claims {
       sub: "alice@mail.com",
       exp: 1735689600,
       iat: 1735671600,
       roles: ["ROLE_USER", "ROLE_ADMIN"]
   }
        ↓
   [claimsResolver.apply(claims)]
        ↓
   🎯 Resultado Tipo T
      (String, Date, List, etc.)
```

---

## 🧪 Ejemplos Prácticos del Método Genérico

### ✅ **Ejemplo 1: Extraer Email (String)**

```java
// Llamada:
String email = getClaimsFromToken(token, Claims::getSubject);

// Internamente:
Claims claims = getAllClaimsFromToken(token); // {sub: "alice@mail.com", ...}
return Claims::getSubject.apply(claims);      // "alice@mail.com"

// Resultado: "alice@mail.com"
```

### ✅ **Ejemplo 2: Extraer Fecha de Expiración (Date)**

```java
// Llamada:
Date expiration = getClaimsFromToken(token, Claims::getExpiration);

// Internamente:
Claims claims = getAllClaimsFromToken(token); // {exp: 1735689600, ...}
return Claims::getExpiration.apply(claims);   // Date object

// Resultado: Tue Jan 01 00:00:00 UTC 2025
```

### ✅ **Ejemplo 3: Extraer Roles (List)**

```java
// Llamada:
List<String> roles = getClaimsFromToken(token, claims -> claims.get("roles", List.class));

// Internamente:
Claims claims = getAllClaimsFromToken(token); // {roles: ["ROLE_USER", "ROLE_ADMIN"], ...}
return claims.get("roles", List.class);        // ["ROLE_USER", "ROLE_ADMIN"]

// Resultado: ["ROLE_USER", "ROLE_ADMIN"]
```

---

## 🎯 Método #2: `getExpirationDateFromToken()` - Método de Conveniencia

### 📋 Firma del Método

```java
private Date getExpirationDateFromToken(String token)
```

---

## 🧩 Desglose Completo del Método de Conveniencia

### 📊 Tabla de Componentes

| Componente | Tipo | Descripción |
|------------|------|-------------|
| `private` | Modificador | 🔒 Solo se usa dentro de esta clase |
| `Date` | Tipo de retorno | ⏱️ Siempre retorna un objeto `Date` |
| `String token` | Parámetro | 🎫 Token JWT a procesar |

---

## 🔄 Flujo de Ejecución

```java
private Date getExpirationDateFromToken(String token) {
    // Reutiliza el método genérico con Claims::getExpiration
    return this.getClaimsFromToken(token, Claims::getExpiration);
}
```

### 📊 Tabla del Flujo

| Paso | Método Llamado | Entrada | Salida |
|------|---------------|---------|--------|
| **1️⃣** | `getClaimsFromToken()` | Token + `Claims::getExpiration` | `Date` |
| **2️⃣** | `getAllClaimsFromToken()` | Token | `Claims` |
| **3️⃣** | `Claims::getExpiration` | `Claims` | `Date` |

---

## 🎨 Diagrama Visual del Flujo

```
🎫 TOKEN JWT↓
   [getExpirationDateFromToken(token)]
        ↓
   [getClaimsFromToken(token, Claims::getExpiration)]
        ↓
   [getAllClaimsFromToken(token)]
        ↓
   📦 Claims { exp: 1735689600, ... }
        ↓
   [Claims::getExpiration.apply(claims)]
        ↓
   ⏱️ Date (Tue Jan 01 00:00:00 UTC 2025)
```

---

## 🧪 Ejemplo Completo con Datos Reales

### 📋 Escenario: Token con Información de Usuario

```json
// Token JWT decodificado (payload):
{
  "sub": "alice@mail.com",
  "exp": 1735689600,
  "iat": 1735671600,
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

### 🔍 Ejecución de `getExpirationDateFromToken()`

```java
// 1️⃣ Llamada al método
Date expiration = getExpirationDateFromToken(token);

// 2️⃣ Internamente llama a:
getClaimsFromToken(token, Claims::getExpiration)

// 3️⃣ Que a su vez:
Claims claims = getAllClaimsFromToken(token)
// Retorna: {sub: "alice@mail.com", exp: 1735689600, iat: 1735671600, roles: [...]}

// 4️⃣ Luego aplica:
Claims::getExpiration.apply(claims)
// Retorna: Date(1735689600000)

// 5️⃣ Resultado final:
// Tue Jan 01 00:00:00 UTC 2025
```

---

## 🔀 Comparación: Método Genérico vs. Método de Conveniencia

| Aspecto | `getClaimsFromToken()` | `getExpirationDateFromToken()` |
|---------|------------------------|--------------------------------|
| **Visibilidad** | 🌐 `public` | 🔒 `private` |
| **Flexibilidad** | 🎯 Alta (acepta cualquier función) | ⚠️ Baja (solo extrae expiración) |
| **Tipo de retorno** | 📦 Genérico `<T>` | ⏱️ Fijo `Date` |
| **Parámetros** | 2 (token + función) | 1 (solo token) |
| **Propósito** | 🔧 Reutilizable para cualquier claim | 🎯 Específico para fecha de expiración |
| **Uso típico** | Desde otras clases | Solo interno (dentro de `JWTService`) |

---

## 📚 Métodos de Conveniencia Adicionales (Recomendados)

```java
// ⏱️ Obtener fecha de emisión
private Date getIssuedAtFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getIssuedAt);
}

// 👤 Obtener subject (username/email)
public String getUsernameFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getSubject);
}

// 🆔 Obtener ID del token
private String getIdFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getId);
}

// 🏢 Obtener emisor
private String getIssuerFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getIssuer);
}

// 👥 Obtener audiencia
private String getAudienceFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getAudience);
}

// 🔐 Obtener roles personalizados
public List<String> getRolesFromToken(String token) {
    return this.getClaimsFromToken(token, claims -> claims.get("roles", List.class));
}
```

---

## 🎯 ¿Por Qué Usar Este Patrón?

### ✅ **Ventajas del Diseño**

| Ventaja | Descripción |
|---------|-------------|
| **🔄 Reutilización** | Un método genérico sirve para todos los claims |
| **🧹 Código Limpio** | Evita duplicación de lógica |
| **🎯 Especialización** | Métodos de conveniencia para casos comunes |
| **🔒 Encapsulación** | Métodos internos privados |
| **🧪 Testeable** | Fácil de probar cada componente |

---

## 🧠 Concepto Clave: Method Reference

### 📝 **Claims::getExpiration** explicado

```java
// 1️⃣ Method Reference (forma corta)
Claims::getExpiration

// 2️⃣ Lambda equivalente (forma media)
claims -> claims.getExpiration()

// 3️⃣ Clase anónima (forma larga/antigua)
new Function<Claims, Date>() {
    @Override
    public Date apply(Claims claims) {
        return claims.getExpiration();
    }
}
```

### 🎯 Todas son equivalentes, pero **Method Reference** es:
- ✅ Más legible
- ✅ Más concisa
- ✅ La forma moderna (Java 8+)

---

## 🔄 Flujo Completo de Validación de Token

```
┌─────────────────────────────────────────────────┐
│            FLUJO DE VALIDACIÓN JWT              │
└─────────────────────────────────────────────────┘

1️⃣ Cliente envía request con header:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
        ↓
2️⃣ Filtro de seguridad extrae el token
        ↓
3️⃣ Llama a métodos de JWTService:
        ↓
   ┌──────────────────────────────┐
   │ getUsernameFromToken(token)  │ → "alice@mail.com"
   ├──────────────────────────────┤
   │ getExpirationDateFromToken() │ → Date(2025-01-01)
   ├──────────────────────────────┤
   │ getRolesFromToken(token)     │ → ["ROLE_USER", "ROLE_ADMIN"]
   └──────────────────────────────┘
        ↓
4️⃣ Todos usan internamente:
   getClaimsFromToken(token, function)
        ↓
5️⃣ Que llama a:
   getAllClaimsFromToken(token)
        ↓
6️⃣ Si válido: ✅ Continúa con el request
   Si inválido: ❌ Retorna 401 Unauthorized
```

---

## 🚀 Implementación Completa Recomendada

```java
@Service
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    // 🔓 Método privado base
    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🎯 Método genérico flexible (PÚBLICO)
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // ⏱️ Métodos de conveniencia (PRIVADOS/PÚBLICOS según necesidad)
    
    // PRIVADO: solo se usa internamente
    private Date getExpirationDateFromToken(String token) {
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    // PÚBLICO: se usa desde otros servicios
    public String getUsernameFromToken(String token) {
        return this.getClaimsFromToken(token, Claims::getSubject);
    }

    // PÚBLICO: validación común
    public boolean isTokenExpired(String token) {
        Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // PÚBLICO: extraer roles
    public List<String> getRolesFromToken(String token) {
        return this.getClaimsFromToken(token, claims -> claims.get("roles", List.class));
    }
}
```

---

## 💡 Casos de Uso Prácticos

### 🔍 **Caso 1: Validar Token en un Filtro**

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, ...) {
        String token = extractTokenFromRequest(request);
        
        // Extrae username
        String username = jwtService.getUsernameFromToken(token);
        
        // Valida expiración
        if (!jwtService.isTokenExpired(token)) {
            // Token válido, continúa
        }
    }
}
```

### 🔍 **Caso 2: Autorización por Roles**

```java
@Service
public class AuthorizationService {
    @Autowired
    private JWTService jwtService;

    public boolean hasRole(String token, String requiredRole) {
        List<String> roles = jwtService.getRolesFromToken(token);
        return roles.contains(requiredRole);
    }
}
```

---

## ✅ Resumen Visual

```
📦 getClaimsFromToken(token, function)
   ↓
┌─────────────────────────────────────────┐
│  🎯 Método GENÉRICO y FLEXIBLE          │
│  ➤ Acepta cualquier función             │
│  ➤ Retorna tipo genérico <T>            │
│  ➤ Reutilizable para todos los claims   │
└─────────────────────────────────────────┘
   ↓
⏱️ getExpirationDateFromToken(token)
   ↓
┌─────────────────────────────────────────┐
│  🎯 Método de CONVENIENCIA              │
│  ➤ Caso específico (expiración)         │
│  ➤ Retorna Date fijo                    │
│  ➤ Reutiliza el método genérico         │
└─────────────────────────────────────────┘
```

---

## 🎓 Para Recordar

```
✅ getClaimsFromToken() = Método genérico flexible
✅ <T> = Tipo genérico que se deduce automáticamente
✅ Function<Claims, T> = Función que transforma Claims en T
✅ getExpirationDateFromToken() = Atajo específico
✅ Claims::getExpiration = Method Reference (forma corta)
✅ Patrón: Un método genérico + varios de conveniencia
```