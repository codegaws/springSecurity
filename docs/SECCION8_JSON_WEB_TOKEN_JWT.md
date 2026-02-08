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

