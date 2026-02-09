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

## 📝 Clase 58 - Configurando el tiempo de caducidad a nuestro JWT 👤👤🕵️‍♂🕵️‍♂🔑 🔑 
# 🔐 Explicación de Métodos de Validación JWT

---

## 📋 Visión General

Estos tres métodos trabajan juntos para **validar** que un token JWT sea legítimo y no haya expirado.

---

## 1️⃣ `isTokenExpired()` - Verificar Expiración

### 🎯 **Propósito**
Verifica si el token JWT **ya caducó** comparando su fecha de expiración con la fecha actual.

### 💻 **Código**
```java
private Boolean isTokenExpired(String token) {
    final var expirationDate = this.getExpirationDateFromToken(token);
    return expirationDate.before(new Date());
}
```

### 🔍 **¿Qué Hace Paso a Paso?**

```
1️⃣ Extrae la fecha de expiración del token
   expirationDate = getExpirationDateFromToken(token)
   Ejemplo: 2024-01-15 14:30:00

2️⃣ Obtiene la fecha/hora actual
   new Date()
   Ejemplo: 2024-01-15 10:00:00

3️⃣ Compara: ¿expirationDate es ANTES que ahora?
   expirationDate.before(new Date())
   ✅ true  → Token EXPIRADO (la fecha de exp ya pasó)
   ❌ false → Token VÁLIDO (aún no expira)
```

### 📊 **Ejemplo Visual**

```
┌─────────────────────────────────────────────┐
│         LÍNEA DE TIEMPO                     │
└─────────────────────────────────────────────┘

Caso 1: Token EXPIRADO ❌
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        [...]                   ↑ Ahora (10:00)
        14:30                   
    expirationDate              

    expirationDate.before(new Date()) = TRUE
    [...]

Caso 2: Token VÁLIDO ✅
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    ↑ Ahora (10:00)             [...]
    10:00                       14:30
                           expirationDate

    expirationDate.before(new Date()) = FALSE
    ✅ El token aún es válido
```

---

## 2️⃣ `getUsernameFromToken()` - Extraer Username

### 🎯 **Propósito**
Extrae el **username** (o email) almacenado en el token JWT.

### 💻 **Código**
```java
private String getUsernameFromToken(String token) {
    return this.getClaimsFromToken(token, Claims::getSubject);
}
```

### 🔍 **¿Qué Hace?**

```
1️⃣ Llama al método genérico getClaimsFromToken()

2️⃣ Pasa como parámetro: Claims::getSubject
   (Method reference que extrae el "subject" del token)

3️⃣ El "subject" en JWT es típicamente el username/email

4️⃣ Retorna: "alice@mail.com" (por ejemplo)
```

### 📦 **Estructura del Token JWT**

```json
{
  "sub": "alice@mail.com",    ← Claims.getSubject() extrae ESTO
  "iat": 1705318800,
  "exp": 1705336800,
  "authorities": ["ROLE_USER", "ROLE_ADMIN"]
}
```

### 🔄 **Alternativa sin Method Reference**

```java
private String getUsernameFromToken(String token) {
    return this.getClaimsFromToken(token, claims -> claims.getSubject());
}
```

---

## 3️⃣ `validateToken()` - Validación Completa

### 🎯 **Propósito**
Valida que el token sea **auténtico** y **no haya expirado**.

### 💻 **Código**
```java
public Boolean validateToken(String token, UserDetails userDetails) {
    final var usernameFromUserDetails = userDetails.getUsername();
    final var usernameFromJWT = this.getUsernameFromToken(token);

    return (usernameFromUserDetails.equals(usernameFromJWT) && !this.isTokenExpired(token));
}
```

### 🔍 **¿Qué Valida?**

| Validación | Método Usado | ¿Qué Verifica? |
|------------|--------------|----------------|
| **1️⃣ Usuario Correcto** | `getUsernameFromToken()` | ¿El username del token coincide con el usuario autenticado? |
| **2️⃣ Token No Expirado** | `isTokenExpired()` | ¿El token aún está vigente? |

### 📊 **Flujo de Validación**

```
┌──────────────────────────────────────────────┐
│  ENTRADA: Token JWT + UserDetails           │
└──────────────────────────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ 1️⃣ Extraer Username        │
        │    del UserDetails        │
        │    [...]
        │    "alice@mail.com"       │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ 2️⃣ Extraer Username        │
        │    del Token JWT          │
        │    [...]
        │    getUsernameFromToken() │
        │    [...]
        │    "alice@mail.com"       │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ 3️⃣ ¿Coinciden?             │
        │    "alice@mail.com" ==    │
        │    "alice@mail.com"       │
        │    [...]
        │    ✅ SÍ                   │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ 4️⃣ ¿Token Expirado?        │
        │    isTokenExpired(token)  │
        │    [...]
        │    ❌ NO                   │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ 5️⃣ Resultado Final         │
        │    true && !false         │
        │    [...]
        │    ✅ TOKEN VÁLIDO         │
        └───────────────────────────┘
```

### 🎯 **Lógica de Validación**

```java
return (usernameFromUserDetails.equals(usernameFromJWT) && !this.isTokenExpired(token));
       │                                                 │
       └─ Condición 1: Usernames coinciden ────────────┘
                                                          │
                    └─ Condición 2: Token NO expirado ──┘
```

### 📋 **Casos de Validación**

| Username Coincide | Token Expirado | Resultado | Motivo |
|-------------------|----------------|-----------|--------|
| ✅ **SÍ** | ❌ **NO** | ✅ **VÁLIDO** | Todo correcto |
| ✅ **SÍ** | ✅ **SÍ** | ❌ **INVÁLIDO** | Token caducado |
| ❌ **NO** | ❌ **NO** | ❌ **INVÁLIDO** | Usuario no coincide |
| ❌ **NO** | ✅ **SÍ** | ❌ **INVÁLIDO** | Ambas condiciones fallan |

---

## 🔄 Flujo Completo de Validación

```
┌─────────────────────────────────────────────┐
│  CLIENTE ENVÍA PETICIÓN CON TOKEN           │
│  GET /api/admin/users                       │
│  Authorization: Bearer eyJhbGc...           │
└─────────────────────────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ JwtAuthenticationFilter   │
        │ [...]
        │ Extrae el token del header│
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ getUsernameFromToken()    │
        │ [...]
        │ Extrae: "alice@mail.com"  │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ UserDetailsService        │
        │ [...]
        │ Carga UserDetails de BD   │
        └───────────────────────────┘
                    [...]
        ┌───────────────────────────┐
        │ validateToken()           │
        │ [...]
        │ ¿Username correcto?       │
        │ [...]
        │ ¿Token no expirado?       │
        └───────────────────────────┘
                    [...]
    ┌─────────────┐      ┌─────────────┐
    │ ✅ VÁLIDO   │      │ ❌ INVÁLIDO │
    │             │      │             │
    │ Continúa    │      │ Retorna     │
    │ la petición │      │ 403/401     │
    └─────────────┘      └─────────────┘
```

---

## 🧪 Ejemplo Práctico Completo

### 📝 **Escenario: Usuario Autenticado**

```java
// 1️⃣ Token JWT recibido
String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBtYWlsLmNvbSIsImV4cCI6MTcwNTMzNjgwMH0...";

// 2️⃣ UserDetails cargado desde BD
UserDetails userDetails = new User(
    "alice@mail.com",
    "$2a$10abc...",
    authorities
);

// 3️⃣ Llamada a validateToken()
Boolean isValid = jwtService.validateToken(token, userDetails);

// ═══════════════════════════════════════
// DENTRO DEL MÉTODO validateToken()
// ═══════════════════════════════════════

// Paso 1: Extraer username de UserDetails
String usernameFromUserDetails = "alice@mail.com";

// Paso 2: Extraer username del token JWT
String usernameFromJWT = getUsernameFromToken(token);
// → Llama a getClaimsFromToken(token, Claims::getSubject)
// → Retorna: "alice@mail.com"

// Paso 3: Verificar expiración
Boolean expired = isTokenExpired(token);
// → getExpirationDateFromToken(token) retorna: 2024-01-15 14:30:00
// → new Date() es: 2024-01-15 10:00:00
// → expirationDate.before(new Date()) = false
// → Token NO expirado ✅

// Paso 4: Validación final
return ("alice@mail.com".equals("alice@mail.com") && !false);
//      (true && true)
//      ✅ true → TOKEN VÁLIDO
```

---

## 🎨 Diagrama de Interacción

```
┌──────────────────────────────────────────────┐
│           validateToken()                    │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │ 1. getUsernameFromToken(token)         │ │
│  │    [...]
│  │    [...]
│  │    getClaimsFromToken(token,           │ │
│  │        Claims::getSubject)             │ │
│  │    [...]
│  │    [...]
│  │    getAllClaimsFromToken(token)        │ │
│  │    [...]
│  │    Parsea JWT y retorna Claims         │ │
│  │    [...]
│  │    [...]
│  │    Retorna claims.getSubject()         │ │
│  │    [...]
│  │    "alice@mail.com"                    │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │ 2. isTokenExpired(token)               │ │
│  │    [...]
│  │    [...]
│  │    getExpirationDateFromToken(token)   │ │
│  │    [...]
│  │    [...]
│  │    getClaimsFromToken(token,           │ │
│  │        Claims::getExpiration)          │ │
│  │    [...]
│  │    Date: 2024-01-15 14:30:00           │ │
│  │    [...]
│  │    [...]
│  │    expirationDate.before(new Date())   │ │
│  │    [...]
│  │    false (no expiró)                   │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │ 3. Comparación Final                   │ │
│  │    [...]
│  │    "alice@mail.com" == "alice@mail.com"│ │
│  │    true                                │ │
│  │    [...]
│  │    !false = true                       │ │
│  │    [...]
│  │    true && true = ✅ TRUE              │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  🔓 Token Válido - Acceso Permitido          │
└──────────────────────────────────────────────┘
```

---

## 🚨 Casos de Error

### ❌ **Error 1: Username No Coincide**

```java
// Token contiene: "bob@mail.com"
// UserDetails contiene: "alice@mail.com"

validateToken(token, userDetails)
// → "alice@mail.com".equals("bob@mail.com") = false
// → false && true = ❌ FALSE
// → Token inválido (posible ataque/token robado)
```

### ❌ **Error 2: Token Expirado**

```java
// Token expira: 2024-01-15 09:00:00
// Fecha actual: 2024-01-15 10:00:00

validateToken(token, userDetails)
// → usernameFromUserDetails.equals(usernameFromJWT) = true
// → isTokenExpired(token) = true
// → true && !true = true && false = ❌ FALSE
// → Token inválido (caducado)
```

---

## 🔑 Conceptos Clave

```
✅ isTokenExpired()      → ⏰ Verifica tiempo de vida del token
✅ getUsernameFromToken() → 👤 Extrae el identificador del usuario
✅ validateToken()        → 🔐 Valida AMBAS condiciones
✅ Claims.getSubject()    → 📝 Campo estándar JWT para username
✅ Claims.getExpiration() → 📅 Campo estándar JWT para fecha exp
✅ .before()              → 📊 Método de Date para comparar fechas
```

---

## 💡 ¿Por Qué Validar Ambas Cosas?

| Validación | Previene |
|------------|----------|
| **Username coincide** | 🚫 Tokens robados o manipulados |
| **Token no expirado** | 🚫 Tokens antiguos/caducados |

```
🛡️ Seguridad en Capas:

Capa 1: ¿El token es para ESTE usuario?
Capa 2: ¿El token aún es VÁLIDO temporalmente?

Ambas deben ser TRUE para autorizar la petición
```
## 📝 Clase 59 - Finalizando la configuracion de nuestro JWT 👤👤🕵️‍♂🕵️‍♂🔑 🔑 


- En JWTService agregamos esto -> 

```java
 public String generateToken(UserDetails userDetails) {
        final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        return this.getToken(claims, userDetails.getUsername());
    }

    private String getToken(Map<String, Object> claims, String subject) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }
```
# 🔨 Generación de Tokens JWT - Explicación Completa

---

## 📋 Visión General

Estos dos métodos trabajan juntos para **crear** un token JWT firmado que contiene información del usuario autenticado.

---

## 🎯 ¿Qué Devuelven?

### 📦 **Retorno: String (Token JWT)**

Ambos métodos devuelven un **String** que representa un **token JWT firmado**.

```
Ejemplo de token generado:
eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH
```

---

## 1️⃣ `generateToken()` - Método Público

### 🎯 **Propósito**
Punto de entrada para generar un token JWT a partir de un `UserDetails`.

### 💻 **Código**
```java
public String generateToken(UserDetails userDetails) {
    final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
    return this.getToken(claims, userDetails.getUsername());
}
```

### 🔍 **¿Qué Hace Paso a Paso?**

```
1️⃣ Extrae las autoridades del usuario
   userDetails.getAuthorities()
   → [SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")]

2️⃣ Convierte a String
   .toString()
   → "[ROLE_USER, ROLE_ADMIN]"

3️⃣ Crea un Map con los claims personalizados
   Collections.singletonMap("ROLES", "[ROLE_USER, ROLE_ADMIN]")
   → Map con UNA entrada: key="ROLES", value="[ROLE_USER, ROLE_ADMIN]"

4️⃣ Extrae el username
   userDetails.getUsername()
   → "alice@mail.com"

5️⃣ Llama a getToken() con los datos preparados
   getToken(claims, "alice@mail.com")
```

### 📊 **Transformación de Datos**

```
┌──────────────────────────────────────────┐
│         UserDetails (Entrada)            │
│                                          │
│  username: "alice@mail.com"              │
│  password: "$2a$10abc..."                │
│  authorities: [                          │
│    SimpleGrantedAuthority("ROLE_USER"),  │
│    SimpleGrantedAuthority("ROLE_ADMIN")  │
│  ]                                       │
└──────────────────────────────────────────┘
                   ↓ generateToken()
┌──────────────────────────────────────────┐
│           Datos Extraídos                │
│                                          │
│  claims: {                               │
│    "ROLES": "[ROLE_USER, ROLE_ADMIN]"    │
│  }                                       │
│                                          │
│  subject: "alice@mail.com"               │
└──────────────────────────────────────────┘
                   ↓ getToken()
┌──────────────────────────────────────────┐
│         Token JWT (Salida)               │
│                                          │
│  "eyJhbGciOiJIUzI1NiJ9..."               │
└──────────────────────────────────────────┘
```

---

## 2️⃣ `getToken()` - Método Privado (Constructor Real)

### 🎯 **Propósito**
Construye y firma el token JWT usando la biblioteca JJWT.

### 💻 **Código**
```java
private String getToken(Map<String, Object> claims, String subject) {
    final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(key)
            .compact();
}
```

### 🔍 **¿Qué Hace Cada Paso?**

| Paso | Método | Descripción | Valor Ejemplo |
|------|--------|-------------|---------------|
| **1️⃣** | `Keys.hmacShaKeyFor()` | Crea la clave de firma HMAC | `SecretKey` object |
| **2️⃣** | `Jwts.builder()` | Inicia la construcción del JWT | Builder instance |
| **3️⃣** | `.setClaims(claims)` | Añade claims personalizados | `{"ROLES": "[ROLE_USER, ROLE_ADMIN]"}` |
| **4️⃣** | `.setSubject(subject)` | Define el "sujeto" (username) | `"alice@mail.com"` |
| **5️⃣** | `.setIssuedAt(...)` | Fecha de emisión | `2024-01-15 10:00:00` |
| **6️⃣** | `.setExpiration(...)` | Fecha de expiración | `2024-01-15 15:00:00` (5h después) |
| **7️⃣** | `.signWith(key)` | Firma el token con la clave | Genera la firma |
| **8️⃣** | `.compact()` | Serializa a String | Token JWT completo |

---

## 🔐 Cálculo de Expiración

### ⏰ **Fórmula**

```java
JWT_TOKEN_VALIDITY = 5 * 60 * 60  // 5 horas en segundos
// = 18000 segundos

setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                      │                          │                      │
//                      └─ Ahora en ms             └─ 18000               └─ Convertir a ms
//                         1705318800000              segundos              * 1000
//                                                                          = 18000000 ms
//                                                                          = 5 horas
```

### 📅 **Ejemplo Real**

```
Fecha actual:    2024-01-15 10:00:00  →  1705318800000 ms
+ 5 horas:       18000000 ms
───────────────────────────────────────────────────────
Fecha expiración: 2024-01-15 15:00:00  →  1705336800000 ms
```

---

## 🏗️ Estructura del Token JWT Generado

### 📦 **Anatomía de un JWT**

```
eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH
│                      │                                                                                                                                             │                                          │
└─ HEADER             └─ PAYLOAD                                                                                                                                    └─ SIGNATURE
   (Base64)              (Base64)                                                                                                                                       (Firma HMAC)
```

### 🔓 **HEADER (Decodificado)**

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### 📋 **PAYLOAD (Decodificado)**

```json
{
  "ROLES": "[ROLE_USER, ROLE_ADMIN]",    ← Claim personalizado (setClaims)
  "sub": "alice@mail.com",                ← Subject (setSubject)
  "iat": 1705318800,                      ← Issued At (setIssuedAt)
  "exp": 1705336800                       ← Expiration (setExpiration)
}
```

### 🔐 **SIGNATURE (Cómo se genera)**

```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

---

## 🔄 Flujo Completo de Generación

```
┌─────────────────────────────────────────────┐
│  1️⃣ USUARIO SE AUTENTICA                     │
│  POST /api/auth/login                       │
│  Body: {                                    │
│    "email": "alice@mail.com",               │
│    "password": "123456"                     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ Spring Security Valida Credenciales     │
│  AuthenticationManager.authenticate()       │
│                                             │
│  ✅ Credenciales correctas                  │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ UserDetailsService Carga UserDetails    │
│  loadUserByUsername("alice@mail.com")       │
│                                             │
│  UserDetails {                              │
│    username: "alice@mail.com"               │
│    authorities: [ROLE_USER, ROLE_ADMIN]     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ LLAMADA A generateToken()               │
│  jwtService.generateToken(userDetails)      │
└─────────────────────────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 4.1 Extraer Authorities   │
        │                           │
        │ userDetails.getAuthorities()
        │ → [SimpleGrantedAuthority("ROLE_USER"),
        │    SimpleGrantedAuthority("ROLE_ADMIN")]
        │                           │
        │ .toString()               │
        │ → "[ROLE_USER, ROLE_ADMIN]"
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 4.2 Crear Map de Claims  │
        │                           │
        │ Collections.singletonMap( │
        │   "ROLES",                │
        │   "[ROLE_USER, ROLE_ADMIN]"
        │ )                         │
        │                           │
        │ → Map<String, Object> {   │
        │     "ROLES": "[ROLE_USER, ROLE_ADMIN]"
        │   }                       │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 4.3 Extraer Username     │
        │                           │
        │ userDetails.getUsername() │
        │ → "alice@mail.com"        │
        └───────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  5️⃣ LLAMADA A getToken()                    │
│  getToken(claims, "alice@mail.com")         │
└─────────────────────────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.1 Crear Clave de Firma │
        │                           │
        │ Keys.hmacShaKeyFor(       │
        │   JWT_SECRET.getBytes()   │
        │ )                         │
        │                           │
        │ Secret: "jxgEQe.XHuPq..." │
        │ → SecretKey instance      │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.2 Construir JWT Builder│
        │                           │
        │ Jwts.builder()            │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.3 Añadir Claims        │
        │                           │
        │ .setClaims({              │
        │   "ROLES": "[ROLE_USER, ROLE_ADMIN]"
        │ })                        │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.4 Añadir Subject       │
        │                           │
        │ .setSubject(              │
        │   "alice@mail.com"        │
        │ )                         │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.5 Fecha de Emisión     │
        │                           │
        │ .setIssuedAt(             │
        │   new Date(               │
        │     System.currentTimeMillis()
        │   )                       │
        │ )                         │
        │                           │
        │ → 2024-01-15 10:00:00     │
        │   (1705318800000 ms)      │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.6 Fecha de Expiración  │
        │                           │
        │ .setExpiration(           │
        │   new Date(               │
        │     System.currentTimeMillis()
        │     + 18000 * 1000        │
        │   )                       │
        │ )                         │
        │                           │
        │ → 2024-01-15 15:00:00     │
        │   (1705336800000 ms)      │
        │   [5 horas después]       │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.7 Firmar el Token      │
        │                           │
        │ .signWith(key)            │
        │                           │
        │ HMAC-SHA256(              │
        │   header + "." + payload, │
        │   secretKey               │
        │ )                         │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ 5.8 Serializar a String  │
        │                           │
        │ .compact()                │
        │                           │
        │ Base64(header)            │
        │ + "."                     │
        │ + Base64(payload)         │
        │ + "."                     │
        │ + signature               │
        └───────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  6️⃣ TOKEN JWT GENERADO                      │
│                                             │
│  eyJhbGciOiJIUzI1NiJ9.                      │
│  eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURN  │
│  SU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJp  │
│  YXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgw  │
│  MH0.                                       │
│  X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI │
│  3gH                                        │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  7️⃣ RETORNAR TOKEN AL CLIENTE               │
│  Response: {                                │
│    "token": "eyJhbGciOiJIUzI1NiJ9..."       │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 🧪 Ejemplo con Datos Reales

### 📝 **Input (UserDetails)**

```java
UserDetails userDetails = new User(
    "alice@mail.com",
    "$2a$10abc...",
    List.of(
        new SimpleGrantedAuthority("ROLE_USER"),
        new SimpleGrantedAuthority("ROLE_ADMIN")
    )
);
```

### 🔄 **Procesamiento**

```java
// 1️⃣ generateToken() - Preparar datos
Map<String, Object> claims = Collections.singletonMap(
    "ROLES", 
    "[ROLE_USER, ROLE_ADMIN]"  // toString() de las authorities
);
String subject = "alice@mail.com";

// 2️⃣ getToken() - Construir JWT
SecretKey key = Keys.hmacShaKeyFor(
    "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4".getBytes(StandardCharsets.UTF_8)
);

String token = Jwts.builder()
    .setClaims({"ROLES": "[ROLE_USER, ROLE_ADMIN]"})
    .setSubject("alice@mail.com")
    .setIssuedAt(new Date(1705318800000L))  // 2024-01-15 10:00:00
    .setExpiration(new Date(1705336800000L))  // 2024-01-15 15:00:00
    .signWith(key)
    .compact();
```

### 📦 **Output (Token JWT)**

```
eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH
```

---

## 🔍 Decodificación del Token Generado

Puedes decodificar el token en [jwt.io](https://jwt.io):

### 🔓 **Header**
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### 📋 **Payload**
```json
{
  "ROLES": "[ROLE_USER, ROLE_ADMIN]",
  "sub": "alice@mail.com",
  "iat": 1705318800,
  "exp": 1705336800
}
```

### 🔐 **Signature**
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  your-256-bit-secret
) secret base64 encoded
```

---

## 📊 Comparación de Responsabilidades

| Método | Responsabilidad | Entrada | Salida |
|--------|----------------|---------|--------|
| **`generateToken()`** | 🎯 Preparar datos del usuario | `UserDetails` | `String` (JWT) |
| **`getToken()`** | 🔨 Construir y firmar JWT | `Map<String, Object>`, `String` | `String` (JWT) |

---

## 🔐 Seguridad del Token

### ✅ **Elementos de Seguridad**

| Elemento | Implementación | Propósito |
|----------|----------------|-----------|
| **Firma HMAC-SHA256** | `.signWith(key)` | Evita manipulación del token |
| **Secret Key** | `JWT_SECRET` (256 bits) | Clave privada para firmar |
| **Expiración** | `setExpiration()` | Limita el tiempo de vida |
| **Subject** | `setSubject()` | Identifica al usuario |

### 🛡️ **Proceso de Firma**

```
📄 HEADER + PAYLOAD
         ↓ Serializar
📝 "eyJhbGc...eyJST0x..."
         ↓ HMAC-SHA256 con SECRET
🔐 Signature
         ↓ Base64
🔑 "X7fK9mP3nQ..."
         ↓ Concatenar
✅ TOKEN COMPLETO
   "eyJhbGc...eyJST0x...X7fK9mP3nQ..."
```

---

## 💡 Conceptos Clave

```
✅ generateToken()          → 🎯 Orquestador (prepara datos)
✅ getToken()               → 🔨 Constructor (crea JWT)
✅ Collections.singletonMap → 📦 Map inmutable con 1 entrada
✅ setClaims()              → 📋 Datos personalizados
✅ setSubject()             → 👤 Identificador del usuario
✅ setIssuedAt()            → 📅 Timestamp de creación
✅ setExpiration()          → ⏰ Timestamp de expiración
✅ signWith()               → 🔐 Firma criptográfica
✅ compact()                → 📜 Serializa a String
```

---

## 🎯 Resumen Final

```
┌──────────────────────────────────────────┐
│  ENTRADA: UserDetails                    │
│  ┌────────────────────────────────────┐  │
│  │ username: "alice@mail.com"         │  │
│  │ authorities: [ROLE_USER, ROLE_ADMIN│  │
│  └────────────────────────────────────┘  │
└──────────────────────────────────────────┘
                   ↓
    ┌──────────────────────────┐
    │   generateToken()        │
    │   (Preparar datos)       │
    └──────────────────────────┘
                   ↓
    ┌──────────────────────────┐
    │   getToken()             │
    │   (Construir + Firmar)   │
    └──────────────────────────┘
                   ↓
┌──────────────────────────────────────────┐
│  SALIDA: Token JWT (String)              │
│                                          │
│  eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltS  │
│  T0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjo │
│  iYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg │
│  4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ │
│  8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH      │
│                                          │
│  ✅ Firmado con HMAC-SHA256              │
│  ✅ Válido por 5 horas                   │
│  ✅ Contiene roles del usuario           │
└──────────────────────────────────────────┘
```
### ¿Entonces ese Token yo soy el que lo creo?
# 🔐 ¿Quién Crea el Token JWT? - Explicación Clara

---

## 🎯 Respuesta Directa

### ✅ **SÍ, TÚ creas el token JWT (no Spring Security)**

```
📦 Spring Security: Autentica al usuario (valida username/password)
📦 TU JWTService:   Genera el token JWT después de la autenticación ✅
```

---

## 🔄 Flujo Completo: Autenticación vs. Generación de Token

```
┌─────────────────────────────────────────────┐
│  1️⃣ USUARIO ENVÍA CREDENCIALES              │
│  POST /api/auth/login                       │
│  {                                          │
│    "email": "alice@mail.com",               │
│    "password": "123456"                     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ SPRING SECURITY VALIDA CREDENCIALES    │
│  AuthenticationManager.authenticate()       │
│                                             │
│  ¿Qué hace?                                 │
│  ✅ Busca usuario en BD (CustomerRepository)│
│  ✅ Compara password con BCrypt             │
│  ✅ Si coincide → Usuario autenticado       │
│  ❌ Si no → Lanza BadCredentialsException   │
└─────────────────────────────────────────────┘
                    ↓
        ✅ Autenticación exitosa
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ TÚ GENERAS EL TOKEN JWT                │
│  jwtService.generateToken(userDetails) ✅   │
│                                             │
│  ¿Qué hace tu JWTService?                   │
│  📋 Extrae roles del usuario                │
│  🔐 Firma el token con tu JWT_SECRET        │
│  ⏰ Establece expiración (5 horas)          │
│  📦 Retorna token JWT como String           │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ RETORNAS EL TOKEN AL CLIENTE           │
│  Response: {                                │
│    "token": "eyJhbGciOiJIUzI1NiJ9..."       │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 📊 Tabla Comparativa: Spring Security vs. Tu Código

| Responsabilidad | ¿Quién lo hace? | Componente |
|----------------|-----------------|------------|
| **Validar username/password** | 🌱 Spring Security | `AuthenticationManager` |
| **Buscar usuario en BD** | 🌱 Spring Security | `UserDetailsService` |
| **Comparar contraseñas** | 🌱 Spring Security | `BCryptPasswordEncoder` |
| **Generar token JWT** | 👤 **TÚ** | `JWTService.generateToken()` |
| **Firmar el token** | 👤 **TÚ** | `JWTService.getToken()` |
| **Definir claims del token** | 👤 **TÚ** | `JWTService.generateToken()` |
| **Validar token en requests** | 👤 **TÚ** | `JWTService.validateToken()` |

---

## 🔍 ¿Con Qué Se Valida el Token?

### 🎯 **Validación en DOS Niveles**

```
┌─────────────────────────────────────────────┐
│  NIVEL 1: Validación Criptográfica 🔐       │
│  ¿El token es auténtico y no fue alterado?  │
└─────────────────────────────────────────────┘
       ↓ validateToken() verifica:
┌─────────────────────────────────────────────┐
│  ✅ Firma del token (con JWT_SECRET)        │
│  ✅ Token no expirado                       │
│  ✅ Estructura válida                       │
└─────────────────────────────────────────────┘

       ↓ SI PASA ↓

┌─────────────────────────────────────────────┐
│  NIVEL 2: Validación con Base de Datos 💾   │
│  ¿El usuario del token existe en la BD?     │
└─────────────────────────────────────────────┘
       ↓ validateToken() verifica:
┌─────────────────────────────────────────────┐
│  ✅ Username del token == Username de BD    │
│  (Extrae "sub" del token y compara)         │
└─────────────────────────────────────────────┘
```

---

## 🧪 Ejemplo Real de Validación

### 📝 **Escenario: Usuario hace un request con token**

```
┌─────────────────────────────────────────────┐
│  REQUEST DEL CLIENTE                        │
│  GET /api/admin/users                       │
│  Headers: {                                 │
│    "Authorization": "Bearer eyJhbGc..."     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  1️⃣ FILTRO JWT INTERCEPTA REQUEST           │
│  JwtAuthenticationFilter                    │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ EXTRAE INFORMACIÓN DEL TOKEN            │
│  String username = jwtService               │
│      .getUsernameFromToken(token);          │
│  // Retorna: "alice@mail.com"               │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ BUSCA USUARIO EN BASE DE DATOS 💾       │
│  UserDetails userDetails = userDetailsService│
│      .loadUserByUsername("alice@mail.com"); │
│                                             │
│  // Spring Security ejecuta:                │
│  // SELECT * FROM customers                 │
│  // WHERE email = 'alice@mail.com'          │
│                                             │
│  // Retorna UserDetails con:               │
│  // - username: "alice@mail.com"            │
│  // - roles: [ROLE_USER, ROLE_ADMIN]        │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ VALIDA EL TOKEN                         │
│  boolean isValid = jwtService               │
│      .validateToken(token, userDetails);    │
│                                             │
│  ¿Qué valida?                               │
│  ✅ Username del token == Username de BD    │
│     "alice@mail.com" == "alice@mail.com" ✅ │
│  ✅ Token no expirado                       │
│     exp: 1705336800 > now: 1705318800 ✅    │
│  ✅ Firma válida (con JWT_SECRET)           │
│                                             │
│  Resultado: true ✅                         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  5️⃣ AUTORIZA EL ACCESO                      │
│  ✅ Token válido                            │
│  ✅ Usuario existe en BD                    │
│  ✅ Continúa con el request                 │
└─────────────────────────────────────────────┘
```

---

## 🔐 ¿Dónde Entra la Base de Datos?

### 📊 **Tabla de Validación**

| Paso | ¿Usa BD? | Componente | Propósito |
|------|----------|------------|-----------|
| **1. Generar token (login)** | ✅ **SÍ** | `CustomerRepository` | Validar credenciales |
| **2. Firmar token** | ❌ NO | `JWTService` | Usar `JWT_SECRET` |
| **3. Validar firma del token** | ❌ NO | `JWTService` | Verificar con `JWT_SECRET` |
| **4. Extraer username del token** | ❌ NO | `JWTService` | Parsear claims |
| **5. Buscar usuario por username** | ✅ **SÍ** | `CustomerRepository` | Verificar existencia |
| **6. Comparar username token vs BD** | ✅ **SÍ** | `JWTService` | Validar autenticidad |

---

## 🎨 Diagrama: Token vs Base de Datos

```
┌─────────────────────────────────────────────┐
│         TOKEN JWT CONTIENE:                 │
│  {                                          │
│    "sub": "alice@mail.com",    ← Username   │
│    "ROLES": "[ROLE_USER, ROLE_ADMIN]",      │
│    "exp": 1705336800,                       │
│    "iat": 1705318800                        │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
        Se compara con ↓
                    ↓
┌─────────────────────────────────────────────┐
│      BASE DE DATOS (Tabla CUSTOMERS)        │
│  +----+------------------+----------+-----+ │
│  | ID | EMAIL            | PASSWORD | ... | │
│  +----+------------------+----------+-----+ │
│  | 1  | alice@mail.com   | $2a$10...|     | │
│  +----+------------------+----------+-----+ │
│                ↑                            │
│    ¿Este usuario existe? ✅                 │
└─────────────────────────────────────────────┘
```

---

## 💡 Concepto Clave: JWT vs Session

### 🆚 **Comparación**

| Característica | Session (Tradicional) | JWT (Tu implementación) |
|---------------|----------------------|------------------------|
| **¿Dónde se almacena el estado?** | 💾 Servidor (memoria/BD) | 📦 Cliente (en el token) |
| **¿Se consulta BD en cada request?** | ✅ SÍ (buscar sesión) | ⚠️ Solo para `UserDetails` |
| **¿Quién valida el token?** | 🌱 Spring Security | 👤 Tu `JWTService` |
| **¿Se puede revocar?** | ✅ SÍ (borrar sesión) | ❌ NO (hasta que expire) |

---

## 🛡️ Validación Sin Consultar BD (Opcional)

Podrías validar solo con la firma del token (sin consultar BD):

```java
public Boolean validateTokenWithoutDB(String token) {
    try {
        // Solo verifica firma y expiración
        Claims claims = this.getAllClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return !expiration.before(new Date());
    } catch (Exception e) {
        return false;
    }
}
```

**Pero generalmente NO se hace** porque:
- ❌ No detecta usuarios eliminados de la BD
- ❌ No detecta cambios en roles/permisos
- ❌ No detecta usuarios bloqueados

---

## 📋 Resumen Final

```
┌─────────────────────────────────────────────┐
│  ¿QUIÉN GENERA EL TOKEN?                    │
│  👤 TÚ (JWTService.generateToken())          │
│                                             │
│  ¿SPRING SECURITY GENERA EL TOKEN?          │
│  ❌ NO, Spring solo AUTENTICA al usuario     │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  ¿CON QUÉ SE VALIDA EL TOKEN?               │
│  1️⃣ Con tu JWT_SECRET (firma criptográfica) │
│  2️⃣ Con la BASE DE DATOS (usuario existe)   │
│  3️⃣ Comparando username token vs BD         │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  FLUJO COMPLETO:                            │
│  1. Usuario se loguea                       │
│  2. Spring valida credenciales (usa BD) ✅  │
│  3. TÚ generas el token JWT ✅              │
│  4. Cliente usa token en cada request       │
│  5. TÚ validas token (firma + BD) ✅        │
│  6. Spring Security autoriza el acceso      │
└─────────────────────────────────────────────┘
```

---

## 🎯 Puntos Clave

```
✅ TÚ creas el token JWT (no Spring Security)
✅ Spring Security solo autentica (valida username/password)
✅ El token se valida con:
   1. JWT_SECRET (firma criptográfica)
   2. Base de Datos (verificar que el usuario existe)
   3. Comparación (username del token == username de BD)
✅ La BD se consulta en:
   - Login (validar credenciales)
   - Cada request (buscar UserDetails por username)
❌ La BD NO se consulta para:
   - Verificar la firma del token (usa JWT_SECRET)
   - Parsear los claims del token
```

---
## 📝 Clase 60 - Configurando el Entry Point de JWT 👤👤🕵️‍♂🕵️‍♂🔑 🔑 

- Se crea en token 

![img](img/img_37.png)

- creamos Components -> JwtAuthenticationEntryPoint implementando AuthenticationEntryPoint
- creamos un controller AuthController

# 🚪 `JwtAuthenticationEntryPoint` - El "Guardia de Seguridad"

---

## 🎯 ¿Para Qué Sirve?

Es el **manejador de errores de autenticación** en Spring Security. Intercepta cuando un usuario **NO autenticado** intenta acceder a un endpoint protegido.

---

## 📋 Función del `AuthenticationEntryPoint`

```
┌─────────────────────────────────────────────┐
│  USUARIO SIN TOKEN (o token inválido)      │
│  intenta acceder a:                         │
│  GET /api/admin/users                       │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  Spring Security detecta:                   │
│  ❌ No hay token                            │
│  ❌ Token expiró                            │
│  ❌ Token inválido                          │
└─────────────────────────────────────────────┘
                    ↓
        ⚠️ AuthenticationException ⚠️
                    ↓
┌─────────────────────────────────────────────┐
│  JwtAuthenticationEntryPoint.commence()     │
│  👮 "¡Alto! No puedes pasar"                 │
│                                             │
│  response.sendError(                        │
│    HttpServletResponse.SC_UNAUTHORIZED,     │
│    "Unauthorized"                           │
│  )                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  RESPUESTA AL CLIENTE                       │
│  HTTP 401 Unauthorized                      │
│  {                                          │
│    "error": "Unauthorized"                  │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 🔍 ¿Qué Hace el Código?

```java
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) 
            throws IOException, ServletException {
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        //                 │                                     │
        //                 └─ Código HTTP 401                   └─ Mensaje de error
    }
}
```

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `request` | `HttpServletRequest` | La petición HTTP que falló |
| `response` | `HttpServletResponse` | La respuesta que se enviará |
| `authException` | `AuthenticationException` | El error de autenticación |

---

## 🎨 Flujo Visual Completo

```
┌─────────────────────────────────────────────┐
│  1️⃣ CLIENTE HACE REQUEST SIN TOKEN          │
│  GET /api/admin/users                       │
│  Headers: {                                 │
│    // ❌ Sin Authorization header           │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ JwtAuthenticationFilter                 │
│  Verifica si hay token...                   │
│  ❌ NO hay token                            │
│  ❌ No puede autenticar al usuario          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ Spring Security lanza Exception         │
│  throw new AuthenticationException(...)     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ JwtAuthenticationEntryPoint.commence()  │
│  👮 Intercepta el error                      │
│                                             │
│  response.sendError(401, "Unauthorized")    │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  5️⃣ RESPUESTA AL CLIENTE                    │
│  Status: 401 Unauthorized                   │
│  Body: "Unauthorized"                       │
└─────────────────────────────────────────────┘
```

---

## 🧪 Ejemplo Real

### 📝 **Escenario: Acceso sin autenticación**

```bash
# Cliente hace request SIN token
curl -X GET http://localhost:8080/api/admin/users

# ❌ Spring Security rechaza la petición
# ↓
# JwtAuthenticationEntryPoint devuelve:
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "timestamp": "2024-01-15T10:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/api/admin/users"
}
```

---

## 🛠️ Personalización del Entry Point

Puedes personalizar la respuesta de error:

```java
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) 
            throws IOException {
        
        // Configurar respuesta JSON personalizada
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        // Crear mensaje de error personalizado
        String jsonResponse = """
            {
                "error": "Unauthorized",
                "message": "Token JWT inválido o expirado",
                "path": "%s",
                "timestamp": "%s"
            }
            """.formatted(
                request.getRequestURI(),
                LocalDateTime.now().toString()
            );
        
        response.getWriter().write(jsonResponse);
    }
}
```

---

## 🔗 Integración con `SecurityConfig`

Debes registrar el `EntryPoint` en tu configuración de seguridad:

```java
@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(sess -> 
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> 
                ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // ← Aquí
            .build();
    }
}
```

---

## 📊 Casos de Uso del Entry Point

| Escenario | Sin Entry Point | Con Entry Point |
|-----------|-----------------|-----------------|
| **Request sin token** | ❌ Error genérico de Spring | ✅ `401 Unauthorized` personalizado |
| **Token expirado** | ❌ Error genérico de Spring | ✅ `401 Unauthorized` personalizado |
| **Token inválido** | ❌ Error genérico de Spring | ✅ `401 Unauthorized` personalizado |
| **Acceso no autorizado** | ❌ `403 Forbidden` genérico | ✅ Respuesta JSON personalizada |

---

## 🚨 Diferencia: `AuthenticationEntryPoint` vs. `AccessDeniedHandler`

| Componente | Cuándo se activa | Error HTTP |
|------------|------------------|------------|
| **`AuthenticationEntryPoint`** | ❌ Usuario **NO autenticado** (sin token) | **401 Unauthorized** |
| **`AccessDeniedHandler`** | ❌ Usuario autenticado pero **sin permisos** | **403 Forbidden** |

### 📋 **Ejemplo**

```java
// Usuario SIN token intenta acceder
GET /api/admin/users
// → AuthenticationEntryPoint → 401 Unauthorized

// Usuario CON token ROLE_USER intenta acceder a endpoint de ADMIN
GET /api/admin/users  (requiere ROLE_ADMIN)
// → AccessDeniedHandler → 403 Forbidden
```

---

## 💡 Resumen

```
✅ JwtAuthenticationEntryPoint = Manejador de errores de autenticación
✅ Se activa cuando NO hay token o es inválido
✅ Devuelve HTTP 401 Unauthorized
✅ Puedes personalizar el mensaje de error
✅ Debe registrarse en SecurityConfig con .authenticationEntryPoint()
```

---

## 🔑 Concepto Clave

```
🚪 AuthenticationEntryPoint es la "puerta de entrada"

Sin token/token inválido
        ↓
🚫 Spring Security bloquea
        ↓
👮 EntryPoint maneja el error
        ↓
📝 Devuelve 401 Unauthorized al cliente
```

---
# 🔐 Explicación Completa: `AuthController` - Endpoint de Autenticación

---

## 📋 Visión General

Este controlador maneja el **proceso de login** y **generación de tokens JWT**.

---

## 🎯 ¿Qué Hace Este Controlador?

```
📥 ENTRADA: Credenciales (username + password)
📤 SALIDA: Token JWT si las credenciales son válidas
```

---

## 🏗️ Arquitectura del Controlador

```java
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService jwtUserDetailService;
    private final JWTService jwtService;

    // ...métodos
}
```

### 📦 **Dependencias Inyectadas**

| Componente | Tipo | Responsabilidad |
|------------|------|-----------------|
| `authenticationManager` | Spring Security | ✅ Valida credenciales (username/password) |
| `jwtUserDetailService` | Custom Service | ✅ Carga datos del usuario desde BD |
| `jwtService` | Custom Service | ✅ Genera y valida tokens JWT |

---

## 1️⃣ Método `postToken()` - Endpoint de Login

### 🎯 **Propósito**
Endpoint público para que los usuarios se autentiquen y reciban un token JWT.

### 💻 **Código**
```java
@PostMapping("/authenticate")
public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
    this.authenticate(request);

    final var userDetails = this.jwtUserDetailService.loadUserByUsername(request.getUsername());

    final String token = this.jwtService.generateToken(userDetails);
    return ResponseEntity.ok(new JWTResponse(token));
}
```

### 🔍 **Flujo Paso a Paso**

```
┌─────────────────────────────────────────────┐
│  1️⃣ CLIENTE ENVÍA PETICIÓN DE LOGIN         │
│  POST /authenticate                         │
│  Body: {                                    │
│    "username": "alice@mail.com",            │
│    "password": "123456"                     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ VALIDA CREDENCIALES                     │
│  this.authenticate(request)                 │
│                                             │
│  ¿Qué hace?                                 │
│  ✅ Llama a AuthenticationManager           │
│  ✅ Verifica username/password con BD       │
│  ✅ Si es correcto: continúa                │
│  ❌ Si es incorrecto: lanza Exception       │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ CARGA DATOS COMPLETOS DEL USUARIO       │
│  jwtUserDetailService.loadUserByUsername(   │
│      "alice@mail.com"                       │
│  )                                          │
│                                             │
│  ¿Qué retorna?                              │
│  UserDetails {                              │
│    username: "alice@mail.com"               │
│    password: "$2a$10abc..."                 │
│    authorities: [ROLE_USER, ROLE_ADMIN]     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ GENERA TOKEN JWT                        │
│  jwtService.generateToken(userDetails)      │
│                                             │
│  ¿Qué hace?                                 │
│  📋 Extrae roles: "[ROLE_USER, ROLE_ADMIN]" │
│  🔐 Firma con JWT_SECRET                    │
│  ⏰ Establece expiración (5h)               │
│  📦 Retorna: "eyJhbGciOiJIUzI1NiJ9..."      │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  5️⃣ RETORNA TOKEN AL CLIENTE                │
│  ResponseEntity.ok(new JWTResponse(token))  │
│                                             │
│  HTTP 200 OK                                │
│  {                                          │
│    "jwt": "eyJhbGciOiJIUzI1NiJ9..."         │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 2️⃣ Método `authenticate()` - Validación de Credenciales

### 🎯 **Propósito**
Delega la validación de credenciales a Spring Security.

### 💻 **Código**
```java
private void authenticate(JWTRequest request) {
    try {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
        );
    } catch (BadCredentialsException | DisabledException e) {
        throw new RuntimeException("Incorrect username or password");
    }
}
```

### 🔍 **¿Qué Hace `AuthenticationManager`?**

```
┌─────────────────────────────────────────────┐
│  AuthenticationManager.authenticate()       │
│                                             │
│  1️⃣ Busca el usuario en la BD               │
│     UserDetailsService.loadUserByUsername() │
│     SELECT * FROM customers                 │
│     WHERE email = 'alice@mail.com'          │
│                                             │
│  2️⃣ Compara la contraseña                   │
│     BCryptPasswordEncoder.matches(          │
│       "123456",              ← Input        │
│       "$2a$10abc..."         ← BD (hash)    │
│     )                                       │
│                                             │
│  3️⃣ Resultado                               │
│     ✅ Contraseñas coinciden → Autenticado  │
│     ❌ No coinciden → BadCredentialsException│
└─────────────────────────────────────────────┘
```

### 📊 **Flujo de Validación**

```
┌─────────────────────────────────────────────┐
│  ENTRADA: JWTRequest                        │
│  {                                          │
│    "username": "alice@mail.com",            │
│    "password": "123456"                     │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ UsernamePasswordAuthenticationToken
        │                           │
        │ Crea objeto con:          │
        │ - Principal: "alice@mail.com"
        │ - Credentials: "123456"   │
        └───────────────────────────┘
                    ↓
        ┌───────────────────────────┐
        │ AuthenticationManager     │
        │                           │
        │ 1. Busca usuario en BD    │
        │ 2. Verifica password      │
        │ 3. Valida estado (enabled)│
        └───────────────────────────┘
                    ↓┌─────────────┐      ┌─────────────┐
    │ ✅ VÁLIDO   │      │ ❌ INVÁLIDO │
    │             │      │             │
    │ Retorna sin │      │ Lanza       │
    │ hacer nada  │      │ Exception   │
    └─────────────┘      └─────────────┘
                              ↓
                    ┌─────────────────┐
                    │ catch block     │
                    │                 │
                    │ throw new       │
                    │ RuntimeException│
                    │ ("Incorrect...") │
                    └─────────────────┘
```

---

## 🧪 Ejemplo Completo con Datos Reales

### 📝 **Escenario: Login Exitoso**

```bash
# 1️⃣ Cliente envía request
POST http://localhost:8080/authenticate
Content-Type: application/json

{
  "username": "alice@mail.com",
  "password": "123456"
}
```

```java
// ═══════════════════════════════════════════
// DENTRO DEL SERVIDOR
// ═══════════════════════════════════════════

// 2️⃣ postToken() recibe el request
JWTRequest request = {
    username: "alice@mail.com",
    password: "123456"
};

// 3️⃣ authenticate(request)
//    ↓
//    AuthenticationManager busca en BD:
//    SELECT * FROM customers WHERE email = 'alice@mail.com'
//    ↓
//    Resultado:
//    Customer {
//        id: 1,
//        email: "alice@mail.com",
//        password: "$2a$10$xyz...", ← Hash BCrypt
//        roles: [ROLE_USER, ROLE_ADMIN]
//    }
//    ↓
//    BCryptPasswordEncoder.matches("123456", "$2a$10$xyz...")
//    ✅ true → Autenticación exitosa

// 4️⃣ Cargar UserDetails completo
UserDetails userDetails = jwtUserDetailService.loadUserByUsername("alice@mail.com");
// Retorna:
// UserDetails {
//     username: "alice@mail.com",
//     password: "$2a$10$xyz...",
//     authorities: [
//         SimpleGrantedAuthority("ROLE_USER"),
//         SimpleGrantedAuthority("ROLE_ADMIN")
//     ]
// }

// 5️⃣ Generar token JWT
String token = jwtService.generateToken(userDetails);
// Retorna:
// "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH"

// 6️⃣ Crear respuesta
JWTResponse response = new JWTResponse(token);
// { "jwt": "eyJhbGc..." }

// 7️⃣ Retornar al cliente
return ResponseEntity.ok(response);
```

```bash
# 8️⃣ Cliente recibe respuesta
HTTP/1.1 200 OK
Content-Type: application/json

{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURNSU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJpYXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgwMH0.X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH"
}
```

---

## 🚨 Manejo de Errores

### ❌ **Caso 1: Credenciales Incorrectas**

```bash
POST /authenticate
{
  "username": "alice@mail.com",
  "password": "wrongpassword"
}
```

```
┌─────────────────────────────────────────────┐
│  authenticate(request)                      │
│  ↓                                          │
│  AuthenticationManager.authenticate()       │
│  ↓                                          │
│  BCryptPasswordEncoder.matches(             │
│    "wrongpassword",                         │
│    "$2a$10$xyz..."                          │
│  )                                          │
│  ↓                                          │
│  ❌ false → BadCredentialsException         │
│  ↓                                          │
│  catch block                                │
│  ↓                                          │
│  throw new RuntimeException(                │
│    "Incorrect username or password"         │
│  )                                          │
└─────────────────────────────────────────────┘
```

```bash
# Respuesta al cliente
HTTP/1.1 500 Internal Server Error

{
  "error": "Incorrect username or password"
}
```

---

### ❌ **Caso 2: Usuario Deshabilitado**

```bash
POST /authenticate
{
  "username": "disabled@mail.com",
  "password": "123456"
}
```

```
┌─────────────────────────────────────────────┐
│  AuthenticationManager detecta:             │
│  Customer.enabled = false                   │
│  ↓                                          │
│  DisabledException                          │
│  ↓                                          │
│  throw new RuntimeException(                │
│    "Incorrect username or password"         │
│  )                                          │
└─────────────────────────────────────────────┘
```

---

## 🎨 Diagrama de Arquitectura Completa

```
┌─────────────────────────────────────────────┐
│           CLIENTE (Postman/Frontend)        │
└─────────────────────────────────────────────┘
                    ↓
        POST /authenticate
        {username, password}
                    ↓
┌─────────────────────────────────────────────┐
│         AuthController.postToken()          │
│                                             │
│  1. authenticate(request)                   │
│     ↓                                       │
│  ┌─────────────────────────────────────┐   │
│  │ AuthenticationManager               │   │
│  │ ↓                                   │   │
│  │ UserDetailsService                  │   │
│  │ ↓                                   │   │
│  │ CustomerRepository (BD)             │   │
│  │ ↓                                   │   │
│  │ BCryptPasswordEncoder               │   │
│  │ ↓                                   │   │
│  │ ✅ Autenticado                      │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  2. loadUserByUsername()                    │
│     ↓                                       │
│  ┌─────────────────────────────────────┐   │
│  │ JwtUserDetailService                │   │
│  │ ↓                                   │   │
│  │ CustomerRepository.findByEmail()    │   │
│  │ ↓                                   │   │
│  │ Retorna UserDetails                 │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  3. generateToken(userDetails)              │
│     ↓                                       │
│  ┌─────────────────────────────────────┐   │
│  │ JWTService                          │   │
│  │ ↓                                   │   │
│  │ Extrae roles                        │   │
│  │ ↓                                   │   │
│  │ Firma con JWT_SECRET                │   │
│  │ ↓                                   │   │
│  │ Retorna token JWT                   │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  4. return ResponseEntity.ok(token)         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│         RESPUESTA AL CLIENTE                │
│  {                                          │
│    "jwt": "eyJhbGciOiJIUzI1NiJ9..."         │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 🔑 ¿Por Qué Se Llama Dos Veces a la BD?

### 📊 **Tabla de Llamadas**

| Paso | Método | ¿Consulta BD? | Propósito |
|------|--------|---------------|-----------|
| **1** | `authenticate()` | ✅ **SÍ** | Validar credenciales (username/password) |
| **2** | `loadUserByUsername()` | ✅ **SÍ** | Obtener datos completos (roles, permisos) |

### 💡 **¿Por Qué?**

```
1️⃣ Primera llamada (authenticate):
   - Spring Security valida SOLO credenciales
   - Retorna un Authentication básico
   - NO incluye todos los datos del UserDetails

2️⃣ Segunda llamada (loadUserByUsername):
   - Necesitamos el UserDetails COMPLETO
   - Para extraer los roles/authorities
   - Para generar el token JWT con los claims
```

---

## 🛠️ Mejora: Evitar la Doble Consulta

Puedes optimizar usando el `Authentication` retornado:

```java
@PostMapping("/authenticate")
public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
    // Autenticar y guardar el resultado
    Authentication authentication = this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );

    // El Authentication ya contiene el UserDetails
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    // Generar token
    final String token = this.jwtService.generateToken(userDetails);
    return ResponseEntity.ok(new JWTResponse(token));
}
```

---

## 📋 DTOs: `JWTRequest` y `JWTResponse`

### 📥 **JWTRequest (Input)**

```java
@Data
public class JWTRequest {
    private String username;
    private String password;
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `username` | `String` | Email del usuario |
| `password` | `String` | Contraseña en texto plano |

---

### 📤 **JWTResponse (Output)**

```java
@Data
@AllArgsConstructor
public class JWTResponse {
    private String jwt;
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `jwt` | `String` | Token JWT generado |

---

## 💡 Resumen Final

```
┌─────────────────────────────────────────────┐
│  FLUJO COMPLETO DE LOGIN:                   │
│                                             │
│  1️⃣ Cliente envía username + password       │
│  2️⃣ AuthenticationManager valida credenciales (BD) │
│  3️⃣ UserDetailsService carga datos completos (BD) │
│  4️⃣ JWTService genera token JWT             │
│  5️⃣ Retorna token al cliente                │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  ¿QUIÉN GENERA EL TOKEN?                    │
│  👤 TÚ (JWTService)                          │
│                                             │
│  ¿SPRING GENERA EL TOKEN?                   │
│  ❌ NO, Spring solo VALIDA credenciales     │
└─────────────────────────────────────────────┘
```

---

## 🎯 Conceptos Clave

```
✅ AuthController = Endpoint de login
✅ AuthenticationManager = Valida credenciales (Spring Security)
✅ JwtUserDetailService = Carga UserDetails desde BD
✅ JWTService = Genera y firma el token JWT
✅ JWTRequest = DTO de entrada (username + password)
✅ JWTResponse = DTO de salida (token JWT)
✅ El token lo generas TÚ, no Spring Security
```

# GRAN PREGUNTA Y PARA QUE DEBO GENERAR EL TOKEN

# 🔐 ¿Por Qué Generar un Token JWT Si Ya Validé el Usuario?

---

## 🎯 La Pregunta Clave

```
❓ Si ya validé username/password con la BD...
❓ ¿Para qué genero un token?
❓ ¿Con qué lo voy a validar si yo mismo lo creé?
```

---

## 💡 La Respuesta: **Evitar Consultar la BD en Cada Request**

### 📊 **Comparación: Sin Token vs Con Token**

```
┌─────────────────────────────────────────────┐
│  SIN TOKEN (Session Tradicional)            │
└─────────────────────────────────────────────┘

Usuario hace 1000 requests:
  GET /api/products          → 💾 Consulta BD
  GET /api/cart              → 💾 Consulta BD
  POST /api/orders           → 💾 Consulta BD
  ...
  (1000 consultas a la BD) ❌

┌─────────────────────────────────────────────┐
│  CON TOKEN JWT                              │
└─────────────────────────────────────────────┘

Usuario hace 1000 requests:
  POST /authenticate         → 💾 Consulta BD (1 vez)
    ↓
  Cliente guarda el token
    [...]
  GET /api/products   + token → ✅ Valida con JWT_SECRET
  GET /api/cart       + token → ✅ Valida con JWT_SECRET
  POST /api/orders    + token → ✅ Valida con JWT_SECRET
  ...
  (0 consultas a la BD) ✅
```

---

## 🔑 El Token JWT Es una "Llave Criptográfica"

### 🎨 **Analogía: Llave de Hotel**

```
┌─────────────────────────────────────────────┐
│  🏨 HOTEL (Tu API)                          │
│                                             │
│  1️⃣ Llegas a recepción (POST /authenticate) │
│     - Muestras tu cédula (username/password)│
│     - Recepcionista valida en sistema (BD)  │
│     - ✅ Te da una LLAVE electrónica (JWT)  │
│                                             │
│  2️⃣ Usas la llave para entrar a tu cuarto   │
│     - GET /api/rooms/101                    │
│     - La cerradura valida la llave          │
│     - ❌ NO consulta a recepción cada vez   │
│     - ✅ La llave tiene firma digital       │
│                                             │
│  3️⃣ ¿Cómo sabe que la llave es legítima?    │
│     - Firmada con código secreto del hotel  │
│     - Tiene fecha de expiración             │
│     - Contiene info del huésped (roles)     │
└─────────────────────────────────────────────┘
```

---

## 🔐 ¿Cómo Se Valida el Token Sin Consultar BD?

### 📋 **El Token Contiene TODO lo Necesario**

```
┌─────────────────────────────────────────────┐
│  TOKEN JWT GENERADO EN LOGIN                │
│                                             │
│  eyJhbGciOiJIUzI1NiJ9.                      │
│  eyJST0xFUyI6IltST0xFX1VTRVIsIFJPTEVfQURN  │
│  SU5dIiwic3ViIjoiYWxpY2VAbWFpbC5jb20iLCJp  │
│  YXQiOjE3MDUzMTg4MDAsImV4cCI6MTcwNTMzNjgw  │
│  MH0.                                       │
│  X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │ HEADER (Algoritmo)                  │   │
│  │ {                                   │   │
│  │   "alg": "HS256",                   │   │
│  │   "typ": "JWT"                      │   │
│  │ }                                   │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │ PAYLOAD (Datos del usuario)         │   │
│  │ {                                   │   │
│  │   "sub": "alice@mail.com",  ← Username│  │
│  │   "ROLES": "[ROLE_USER, ROLE_ADMIN]",│  │
│  │   "iat": 1705318800,  ← Creado      │   │
│  │   "exp": 1705336800   ← Expira      │   │
│  │ }                                   │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │ SIGNATURE (Firma Digital)           │   │
│  │                                     │   │
│  │ HMACSHA256(                         │   │
│  │   base64(header) + "." +            │   │
│  │   base64(payload),                  │   │
│  │   JWT_SECRET  ← ¡CLAVE SECRETA!     │   │
│  │ )                                   │   │
│  └─────────────────────────────────────┘   │
└─────────────────────────────────────────────┘
```

---

## 🔍 Validación del Token (Sin BD)

### 🎯 **3 Validaciones Criptográficas**

```java
// Código en JWTService.validateToken()

public Boolean validateToken(String token, UserDetails userDetails) {
    // 1️⃣ Extraer username del token
    final String username = getUsernameFromToken(token);
    //    ↓
    //    Decodifica el payload
    //    Retorna: "alice@mail.com"

    // 2️⃣ Verificar que el username coincide
    boolean usernameMatches = username.equals(userDetails.getUsername());
    //    ↓
    //    "alice@mail.com" == "alice@mail.com" ✅

    // 3️⃣ Verificar que el token NO expiró
    boolean isNotExpired = !isTokenExpired(token);
    //    ↓
    //    exp: 1705336800 (timestamp futuro)
    //    now: 1705318800 (timestamp actual)
    //    1705336800 > 1705318800 ✅

    // 4️⃣ Verificar la firma criptográfica (¡CLAVE!)
    //    Jwts.parser()
    //        .setSigningKey(JWT_SECRET)  ← Usa tu secreto
    //        .parseClaimsJws(token);     ← Valida firma
    //    ↓
    //    Si la firma NO coincide → SignatureException ❌
    //    Si coincide → ✅ Token legítimo

    return usernameMatches && isNotExpired;
}
```

---

## 🛡️ ¿Con Qué Se Valida el Token?

### 📊 **Tabla de Validación**

| Validación | ¿Consulta BD? | ¿Cómo se valida? |
|------------|---------------|------------------|
| **1. Firma del token** | ❌ NO | Con `JWT_SECRET` (clave secreta) |
| **2. Expiración** | ❌ NO | Comparando `exp` claim con fecha actual |
| **3. Estructura del token** | ❌ NO | Verificando formato JSON válido |
| **4. Username existe** | ⚠️ OPCIONAL | Consultando BD |

---

## 🔐 La Magia: La Firma Criptográfica

### 🎯 **¿Cómo Funciona?**

```
┌─────────────────────────────────────────────┐
│  GENERACIÓN DEL TOKEN (Login)               │
└─────────────────────────────────────────────┘

1️⃣ Creas el payload:
   {
     "sub": "alice@mail.com",
     "ROLES": "[ROLE_USER, ROLE_ADMIN]",
     "exp": 1705336800
   }

2️⃣ Firmas con tu JWT_SECRET:
   signature = HMACSHA256(
     header + payload,
     "mi_super_secreto_que_nadie_conoce_xyz123"
   )↓
   Resultado: X7fK9mP3nQ8uR2vL5wE6yT4hJ1sA0bN9cM8dO6pI3gH

3️⃣ Token completo:
   header.payload.signature

┌─────────────────────────────────────────────┐
│  VALIDACIÓN DEL TOKEN (Requests posteriores)│
└─────────────────────────────────────────────┘

1️⃣ Cliente envía el token:
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

2️⃣ TU servidor recibe el token

3️⃣ Intentas validar la firma:
   expectedSignature = HMACSHA256(
     header + payload,
     "mi_super_secreto_que_nadie_conoce_xyz123"
   )

4️⃣ Comparas firmas:
   expectedSignature == tokenSignature
   ↓
   ✅ SÍ coinciden → Token legítimo
   ❌ NO coinciden → Token falso/alterado
```

---

## 🚨 ¿Qué Pasa Si Alguien Intenta Falsificar el Token?

### 📝 **Escenario: Hacker Malicioso**

```
┌─────────────────────────────────────────────┐
│  HACKER intenta cambiar el payload          │
└─────────────────────────────────────────────┘

1️⃣ Token original:
   {
     "sub": "hacker@mail.com",
     "ROLES": "[ROLE_USER]",    ← Solo USER
     "exp": 1705336800
   }

2️⃣ Hacker modifica el payload:
   {
     "sub": "hacker@mail.com",
     "ROLES": "[ROLE_USER, ROLE_ADMIN]", ← ¡Agregó ADMIN!
     "exp": 1705336800
   }

3️⃣ Hacker envía el token modificado a tu API

4️⃣ Tu servidor intenta validar:
   expectedSignature = HMACSHA256(
     header + PAYLOAD_MODIFICADO,
     JWT_SECRET
   )
   ↓
   expectedSignature ≠ tokenSignature ❌
   ↓
   SignatureException: "JWT signature does not match"
   ↓
   🚫 ACCESO DENEGADO

┌─────────────────────────────────────────────┐
│  ¿POR QUÉ FALLA?                            │
│                                             │
│  El hacker NO conoce tu JWT_SECRET          │
│  No puede generar una firma válida          │
│  Cualquier modificación invalida el token   │
└─────────────────────────────────────────────┘
```

---

## 🎨 Flujo Completo: Login → Request Protegido

```
┌─────────────────────────────────────────────┐
│  1️⃣ LOGIN (POST /authenticate)              │
│                                             │
│  Cliente envía:                             │
│  {                                          │
│    "username": "alice@mail.com",            │
│    "password": "123456"                     │
│  }                                          │
│                                             │
│  ↓                                          │
│  AuthenticationManager valida con BD ✅     │
│  ↓                                          │
│  JWTService genera token:                   │
│  - Extrae roles de BD                       │
│  - Firma con JWT_SECRET                     │
│  - Establece expiración                     │
│  ↓                                          │
│  Retorna: "eyJhbGciOiJIUzI1NiJ9..."         │
└─────────────────────────────────────────────┘
                    ↓
        Cliente guarda el token
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ REQUEST PROTEGIDO                       │
│  (GET /api/admin/users)                     │
│                                             │
│  Cliente envía:                             │
│  Headers: {                                 │
│    Authorization: "Bearer eyJhbGc..."       │
│  }                                          │
│                                             │
│  ↓                                          │
│  JwtAuthenticationFilter intercepta         │
│  ↓                                          │
│  JWTService.validateToken():                │
│  ┌─────────────────────────────────────┐   │
│  │ 1. Verifica firma con JWT_SECRET ✅ │   │
│  │ 2. Verifica expiración ✅           │   │
│  │ 3. Extrae username y roles ✅       │   │
│  │ 4. NO consulta BD ✅                │   │
│  └─────────────────────────────────────┘   │
│  ↓                                          │
│  Spring Security autoriza el acceso ✅      │
│  ↓                                          │
│  Retorna datos al cliente                   │
└─────────────────────────────────────────────┘
```

---

## 📊 Comparación: Session vs JWT

| Característica | Session (Tradicional) | JWT (Tu implementación) |
|---------------|----------------------|------------------------|
| **¿Dónde se guarda el estado?** | 💾 Servidor (memoria/BD) | 📦 Cliente (token) |
| **¿Consulta BD en cada request?** | ✅ SÍ (buscar sesión) | ❌ NO (valida con firma) |
| **Escalabilidad** | ❌ Difícil (sesiones en memoria) | ✅ Fácil (stateless) |
| **¿Se puede revocar?** | ✅ SÍ (eliminar sesión) | ⚠️ NO (hasta que expire) |
| **Seguridad ante modificación** | ✅ Inmune (estado en servidor) | ✅ Inmune (firma criptográfica) |

---

## 💡 Resumen: ¿Para Qué Sirve el Token?

```
┌─────────────────────────────────────────────┐
│  PROPÓSITO DEL TOKEN JWT:                   │
│                                             │
│  ✅ Evitar consultar BD en cada request     │
│  ✅ Validar usuario con firma criptográfica │
│  ✅ Contener roles/permisos del usuario     │
│  ✅ Tener expiración automática             │
│  ✅ Ser stateless (no guardar sesiones)     │
│  ✅ Escalar horizontalmente                 │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  ¿CON QUÉ SE VALIDA?                        │
│                                             │
│  🔐 Con tu JWT_SECRET (clave secreta)       │
│  ⏰ Con la fecha de expiración (exp claim)  │
│  📋 Con la estructura del token             │
│  ⚠️ OPCIONAL: Con la BD (para casos especiales) │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  LA CLAVE:                                  │
│                                             │
│  El token ES una "llave firmada digitalmente"│
│  Solo quien conoce JWT_SECRET puede validarlo│
│  Es imposible falsificar sin el secreto     │
└─────────────────────────────────────────────┘
```

---

## 🎯 Conceptos Clave

```
✅ JWT = "Llave criptográfica" auto-contenida
✅ Se valida con JWT_SECRET (no con BD)
✅ Firma digital impide falsificaciones
✅ Reduce carga en BD (solo 1 consulta en login)
✅ Token contiene TODO (username, roles, expiración)
✅ Es stateless (servidor no guarda sesiones)
```
---

# ¿ALGO INTERESANTE COMO SE COMPARA EL TOKEN CON EL SECRET?🔐🔐🔐

## 🔐 ¡EXACTO! - Explicación del `JWT_SECRET`

---

## 🎯 Respuesta Directa

**SÍ**, el `JWT_SECRET` se usa para **validar** que tu token es legítimo.

---

## 🔍 ¿Cómo Funciona? - Explicación Paso a Paso

### 📋 **Tu Token Real**

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3ViIjoiYWNjb3VudEBkZWJ1Z2dlYW5kb2llYXMuY29tIiwiaWF0IjoxNzcwNTg5MzU0LCJleHAiOjE3NzA2MDczNTR9.GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY"
}
```

---

## 🧩 Decodificación de Tu Token

### 🔓 **Partes del Token**

```
eyJhbGciOiJIUzI1NiJ9
│
└─ HEADER

eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3ViIjoiYWNjb3VudEBkZWJ1Z2dlYW5kb2llYXMuY29tIiwiaWF0IjoxNzcwNTg5MzU0LCJleHAiOjE3NzA2MDczNTR9
│
└─ PAYLOAD

GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY
│
└─ SIGNATURE ← ¡AQUÍ SE USA JWT_SECRET!
```

---

### 📦 **HEADER (Decodificado)**

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

---

### 📋 **PAYLOAD (Decodificado)**

```json
{
  "ROLES": "[VIEW_ACCOUNT]",
  "sub": "account@debuggeanoideas.com",
  "iat": 1770589354,
  "exp": 1770607354
}
```

---

### 🔐 **SIGNATURE (La Clave)**

```
GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY
```

**Esta firma se generó usando tu `JWT_SECRET`:**

```java
JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";
```

---

## 🔨 ¿Cómo Se Generó la Firma?

### 🎨 **Proceso de Generación (Login)**

```
┌─────────────────────────────────────────────┐
│  1️⃣ CUANDO GENERASTE EL TOKEN               │
│  (POST /authenticate)                       │
└─────────────────────────────────────────────┘

1. Creas el HEADER:
   {
     "alg": "HS256",
     "typ": "JWT"
   }↓
   Base64URL: eyJhbGciOiJIUzI1NiJ9

2. Creas el PAYLOAD:
   {
     "ROLES": "[VIEW_ACCOUNT]",
     "sub": "account@debuggeanoideas.com",
     "iat": 1770589354,
     "exp": 1770607354
   }
   ↓
   Base64URL: eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3Vi...

3. Generas la FIRMA con tu JWT_SECRET:
   ↓
   signature = HMACSHA256(
     "eyJhbGciOiJIUzI1NiJ9" + "." + "eyJST0xFUyI6Ilt...",
     "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4"  ← JWT_SECRET
   )
   ↓
   Resultado: GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

4. Token completo:
   header.payload.signature
```

---

## 🛡️ ¿Cómo Se Valida la Firma?

### 🔍 **Proceso de Validación (Request)**

```
┌─────────────────────────────────────────────┐
│  2️⃣ CUANDO VALIDAS EL TOKEN                 │
│  (GET /api/admin/users)                     │
└─────────────────────────────────────────────┘

1. Cliente envía el token:
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6...

2. Tu JWTService extrae las partes:
   header  = "eyJhbGciOiJIUzI1NiJ9"
   payload = "eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3Vi..."
   signatureReceived = "GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY"

3. Tu servidor RECALCULA la firma con tu JWT_SECRET:
   ↓
   expectedSignature = HMACSHA256(
     header + "." + payload,
     "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4"  ← JWT_SECRET
   )
   ↓
   Resultado: GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

4. Compara las firmas:
   ↓
   expectedSignature == signatureReceived
   ↓
   "GCk5CWs70Wf3..." == "GCk5CWs70Wf3..." ✅
   ↓
   ✅ Token válido (firma coincide)
```

---

## 🧪 Ejemplo Visual: Generación vs Validación

```
┌─────────────────────────────────────────────┐
│  LOGIN (Generación del Token)               │
└─────────────────────────────────────────────┘

Input:
  username: "account@debuggeanoideas.com"
  password: "123456"

Proceso:
  1. Validar credenciales con BD ✅
  2. Crear payload con roles:
     {
       "ROLES": "[VIEW_ACCOUNT]",
       "sub": "account@debuggeanoideas.com",
       "iat": 1770589354,
       "exp": 1770607354
     }
  3. Firmar con JWT_SECRET:
     HMACSHA256(
       header + payload,
       "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4"
     )
     ↓
     Firma: GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

Output:
  eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3ViOiJhY2NvdW50QGRlYnVnZ2VhbmRvaWVhcy5jb20iLCJpYXQiOjE3NzA1ODkzNTQsImV4cCI6MTc3MDYwNzM1NH0.GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

═════════════════════════════════════════════

┌─────────────────────────────────────────────┐
│  REQUEST (Validación del Token)             │
└─────────────────────────────────────────────┘

Input:
  Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6...

Proceso:
  1. Extraer header y payload del token
  2. RECALCULAR la firma con JWT_SECRET:
     HMACSHA256(
       header + payload,
       "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4"
     )
     ↓
     Firma esperada: GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY
  3. Comparar con la firma recibida:
     GCk5CWs70Wf3... == GCk5CWs70Wf3... ✅

Output:
  ✅ Token válido → Continúa con el request
```

---

## 🚨 ¿Qué Pasa Si Alguien Cambia el Token?

### 📝 **Escenario: Hacker Intenta Modificar**

```
┌─────────────────────────────────────────────┐
│  TOKEN ORIGINAL                             │
└─────────────────────────────────────────────┘

Payload:
{
  "ROLES": "[VIEW_ACCOUNT]",
  "sub": "account@debuggeanoideas.com",
  "iat": 1770589354,
  "exp": 1770607354
}

Firma: GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

═════════════════════════════════════════════

┌─────────────────────────────────────────────┐
│  HACKER MODIFICA EL PAYLOAD                 │
└─────────────────────────────────────────────┘

Payload modificado:
{
  "ROLES": "[VIEW_ACCOUNT, ROLE_ADMIN]", ← ¡Agregó ADMIN!
  "sub": "account@debuggeanoideas.com",
  "iat": 1770589354,
  "exp": 1770607354
}

Firma (sigue igual): GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY

═════════════════════════════════════════════

┌─────────────────────────────────────────────┐
│  TU SERVIDOR VALIDA EL TOKEN                │
└─────────────────────────────────────────────┘

1. Recalcula la firma con JWT_SECRET:
   HMACSHA256(
     header + PAYLOAD_MODIFICADO,
     "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4"
   )
   ↓
   Nueva firma: XYZ123diferentes456...  ← ¡DISTINTA!

2. Compara firmas:
   XYZ123diferentes456... ≠ GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY
   ↓
   ❌ FIRMAS NO COINCIDEN

3. Resultado:
   SignatureException: "JWT signature does not match"
   ↓
   🚫 ACCESO DENEGADO
```

---

## 🔑 Código Real: ¿Dónde Se Usa `JWT_SECRET`?

### 📋 **En tu `JWTService`**

```java
@Service
public class JWTService {
    // 🔐 TU CLAVE SECRETA
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    // ═══════════════════════════════════════════
    // 1️⃣ GENERACIÓN DEL TOKEN (Login)
    // ═══════════════════════════════════════════
    private String getToken(Map<String, Object> claims, String subject) {
        // Crear clave de firma con JWT_SECRET
        final var key = Keys.hmacShaKeyFor(
            JWT_SECRET.getBytes(StandardCharsets.UTF_8)
        );
        //          ↑↑↑↑↑↑↑↑↑↑
        //      ¡SE USA JWT_SECRET!

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(key)  // ← Firma con la clave generada
            //    ↑↑↑
            // ¡FIRMA EL TOKEN CON JWT_SECRET!
            .compact();
    }

    // ═══════════════════════════════════════════
    // 2️⃣ VALIDACIÓN DEL TOKEN (Requests)
    // ═══════════════════════════════════════════
    private Claims getAllClaimsFromToken(String token) {
        // Crear clave de firma con JWT_SECRET
        final var key = Keys.hmacShaKeyFor(
            JWT_SECRET.getBytes(StandardCharsets.UTF_8)
        );
        //          ↑↑↑↑↑↑↑↑↑↑
        //      ¡SE USA JWT_SECRET!

        return Jwts.parserBuilder()
            .setSigningKey(key)  // ← Valida con la clave generada
            //         ↑↑↑
            // ¡VALIDA LA FIRMA CON JWT_SECRET!
            .build()
            .parseClaimsJws(token)  // ← Si la firma no coincide → Exception
            .getBody();
    }
}
```

---

## 📊 Tabla Comparativa: Generación vs Validación

| Proceso | Método | ¿Usa `JWT_SECRET`? | Propósito |
|---------|--------|-------------------|-----------|
| **Generación** | `getToken()` | ✅ **SÍ** | Firmar el token |
| **Validación** | `getAllClaimsFromToken()` | ✅ **SÍ** | Verificar la firma |
| **Extracción de claims** | `getUsernameFromToken()` | ✅ **SÍ** (internamente) | Parsear el token |

---

## 💡 La Analogía del Sello de Cera

```
🏰 REY (Tu servidor)
   │
   ├─ Tiene un SELLO ÚNICO (JWT_SECRET)
   │
   └─ Cuando escribe una CARTA (token):
      1. Escribe el mensaje (payload)
      2. SELLA con cera usando su sello único (firma)
      3. Envía la carta al mensajero (cliente)

📬 MENSAJERO (Cliente)
   │
   └─ Guarda la carta sellada (token)

🏰 GUARDIÁN DEL CASTILLO (JwtAuthenticationFilter)
   │
   ├─ Cuando el mensajero regresa:
   │  1. Verifica el SELLO de la carta
   │  2. Solo el REY tiene ese sello
   │  3. Si el sello coincide → Carta legítima ✅
   │  4. Si el sello NO coincide → Carta falsa ❌
   │
   └─ NO necesita preguntarle al Rey cada vez
      (No consulta BD)
```

---

## 🎯 Resumen Final

```
┌─────────────────────────────────────────────┐
│  ¿CÓMO SE RELACIONAN?                       │
│                                             │
│  JWT_SECRET                                 │
│      ↓                                      │
│  Se usa para FIRMAR el token (login)        │
│      ↓                                      │
│  Token: header.payload.FIRMA                │
│                            ↑                │
│                    Generada con JWT_SECRET  │
│      ↓                                      │
│  Se usa para VALIDAR el token (requests)    │
│      ↓                                      │
│  Recalcula la firma con JWT_SECRET          │
│      ↓                                      │
│  Compara firma recalculada vs firma del token│
│      ↓                                      │
│  Si coinciden → ✅ Token válido             │
│  Si NO coinciden → ❌ Token falso           │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  TU TOKEN:                                  │
│                                             │
│  eyJhbGciOiJIUzI1NiJ9.                      │
│  eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3ViOiJhY2NvdW50QGRlYnVnZ2VhbmRvaWVhcy5jb20iLCJpYXQiOjE3NzA1ODkzNTQsImV4cCI6MTc3MDYwNzM1NH0.
│  GCk5CWs70Wf3-zmTI1URArCzaNa1sf7R8cKCvNsjQgY
│                    ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑        │
│          Esta firma se generó CON tu        │
│          JWT_SECRET y solo puede validarse  │
│          CON el mismo JWT_SECRET            │
└─────────────────────────────────────────────┘
```

---

## 🔑 Conceptos Clave

```
✅ JWT_SECRET = Clave secreta para firmar y validar
✅ La firma del token se genera CON JWT_SECRET
✅ La firma se valida RECALCULÁNDOLA con JWT_SECRET
✅ Si alguien modifica el token, la firma NO coincide
✅ Solo quien conoce JWT_SECRET puede generar tokens válidos
✅ NO se "compara" directamente, se RECALCULA la firma
```
---

# 🚨 ¡IMPORTANTE! - Error de Codificación en `JWT_SECRET`

---

## ❌ Problema Detectado

Tu `JWT_SECRET` tiene una **codificación incorrecta**. El mensaje de error indica:

```
Invalid base64url string. Use the Base64 encoding using the URL and
filename-safe character set as defined in Section 5 of RFC 4648.
```

---

## 🔍 ¿Qué Está Pasando?

Tu clave secreta:

```java
JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";
```

Contiene caracteres que **NO son válidos** en **Base64URL**:

| Carácter | ¿Es válido en Base64URL? | Problema |
|----------|-------------------------|----------|
| `.` (punto) | ❌ **NO** | Debe ser `-` o `_` |

---

## ✅ Solución 1: Generar una Clave Válida

### 🔐 **Opción A: Usar Base64 Estándar**

Genera una clave con caracteres válidos:

```java
// Genera una clave segura de 256 bits (32 bytes)
String validSecret = "jxgEQeXHuPq8VdbyYFNkANdudQ0903YUn4abcdefghij1234567890ABCD";
//                      ↑ Sin puntos, solo letras/números
```

### 🔐 **Opción B: Generar Clave Aleatoria (Recomendado)**

Usa este código para generar una clave segura:

```java
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // Genera clave de 256 bits (32 bytes)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        
        // Convierte a Base64
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        
        System.out.println("JWT_SECRET: " + base64Key);
    }
}
```

**Ejemplo de salida:**

```
JWT_SECRET: 3K9mP2nQ5tR8uV1wY4zA7cE0fH3jL6oN9qS2vX5yB8eG1iM4pT7wZ0dC3fI6k
```

---

## 🛠️ Solución 2: Usar la Clave Actual con Codificación Correcta

Si quieres mantener tu clave, debes codificarla correctamente:

```java
@Service
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    
    // ❌ ANTES (con punto)
    // public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";
    
    // ✅ DESPUÉS (sin puntos, solo letras/números)
    public static final String JWT_SECRET = "jxgEQeXHuPq8VdbyYFNkANdudQ0903YUn4abcd1234567890";
    
    // ...resto del código
}
```

---

## 🔐 Solución 3: Usar `application.properties`

**Mejor práctica:** No hardcodear la clave en el código.

### 📝 **1. En `application.properties`:**

```properties
jwt.secret=jxgEQeXHuPq8VdbyYFNkANdudQ0903YUn4abcd1234567890
jwt.expiration=18000
```

### 💻 **2. En `JWTService.java`:**

```java
@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private Long JWT_TOKEN_VALIDITY;
    
    // ...resto del código
}
```

---

## 🧪 Cómo Verificar que Tu Clave Es Válida

### 🔍 **Usa JWT.io**

1. Ve a [https://jwt.io/](https://jwt.io/)
2. Pega tu token en el campo **Encoded**
3. En **Verify Signature**, pega tu `JWT_SECRET`:

```
jxgEQeXHuPq8VdbyYFNkANdudQ0903YUn4abcd1234567890
```

4. Si dice **"Signature Verified"** → ✅ Clave válida
5. Si dice **"Invalid Signature"** → ❌ Clave incorrecta

---

## 📋 Caracteres Válidos en JWT Secret

| Tipo | Caracteres Permitidos |
|------|----------------------|
| **Letras mayúsculas** | `A-Z` |
| **Letras minúsculas** | `a-z` |
| **Números** | `0-9` |
| **Símbolos especiales** | `-` (guión), `_` (guion bajo) |

| ❌ **NO Permitidos** | `.` (punto), `,` (coma), `/` (slash), `+` (plus) |

---

## 🚀 Código Corregido Completo

```java
@Service
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    
    // ✅ Clave válida sin puntos
    public static final String JWT_SECRET = 
        "jxgEQeXHuPq8VdbyYFNkANdudQ0903YUn4abcd1234567890ABCDEF";
    
    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(
            JWT_SECRET.getBytes(StandardCharsets.UTF_8)
        );
        
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // ...resto de los métodos sin cambios
}
```

---

## 💡 Resumen

```
┌─────────────────────────────────────────────┐
│  PROBLEMA:                                  │
│  JWT_SECRET = "jxg...N.dudQ..."             │
│                    ↑                        │
│              Punto (.) no válido            │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  SOLUCIÓN:                                  │
│  JWT_SECRET = "jxg...Ndudq..."              │
│                    ↑                        │
│              Sin punto (solo letras/números)│
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  CARACTERES VÁLIDOS:                        │
│  ✅ A-Z, a-z, 0-9, -, _                     │
│  ❌ . , / + = (estos NO)                    │
└─────────────────────────────────────────────┘
```

---

## 🎯 Acción Inmediata

1. **Cambia tu `JWT_SECRET`** eliminando los puntos
2. **Genera nuevos tokens** (los anteriores ya no servirán)
3. **Pide a los usuarios que hagan login de nuevo**


----

## 📝 Clase 64 - 65 - 66 - VALIDANDO NUESTRO JWT EN EL PROCESO DE AUTORIZACION PARTE 1 👤👤🕵️‍♂🕵️‍♂🔑 🔑

```java
@Component
@AllArgsConstructor
public class JWTValidationfilter {

    private final JWTService jwtService;
    private final JWTUserDetailService jwtUserDetailService;
}

```

### ¿Para qué se usa `@Component` en ese contexto?

## ¿Qué es `@Component`?

`@Component` es una anotación de **Spring Framework** que le indica a Spring que esa clase 
debe ser gestionada como un **bean** dentro del contenedor de inversión de control (IoC). Es decir, Spring se encargará de:

1. **Crear una instancia** de esa clase automáticamente al iniciar la aplicación.
2. **Inyectar sus dependencias** (en este caso `jwtService` y `jwtUserDetailService`).
3. **Gestionar su ciclo de vida**.

## Desglose de tu código

```java
@Component          // 1️⃣ Registra esta clase como un bean de Spring
@AllArgsConstructor  // 2️⃣ Lombok genera un constructor con todos los campos
public class JWTValidationfilter {

    private final JWTService jwtService;               // 3️⃣ Inyectado por Spring
    private final JWTUserDetailService jwtUserDetailService; // 3️⃣ Inyectado por Spring
}
```

| Anotación | Función |
|---|---|
| `@Component` | Marca la clase para que Spring la detecte durante el **component scanning** y la registre como bean. |
| `@AllArgsConstructor` | Genera un constructor con **todos los campos** (`jwtService`, `jwtUserDetailService`), 
lo cual permite la **inyección por constructor**. |

## ¿Por qué `@Component` y no `@Service`, `@Repository` o `@Controller`?

Todas estas anotaciones son **especializaciones** de `@Component`:

```
@Component          → Bean genérico
  ├── @Service      → Lógica de negocio
  ├── @Repository   → Acceso a datos
  ├── @Controller   → Controladores web
  └── @Configuration → Clases de configuración
```

En este caso se usa `@Component` porque `JWTValidationfilter` es un **filtro de validación JWT**,
que no encaja exactamente en las categorías de `@Service`, `@Repository` ni `@Controller`. 
Es un componente de infraestructura/seguridad genérico.

## ¿Qué pasaría sin `@Component`?

Sin `@Component`, Spring **no sabría que debe crear** una instancia de `JWTValidationfilter`, 
y al intentar usarlo obtendrías un error como:

```
No qualifying bean of type 'JWTValidationfilter' available
```

## Flujo simplificado

```
1. Spring inicia → escanea paquetes buscando @Component (y derivadas)
2. Encuentra JWTValidationfilter con @Component
3. Ve que necesita JWTService y JWTUserDetailService (por el constructor)
4. Busca esos beans en el contenedor, los encuentra y los inyecta
5. JWTValidationfilter queda listo para usarse ✅
```

### En resumen

`@Component` es necesario para que Spring **detecte, instancie y gestione** automáticamente tu 
filtro JWT, permitiendo que las dependencias (`jwtService` y `jwtUserDetailService`) se inyecten 
sin que tú tengas que crearlas manualmente con `new`.

## BORRAMOS EL MYAUTHENTICATION LA CLASE POR QUE AHORA NOS LOGUAMOS CON JWT

---

# 🔐 `JWTValidationFilter`: Flujo Completo y Detallado

---

## 🎯 ¿Por Qué Se Implementa Este Filtro?

```
┌─────────────────────────────────────────────┐
│  🚪 PROBLEMA: Proteger Endpoints            │
│                                             │
│  Sin JWT:                                   │
│  GET /api/accounts → ❌ Cualquiera accede   │
│                                             │
│  Con JWT:                                   │
│  GET /api/accounts                          │
│  Authorization: Bearer TOKEN                │
│  → ✅ Solo usuarios autenticados            │
└─────────────────────────────────────────────┘
```

---

## 🔄 Flujo Completo: De Login a Endpoint Protegido

```
┌─────────────────────────────────────────────┐
│  1️⃣ USUARIO HACE LOGIN                      │
│  POST /authenticate                         │
│  {                                          │
│    "username": "account@example.com",       │
│    "password": "password123"                │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  2️⃣ SERVIDOR GENERA TOKEN                   │
│  JWTService.generateToken(userDetails)      │
│                                             │
│  Response:                                  │
│  {                                          │
│    "jwt": "eyJhbGci...GCk5CWs70Wf3..."      │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  3️⃣ USUARIO GUARDA EL TOKEN                 │
│  Token almacenado en:                       │
│  - LocalStorage (frontend)                  │
│  - Postman (para testing)                   │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  4️⃣ USUARIO ACCEDE A ENDPOINT PROTEGIDO     │
│  GET /api/accounts                          │
│  Authorization: Bearer eyJhbGci...          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  5️⃣ JWTValidationFilter SE EJECUTA          │
│  → Aquí comienza el filtro                  │
└─────────────────────────────────────────────┘
```

---

## 🔍 Flujo Interno de `JWTValidationFilter`

### 📋 **Paso a Paso**

```
┌─────────────────────────────────────────────┐
│  🔸 ENTRADA: HttpServletRequest              │
│  GET /api/accounts                          │
│  Authorization: Bearer eyJhbGci...          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ①  EXTRAER HEADER "Authorization"          │
│                                             │
│  final var requestTokenHeader =             │
│      request.getHeader("Authorization");    │
│                                             │
│  Resultado:                                 │
│  "Bearer eyJhbGciOiJIUzI1NiJ9..."           │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ②  VALIDAR QUE EMPIECE CON "Bearer "       │
│                                             │
│  if (requestTokenHeader != null &&          │
│      requestTokenHeader.startsWith("Bearer"))│
│                                             │
│  ✅ Sí empieza con "Bearer "                │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ③  EXTRAER EL TOKEN (sin "Bearer ")        │
│                                             │
│  jwt = requestTokenHeader.substring(7);     │
│                                             │
│  Resultado:                                 │
│  "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6..."     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ④  EXTRAER USERNAME DEL TOKEN              │
│                                             │
│  try {                                      │
│    username = jwtService.getUsernameFromToken(jwt);│
│  }                                          │
│                                             │
│  Resultado:                                 │
│  "account@debuggeanoideas.com"              │
│                                             │
│  ⚠️ EXCEPCIONES:                            │
│  - IllegalArgumentException → Token vacío   │
│  - ExpiredJwtException → Token expirado     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑤  VERIFICAR SI YA ESTÁ AUTENTICADO        │
│                                             │
│  if (username != null &&                    │
│      SecurityContextHolder.getContext()     │
│          .getAuthentication() == null)      │
│                                             │
│  ✅ Username existe                         │
│  ✅ No hay autenticación previa             │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑥  CARGAR DETALLES DEL USUARIO             │
│                                             │
│  final var userDetails =                    │
│      jwtUserDetailService                   │
│          .loadUserByUsername(username);     │
│                                             │
│  Resultado:                                 │
│  UserDetails {                              │
│    username: "account@debuggeanoideas.com", │
│    authorities: [VIEW_ACCOUNT]              │
│  }                                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑦  VALIDAR TOKEN                           │
│                                             │
│  if (jwtService.validateToken(jwt, userDetails))│
│                                             │
│  Valida:                                    │
│  - ✅ Username coincide                     │
│  - ✅ Token no expirado                     │
│  - ✅ Firma válida                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑧  CREAR AUTENTICACIÓN DE SPRING SECURITY  │
│                                             │
│  var authToken =                            │
│      new UsernamePasswordAuthenticationToken(│
│          userDetails,                       │
│          null,                              │
│          userDetails.getAuthorities()       │
│      );                                     │
│                                             │
│  authToken.setDetails(                      │
│      new WebAuthenticationDetailsSource()   │
│          .buildDetails(request)             │
│  );                                         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑨  GUARDAR AUTENTICACIÓN EN EL CONTEXTO    │
│                                             │
│  SecurityContextHolder.getContext()         │
│      .setAuthentication(authToken);         │
│                                             │
│  ✅ Usuario autenticado en Spring Security  │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ⑩  CONTINUAR CON LA PETICIÓN               │
│                                             │
│  filterChain.doFilter(request, response);   │
│                                             │
│  → El request llega al Controller           │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│  ✅  CONTROLLER RECIBE PETICIÓN AUTENTICADA │
│                                             │
│  @GetMapping("/accounts")                   │
│  public String getAccounts() {              │
│      // Usuario ya autenticado ✅           │
│      return "Account data";                 │
│  }                                          │
└─────────────────────────────────────────────┘
```

---

## 🔐 Flujo Visual: Request → Response

```
┌──────────────────────┐
│   👤 CLIENTE         │
│   (Postman/Frontend) │
└──────────────────────┘
          │
          │ GET /api/accounts
          │ Authorization: Bearer eyJhbGci...
          ↓
┌──────────────────────────────────────────┐
│   🔐 JWTValidationFilter                 │
│                                          │
│   1️⃣ Extrae token                        │
│   2️⃣ Extrae username                     │
│   3️⃣ Carga UserDetails                   │
│   4️⃣ Valida token                        │
│   5️⃣ Autentica en SecurityContext        │
└──────────────────────────────────────────┘
          │
          │ Token válido ✅
          ↓
┌──────────────────────────────────────────┐
│   🎯 @PreAuthorize("hasAuthority(...)")  │
│      Spring Security verifica permisos   │
└──────────────────────────────────────────┘
          │
          │ Usuario tiene permiso ✅
          ↓
┌──────────────────────────────────────────┐
│   🎮 AccountController                   │
│                                          │
│   @GetMapping("/accounts")               │
│   public String getAccounts() {          │
│       return "Account data";             │
│   }                                      │
└──────────────────────────────────────────┘
          │
          │ Response: 200 OK
          ↓
┌──────────────────────┐
│   👤 CLIENTE         │
│   Recibe datos ✅    │
└──────────────────────┘
```

---

## 🚨 Casos de Error

### ❌ **1. Token No Enviado**

```
GET /api/accounts
(Sin header Authorization)
        ↓
JWTValidationFilter → requestTokenHeader = null
        ↓
filterChain.doFilter() → Sin autenticación
        ↓
403 Forbidden (Spring Security rechaza)
```

---

### ❌ **2. Token Expirado**

```
Authorization: Bearer eyJhbGci... (expirado)
        ↓
JWTValidationFilter → getUsernameFromToken()
        ↓
❌ ExpiredJwtException
        ↓
log.warn("Token expired")
        ↓
filterChain.doFilter() → Sin autenticación
        ↓
403 Forbidden
```

---

### ❌ **3. Token Inválido (Firma Incorrecta)**

```
Authorization: Bearer eyJhbGci... (firma alterada)
        ↓
JWTValidationFilter → validateToken()
        ↓
❌ SignatureException
        ↓
filterChain.doFilter() → Sin autenticación
        ↓
403 Forbidden
```

---

## 🔗 Integración con Spring Security

### 📋 **`SecurityFilterChain`**

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/authenticate").permitAll()  // Sin filtro
            .anyRequest().authenticated()                  // Con filtro
        )
        .addFilterBefore(
            jwtValidationFilter,                           // ← Tu filtro
            UsernamePasswordAuthenticationFilter.class
        );
    return http.build();
}
```

---

## 📊 Tabla: Filtro vs Controller

| Elemento | `JWTValidationFilter` | `Controller` |
|----------|----------------------|--------------|
| **Se ejecuta** | ✅ Antes del Controller | ⏱️ Después del filtro |
| **Función** | Validar JWT + autenticar | Procesar lógica de negocio |
| **Accede a** | `HttpServletRequest`, `HttpServletResponse` | `Authentication` (ya autenticado) |
| **Si JWT inválido** | ❌ No autentica → 403 | ❌ No llega al Controller |

---

## 💡 Resumen

```
┌─────────────────────────────────────────────┐
│  ¿POR QUÉ IMPLEMENTAR JWTValidationFilter?  │
│                                             │
│  ✅ Intercepta TODAS las peticiones         │
│  ✅ Valida el JWT antes de llegar al Controller│
│  ✅ Autentica al usuario en Spring Security │
│  ✅ Permite que @PreAuthorize funcione      │
│  ✅ Protege endpoints sin sesiones (stateless)│
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│  FLUJO RESUMIDO:                            │
│                                             │
│  Request → JWTValidationFilter →            │
│  → Valida Token →                           │
│  → Autentica en SecurityContext →           │
│  → Controller (usuario autenticado) →       │
│  → Response                                 │
└─────────────────────────────────────────────┘
```