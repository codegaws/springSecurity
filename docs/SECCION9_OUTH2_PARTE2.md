# **🛡️ 🌐🔒 Spring Security — OAUTH2 PARTE 2 🔐🔐🔑🔑** 
## 📝 Clase 78  - CONFIGURANDO AUTHENTICATIONPROVIDER Y RESOURCESERVER👤🕵️‍♂🕵️‍♂🔑 🔑

- Se agregan estos metodos a SecurityConfig ->

```java
@Bean
AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(encoder);
    authProvider.setUserDetailsService(userDetails);
    return authProvider;
}

AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```
### 🔐 AuthenticationProvider y AuthorizationServerSettings

### 📑 Índice
- [🔍 AuthenticationProvider](#-authenticationprovider)
    - [🎯 ¿Qué es un AuthenticationProvider?](#-qué-es-un-authenticationprovider)
    - [🏗️ Configuración del DaoAuthenticationProvider](#️-configuración-del-dauthenticationprovider)
    - [🔄 Flujo de Autenticación](#-flujo-de-autenticación)
    - [📊 Componentes Involucrados](#-componentes-involucrados)
    - [💡 Ejemplo Práctico con tu Código](#-ejemplo-práctico-con-tu-código)
- [⚙️ AuthorizationServerSettings](#️-authorizationserversettings)
    - [🎯 ¿Qué es AuthorizationServerSettings?](#-qué-es-authorizationserversettings)
    - [🔧 Configuración por Defecto vs Personalizada](#-configuración-por-defecto-vs-personalizada)
    - [📝 Endpoints Configurables](#-endpoints-configurables)

---

### 🔍 AuthenticationProvider

#### 🎯 ¿Qué es un AuthenticationProvider?

```java
@Bean
AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(encoder);
    authProvider.setUserDetailsService(userDetails);
    return authProvider;
}
```

**`AuthenticationProvider`** es una interfaz de Spring Security que define **CÓMO** se validan las credenciales de un usuario durante el proceso de autenticación.

#### 📦 Analogía del Mundo Real

Imagina que es como un **guardia de seguridad en un edificio**:

```
┌─────────────────────────────────────────────────────────────┐
│  👤 Usuario intenta acceder                                  │
│  ├─ Username: account@debuggeandoieas.com                   │
│  └─ Password: 12345                                          │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  🛡️ AuthenticationProvider (Guardia de Seguridad)           │
│  ├─ ¿Este usuario existe? ➡️ UserDetailsService             │
│  ├─ ¿La contraseña coincide? ➡️ PasswordEncoder             │
│  └─ ¿Tiene permisos? ➡️ Authorities/Roles                   │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  ✅ Autenticación Exitosa                                    │
│  └─ Usuario puede acceder a los recursos                    │
└─────────────────────────────────────────────────────────────┘
```

---

### 🏗️ Configuración del DaoAuthenticationProvider

#### 📌 Línea por Línea

```java
// 1️⃣ Creamos una instancia de DaoAuthenticationProvider
var authProvider = new DaoAuthenticationProvider();
```

**`DaoAuthenticationProvider`** es una implementación concreta de `AuthenticationProvider` que:
- 🔍 Busca usuarios en una **base de datos** (DAO = Data Access Object)
- 🔐 Valida contraseñas usando un **PasswordEncoder**
- ✅ Carga los detalles del usuario usando un **UserDetailsService**

---

```java
// 2️⃣ Configuramos el codificador de contraseñas
authProvider.setPasswordEncoder(encoder);
```

| Concepto | Explicación |
|----------|-------------|
| **¿Qué hace?** | Indica cómo están **encriptadas** las contraseñas en la BD |
| **¿Por qué es necesario?** | Las contraseñas en BD están hasheadas (ej: `$2a$10$xyz...`), necesita saber cómo compararlas |
| **Encoder en tu caso** | `BCryptPasswordEncoder` - usa el algoritmo BCrypt |

**🔐 Proceso de Validación de Contraseña:**

```
Usuario envía: "12345" (texto plano)
                ⬇️
PasswordEncoder.matches("12345", "$2a$10$xyz...")
                ⬇️
BCrypt hashea "12345" y compara con el hash de la BD
                ⬇️
✅ Coincide → Autenticación exitosa
❌ No coincide → Autenticación fallida
```

---

```java
// 3️⃣ Configuramos el servicio que carga usuarios
authProvider.setUserDetailsService(userDetails);
```

| Concepto | Explicación |
|----------|-------------|
| **¿Qué es?** | `CustomerUserDetails` - Tu clase que implementa `UserDetailsService` |
| **¿Qué hace?** | Busca el usuario en la BD usando el **email/username** |
| **Método clave** | `loadUserByUsername(String username)` |

**📊 Flujo de CustomerUserDetails:**

```java
// Tu implementación actual:
@Service
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    // Convierte CustomerEntity → UserDetails
                    final var authorities = customer.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList();
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
    }
}
```

---

### 🔄 Flujo de Autenticación

```mermaid
┌────────────────────────────────────────────────────────────────────┐
│                   🔐 FLUJO DE AUTENTICACIÓN                         │
└────────────────────────────────────────────────────────────────────┘

1️⃣ Usuario envía credenciales
   ├─ POST /authenticate
   ├─ Body: { "username": "account@debuggeandoieas.com", "password": "12345" }
   └─ Controller recibe el request

                        ⬇️

2️⃣ AuthenticationManager.authenticate()
   ├─ Crea un token: UsernamePasswordAuthenticationToken
   └─ Delega al AuthenticationProvider configurado

                        ⬇️

3️⃣ DaoAuthenticationProvider procesa
   ├─ Paso A: authProvider.setUserDetailsService(userDetails)
   │   └─> Llama a CustomerUserDetails.loadUserByUsername("account@debuggeandoieas.com")
   │       └─> Consulta a BD: SELECT * FROM customer WHERE email = ?
   │           └─> Retorna: UserDetails con password hasheado y roles
   │
   ├─ Paso B: authProvider.setPasswordEncoder(encoder)
   │   └─> encoder.matches("12345", "$2a$10$xyz...") 
   │       └─> Compara la contraseña enviada vs la de BD
   │
   └─ Paso C: Valida authorities
       └─> Verifica que el usuario tenga roles/permisos

                        ⬇️

4️⃣ Resultado
   ├─ ✅ SUCCESS: Retorna Authentication con detalles del usuario
   │   └─> Se guarda en SecurityContextHolder
   │       └─> Se genera JWT Token
   │
   └─ ❌ FAIL: Lanza BadCredentialsException
       └─> Respuesta 401 Unauthorized
```

---

### 📊 Componentes Involucrados

| Componente | Rol | Responsabilidad |
|------------|-----|-----------------|
| 🛡️ **AuthenticationProvider** | Coordinador | Orquesta todo el proceso de autenticación |
| 🔍 **UserDetailsService** | Buscador de usuarios | Consulta la BD para obtener el usuario |
| 🔐 **PasswordEncoder** | Validador de contraseñas | Compara la contraseña enviada vs BD |
| 👤 **UserDetails** | Modelo de usuario | Contiene username, password, authorities |
| 🎫 **Authentication** | Resultado | Token de autenticación exitosa |

---

### 💡 Ejemplo Práctico con tu Código

**Escenario:** Usuario intenta hacer login con OAuth2

```java
// 🎬 ACCIÓN: Usuario envía credenciales
POST /authenticate
{
    "username": "account@debuggeandoieas.com",
    "password": "12345"
}

// ────────────────────────────────────────────────────────────

// 1️⃣ AuthController recibe el request
@PostMapping("/authenticate")
public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
    this.authenticate(request); // ⬅️ Aquí empieza el proceso
    // ...
}

// ────────────────────────────────────────────────────────────

// 2️⃣ authenticate() usa el AuthenticationManager
private void authenticate(JWTRequest request) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),  // "account@debuggeandoieas.com"
            request.getPassword()   // "12345"
        )
    );
}

// ────────────────────────────────────────────────────────────

// 3️⃣ AuthenticationManager delega a tu AuthenticationProvider
@Bean
AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
    var authProvider = new DaoAuthenticationProvider();
    
    // ⬇️ Este método será llamado automáticamente
    authProvider.setUserDetailsService(userDetails);
    // CustomerUserDetails.loadUserByUsername("account@debuggeandoieas.com")
    //     ➡️ SELECT * FROM customer WHERE email = 'account@debuggeandoieas.com'
    //     ➡️ Retorna: User("account@debuggeandoieas.com", "$2a$10$xyz...", [VIEW_ACCOUNT])
    
    // ⬇️ Este método será llamado para validar la contraseña
    authProvider.setPasswordEncoder(encoder);
    // encoder.matches("12345", "$2a$10$xyz...")
    //     ➡️ BCrypt.hashpw("12345", salt) == "$2a$10$xyz..." ?
    //     ➡️ ✅ true: Contraseña correcta
    
    return authProvider;
}

// ────────────────────────────────────────────────────────────

// 4️⃣ Si todo es exitoso:
// ✅ Authentication creado y almacenado en SecurityContextHolder
// ✅ Se genera el JWT Token
// ✅ Respuesta: { "jwt": "eyJhbGciOiJIUzI1NiJ9..." }
```

---

### 🎯 Preguntas Frecuentes

#### ❓ ¿Por qué necesito un AuthenticationProvider personalizado?

**Respuesta:** Spring Security necesita saber:
1. 📂 **¿Dónde están los usuarios?** → Tu base de datos (CustomerRepository)
2. 🔐 **¿Cómo valido contraseñas?** → BCrypt (PasswordEncoder)
3. 👤 **¿Cómo cargo un usuario?** → CustomerUserDetails

Sin este bean, Spring no sabría cómo autenticar contra tu BD.

---

#### ❓ ¿Qué pasa si no configuro el AuthenticationProvider?

```java
// ❌ Sin AuthenticationProvider configurado:
POST /authenticate
{
    "username": "account@debuggeandoieas.com",
    "password": "12345"
}

// ⚠️ Resultado:
// Error: No AuthenticationProvider found for UsernamePasswordAuthenticationToken
// Status: 500 Internal Server Error
```

---

#### ❓ ¿Puedo tener múltiples AuthenticationProviders?

**Sí!** Spring Security soporta múltiples proveedores:

```java
@Bean
AuthenticationProvider daoAuthProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(encoder);
    authProvider.setUserDetailsService(userDetails);
    return authProvider;
}

@Bean
AuthenticationProvider ldapAuthProvider() {
    // Autenticación contra LDAP (Active Directory)
    return new LdapAuthenticationProvider(...);
}

@Bean
AuthenticationProvider customAuthProvider() {
    // Autenticación personalizada (ej: contra API externa)
    return new CustomAuthenticationProvider();
}
```

Spring intentará autenticar con cada proveedor en orden hasta que uno tenga éxito.

---

### ⚙️ AuthorizationServerSettings

### 🎯 ¿Qué es AuthorizationServerSettings?

```java
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```

**`AuthorizationServerSettings`** configura los **endpoints y URLs** del servidor de autorización OAuth2.

#### 🏢 Analogía del Mundo Real

Es como configurar las **direcciones de las oficinas** de un banco:

```
🏦 Banco de Autenticación (Authorization Server)
├─ 🚪 Oficina de Tokens: /oauth2/token
├─ 🔑 Oficina de Autorización: /oauth2/authorize
├─ 📋 Oficina de Información: /oauth2/jwks
└─ ❌ Oficina de Revocación: /oauth2/revoke
```

---

### 🔧 Configuración por Defecto vs Personalizada

#### 📦 Configuración por Defecto (Tu Código)

```java
// ⚠️ Nota: Este método NO tiene @Bean
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```

**⚠️ PROBLEMA:** Este método **NO** está anotado con `@Bean`, por lo que **NO se registra** en el contexto de Spring.

```
┌─────────────────────────────────────────────────────────┐
│  ❌ authorizationServerSettings()                        │
│  └─ Sin @Bean → Spring lo ignora                        │
│                                                          │
│  ✅ Spring usa la configuración por defecto:            │
│  ├─ issuerUrl: http://localhost:8080                    │
│  ├─ tokenEndpoint: /oauth2/token                        │
│  ├─ authorizationEndpoint: /oauth2/authorize            │
│  ├─ jwkSetEndpoint: /oauth2/jwks                        │
│  └─ tokenRevocationEndpoint: /oauth2/revoke             │
└─────────────────────────────────────────────────────────┘
```

---

#### ✅ Configuración Correcta (Con @Bean)

```java
@Bean  // ⬅️ IMPORTANTE: Agregar @Bean
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```

---

#### 🎨 Configuración Personalizada

```java
@Bean
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
            // 🌐 URL base del servidor
            .issuer("https://mi-servidor.com")
            
            // 🎫 Endpoint para obtener tokens
            .tokenEndpoint("/api/auth/token")
            
            // 🔑 Endpoint de autorización
            .authorizationEndpoint("/api/auth/authorize")
            
            // 📋 Endpoint de claves públicas (para validar JWT)
            .jwkSetEndpoint("/api/auth/jwks")
            
            // ❌ Endpoint para revocar tokens
            .tokenRevocationEndpoint("/api/auth/revoke")
            
            // 🔍 Endpoint de introspección de tokens
            .tokenIntrospectionEndpoint("/api/auth/introspect")
            
            // ℹ️ Endpoint de información del servidor
            .oidcUserInfoEndpoint("/api/auth/userinfo")
            
            .build();
}
```

---

### 📝 Endpoints Configurables

| Endpoint | Ruta por Defecto | Descripción | Ejemplo de Uso |
|----------|------------------|-------------|----------------|
| 🎫 **Token Endpoint** | `/oauth2/token` | Obtener access tokens | `POST /oauth2/token` con credenciales |
| 🔑 **Authorization Endpoint** | `/oauth2/authorize` | Autorizar clientes (flujo OAuth2) | Redirección del navegador |
| 📋 **JWK Set Endpoint** | `/oauth2/jwks` | Claves públicas para validar JWT | `GET /oauth2/jwks` |
| ❌ **Token Revocation Endpoint** | `/oauth2/revoke` | Revocar tokens | `POST /oauth2/revoke` |
| 🔍 **Token Introspection Endpoint** | `/oauth2/introspect` | Validar tokens | `POST /oauth2/introspect` |
| ℹ️ **User Info Endpoint** | `/userinfo` | Información del usuario autenticado | `GET /userinfo` |

---

### 🔄 Flujo de Uso de Endpoints

```mermaid
┌────────────────────────────────────────────────────────────┐
│           🔐 FLUJO OAUTH2 CON AUTHORIZATION SERVER          │
└────────────────────────────────────────────────────────────┘

1️⃣ Cliente solicita autorización
   └─> GET /oauth2/authorize
       ?response_type=code
       &client_id=my-client
       &redirect_uri=http://localhost:3000/callback
       &scope=read write

                        ⬇️

2️⃣ Usuario se autentica y autoriza
   └─> Spring muestra formulario de login
       └─> Usuario ingresa credenciales
           └─> Spring redirige con authorization code

                        ⬇️

3️⃣ Cliente intercambia código por token
   └─> POST /oauth2/token
       Body: {
           "grant_type": "authorization_code",
           "code": "abc123",
           "client_id": "my-client",
           "client_secret": "secret",
           "redirect_uri": "http://localhost:3000/callback"
       }

                        ⬇️

4️⃣ Authorization Server retorna tokens
   └─> Response: {
           "access_token": "eyJhbGciOiJIUzI1NiJ9...",
           "token_type": "Bearer",
           "expires_in": 3600,
           "refresh_token": "xyz789",
           "scope": "read write"
       }

                        ⬇️

5️⃣ Cliente accede a recursos protegidos
   └─> GET /accounts
       Header: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

                        ⬇️

6️⃣ Resource Server valida token
   └─> GET /oauth2/jwks (obtiene clave pública)
       └─> Valida firma del JWT
           └─> ✅ Token válido → Permite acceso
           └─> ❌ Token inválido → 401 Unauthorized
```

---

### 💡 Ejemplo Práctico

#### Escenario: Validar un JWT Token

```java
// 🎬 Cliente envía request con JWT
GET /accounts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2NvdW50QGRlYnVnZ2VhbmRvaWVhcy5jb20iLCJleHAiOjE3NzA2MjIzNzF9.z3LQwigZ1NUwMWLBuGk6TI0Ub9YxirWmQM4LeQQEpmc

// ────────────────────────────────────────────────────────────

// 1️⃣ Spring Security intercepta el request
//    └─> JWTValidationFilter extrae el token

// ────────────────────────────────────────────────────────────

// 2️⃣ Resource Server necesita validar el token
//    ¿Cómo sabe dónde obtener la clave pública?
//    └─> AuthorizationServerSettings le indica: /oauth2/jwks

// ────────────────────────────────────────────────────────────

// 3️⃣ Obtiene la clave pública
GET /oauth2/jwks
Response: {
    "keys": [
        {
            "kty": "RSA",
            "e": "AQAB",
            "kid": "key-id-1",
            "n": "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM..."
        }
    ]
}

// ────────────────────────────────────────────────────────────

// 4️⃣ Valida la firma del JWT
//    ✅ Firma válida → Usuario autenticado
//    ✅ Token no expirado → Permite acceso
//    └─> Respuesta: 200 OK con datos de cuentas
```

---

### 🎯 ¿Por qué es Importante?

| Aspecto | Sin AuthorizationServerSettings | Con AuthorizationServerSettings |
|---------|--------------------------------|--------------------------------|
| 🌐 **Issuer URL** | http://localhost:8080 | https://mi-dominio.com |
| 🎫 **Token Endpoint** | /oauth2/token | /api/v1/auth/token |
| 📋 **JWK Set Endpoint** | /oauth2/jwks | /api/v1/auth/keys |
| 🔧 **Flexibilidad** | URLs fijas | URLs personalizables |
| 🏢 **Producción** | No recomendado | ✅ Recomendado |

---

### ⚠️ Recomendación para tu Código

**Opción 1: Agregar @Bean** (si necesitas configuración por defecto)

```java
@Bean  // ⬅️ Agregar esto
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```

**Opción 2: Eliminar el método** (Spring usará configuración por defecto automáticamente)

```java
// ❌ Eliminar este método si no lo necesitas
// AuthorizationServerSettings authorizationServerSettings() {
//     return AuthorizationServerSettings.builder().build();
// }
```

**Opción 3: Personalizar para producción** (recomendado)

```java
@Bean
AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
            .issuer("https://auth.tuempresa.com")  // URL de producción
            .build();
}
```

---

### 🎓 Resumen Final

### 🛡️ AuthenticationProvider

| Aspecto | Descripción |
|---------|-------------|
| **¿Qué es?** | Define CÓMO se validan las credenciales |
| **Componentes** | UserDetailsService + PasswordEncoder |
| **Responsabilidad** | Buscar usuario en BD y validar contraseña |
| **Cuándo se usa** | En cada intento de login |

### ⚙️ AuthorizationServerSettings

| Aspecto | Descripción |
|---------|-------------|
| **¿Qué es?** | Configura los endpoints del servidor OAuth2 |
| **Componentes** | URLs de token, autorización, JWKS, etc. |
| **Responsabilidad** | Definir rutas de los servicios OAuth2 |
| **Cuándo se usa** | Al iniciar el Authorization Server |

---

### 🔗 Relación entre Ambos

```
┌───────────────────────────────────────────────────────────┐
│                   🏗️ ARQUITECTURA                          │
└───────────────────────────────────────────────────────────┘

👤 Usuario                        🌐 Cliente OAuth2
    │                                  │
    │ 1. Login                        │ 2. Solicita Token
    ├─────────────────────────────────┤
    │                                  │
    ⬇️                                  ⬇️
┌─────────────────┐            ┌──────────────────────┐
│ AuthProvider    │            │ Authorization Server │
│ (Valida creds)  │            │ (Genera tokens)      │
├─────────────────┤            ├──────────────────────┤
│ • UserDetails   │            │ • Token Endpoint     │
│ • PasswordEnc   │            │ • JWKS Endpoint      │
│ • DB Query      │            │ • AuthorizationSett  │
└─────────────────┘            └──────────────────────┘
         │                              │
         └──────────────┬───────────────┘
                        ⬇️
              ✅ Usuario Autenticado
              🎫 Token JWT Generado
              🔐 Acceso a Recursos
```

---

### 📚 Próximos Pasos Recomendados

1. ✅ **Agregar @Bean a authorizationServerSettings()**
2. 🔍 **Revisar JWTValidationFilter** para entender cómo se validan los tokens
3. 🧪 **Probar el flujo completo** con Postman
4. 📝 **Configurar issuer URL** para producción

---

**🎉 ¡Ahora entiendes cómo funciona la autenticación en tu aplicación OAuth2!**


---

## 📝 Clase 79  - CONFIGURANDO JWT 👤🕵️‍♂🕵️‍♂🔑 🔑

```java


```
### 🔄 JWT Converters: Transformación de Tokens a Autoridades

### 📑 Índice
- [🎯 Introducción al Problema](#-introducción-al-problema)
- [🔧 JwtGrantedAuthoritiesConverter](#-jwtgrantedauthoritiesconverter)
  - [🎯 ¿Qué es JwtGrantedAuthoritiesConverter?](#-qué-es-jwtgrantedauthoritiesconverter)
  - [🔍 Análisis Línea por Línea](#-análisis-línea-por-línea)
  - [📊 Comportamiento Por Defecto vs Personalizado](#-comportamiento-por-defecto-vs-personalizado)
  - [💡 Ejemplo Práctico con tu JWT](#-ejemplo-práctico-con-tu-jwt)
- [🔀 JwtAuthenticationConverter](#-jwtauthenticationconverter)
  - [🎯 ¿Qué es JwtAuthenticationConverter?](#-qué-es-jwtauthenticationconverter)
  - [🔗 Relación entre los Dos Converters](#-relación-entre-los-dos-converters)
  - [🔄 Flujo Completo de Conversión](#-flujo-completo-de-conversión)
- [🎬 Flujo Completo: Del Token JWT al Usuario Autenticado](#-flujo-completo-del-token-jwt-al-usuario-autenticado)
- [🧪 Casos de Uso Prácticos](#-casos-de-uso-prácticos)
- [🎓 Resumen y Mejores Prácticas](#-resumen-y-mejores-prácticas)

---

### 🎯 Introducción al Problema

### ❓ ¿Por qué necesitamos estos Converters?

Cuando un usuario envía un **JWT Token** para acceder a recursos protegidos, Spring Security necesita:

```
┌──────────────────────────────────────────────────────────────┐
│  🎫 JWT Token (String)                                        │
│  "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIn0..." │
└──────────────────────────────────────────────────────────────┘
                        ⬇️  ❓ ¿Cómo convertir?
┌──────────────────────────────────────────────────────────────┐
│  👤 Usuario Autenticado (Authentication)                      │
│  ├─ Username: account@debuggeandoieas.com                    │
│  ├─ Authorities: [VIEW_ACCOUNT]                              │
│  └─ Authenticated: true                                      │
└──────────────────────────────────────────────────────────────┘
```

**Problema:** JWT es solo un **string codificado**, necesitamos extraer:
1. 👤 **Subject** (usuario)
2. 🔑 **Claims** (información adicional)
3. 🛡️ **Authorities** (roles/permisos)

**Solución:** Los **Converters** transforman el JWT en objetos que Spring Security entiende.

---

### 🔧 JwtGrantedAuthoritiesConverter

### 🎯 ¿Qué es JwtGrantedAuthoritiesConverter?

```java
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    return converter;
}
```

**`JwtGrantedAuthoritiesConverter`** es un componente que **extrae los roles/permisos** del JWT y los convierte en `GrantedAuthority` (objetos que Spring Security usa para controlar acceso).

#### 🏭 Analogía del Mundo Real

Imagina una **fábrica que procesa credenciales**:

```
🏭 Fábrica de Autoridades (JwtGrantedAuthoritiesConverter)

📥 ENTRADA (JWT Claim):
   {
       "scope": "read write",
       "ROLES": "[VIEW_ACCOUNT]"
   }

🔄 PROCESAMIENTO:
   1. Lee el claim "scope"
   2. Separa por espacios: ["read", "write"]
   3. Agrega prefijo: "SCOPE_read", "SCOPE_write"
   4. Convierte a SimpleGrantedAuthority

📤 SALIDA (Authorities):
   [
       SimpleGrantedAuthority("SCOPE_read"),
       SimpleGrantedAuthority("SCOPE_write")
   ]
```

---

### 🔍 Análisis Línea por Línea

#### 1️⃣ Crear la Instancia

```java
var converter = new JwtGrantedAuthoritiesConverter();
```

| Aspecto | Descripción |
|---------|-------------|
| **Clase** | `JwtGrantedAuthoritiesConverter` |
| **Package** | `org.springframework.security.oauth2.server.resource.authentication` |
| **Propósito** | Extraer autoridades del JWT |
| **Configuración por defecto** | Lee el claim `scope` y agrega prefijo `SCOPE_` |

---

#### 2️⃣ Configurar el Prefijo

```java
converter.setAuthorityPrefix("");
```

**🎯 Esta es la línea MÁS IMPORTANTE del método**

| Configuración | Valor | Resultado |
|---------------|-------|-----------|
| **Por defecto** | `"SCOPE_"` | `SCOPE_read`, `SCOPE_write` |
| **Tu configuración** | `""` (vacío) | `read`, `write` |

#### ⚠️ ¿Por qué cambiar el prefijo?

**Comportamiento por defecto de Spring:**

```java
// JWT Claim:
{
    "scope": "read write"
}

// ❌ CON PREFIJO (por defecto):
// Spring genera: ["SCOPE_read", "SCOPE_write"]

// Tu código de seguridad:
.hasAuthority("read")  // ❌ FALLA porque busca "read" pero tiene "SCOPE_read"
```

**Comportamiento con prefijo vacío:**

```java
// JWT Claim:
{
    "scope": "read write"
}

// ✅ SIN PREFIJO (tu configuración):
// Spring genera: ["read", "write"]

// Tu código de seguridad:
.hasAuthority("read")  // ✅ FUNCIONA porque coincide exactamente
```

---

### 📊 Comportamiento Por Defecto vs Personalizado

#### 🔴 Escenario 1: Sin Configuración Personalizada

```java
// ❌ SIN el bean JwtGrantedAuthoritiesConverter
// Spring usa configuración por defecto

// JWT Token decodificado:
{
    "sub": "account@debuggeandoieas.com",
    "scope": "read write",
    "ROLES": "[VIEW_ACCOUNT]",
    "iat": 1770604371,
    "exp": 1770622371
}

// Authorities generadas automáticamente:
[
    SimpleGrantedAuthority("SCOPE_read"),     // ⬅️ Prefijo "SCOPE_" agregado
    SimpleGrantedAuthority("SCOPE_write")     // ⬅️ Prefijo "SCOPE_" agregado
]

// Tu SecurityConfig:
http.authorizeHttpRequests(auth ->
    auth.requestMatchers(ADMIN_RESOURCES).hasAuthority("write")  // ❌ FALLA
        .requestMatchers(USER_RESOURCES).hasAuthority("read"));  // ❌ FALLA

// ⚠️ Problema: Busca "write" pero tiene "SCOPE_write"
```

---

#### 🟢 Escenario 2: Con tu Configuración Personalizada

```java
// ✅ CON el bean JwtGrantedAuthoritiesConverter
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");  // ⬅️ Elimina el prefijo
    return converter;
}

// JWT Token decodificado (mismo):
{
    "sub": "account@debuggeandoieas.com",
    "scope": "read write",
    "ROLES": "[VIEW_ACCOUNT]",
    "iat": 1770604371,
    "exp": 1770622371
}

// Authorities generadas:
[
    SimpleGrantedAuthority("read"),      // ✅ Sin prefijo
    SimpleGrantedAuthority("write")      // ✅ Sin prefijo
]

// Tu SecurityConfig:
http.authorizeHttpRequests(auth ->
    auth.requestMatchers(ADMIN_RESOURCES).hasAuthority("write")  // ✅ FUNCIONA
        .requestMatchers(USER_RESOURCES).hasAuthority("read"));  // ✅ FUNCIONA
```

---

### 💡 Ejemplo Práctico con tu JWT

#### 🎫 Tu JWT Token Real

```json
// Token generado por tu aplicación:
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwic3ViIjoiYWNjb3VudEBkZWJ1Z2dlYW5kb2llYXMuY29tIiwiaWF0IjoxNzcwNjA0MzcxLCJleHAiOjE3NzA2MjIzNzF9.z3LQwigZ1NUwMWLBuGk6TI0Ub9YxirWmQM4LeQQEpmc"
}

// Decodificado (payload):
{
    "ROLES": "[VIEW_ACCOUNT]",
    "sub": "account@debuggeandoieas.com",
    "iat": 1770604371,
    "exp": 1770622371
}
```

#### 🔄 Proceso de Conversión

```java
// 1️⃣ Usuario envía request con token
GET /accounts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

// ─────────────────────────────────────────────────────────────

// 2️⃣ Spring Security intercepta y extrae el token
String jwt = "eyJhbGciOiJIUzI1NiJ9...";

// ─────────────────────────────────────────────────────────────

// 3️⃣ Decodifica el JWT
Jwt decodedJwt = {
    header: { "alg": "HS256" },
    claims: {
        "ROLES": "[VIEW_ACCOUNT]",
        "sub": "account@debuggeandoieas.com",
        "iat": 1770604371,
        "exp": 1770622371
    }
}

// ─────────────────────────────────────────────────────────────

// 4️⃣ JwtGrantedAuthoritiesConverter extrae authorities
// ⚠️ Problema: Tu JWT no tiene el claim "scope" estándar
// Solo tiene "ROLES": "[VIEW_ACCOUNT]"

// Por defecto, JwtGrantedAuthoritiesConverter busca:
String scopeClaim = jwt.getClaim("scope");  // ❌ null (no existe)

// Resultado por defecto:
Collection<GrantedAuthority> authorities = [];  // ⬅️ VACÍO!

// ─────────────────────────────────────────────────────────────

// 5️⃣ Usuario NO tiene authorities
// Security check FALLA:
.hasAuthority("write")  // ❌ authorities está vacío
```

#### ⚠️ Problema Detectado en tu Código

Tu JWT tiene el claim `"ROLES": "[VIEW_ACCOUNT]"`, pero **JwtGrantedAuthoritiesConverter** por defecto solo lee el claim **`scope`**.

**Solución:** Necesitas configurar el converter para leer el claim correcto:

```java
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    
    // 🔥 AGREGAR ESTA LÍNEA:
    converter.setAuthoritiesClaimName("ROLES");  // ⬅️ Lee el claim "ROLES"
    
    return converter;
}
```

---

### 🔧 Métodos Adicionales de Configuración

| Método | Descripción | Valor por Defecto | Ejemplo |
|--------|-------------|-------------------|---------|
| `setAuthorityPrefix(String)` | Prefijo para authorities | `"SCOPE_"` | `""` (tu config) |
| `setAuthoritiesClaimName(String)` | Nombre del claim a leer | `"scope"` | `"ROLES"` (recomendado para ti) |
| `setAuthoritiesClaimDelimiter(String)` | Delimitador de authorities | `" "` (espacio) | `","` (coma) |

---

### 🔀 JwtAuthenticationConverter

### 🎯 ¿Qué es JwtAuthenticationConverter?

```java
@Bean
JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter settings) {
    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(settings);
    return converter;
}
```

**`JwtAuthenticationConverter`** es el **orquestador principal** que:
1. 📥 Recibe el JWT completo
2. 🔄 Extrae el **username** (claim `sub`)
3. 🛡️ Extrae las **authorities** (usando `JwtGrantedAuthoritiesConverter`)
4. 📤 Crea el objeto **`Authentication`** que Spring Security usa

#### 🎭 Analogía del Mundo Real

Es como un **director de orquesta**:

```
🎭 Director de Orquesta (JwtAuthenticationConverter)
├─ 🎻 Violín (claim "sub") → Extrae el username
├─ 🎺 Trompeta (JwtGrantedAuthoritiesConverter) → Extrae authorities
├─ 🥁 Batería (otros claims) → Información adicional
└─ 🎼 Crea la Sinfonía (Authentication object)
```

---

### 🔗 Relación entre los Dos Converters

```
┌───────────────────────────────────────────────────────────────┐
│             🔄 RELACIÓN ENTRE CONVERTERS                       │
└───────────────────────────────────────────────────────────────┘

📦 JwtAuthenticationConverter (ORQUESTADOR)
   │
   ├─ 👤 Extrae Subject (username)
   │   └─> jwt.getClaim("sub") → "account@debuggeandoieas.com"
   │
   ├─ 🛡️ Delega extracción de authorities
   │   └─> Llama a JwtGrantedAuthoritiesConverter
   │       │
   │       📦 JwtGrantedAuthoritiesConverter (ESPECIALISTA)
   │          ├─ Lee claim: jwt.getClaim("scope")
   │          ├─ Separa por espacios: ["read", "write"]
   │          ├─ Agrega prefijo: "" (vacío)
   │          └─> Retorna: [Authority("read"), Authority("write")]
   │
   └─ 🎫 Crea Authentication
       └─> JwtAuthenticationToken(
               principal: "account@debuggeandoieas.com",
               authorities: [Authority("read"), Authority("write")]
           )
```

---

### 🔄 Flujo Completo de Conversión

#### Código del Método

```java
@Bean
JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter settings) {
    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(settings);  // ⬅️ Inyecta el converter de authorities
    return converter;
}
```

#### Análisis

| Línea | Código | Explicación |
|-------|--------|-------------|
| 1️⃣ | `JwtAuthenticationConverter converter = new ...()` | Crea el converter principal |
| 2️⃣ | `converter.setJwtGrantedAuthoritiesConverter(settings)` | Inyecta el converter de authorities personalizado |
| 3️⃣ | `return converter` | Registra como bean de Spring |

---

#### 🔗 Inyección de Dependencia

```java
// Spring ve este parámetro:
JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter settings)
                                                       ↑
                                                       └─ Spring inyecta automáticamente
                                                          el bean que creaste antes

// ¿De dónde viene "settings"?
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    // ⬅️ Este bean es inyectado como "settings"
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    return converter;
}
```

---

### 🎬 Flujo Completo: Del Token JWT al Usuario Autenticado

### 📊 Diagrama de Flujo

```
┌────────────────────────────────────────────────────────────────┐
│         🔐 FLUJO COMPLETO DE AUTENTICACIÓN JWT                  │
└────────────────────────────────────────────────────────────────┘

1️⃣ Cliente envía request con JWT
   ├─ GET /accounts
   └─ Header: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

                        ⬇️

2️⃣ Spring Security intercepta (Filtro de seguridad)
   └─> Extrae el token: "eyJhbGciOiJIUzI1NiJ9..."

                        ⬇️

3️⃣ Valida firma del JWT
   ├─ Obtiene clave secreta: JWT_SECRET
   ├─ Verifica firma HMAC-SHA256
   └─> ✅ Firma válida → Continúa
       ❌ Firma inválida → 401 Unauthorized

                        ⬇️

4️⃣ Decodifica el JWT (obtiene claims)
   └─> Jwt {
           header: { "alg": "HS256" },
           claims: {
               "ROLES": "[VIEW_ACCOUNT]",
               "sub": "account@debuggeandoieas.com",
               "iat": 1770604371,
               "exp": 1770622371
           }
       }

                        ⬇️

5️⃣ JwtAuthenticationConverter.convert(jwt)
   │
   ├─ A) Extrae username
   │   └─> jwt.getClaim("sub")
   │       └─> "account@debuggeandoieas.com"
   │
   ├─ B) Extrae authorities (delega)
   │   └─> jwtGrantedAuthoritiesConverter.convert(jwt)
   │       │
   │       └─> JwtGrantedAuthoritiesConverter:
   │           ├─ Lee claim: jwt.getClaim("scope")  // ❌ null en tu caso
   │           ├─ ⚠️ Tu JWT no tiene "scope", tiene "ROLES"
   │           └─> Retorna: []  // ⬅️ VACÍO (problema actual)
   │
   └─ C) Crea Authentication
       └─> JwtAuthenticationToken(
               principal: "account@debuggeandoieas.com",
               credentials: jwt,
               authorities: []  // ⬅️ VACÍO (por el problema anterior)
           )

                        ⬇️

6️⃣ Guarda en SecurityContextHolder
   └─> SecurityContextHolder.getContext().setAuthentication(auth)

                        ⬇️

7️⃣ Security check
   └─> .hasAuthority("write")
       └─> Authorities: []  // ⬅️ VACÍO
           └─> ❌ ACCESO DENEGADO → 403 Forbidden

                        ⬇️

8️⃣ Respuesta al cliente
   └─> 403 Forbidden
       └─> "Access Denied"
```

---

### ⚠️ Problema Actual en tu Código

Tu JWT tiene este formato:

```json
{
    "ROLES": "[VIEW_ACCOUNT]",
    "sub": "account@debuggeandoieas.com",
    "iat": 1770604371,
    "exp": 1770622371
}
```

Pero **`JwtGrantedAuthoritiesConverter`** por defecto busca el claim **`scope`**, no **`ROLES`**.

---

### ✅ Solución: Configurar el Claim Correcto

```java
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");  // Sin prefijo
    converter.setAuthoritiesClaimName("ROLES");  // ⬅️ AGREGAR ESTA LÍNEA
    return converter;
}
```

#### 🔄 Flujo Corregido

```
5️⃣ JwtAuthenticationConverter.convert(jwt)
   │
   ├─ B) Extrae authorities (delega)
   │   └─> jwtGrantedAuthoritiesConverter.convert(jwt)
   │       │
   │       └─> JwtGrantedAuthoritiesConverter:
   │           ├─ Lee claim: jwt.getClaim("ROLES")  // ✅ Ahora lee "ROLES"
   │           ├─ Valor: "[VIEW_ACCOUNT]"
   │           ├─ Parsea: ["VIEW_ACCOUNT"]
   │           ├─ Agrega prefijo: "" (vacío)
   │           └─> Retorna: [Authority("VIEW_ACCOUNT")]  // ✅ Correcto
   │
   └─ C) Crea Authentication
       └─> JwtAuthenticationToken(
               principal: "account@debuggeandoieas.com",
               credentials: jwt,
               authorities: [Authority("VIEW_ACCOUNT")]  // ✅ Ahora tiene authorities
           )
```

---

### 🧪 Casos de Uso Prácticos

### 📌 Caso 1: OAuth2 con Scopes Estándar

**Escenario:** Servidor OAuth2 que usa el claim `scope` estándar

```java
// JWT generado por servidor OAuth2:
{
    "sub": "user@example.com",
    "scope": "read write delete",
    "exp": 1770622371
}

// Configuración:
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");  // Sin prefijo
    // No necesitas setAuthoritiesClaimName porque "scope" es el default
    return converter;
}

// Authorities generadas:
[
    Authority("read"),
    Authority("write"),
    Authority("delete")
]

// Security config:
http.authorizeHttpRequests(auth ->
    auth.requestMatchers("/admin/**").hasAuthority("delete")  // ✅ Funciona
        .requestMatchers("/user/**").hasAuthority("read"));   // ✅ Funciona
```

---

### 📌 Caso 2: JWT Personalizado con Roles (Tu Caso)

**Escenario:** JWT personalizado con claim `ROLES`

```java
// JWT generado por tu aplicación:
{
    "sub": "account@debuggeandoieas.com",
    "ROLES": "[VIEW_ACCOUNT]",
    "exp": 1770622371
}

// Configuración:
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    converter.setAuthoritiesClaimName("ROLES");  // ⬅️ Lee "ROLES" en lugar de "scope"
    return converter;
}

// Authorities generadas:
[
    Authority("VIEW_ACCOUNT")
]

// Security config:
http.authorizeHttpRequests(auth ->
    auth.requestMatchers("/accounts/**").hasAuthority("VIEW_ACCOUNT"));  // ✅ Funciona
```

---

### 📌 Caso 3: Múltiples Roles con Prefijo

**Escenario:** JWT con roles que necesitan prefijo `ROLE_`

```java
// JWT:
{
    "sub": "admin@example.com",
    "authorities": "ADMIN USER MODERATOR",
    "exp": 1770622371
}

// Configuración:
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("ROLE_");  // ⬅️ Agrega prefijo "ROLE_"
    converter.setAuthoritiesClaimName("authorities");
    return converter;
}

// Authorities generadas:
[
    Authority("ROLE_ADMIN"),
    Authority("ROLE_USER"),
    Authority("ROLE_MODERATOR")
]

// Security config:
http.authorizeHttpRequests(auth ->
    auth.requestMatchers("/admin/**").hasRole("ADMIN"));  // ✅ Funciona
    // hasRole("ADMIN") internamente busca "ROLE_ADMIN"
```

---

### 📌 Caso 4: Delimitador Personalizado

**Escenario:** JWT con roles separados por comas

```java
// JWT:
{
    "sub": "user@example.com",
    "permissions": "read,write,delete",
    "exp": 1770622371
}

// Configuración:
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    converter.setAuthoritiesClaimName("permissions");
    converter.setAuthoritiesClaimDelimiter(",");  // ⬅️ Usa coma como delimitador
    return converter;
}

// Authorities generadas:
[
    Authority("read"),
    Authority("write"),
    Authority("delete")
]
```

---

### 🎓 Resumen y Mejores Prácticas

### 📊 Tabla Comparativa de Converters

| Aspecto | JwtGrantedAuthoritiesConverter | JwtAuthenticationConverter |
|---------|-------------------------------|---------------------------|
| **Propósito** | Extraer authorities del JWT | Convertir JWT completo a Authentication |
| **Enfoque** | Especialista en authorities | Orquestador general |
| **Input** | JWT (objeto Jwt) | JWT (objeto Jwt) |
| **Output** | `Collection<GrantedAuthority>` | `AbstractAuthenticationToken` |
| **Configurable** | Prefijo, claim name, delimiter | Converter de authorities, principal extractor |
| **Dependencias** | Ninguna | JwtGrantedAuthoritiesConverter |

---

### ✅ Mejores Prácticas

#### 1️⃣ **Siempre configura el claim correcto**

```java
// ❌ MAL: Asume que el claim es "scope"
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();  // Usa defaults
}

// ✅ BIEN: Especifica el claim que usas
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthoritiesClaimName("ROLES");  // ⬅️ Explícito
    return converter;
}
```

---

#### 2️⃣ **Elimina prefijos innecesarios**

```java
// ❌ MAL: Prefijo por defecto causa problemas
// Genera: "SCOPE_read" cuando necesitas "read"

// ✅ BIEN: Sin prefijo para authorities simples
converter.setAuthorityPrefix("");

// ✅ BIEN: Con prefijo para roles
converter.setAuthorityPrefix("ROLE_");  // Para usar hasRole()
```

---

#### 3️⃣ **Usa nombres de claims estándar cuando sea posible**

| Standard OAuth2 | Recomendado |
|-----------------|-------------|
| `scope` | ✅ Para permisos OAuth2 |
| `scp` | ✅ Alias de `scope` |
| `authorities` | ✅ Para Spring Security |
| `roles` | ✅ Para roles de aplicación |
| `ROLES` | ⚠️ Funciona pero no es estándar |

---

#### 4️⃣ **Configura correctamente en SecurityFilterChain**

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                       JwtAuthenticationConverter jwtConverter) throws Exception {
    http.oauth2ResourceServer(oauth ->
        oauth.jwt(jwt -> 
            jwt.jwtAuthenticationConverter(jwtConverter)  // ⬅️ Inyecta tu converter
        )
    );
    return http.build();
}
```

---

### 🔧 Configuración Recomendada para tu Proyecto

```java
@Bean
JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");  // Sin prefijo
    converter.setAuthoritiesClaimName("ROLES");  // ⬅️ AGREGAR ESTA LÍNEA
    return converter;
}

@Bean
JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter converter) {
    var jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtConverter;
}
```

---

### 🎯 Puntos Clave a Recordar

1. 🔄 **JwtGrantedAuthoritiesConverter** → Extrae authorities (roles/permisos)
2. 🔀 **JwtAuthenticationConverter** → Orquesta todo el proceso de conversión
3. 🎫 **Claim name** → Debe coincidir con tu JWT (`"ROLES"` en tu caso)
4. 🏷️ **Prefix** → Vacío `""` para authorities simples, `"ROLE_"` para roles
5. 🔗 **Inyección** → Spring conecta automáticamente los dos converters
6. ⚙️ **OAuth2 Resource Server** → Usa estos converters para validar JWT

---

### 🐛 Troubleshooting

| Problema | Causa | Solución |
|----------|-------|----------|
| `403 Forbidden` | Authorities vacías | Configura `setAuthoritiesClaimName()` |
| `hasAuthority("read")` falla | Prefijo incorrecto | `setAuthorityPrefix("")` |
| `hasRole("ADMIN")` falla | Sin prefijo `ROLE_` | `setAuthorityPrefix("ROLE_")` |
| Authorities `null` | Claim no existe en JWT | Verifica que el JWT tenga el claim correcto |
| Delimitador incorrecto | Usa comas en lugar de espacios | `setAuthoritiesClaimDelimiter(",")` |

---

### 🎉 Conclusión

Los **JWT Converters** son piezas fundamentales en la arquitectura de seguridad OAuth2:

```
🔐 JWT Token
    ↓
🔄 JwtAuthenticationConverter (Orquestador)
    ├─ 👤 Extrae username
    └─ 🛡️ JwtGrantedAuthoritiesConverter (Especialista)
        └─ Extrae authorities
    ↓
✅ Authentication (Usuario autenticado)
    ├─ Principal: "account@debuggeandoieas.com"
    └─ Authorities: ["VIEW_ACCOUNT"]
    ↓
🎯 Security checks: hasAuthority(), hasRole()
```

**Próximo paso recomendado:** Agregar `setAuthoritiesClaimName("ROLES")` a tu configuración para que funcione correctamente con tu JWT personalizado. 🚀



---

## 📝 Clase 80  - GENERANDO LLAVES RSA👤🕵️‍♂🕵️‍♂🔑 🔑

- Se crean dos metodos para generar un par de llaves RSA y construir un objeto RSAKey con la información de la llave pública
- y privada, además de un ID único para la llave. Este RSAKey se utilizará posteriormente para configurar el JWKSource en el
- Authorization Server, permitiendo que el servidor firme los tokens JWT con la llave privada y exponga la llave pública a 
- través del endpoint JWKS para que los clientes puedan verificar las firmas de los tokens.
- 
```java

    private static KeyPair generateRSA() {
        KeyPair keyPair;

        try {
            var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(RSA_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    private static RSAKey generateKeys() {
        var keyPair = generateRSA();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
                
    }

```

### 🔑 Generación de Claves RSA y JWK (JSON Web Key)

### 📑 Índice
- [🎯 ¿Qué son las Claves RSA?](#-qué-son-las-claves-rsa)
- [🔐 Método generateRSA()](#-método-generatersa)
  - [🏗️ Paso a Paso del Código](#️-paso-a-paso-del-código)
  - [🔍 ¿Qué es KeyPairGenerator?](#-qué-es-keypairgenerator)
  - [📊 Diagrama de Flujo](#-diagrama-de-flujo)
  - [💡 Ejemplo con tu Proyecto](#-ejemplo-con-tu-proyecto)
- [🛠️ Método generateKeys()](#️-método-generatekeys)
  - [🏗️ Construcción de RSAKey](#️-construcción-de-rsakey)
  - [🔑 Componentes de la Clave](#-componentes-de-la-clave)
  - [📦 Estructura del JWK](#-estructura-del-jwk)
- [🔄 Flujo Completo](#-flujo-completo)
- [⚠️ Consideraciones de Seguridad](#️-consideraciones-de-seguridad)

---

### 🎯 ¿Qué son las Claves RSA?

**RSA** (Rivest-Shamir-Adleman) es un **algoritmo de cifrado asimétrico** que utiliza dos claves:

```
┌─────────────────────────────────────────────────────────────┐
│  🔑 PAR DE CLAVES RSA                                        │
│                                                               │
│  ┌─────────────────────┐    ┌─────────────────────┐         │
│  │  🔓 CLAVE PÚBLICA    │    │  🔐 CLAVE PRIVADA   │         │
│  │                      │    │                      │         │
│  │  ✅ Se comparte      │    │  ❌ Se mantiene      │         │
│  │  ✅ Cifra datos      │    │     en secreto       │         │
│  │  ✅ Verifica firmas  │    │  ✅ Descifra datos   │         │
│  │                      │    │  ✅ Firma tokens     │         │
│  └─────────────────────┘    └─────────────────────┘         │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 📌 ¿Por qué se usa RSA en OAuth2?

| Característica | Explicación |
|----------------|-------------|
| **🔐 Seguridad** | Los tokens JWT se **firman** con la clave privada y se **verifican** con la clave pública |
| **🌐 Distribución** | Los clientes pueden verificar tokens sin necesidad de compartir secretos |
| **✅ Integridad** | Garantiza que el token no ha sido modificado |
| **🔒 No Repudio** | Solo el Authorization Server puede crear tokens válidos |

#### 🔄 Flujo en OAuth2

```
┌─────────────────────────────────────────────────────────────┐
│  1️⃣ Usuario se autentica                                     │
│     ├─ Username: account@debuggeandoieas.com                │
│     └─ Password: ******                                      │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  2️⃣ Authorization Server genera JWT                          │
│     ├─ Crea el payload con claims                           │
│     └─ 🔐 FIRMA con CLAVE PRIVADA RSA                       │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  3️⃣ JWT enviado al cliente                                   │
│     eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhY2NvdW50QC4uLiJ9....   │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  4️⃣ Resource Server valida JWT                               │
│     └─ 🔓 VERIFICA con CLAVE PÚBLICA RSA                    │
└─────────────────────────────────────────────────────────────┘
```

---

### 🔐 Método generateRSA()

```java
private static KeyPair generateRSA() {
    KeyPair keyPair;
    
    try {
        var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(RSA_SIZE);
        keyPair = keyPairGenerator.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    }
    return keyPair;
}
```

#### 🎯 Propósito del Método

Este método genera un **par de claves RSA** (pública y privada) utilizando la API de criptografía de Java.

---

### 🏗️ Paso a Paso del Código

#### **Línea 1-2: Declaración de Variables**

```java
KeyPair keyPair;
```

| Concepto | Explicación |
|----------|-------------|
| **KeyPair** | Clase de Java que contiene **dos claves**: una pública y una privada |
| **Declaración** | Se declara pero no se inicializa aún |

---

#### **Línea 4: KeyPairGenerator.getInstance(RSA)**

```java
var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
```

| Concepto | Explicación |
|----------|-------------|
| **KeyPairGenerator** | Clase de `java.security` que genera pares de claves criptográficas |
| **getInstance(RSA)** | Solicita una instancia para generar claves del algoritmo **RSA** |
| **RSA** | Constante que debe ser `"RSA"` (nombre del algoritmo) |

📝 **Nota**: `RSA` debe estar definido como:
```java
private static final String RSA = "RSA";
```

---

#### **Línea 5: keyPairGenerator.initialize(RSA_SIZE)**

```java
keyPairGenerator.initialize(RSA_SIZE);
```

| Concepto | Explicación |
|----------|-------------|
| **initialize()** | Configura el **tamaño de la clave** en bits |
| **RSA_SIZE** | Tamaño recomendado: **2048 bits** (mínimo seguro) o **4096 bits** (mayor seguridad) |

📝 **Nota**: `RSA_SIZE` debe estar definido como:
```java
private static final int RSA_SIZE = 2048;
```

#### 🔢 Tamaños de Clave RSA

| Tamaño | Seguridad | Uso Recomendado |
|--------|-----------|-----------------|
| **1024 bits** | ⚠️ Inseguro | ❌ Ya no se recomienda |
| **2048 bits** | ✅ Seguro | ✅ Estándar actual (OAuth2) |
| **4096 bits** | 🔒 Muy seguro | ✅ Aplicaciones críticas |

---

#### **Línea 6: keyPairGenerator.generateKeyPair()**

```java
keyPair = keyPairGenerator.generateKeyPair();
```

| Concepto | Explicación |
|----------|-------------|
| **generateKeyPair()** | **Genera aleatoriamente** el par de claves RSA |
| **Resultado** | Un objeto `KeyPair` con la clave pública y privada |

---

#### **Línea 7-9: Manejo de Excepciones**

```java
} catch (NoSuchAlgorithmException e) {
    throw new IllegalStateException(e);
}
```

| Excepción | Cuándo Ocurre | Solución |
|-----------|---------------|----------|
| **NoSuchAlgorithmException** | Si el algoritmo "RSA" no está disponible en el sistema | Verificar la instalación de JDK (RSA es estándar) |
| **IllegalStateException** | Se relanza como error de configuración | Indica un problema crítico en el sistema |

---

### 🔍 ¿Qué es KeyPairGenerator?

```java
┌─────────────────────────────────────────────────────────────┐
│  KeyPairGenerator                                            │
│  ├─ getInstance("RSA") ➡️ Obtiene generador para RSA        │
│  ├─ initialize(2048) ➡️ Configura tamaño de 2048 bits       │
│  └─ generateKeyPair() ➡️ Genera las claves                  │
└─────────────────────────────────────────────────────────────┘
                        ⬇️
┌─────────────────────────────────────────────────────────────┐
│  KeyPair                                                     │
│  ├─ getPublic() ➡️ RSAPublicKey (para verificar)            │
│  └─ getPrivate() ➡️ RSAPrivateKey (para firmar)             │
└─────────────────────────────────────────────────────────────┘
```

---

### 📊 Diagrama de Flujo

```
        ┌──────────────────────┐
        │  Iniciar Aplicación  │
        └──────────┬───────────┘
                   │
                   ⬇️
        ┌──────────────────────┐
        │ generateRSA() llamado│
        └──────────┬───────────┘
                   │
                   ⬇️
        ┌──────────────────────────────────┐
        │ KeyPairGenerator.getInstance()   │
        │ Algoritmo: "RSA"                 │
        └──────────┬───────────────────────┘
                   │
                   ⬇️
        ┌──────────────────────────────────┐
        │ initialize(2048)                 │
        │ Tamaño de clave: 2048 bits       │
        └──────────┬───────────────────────┘
                   │
                   ⬇️
        ┌──────────────────────────────────┐
        │ generateKeyPair()                │
        │ Genera claves aleatoriamente     │
        └──────────┬───────────────────────┘
                   │
                   ⬇️
        ┌──────────────────────────────────┐
        │ KeyPair generado                 │
        │ ├─ Clave Pública 🔓              │
        │ └─ Clave Privada 🔐              │
        └──────────┬───────────────────────┘
                   │
                   ⬇️
        ┌──────────────────────────────────┐
        │ Retorna KeyPair                  │
        └──────────────────────────────────┘
```

---

### 💡 Ejemplo con tu Proyecto

Cuando tu aplicación **inicia**, este método se ejecuta:

```java
// En SecurityConfig.java (aproximadamente)

@Bean
public JWKSource<SecurityContext> jwkSource() {
    // 1️⃣ Llama a generateRSA()
    RSAKey rsaKey = generateKeys(); // Internamente llama a generateRSA()
    
    // 2️⃣ Las claves se usan para firmar/verificar tokens
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
}
```

#### 🔄 Proceso Completo

```
Usuario se autentica
     ⬇️
generateRSA() genera claves RSA
     ⬇️
Clave privada firma el JWT Token
     ⬇️
JWT enviado al cliente: eyJhbGciOiJSUzI1NiJ9...
     ⬇️
Resource Server usa clave pública para verificar
```

---

### 🛠️ Método generateKeys()

```java
private static RSAKey generateKeys() {
    var keyPair = generateRSA();
    var publicKey = (RSAPublicKey) keyPair.getPublic();
    var privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
}
```

#### 🎯 Propósito del Método

Este método toma el **KeyPair** generado y lo convierte en un **RSAKey** (JWK - JSON Web Key) que Spring Security OAuth2 puede usar.

---

### 🏗️ Construcción de RSAKey

#### **Línea 1: Generar el KeyPair**

```java
var keyPair = generateRSA();
```

| Acción | Resultado |
|--------|-----------|
| Llama a `generateRSA()` | Obtiene un `KeyPair` con claves pública y privada |

---

#### **Línea 2-3: Extraer las Claves**

```java
var publicKey = (RSAPublicKey) keyPair.getPublic();
var privateKey = (RSAPrivateKey) keyPair.getPrivate();
```

| Método | Tipo Retornado | Uso |
|--------|----------------|-----|
| **keyPair.getPublic()** | `PublicKey` (genérico) | Se castea a `RSAPublicKey` |
| **keyPair.getPrivate()** | `PrivateKey` (genérico) | Se castea a `RSAPrivateKey` |

📝 **Casting**: Se necesita porque `KeyPair` retorna tipos genéricos, pero necesitamos tipos específicos de RSA.

---

#### **Línea 4-7: Construcción del RSAKey**

```java
return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
```

| Método | Parámetro | Explicación |
|--------|-----------|-------------|
| **Builder(publicKey)** | `RSAPublicKey` | Constructor: la clave pública es **obligatoria** |
| **.privateKey()** | `RSAPrivateKey` | Añade la clave privada (opcional pero necesaria para firmar) |
| **.keyID()** | `String` (UUID) | Identificador único de esta clave (para rotar claves) |
| **.build()** | - | Construye el objeto `RSAKey` final |

---

### 🔑 Componentes de la Clave

```
┌─────────────────────────────────────────────────────────────┐
│  RSAKey (JWK - JSON Web Key)                                │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🔓 RSAPublicKey                                     │    │
│  │  ├─ Módulo (n)                                       │    │
│  │  └─ Exponente público (e)                           │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🔐 RSAPrivateKey                                    │    │
│  │  └─ Exponente privado (d)                           │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🆔 Key ID (kid)                                     │    │
│  │  └─ "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a"         │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

### 📦 Estructura del JWK

El **RSAKey** se puede exportar a formato JSON (JWK):

```json
{
  "kty": "RSA",
  "kid": "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a",
  "use": "sig",
  "alg": "RS256",
  "n": "xGOr-H7A...public key modulus...",
  "e": "AQAB"
}
```

| Campo | Significado | Valor en tu Proyecto |
|-------|-------------|----------------------|
| **kty** | Key Type | `"RSA"` |
| **kid** | Key ID | UUID generado aleatoriamente |
| **use** | Public Key Use | `"sig"` (para firmar) |
| **alg** | Algorithm | `"RS256"` (RSA + SHA-256) |
| **n** | Modulus | Parte de la clave pública |
| **e** | Exponent | Generalmente `65537` (AQAB en Base64) |

---

#### 🔍 ¿Por qué UUID.randomUUID()?

```java
.keyID(UUID.randomUUID().toString())
```

| Razón | Explicación |
|-------|-------------|
| **🆔 Identificación única** | Cada clave tiene un ID único |
| **🔄 Rotación de claves** | Permite tener múltiples claves activas |
| **📍 Tracking** | Los tokens especifican qué clave los firmó |

#### 📌 Ejemplo de Token JWT con kid

```
Header del JWT:
{
  "alg": "RS256",
  "kid": "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a"
}
```

El Resource Server puede:
1. Leer el `kid` del token
2. Buscar la clave pública correspondiente
3. Verificar la firma

---

### 🔄 Flujo Completo

```
┌──────────────────────────────────────────────────────────────┐
│  1️⃣ generateRSA()                                             │
│     Genera KeyPair de 2048 bits                              │
│     ├─ PublicKey (para verificar)                            │
│     └─ PrivateKey (para firmar)                              │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  2️⃣ generateKeys()                                            │
│     Extrae claves del KeyPair                                │
│     ├─ RSAPublicKey publicKey = keyPair.getPublic()         │
│     └─ RSAPrivateKey privateKey = keyPair.getPrivate()      │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  3️⃣ Construcción del RSAKey                                   │
│     new RSAKey.Builder(publicKey)                            │
│         .privateKey(privateKey)                              │
│         .keyID("3f2e1a45-8d7c-4b9e...")                      │
│         .build()                                             │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  4️⃣ RSAKey se registra en JWKSource                           │
│     Spring Security lo usa para:                             │
│     ├─ Firmar tokens JWT (Authorization Server)             │
│     └─ Verificar tokens JWT (Resource Server)               │
└──────────────────────────────────────────────────────────────┘
```

---

### 💡 Ejemplo Completo en tu SecurityConfig

```java
@Configuration
public class SecurityConfig {
    
    private static final String RSA = "RSA";
    private static final int RSA_SIZE = 2048;
    
    // 🔑 Bean principal que Spring Security usa
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateKeys();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }
    
    // 🛠️ Método auxiliar: Crea el JWK
    private static RSAKey generateKeys() {
        var keyPair = generateRSA(); // Llama al generador de claves
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }
    
    // 🔐 Método auxiliar: Genera el par de claves RSA
    private static KeyPair generateRSA() {
        KeyPair keyPair;
        
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(RSA_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }
}
```

---

#### 🔄 Uso en Autenticación

```
┌──────────────────────────────────────────────────────────────┐
│  Cliente solicita token                                       │
│  POST /authenticate                                           │
│  Body: { username: "account@debuggeandoieas.com", ... }      │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  AuthController valida credenciales                          │
│  └─ authenticationManager.authenticate(...)                  │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  JWTService genera token                                     │
│  ├─ Crea payload: { sub: "account@...", roles: [...] }      │
│  └─ 🔐 FIRMA con RSAPrivateKey (de generateKeys())           │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  Token JWT devuelto                                          │
│  {                                                            │
│    "jwt": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjNmMmUxYTQ1...     │
│  }                                                            │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  Cliente usa token en peticiones                             │
│  GET /accounts                                               │
│  Header: Authorization: Bearer eyJhbGciOiJSUzI1NiI...        │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  JWTValidationFilter valida token                            │
│  └─ 🔓 VERIFICA con RSAPublicKey (de generateKeys())         │
└──────────────────────────────────────────────────────────────┘
```

---

### ⚠️ Consideraciones de Seguridad

#### 🔐 Buenas Prácticas

| Práctica | ✅ Recomendación |
|----------|------------------|
| **Tamaño de clave** | Usar mínimo 2048 bits (mejor 4096) |
| **Rotación de claves** | Cambiar claves periódicamente |
| **Almacenamiento** | ⚠️ Nunca hardcodear las claves en el código |
| **Producción** | Cargar claves desde archivo o vault seguro |
| **Key ID** | Usar UUID único para rastrear claves |

---

#### ⚠️ Problema en tu Implementación Actual

```java
// ❌ PROBLEMA: Las claves se generan en cada inicio
private static RSAKey generateKeys() {
    var keyPair = generateRSA(); // Claves ALEATORIAS cada vez
    // ...
}
```

**Consecuencias**:
- ❌ Si reinicias la aplicación, los tokens antiguos **no se pueden verificar**
- ❌ Los usuarios tendrán que volver a autenticarse

---

#### ✅ Solución para Producción

**Opción 1: Cargar desde archivo**

```java
@Bean
public JWKSource<SecurityContext> jwkSource() throws Exception {
    // Cargar clave desde archivo
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream("keystore.jks"), "password".toCharArray());
    
    // Extraer el par de claves
    Key key = keyStore.getKey("mykey", "keypassword".toCharArray());
    // ... construir RSAKey
}
```

**Opción 2: Usar Spring Cloud Config o Vault**

```yaml
# application.yml
spring:
  security:
    oauth2:
      authorizationserver:
        jwk:
          set:
            uri: http://config-server/jwks.json
```

---

#### 📊 Comparación: JWT con HS256 vs RS256

| Aspecto | HS256 (HMAC) | RS256 (RSA) - Tu Proyecto |
|---------|--------------|---------------------------|
| **Tipo** | Simétrico | Asimétrico |
| **Clave** | Una clave secreta compartida | Par de claves (pública/privada) |
| **Firma** | Con JWT_SECRET | Con `RSAPrivateKey` |
| **Verificación** | Con JWT_SECRET | Con `RSAPublicKey` |
| **Distribución** | ⚠️ Todos necesitan el secreto | ✅ Solo se comparte la clave pública |
| **Uso** | Aplicaciones monolíticas | Microservicios, OAuth2 |

---

#### 🔄 Evolución: De HS256 a RS256

**Antes (tu código JWT anterior)**:
```java
// HS256: Clave simétrica
public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN...";

// Firma Y verifica con la MISMA clave
Jwts.builder()
    .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
    .compact();
```

**Ahora (OAuth2 con RS256)**:
```java
// RS256: Claves asimétricas
RSAKey rsaKey = generateKeys(); // Genera par de claves

// FIRMA con clave PRIVADA
// (Authorization Server)

// VERIFICA con clave PÚBLICA
// (Resource Server)
```

---

### 🎓 Resumen para Estudiantes

```
┌─────────────────────────────────────────────────────────────┐
│  📚 CONCEPTOS CLAVE                                          │
│                                                               │
│  1️⃣ RSA es un algoritmo de cifrado ASIMÉTRICO               │
│     └─ Usa DOS claves: pública y privada                    │
│                                                               │
│  2️⃣ generateRSA() genera el par de claves                   │
│     └─ Usa KeyPairGenerator de Java                         │
│                                                               │
│  3️⃣ generateKeys() convierte KeyPair a RSAKey (JWK)         │
│     └─ Formato que Spring Security OAuth2 entiende          │
│                                                               │
│  4️⃣ KeyID (UUID) identifica cada clave                      │
│     └─ Permite rotación y múltiples claves activas          │
│                                                               │
│  5️⃣ En OAuth2, el token se FIRMA con privada                │
│     └─ Y se VERIFICA con la pública                          │
│                                                               │
│  ⚠️ Problema: Claves se regeneran en cada inicio             │
│     └─ En producción: cargar desde almacenamiento seguro    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔍 Diferencia con tu JWT Anterior

**Tu código JWT (Sección 8)**:
- Usabas **HS256** (simétrico)
- Una sola clave: `JWT_SECRET`
- Tú generabas y validabas el token manualmente

**OAuth2 (Sección 9)**:
- Usa **RS256** (asimétrico)
- Dos claves: pública y privada
- Spring Authorization Server maneja todo automáticamente

---

#### 💡 Analogía del Mundo Real

```
HS256 (Clave simétrica):
┌─────────────────────────────────────────┐
│  🔑 Una sola llave                      │
│  ├─ Abre la puerta (firma)             │
│  └─ Cierra la puerta (verifica)        │
│                                          │
│  ⚠️ Problema: Todos necesitan la llave  │
└─────────────────────────────────────────┘

RS256 (Claves asimétricas):
┌─────────────────────────────────────────┐
│  🔐 Llave maestra (privada)             │
│  └─ Solo el servidor la tiene           │
│     └─ Firma tokens                     │
│                                          │
│  🔓 Llave de verificación (pública)     │
│  └─ Todos pueden tenerla                │
│     └─ Solo verifica, no crea tokens    │
└─────────────────────────────────────────┘
```


---

## 📝 Clase 81  -  CONFIGURANDO FIRMA DE NUESTRO JWT👤🕵️‍♂🕵️‍♂🔑 🔑

- Se crea estos metodos para configurar la firma de los tokens JWT utilizando las claves 
- RSA generadas previamente. El método `jwkSource()` construye un `JWKSet` con la clave RSA
- y lo expone como un `JWKSource` que Spring Security puede usar para firmar y verificar tokens. 
- El método `jwtDecoder()` configura el decodificador JWT para que utilice el `JWKSource`, 
- permitiendo que el Resource Server verifique las firmas de los tokens JWT utilizando la clave pública RSA.

```java

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        var rsa = generateKeys();
        var jwkSet = new JWKSet(rsa);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

```

### 🎯 JWKSource y JwtDecoder en tu Proyecto

### 📑 Índice
- [🔑 ¿Qué es JWKSource?](#-qué-es-jwksource)
  - [📦 Propósito y Función](#-propósito-y-función)
  - [🏗️ Análisis del Método jwkSource()](#️-análisis-del-método-jwksource)
  - [🔍 Línea por Línea](#-línea-por-línea)
  - [🧩 Lambda Expression Explicada](#-lambda-expression-explicada)
  - [💡 Ejemplo Práctico](#-ejemplo-práctico)
- [🔓 ¿Qué es JwtDecoder?](#-qué-es-jwtdecoder)
  - [📦 Propósito y Función](#-propósito-y-función-1)
  - [🏗️ Análisis del Método jwtDecoder()](#️-análisis-del-método-jwtdecoder)
  - [🔍 Proceso de Decodificación](#-proceso-de-decodificación)
- [🔄 Flujo Completo de Autenticación OAuth2](#-flujo-completo-de-autenticación-oauth2)
- [🎭 Roles: Authorization Server vs Resource Server](#-roles-authorization-server-vs-resource-server)
- [🧪 Ejemplo Real con tu Proyecto](#-ejemplo-real-con-tu-proyecto)

---

### 🔑 ¿Qué es JWKSource?

```java
@Bean
JWKSource<SecurityContext> jwkSource() {
    var rsa = generateKeys();
    var jwkSet = new JWKSet(rsa);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
}
```

#### 📦 Propósito y Función

**`JWKSource`** es una **fuente de claves JSON Web Key (JWK)** que proporciona las claves criptográficas para:

```
┌─────────────────────────────────────────────────────────────┐
│  🔑 JWKSource - Proveedor de Claves                          │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🎯 Responsabilidades                                │    │
│  │                                                       │    │
│  │  1️⃣ Almacenar las claves RSA (JWK)                  │    │
│  │  2️⃣ Proporcionar claves para FIRMAR tokens (🔐)     │    │
│  │  3️⃣ Proporcionar claves para VERIFICAR tokens (🔓)  │    │
│  │  4️⃣ Soportar rotación de claves                     │    │
│  │                                                       │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

#### 🔑 Analogía del Mundo Real

Imagina una **caja fuerte con llaves maestras**:

```
┌─────────────────────────────────────────────────────────────┐
│  🏦 Banco (Authorization Server)                             │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🔑 JWKSource (Caja Fuerte de Llaves)               │    │
│  │                                                       │    │
│  │  ┌──────────────┐  ┌──────────────┐                │    │
│  │  │ 🔐 Llave 1   │  │ 🔐 Llave 2   │                │    │
│  │  │ (RSA Key)    │  │ (RSA Key)    │                │    │
│  │  │ kid: abc123  │  │ kid: def456  │                │    │
│  │  └──────────────┘  └──────────────┘                │    │
│  │                                                       │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  Empleado necesita firmar documento (JWT)                   │
│  └─ JWKSource le proporciona la llave correcta              │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

### 🏗️ Análisis del Método jwkSource()

```java
@Bean
JWKSource<SecurityContext> jwkSource() {
    var rsa = generateKeys();                                    // 1️⃣
    var jwkSet = new JWKSet(rsa);                               // 2️⃣
    return (jwkSelector, securityContext) -> 
           jwkSelector.select(jwkSet);                           // 3️⃣
}
```

---

### 🔍 Línea por Línea

#### **Línea 1️⃣: var rsa = generateKeys();**

```java
var rsa = generateKeys();
```

| Concepto | Explicación |
|----------|-------------|
| **generateKeys()** | Llama al método que generaste en la Clase 93 |
| **Retorna** | Un objeto `RSAKey` con las claves pública y privada |
| **rsa** | Variable que almacena el par de claves JWK |

📌 **Recordatorio**: Este método fue explicado en la clase anterior:

```java
private static RSAKey generateKeys() {
    var keyPair = generateRSA();
    var publicKey = (RSAPublicKey) keyPair.getPublic();
    var privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())  // Ej: "3f2e1a45-8d7c..."
            .build();
}
```

**Resultado de rsa**:
```
RSAKey {
  kid: "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a"
  publicKey: RSAPublicKey (para verificar)
  privateKey: RSAPrivateKey (para firmar)
}
```

---

#### **Línea 2️⃣: var jwkSet = new JWKSet(rsa);**

```java
var jwkSet = new JWKSet(rsa);
```

| Concepto | Explicación |
|----------|-------------|
| **JWKSet** | Conjunto (Set) de claves JWK |
| **Propósito** | Agrupa múltiples claves (aunque aquí solo hay una) |
| **Beneficio** | Permite tener varias claves activas simultáneamente |

#### 🗂️ Estructura de JWKSet

```
┌─────────────────────────────────────────────────────────────┐
│  📦 JWKSet - Conjunto de Claves                              │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🔑 RSAKey 1                                         │    │
│  │  ├─ kid: "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a"    │    │
│  │  ├─ publicKey: RSAPublicKey                         │    │
│  │  └─ privateKey: RSAPrivateKey                       │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  (Podrías agregar más claves aquí para rotación)            │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 💡 Ejemplo: JWKSet con Múltiples Claves

```java
// En un escenario de producción con rotación de claves:
var oldKey = generateKeys();  // Clave antigua (aún válida)
var newKey = generateKeys();  // Clave nueva

var jwkSet = new JWKSet(List.of(oldKey, newKey));
// Ahora tienes 2 claves activas simultáneamente
```

**Beneficio**: Los tokens antiguos firmados con `oldKey` siguen siendo válidos mientras introduces `newKey`.

---

#### **Línea 3️⃣: Lambda Expression**

```java
return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
```

Esta es una **expresión lambda** que implementa la interfaz funcional `JWKSource<SecurityContext>`.

#### 🎯 Interfaz JWKSource

```java
@FunctionalInterface
public interface JWKSource<C extends SecurityContext> {
    List<JWK> get(JWKSelector jwkSelector, C context) 
        throws KeySourceException;
}
```

| Parámetro | Tipo | Explicación |
|-----------|------|-------------|
| **jwkSelector** | `JWKSelector` | Objeto que **selecciona** qué clave usar del conjunto |
| **securityContext** | `SecurityContext` | Contexto de seguridad (no se usa en este caso) |
| **Retorna** | `List<JWK>` | Lista de claves que coinciden con los criterios |

---

### 🧩 Lambda Expression Explicada

#### **Formato Completo vs Lambda**

**Forma tradicional (sin lambda)**:
```java
return new JWKSource<SecurityContext>() {
    @Override
    public List<JWK> get(JWKSelector jwkSelector, 
                         SecurityContext securityContext) 
                         throws KeySourceException {
        return jwkSelector.select(jwkSet);
    }
};
```

**Forma lambda (tu código)**:
```java
return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
```

#### 📊 Comparación

```
┌─────────────────────────────────────────────────────────────┐
│  Forma Tradicional (Anónima)                                 │
│  ❌ 7 líneas de código                                       │
│  ❌ Más verbosa                                              │
│  ❌ Menos legible                                            │
└─────────────────────────────────────────────────────────────┘
                        VS
┌─────────────────────────────────────────────────────────────┐
│  Forma Lambda (Funcional)                                    │
│  ✅ 1 línea de código                                        │
│  ✅ Más concisa                                              │
│  ✅ Más moderna (Java 8+)                                    │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔍 ¿Qué hace jwkSelector.select(jwkSet)?

```java
jwkSelector.select(jwkSet)
```

| Acción | Descripción |
|--------|-------------|
| **jwkSelector** | Recibe criterios de búsqueda (ej: "dame la clave con kid=abc123") |
| **select(jwkSet)** | Busca en el `jwkSet` las claves que coincidan |
| **Retorna** | Lista de claves que cumplen los criterios |

#### 📝 Ejemplo de Selección

```
┌─────────────────────────────────────────────────────────────┐
│  🔍 Proceso de Selección                                     │
│                                                               │
│  1️⃣ Token JWT llega con header:                             │
│     {                                                         │
│       "alg": "RS256",                                        │
│       "kid": "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a"        │
│     }                                                         │
│                                                               │
│  2️⃣ JWKSelector busca: "Dame la clave con kid=3f2e1a45..."  │
│                                                               │
│  3️⃣ select(jwkSet) devuelve:                                 │
│     [RSAKey con kid="3f2e1a45..."]                           │
│                                                               │
│  4️⃣ Se usa la clave pública para verificar el token         │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

### 💡 Ejemplo Práctico

#### 🔄 Flujo de Ejecución en tu Aplicación

```
┌──────────────────────────────────────────────────────────────┐
│  📱 Aplicación Inicia                                         │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🔧 Spring llama a @Bean jwkSource()                          │
│  ├─ var rsa = generateKeys()                                 │
│  │  └─ Genera: RSAKey con kid="3f2e1a45..."                 │
│  ├─ var jwkSet = new JWKSet(rsa)                             │
│  │  └─ Crea conjunto con 1 clave                            │
│  └─ return (jwkSelector, ctx) -> jwkSelector.select(jwkSet) │
│     └─ Registra la lambda como JWKSource                     │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  ✅ JWKSource Bean registrado en Spring Context              │
│     ├─ Authorization Server usa clave PRIVADA (firma)        │
│     └─ Resource Server usa clave PÚBLICA (verifica)          │
└──────────────────────────────────────────────────────────────┘
```

---

#### 🎬 Caso de Uso Real

**Escenario 1: Usuario se autentica**

```
Usuario: POST /oauth2/token
         username=account@debuggeandoieas.com
         password=12345

         ⬇️

Authorization Server:
├─ Valida credenciales ✅
├─ Crea JWT payload: { sub: "account@...", roles: "VIEW_ACCOUNT" }
└─ 🔐 FIRMA con clave PRIVADA del JWKSource

         ⬇️

Token generado:
eyJhbGciOiJSUzI1NiIsImtpZCI6IjNmMmUxYTQ1...
Header: { "alg": "RS256", "kid": "3f2e1a45..." }
```

**Escenario 2: Usuario accede a un recurso**

```
Cliente: GET /accounts
         Authorization: Bearer eyJhbGciOiJSUzI1NiI...

         ⬇️

Resource Server:
├─ 1️⃣ Lee el token
├─ 2️⃣ Extrae kid="3f2e1a45..." del header
├─ 3️⃣ Llama a JWKSource con JWKSelector(kid="3f2e1a45...")
├─ 4️⃣ JWKSource ejecuta: jwkSelector.select(jwkSet)
├─ 5️⃣ Obtiene la clave pública del RSAKey
└─ 6️⃣ 🔓 VERIFICA la firma del token

         ⬇️

Si la firma es válida ✅
└─ Permite acceso al recurso
```

---

### 🔓 ¿Qué es JwtDecoder?

```java
@Bean
JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
}
```

#### 📦 Propósito y Función

**`JwtDecoder`** es el componente que **decodifica y valida** los tokens JWT en el **Resource Server**.

```
┌─────────────────────────────────────────────────────────────┐
│  🔓 JwtDecoder - Validador de Tokens                         │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🎯 Responsabilidades                                │    │
│  │                                                       │    │
│  │  1️⃣ Decodificar el token JWT                        │    │
│  │  2️⃣ Verificar la firma con la clave pública         │    │
│  │  3️⃣ Validar expiración (exp claim)                  │    │
│  │  4️⃣ Validar emisor (iss claim)                      │    │
│  │  5️⃣ Extraer claims (subject, roles, etc.)           │    │
│  │                                                       │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

### 🏗️ Análisis del Método jwtDecoder()

```java
@Bean
JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
}
```

#### 📋 Parámetros y Retorno

| Elemento | Tipo | Explicación |
|----------|------|-------------|
| **Parámetro** | `JWKSource<SecurityContext>` | Spring inyecta automáticamente el bean `jwkSource()` que creaste antes |
| **Retorna** | `JwtDecoder` | Instancia configurada para decodificar tokens JWT |
| **Método Factory** | `OAuth2AuthorizationServerConfiguration.jwtDecoder()` | Spring provee este método para crear el decoder configurado |

---

#### 🔗 Inyección de Dependencias

```
┌─────────────────────────────────────────────────────────────┐
│  🔧 Spring Container                                         │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  @Bean jwkSource()                                   │    │
│  │  └─ Crea JWKSource con claves RSA                   │    │
│  └─────────────────┬───────────────────────────────────┘    │
│                    │                                          │
│                    │ Inyección automática                    │
│                    ⬇️                                         │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  @Bean jwtDecoder(JWKSource jwkSource)               │    │
│  │  └─ Recibe jwkSource como parámetro                 │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

📝 **Nota**: Spring ve que `jwtDecoder()` necesita un `JWKSource` y automáticamente inyecta el bean `jwkSource()`.

---

#### 🏭 OAuth2AuthorizationServerConfiguration.jwtDecoder()

```java
OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
```

Este método estático de Spring Security hace lo siguiente:

```
┌─────────────────────────────────────────────────────────────┐
│  🏭 Factory Method: jwtDecoder(jwkSource)                    │
│                                                               │
│  1️⃣ Crea una instancia de NimbusJwtDecoder                  │
│  2️⃣ Configura el decoder con el JWKSource                   │
│  3️⃣ Configura validadores:                                  │
│     ├─ Validador de firma (RSA)                             │
│     ├─ Validador de expiración                              │
│     └─ Validador de emisor                                  │
│  4️⃣ Retorna JwtDecoder listo para usar                      │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 🔍 Configuración Interna (Conceptual)

Si escribieras esto manualmente, sería algo así:

```java
@Bean
JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    // Configuración manual (NO NECESITAS HACER ESTO)
    var jwtDecoder = NimbusJwtDecoder.withJwkSetUri("/.well-known/jwks.json").build();
    
    // O usando el JWKSource directamente:
    var jwtDecoder = new NimbusJwtDecoder(new JWSVerificationKeySelector<>(
        JWSAlgorithm.RS256, 
        jwkSource
    ));
    
    return jwtDecoder;
}
```

✅ **Ventaja de usar el método de Spring**: Todo está preconfigurado y optimizado.

---

### 🔍 Proceso de Decodificación

#### 📊 Flujo Detallado

```
┌──────────────────────────────────────────────────────────────┐
│  1️⃣ Request con JWT                                          │
│     GET /accounts                                             │
│     Authorization: Bearer eyJhbGciOiJSUzI1NiI...             │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  2️⃣ JwtAuthenticationFilter intercepta                       │
│     └─ Extrae el token del header                            │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  3️⃣ JwtDecoder.decode(token) es llamado                      │
│                                                               │
│     ┌────────────────────────────────────────────────────┐   │
│     │  A. Decodifica Base64URL                           │   │
│     │     Header:  {"alg":"RS256","kid":"3f2e1a45..."}   │   │
│     │     Payload: {"sub":"account@...","exp":...}       │   │
│     │     Signature: [bytes de la firma]                 │   │
│     └────────────────────────────────────────────────────┘   │
│                                                               │
│     ┌────────────────────────────────────────────────────┐   │
│     │  B. Busca la clave pública                         │   │
│     │     ├─ Lee kid="3f2e1a45..." del header            │   │
│     │     ├─ Llama a jwkSource con selector(kid)         │   │
│     │     └─ Obtiene RSAPublicKey                        │   │
│     └────────────────────────────────────────────────────┘   │
│                                                               │
│     ┌────────────────────────────────────────────────────┐   │
│     │  C. Verifica la firma                              │   │
│     │     ├─ Recalcula: RS256(header + payload, pubKey)  │   │
│     │     └─ Compara con la firma del token              │   │
│     └────────────────────────────────────────────────────┘   │
│                                                               │
│     ┌────────────────────────────────────────────────────┐   │
│     │  D. Valida claims                                  │   │
│     │     ├─ exp > now() (no expirado)                   │   │
│     │     ├─ iss == expected issuer                      │   │
│     │     └─ aud == expected audience (opcional)         │   │
│     └────────────────────────────────────────────────────┘   │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  4️⃣ Si todo es válido ✅                                     │
│     └─ Retorna objeto Jwt con claims                         │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  5️⃣ JwtAuthenticationConverter convierte a Authentication    │
│     └─ Extrae roles y authorities                            │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  6️⃣ SecurityContextHolder guarda la autenticación           │
│     └─ Usuario autenticado puede acceder al recurso          │
└──────────────────────────────────────────────────────────────┘
```

---

#### ❌ Errores Posibles

| Error | Causa | Excepción |
|-------|-------|-----------|
| **🚫 Firma inválida** | Token modificado o clave incorrecta | `JwtException` |
| **⏰ Token expirado** | `exp` claim es anterior a la hora actual | `JwtException` |
| **🔑 Clave no encontrada** | `kid` del token no existe en JWKSource | `JwtException` |
| **📝 Formato inválido** | Token mal formado | `JwtException` |

---

### 🔄 Flujo Completo de Autenticación OAuth2

```
┌──────────────────────────────────────────────────────────────┐
│  👤 USUARIO                                                   │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   │ POST /oauth2/token
                   │ username + password
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🏛️ AUTHORIZATION SERVER (OAuth2 Filter Chain - Order 1)     │
│                                                               │
│  ├─ AuthenticationManager valida credenciales               │
│  ├─ UserDetailsService carga usuario de BD                  │
│  └─ PasswordEncoder verifica contraseña                     │
│                                                               │
│  ✅ Autenticación exitosa                                    │
│                                                               │
│  ├─ 🔑 JWKSource provee clave PRIVADA                        │
│  └─ 🔐 Firma JWT con RSAPrivateKey                           │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   │ Response:
                   │ { "access_token": "eyJhbGci..." }
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  👤 USUARIO recibe JWT Token                                 │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   │ GET /accounts
                   │ Authorization: Bearer eyJhbGci...
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🛡️ RESOURCE SERVER (Client Filter Chain - Order 2)          │
│                                                               │
│  ├─ JwtAuthenticationFilter intercepta request              │
│  ├─ Extrae token del header                                 │
│  │                                                            │
│  ├─ 🔓 JwtDecoder decodifica token                           │
│  │  ├─ 🔑 JWKSource provee clave PÚBLICA                     │
│  │  ├─ Verifica firma con RSAPublicKey                      │
│  │  └─ Valida expiración y claims                           │
│  │                                                            │
│  ├─ JwtAuthenticationConverter extrae authorities           │
│  └─ SecurityContextHolder guarda autenticación              │
│                                                               │
│  ✅ Token válido                                             │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   │ Response:
                   │ { "account": {...} }
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  👤 USUARIO recibe respuesta del recurso                     │
└──────────────────────────────────────────────────────────────┘
```

---

### 🎭 Roles: Authorization Server vs Resource Server

Tu aplicación actúa como **AMBOS** roles simultáneamente:

```
┌─────────────────────────────────────────────────────────────┐
│  🏢 TU APLICACIÓN SPRING BOOT                                │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  🏛️ AUTHORIZATION SERVER (Order 1)                   │   │
│  │                                                        │   │
│  │  Endpoints:                                           │   │
│  │  ├─ POST /oauth2/token (generar tokens)              │   │
│  │  ├─ GET  /oauth2/authorize (autorización)            │   │
│  │  └─ GET  /.well-known/jwks.json (claves públicas)    │   │
│  │                                                        │   │
│  │  Usa:                                                 │   │
│  │  ├─ 🔑 JWKSource (clave PRIVADA para firmar)         │   │
│  │  └─ UserDetailsService, PasswordEncoder              │   │
│  │                                                        │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  🛡️ RESOURCE SERVER (Order 2)                        │   │
│  │                                                        │   │
│  │  Endpoints:                                           │   │
│  │  ├─ GET /accounts (requiere authority "write")       │   │
│  │  ├─ GET /cards (requiere authority "write")          │   │
│  │  ├─ GET /loans (requiere authority "read")           │   │
│  │  └─ GET /balance (requiere authority "read")         │   │
│  │                                                        │   │
│  │  Usa:                                                 │   │
│  │  ├─ 🔓 JwtDecoder (verifica tokens)                  │   │
│  │  ├─ 🔑 JWKSource (clave PÚBLICA para verificar)      │   │
│  │  └─ JwtAuthenticationConverter                       │   │
│  │                                                        │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 📊 Tabla Comparativa

| Aspecto | Authorization Server | Resource Server |
|---------|---------------------|-----------------|
| **🎯 Propósito** | Generar tokens JWT | Validar tokens JWT |
| **🔑 Usa clave** | PRIVADA (firma) | PÚBLICA (verifica) |
| **🔧 Componentes** | `jwkSource()` | `jwtDecoder()`, `jwkSource()` |
| **📍 Endpoints** | `/oauth2/token`, `/oauth2/authorize` | `/accounts`, `/loans`, etc. |
| **🔒 Security Filter** | `OAuth2AuthorizationServerConfiguration` | `oauth2ResourceServer()` |
| **📝 Order** | `@Order(1)` | `@Order(2)` |

---

### 🧪 Ejemplo Real con tu Proyecto

#### 🔍 Caso de Uso Completo

**1️⃣ Usuario solicita token (Authorization Server)**

```http
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&
username=account@debuggeandoieas.com&
password=12345
```

**Procesamiento interno**:

```java
// SecurityConfig.java

@Bean
@Order(1)  // 🏛️ Este filter chain maneja /oauth2/**
SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    // ...
    return http.build();
}

// Internamente, el Authorization Server:
// 1. Valida credenciales con UserDetailsService
// 2. Obtiene clave PRIVADA de jwkSource()
// 3. Crea JWT con claims: { sub: "account@...", ROLES: "[VIEW_ACCOUNT]" }
// 4. Firma con RSAPrivateKey
```

**Response**:

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjNmMmUxYTQ1LThkN2MtNGI5ZS1hMWY2LTVjOGQyZTdmOWIzYSJ9.eyJzdWIiOiJhY2NvdW50QGRlYnVnZ2VhbmRvaWVhcy5jb20iLCJST0xFUyI6IltWSUVXX0FDQ09VTlRdIiwiaWF0IjoxNzA5MjU4NDAwLCJleHAiOjE3MDkyNjIwMDB9.signature...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

**2️⃣ Usuario accede a recurso (Resource Server)**

```http
GET http://localhost:8080/accounts
Authorization: Bearer eyJhbGciOiJSUzI1NiI...
```

**Procesamiento interno**:

```java
// SecurityConfig.java

@Bean
@Order(2)  // 🛡️ Este filter chain maneja recursos protegidos
SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) {
    http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/accounts/**").hasAuthority("write")  // ❌ Requiere "write"
                .anyRequest().permitAll());
    
    http.oauth2ResourceServer(oauth ->
            oauth.jwt(Customizer.withDefaults()));  // 🔓 Usa JwtDecoder
    
    return http.build();
}

// Flujo de validación:
// 1. JwtAuthenticationFilter extrae token
// 2. jwtDecoder() decodifica y valida:
//    ├─ Busca clave PÚBLICA en jwkSource() (kid="3f2e1a45...")
//    ├─ Verifica firma con RSAPublicKey
//    └─ Valida expiración
// 3. jwtAuthenticationConverter() extrae authorities:
//    ├─ Lee claim "ROLES": "[VIEW_ACCOUNT]"
//    └─ Convierte a authority: "VIEW_ACCOUNT"
// 4. Compara con .hasAuthority("write")
//    └─ "VIEW_ACCOUNT" ≠ "write" ❌ ACCESO DENEGADO
```

**Response**:

```json
{
  "timestamp": "2026-02-26T10:30:00.000Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/accounts"
}
```

❌ **Error**: El usuario tiene authority `"VIEW_ACCOUNT"` pero el endpoint requiere `"write"`.

---

#### ✅ Solución: Ajustar Authorities

**Opción 1: Cambiar la configuración**

```java
http.authorizeHttpRequests(auth ->
    auth.requestMatchers("/accounts/**").hasAuthority("VIEW_ACCOUNT")  // ✅ Coincide
        .anyRequest().permitAll());
```

**Opción 2: Agregar más authorities al usuario en la BD**

```sql
-- En tu base de datos
INSERT INTO roles (name, customer_id) VALUES ('write', 1);
```

---

### 🎓 Resumen para Estudiantes

```
┌─────────────────────────────────────────────────────────────┐
│  📚 CONCEPTOS CLAVE                                          │
│                                                               │
│  1️⃣ JWKSource es la "caja fuerte de claves"                 │
│     ├─ Almacena claves RSA en formato JWK                   │
│     ├─ Provee clave PRIVADA para firmar (Auth Server)       │
│     └─ Provee clave PÚBLICA para verificar (Resource Server)│
│                                                               │
│  2️⃣ jwkSource() retorna una lambda                          │
│     └─ (selector, ctx) -> selector.select(jwkSet)           │
│     └─ Permite buscar claves por kid                        │
│                                                               │
│  3️⃣ JwtDecoder valida tokens JWT                            │
│     ├─ Decodifica Base64URL                                 │
│     ├─ Verifica firma con clave pública                     │
│     ├─ Valida expiración                                    │
│     └─ Extrae claims                                        │
│                                                               │
│  4️⃣ jwtDecoder() usa el JWKSource inyectado                 │
│     └─ Spring conecta ambos beans automáticamente           │
│                                                               │
│  5️⃣ Tu app es AMBOS: Auth Server Y Resource Server          │
│     ├─ @Order(1): Genera tokens                             │
│     └─ @Order(2): Valida tokens                             │
│                                                               │
│  6️⃣ Flujo completo:                                         │
│     POST /oauth2/token → genera JWT (firma con privada)     │
│     GET /accounts → valida JWT (verifica con pública)       │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔗 Conexión entre Componentes

```
generateKeys()
     ⬇️
RSAKey (con publicKey + privateKey)
     ⬇️
jwkSource()
     ├─ new JWKSet(rsaKey)
     └─ lambda: (selector, ctx) -> selector.select(jwkSet)
     ⬇️
     ├──────────────────────┬──────────────────────┐
     ⬇️                     ⬇️                      ⬇️
Authorization Server   jwtDecoder()         Resource Server
(Firma tokens)         (Valida tokens)      (Protege endpoints)
Usa clave PRIVADA      Usa clave PÚBLICA    Requiere authorities
```

---

#### 💡 Analogía Final

```
🏦 Banco (Authorization Server)
├─ 🔐 Tiene máquina de sellar cheques (clave privada)
└─ Emite cheques firmados (tokens JWT)

🏪 Tienda (Resource Server)
├─ 🔓 Tiene lupa para verificar sellos (clave pública)
├─ Recibe cheques de clientes
├─ Verifica el sello con la lupa
└─ Si es válido, acepta el cheque

🔑 JWKSource
└─ La bóveda que guarda AMBAS:
   ├─ Máquina de sellar (para el banco)
   └─ Lupa de verificación (para la tienda)

📖 JwtDecoder
└─ El proceso de inspeccionar el cheque:
   ├─ Leer información
   ├─ Verificar firma/sello
   └─ Confirmar que no está vencido
```

---

## 📝 Clase 82  - AÑADIENDO PAYLOAD A NUESTRO JWT 👤🕵️‍♂🕵️‍♂🔑 🔑

```java

    OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claims(claim ->
                        claim.putAll(Map.of(
                                "owner",
                                APPLICATION_OWNER,
                                "data_request",
                                LocalDateTime.now().toString())));
            }
        };
    }


```

### 🎯 OAuth2TokenCustomizer - Agregando Claims Personalizados

### 📑 Índice
- [🎨 ¿Qué es OAuth2TokenCustomizer?](#-qué-es-oauth2tokencustomizer)
  - [📦 Propósito y Función](#-propósito-y-función)
  - [🔍 ¿Por qué Personalizar Tokens?](#-por-qué-personalizar-tokens)
- [🏗️ Análisis del Método oAuth2TokenCustomizer()](#️-análisis-del-método-oauth2tokencustomizer)
  - [🔍 Línea por Línea](#-línea-por-línea)
  - [🧩 Lambda Expressions Anidadas](#-lambda-expressions-anidadas)
  - [📊 Estructura del Context](#-estructura-del-context)
- [🔐 Tipos de Tokens OAuth2](#-tipos-de-tokens-oauth2)
- [📦 Claims: Información dentro del Token](#-claims-información-dentro-del-token)
- [🔄 Flujo de Personalización](#-flujo-de-personalización)
- [🧪 Ejemplo Real: Antes y Después](#-ejemplo-real-antes-y-después)
- [💡 Casos de Uso Prácticos](#-casos-de-uso-prácticos)
- [⚠️ Consideraciones de Seguridad](#️-consideraciones-de-seguridad)

---

### 🎨 ¿Qué es OAuth2TokenCustomizer?

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            context.getClaims().claims(claim ->
                    claim.putAll(Map.of(
                            "owner",
                            APPLICATION_OWNER,
                            "data_request",
                            LocalDateTime.now().toString())));
        }
    };
}
```

#### 📦 Propósito y Función

**`OAuth2TokenCustomizer`** es una interfaz funcional que permite **modificar el contenido (claims) de los tokens JWT** antes de que sean firmados y devueltos al cliente.

```
┌─────────────────────────────────────────────────────────────┐
│  🎨 OAuth2TokenCustomizer - Personalizador de Tokens        │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🎯 Responsabilidades                                │    │
│  │                                                       │    │
│  │  1️⃣ Interceptar el proceso de creación del token    │    │
│  │  2️⃣ Agregar claims personalizados al payload        │    │
│  │  3️⃣ Modificar claims existentes                     │    │
│  │  4️⃣ Condicionar cambios según el tipo de token      │    │
│  │                                                       │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔍 ¿Por qué Personalizar Tokens?

| Razón | Ejemplo |
|-------|---------|
| **📝 Metadatos** | Agregar información del emisor, versión de API |
| **⏰ Auditoría** | Registrar fecha/hora de emisión del token |
| **🏢 Información Corporativa** | Nombre de la empresa, departamento |
| **🌍 Geolocalización** | IP del cliente, región |
| **🔢 Identificadores** | Transaction ID, Request ID |
| **👤 Datos del Usuario** | Email, nombre completo, avatar |

#### 🎭 Analogía del Mundo Real

Imagina que estás **emitiendo un pasaporte**:

```
┌─────────────────────────────────────────────────────────────┐
│  🛂 Pasaporte Estándar (Token sin personalizar)             │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Nombre: Juan Pérez                                  │    │
│  │  Fecha de Nacimiento: 1990-01-01                     │    │
│  │  Fecha de Expiración: 2030-01-01                     │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                        ⬇️ PERSONALIZAR
┌─────────────────────────────────────────────────────────────┐
│  🛂 Pasaporte Personalizado (Token con customizer)          │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Nombre: Juan Pérez                                  │    │
│  │  Fecha de Nacimiento: 1990-01-01                     │    │
│  │  Fecha de Expiración: 2030-01-01                     │    │
│  │  ────────────────────────────────────────            │    │
│  │  🎨 INFORMACIÓN ADICIONAL:                           │    │
│  │  ✅ Emitido por: "Debugueando ideas"                 │    │
│  │  ✅ Fecha de emisión: "2026-02-26T10:30:00"          │    │
│  │  ✅ Oficina: "Quito, Ecuador"                        │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

### 🏗️ Análisis del Método oAuth2TokenCustomizer()

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            context.getClaims().claims(claim ->
                    claim.putAll(Map.of(
                            "owner",
                            APPLICATION_OWNER,
                            "data_request",
                            LocalDateTime.now().toString())));
        }
    };
}
```

---

### 🔍 Línea por Línea

#### **Línea 1-2: Declaración del Bean**

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
```

| Concepto | Explicación |
|----------|-------------|
| **@Bean** | Registra el customizer en el contexto de Spring |
| **OAuth2TokenCustomizer<T>** | Interfaz funcional genérica para personalizar tokens |
| **JwtEncodingContext** | Contexto específico para tokens JWT (contiene claims, headers, etc.) |
| **Tipo Genérico** | Indica que trabajaremos con el contexto de **codificación** de JWT |

#### 📌 Interfaz OAuth2TokenCustomizer

```java
@FunctionalInterface
public interface OAuth2TokenCustomizer<T extends OAuth2TokenContext> {
    void customize(T context);
}
```

Como es **@FunctionalInterface**, podemos usar una **lambda**:

```
Forma tradicional          →     Forma lambda
─────────────────────────────────────────────────
new OAuth2TokenCustomizer() {    context -> {
    @Override                        // código
    public void customize(...) {  }
        // código
    }
}
```

---

#### **Línea 3: Lambda Principal**

```java
return context -> {
```

| Elemento | Explicación |
|----------|-------------|
| **context** | Parámetro de tipo `JwtEncodingContext` |
| **->** | Operador lambda |
| **{ }** | Cuerpo del método `customize(context)` |

**¿Qué contiene `context`?**

```
┌─────────────────────────────────────────────────────────────┐
│  📦 JwtEncodingContext                                       │
│                                                               │
│  ├─ 🔑 TokenType: ACCESS_TOKEN / REFRESH_TOKEN              │
│  ├─ 📝 Claims: { sub, exp, iat, ... }                       │
│  ├─ 📄 Headers: { alg, kid, typ, ... }                      │
│  ├─ 👤 Principal: Información del usuario autenticado       │
│  ├─ 📋 RegisteredClient: Información del cliente OAuth2     │
│  └─ ⚙️ Authorization: Detalles de la autorización           │
└─────────────────────────────────────────────────────────────┘
```

---

#### **Línea 4: Validación del Tipo de Token**

```java
if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
```

| Concepto | Explicación |
|----------|-------------|
| **getTokenType()** | Obtiene el tipo de token que se está generando |
| **OAuth2TokenType.ACCESS_TOKEN** | Constante que representa el token de acceso |
| **Condición** | Solo personaliza **access tokens**, no refresh tokens |

#### 🔐 Tipos de Tokens OAuth2

```
┌─────────────────────────────────────────────────────────────┐
│  🔐 Tipos de Tokens en OAuth2                                │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🎫 ACCESS_TOKEN                                     │    │
│  │  ├─ Propósito: Acceder a recursos protegidos        │    │
│  │  ├─ Duración: Corta (minutos/horas)                 │    │
│  │  ├─ Contiene: sub, exp, roles, authorities          │    │
│  │  └─ Ejemplo: eyJhbGciOiJSUzI1NiI9...                │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🔄 REFRESH_TOKEN                                    │    │
│  │  ├─ Propósito: Obtener nuevos access tokens         │    │
│  │  ├─ Duración: Larga (días/semanas)                  │    │
│  │  ├─ Contiene: Menos información                     │    │
│  │  └─ No se envía en cada request                     │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🆔 ID_TOKEN (OpenID Connect)                        │    │
│  │  ├─ Propósito: Información de identidad del usuario │    │
│  │  ├─ Contiene: name, email, picture, etc.            │    │
│  │  └─ No se usa para autorización                     │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

#### ❓ ¿Por qué solo ACCESS_TOKEN?

```java
// ✅ Solo queremos agregar metadatos a los access tokens
if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
    // Agregar "owner" y "data_request"
}

// ❌ NO queremos modificar refresh tokens porque:
// - Son más duraderos y no necesitan metadatos temporales
// - No contienen información detallada del usuario
// - Solo se usan internamente para renovar access tokens
```

---

#### **Línea 5-9: Modificación de Claims**

```java
context.getClaims().claims(claim ->
        claim.putAll(Map.of(
                "owner",
                APPLICATION_OWNER,
                "data_request",
                LocalDateTime.now().toString())));
```

#### 🧩 Lambda Expressions Anidadas

Aquí tenemos **DOS lambdas anidadas**:

```
Lambda Externa (Customizer):
└─ context -> {
    
    Lambda Interna (Claims Modifier):
    └─ claim -> { ... }
}
```

#### 📊 Desglose Detallado

**1️⃣ context.getClaims()**

```java
context.getClaims()
```

| Método | Retorna | Explicación |
|--------|---------|-------------|
| **getClaims()** | `JwtClaimsSet.Builder` | Objeto constructor para modificar los claims del JWT |

**2️⃣ .claims(Consumer<Map<String, Object>>)**

```java
.claims(claim -> ...)
```

| Elemento | Tipo | Explicación |
|----------|------|-------------|
| **claims()** | Método que acepta un `Consumer` | Permite modificar el Map de claims |
| **claim** | Parámetro de tipo `Map<String, Object>` | El mapa de claims actual del token |
| **->** | Operador lambda | Define qué hacer con el mapa |

**3️⃣ claim.putAll(Map.of(...))**

```java
claim.putAll(Map.of(
        "owner",
        APPLICATION_OWNER,
        "data_request",
        LocalDateTime.now().toString()))
```

| Método | Parámetros | Explicación |
|--------|------------|-------------|
| **putAll()** | `Map<String, Object>` | Agrega **todos** los entries del mapa al claims set |
| **Map.of()** | Pares clave-valor | Crea un Map inmutable de forma concisa (Java 9+) |

---

#### 📦 Claims: Información dentro del Token

**Claims agregados en tu código:**

```java
Map.of(
    "owner", APPLICATION_OWNER,              // "Debugueando ideas"
    "data_request", LocalDateTime.now()      // "2026-02-26T10:30:45.123"
)
```

| Claim | Tipo | Valor | Propósito |
|-------|------|-------|-----------|
| **owner** | `String` | `"Debugueando ideas"` | Identifica quién emitió el token |
| **data_request** | `String` | `"2026-02-26T10:30:45.123"` | Timestamp exacto de cuando se generó el token |

#### 📝 Constante APPLICATION_OWNER

En tu `SecurityConfig.java`:

```java
private static final String APPLICATION_OWNER = "Debugueando ideas";
```

---

### 📊 Estructura del Context

#### 🔍 JwtEncodingContext Completo

```
┌─────────────────────────────────────────────────────────────┐
│  📦 JwtEncodingContext                                       │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  getTokenType()                                      │    │
│  │  └─ ACCESS_TOKEN                                     │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  getClaims() ➡️ JwtClaimsSet.Builder                │    │
│  │  ├─ sub: "account@debuggeandoieas.com"              │    │
│  │  ├─ iss: "http://localhost:8080"                    │    │
│  │  ├─ aud: ["client-id"]                              │    │
│  │  ├─ exp: 1709262000                                 │    │
│  │  ├─ iat: 1709258400                                 │    │
│  │  ├─ ROLES: ["VIEW_ACCOUNT"]                         │    │
│  │  ├─ owner: "Debugueando ideas" ⬅️ AGREGADO          │    │
│  │  └─ data_request: "2026-02-26T10:30:45" ⬅️ AGREGADO │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  getHeaders()                                        │    │
│  │  ├─ alg: "RS256"                                     │    │
│  │  ├─ kid: "3f2e1a45-8d7c..."                         │    │
│  │  └─ typ: "JWT"                                       │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  getPrincipal()                                      │    │
│  │  └─ UsernamePasswordAuthenticationToken             │    │
│  │     └─ username: "account@debuggeandoieas.com"      │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

### 🔐 Tipos de Tokens OAuth2

#### 📊 Comparación Detallada

| Característica | ACCESS_TOKEN | REFRESH_TOKEN |
|----------------|--------------|---------------|
| **🎯 Propósito** | Acceder a recursos | Renovar access tokens |
| **⏰ Duración** | 15-60 minutos | 7-30 días |
| **📦 Claims** | Muchos (roles, permisos, metadatos) | Pocos (solo identificadores) |
| **🔒 Seguridad** | Se envía en cada request | Solo en endpoint de refresh |
| **🎨 Personalización** | ✅ Sí (tu código) | ❌ Generalmente no |
| **📍 Uso** | Header `Authorization: Bearer ...` | Cuerpo de request de refresh |

#### 🔄 Flujo de Tokens

```
┌──────────────────────────────────────────────────────────────┐
│  1️⃣ Usuario se autentica                                     │
│     POST /oauth2/token                                        │
│     username + password                                       │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  2️⃣ Authorization Server genera AMBOS tokens                 │
│                                                               │
│     🎫 ACCESS_TOKEN                                           │
│     ├─ OAuth2TokenCustomizer SE EJECUTA ✅                   │
│     ├─ Se agregan claims personalizados                      │
│     └─ Token final: eyJhbGciOiJSUzI1NiI9...                  │
│                                                               │
│     🔄 REFRESH_TOKEN                                          │
│     ├─ OAuth2TokenCustomizer NO se ejecuta ❌                │
│     └─ Token simple sin metadatos extra                      │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  3️⃣ Cliente recibe ambos                                     │
│     {                                                         │
│       "access_token": "eyJhbGci...",                          │
│       "refresh_token": "eyJhbGci...",                         │
│       "expires_in": 3600                                      │
│     }                                                         │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  4️⃣ Cliente usa ACCESS_TOKEN para acceder a recursos        │
│     GET /accounts                                             │
│     Authorization: Bearer eyJhbGci...                         │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  5️⃣ Cuando expira, usa REFRESH_TOKEN para renovar           │
│     POST /oauth2/token                                        │
│     grant_type=refresh_token&refresh_token=...               │
└──────────────────────────────────────────────────────────────┘
```

---

### 🔄 Flujo de Personalización

```
┌──────────────────────────────────────────────────────────────┐
│  👤 Usuario solicita token                                    │
│     POST /oauth2/token                                        │
│     username=account@debuggeandoieas.com                      │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🔐 AuthenticationManager valida credenciales ✅             │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🏭 OAuth2TokenGenerator comienza a crear el token           │
│                                                               │
│  Paso 1: Crea claims estándar                                │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  {                                                   │    │
│  │    "sub": "account@debuggeandoieas.com",            │    │
│  │    "iss": "http://localhost:8080",                  │    │
│  │    "iat": 1709258400,                               │    │
│  │    "exp": 1709262000,                               │    │
│  │    "ROLES": ["VIEW_ACCOUNT"]                        │    │
│  │  }                                                   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🎨 OAuth2TokenCustomizer SE EJECUTA                         │
│                                                               │
│  context.getTokenType() == ACCESS_TOKEN? ➡️ SÍ ✅            │
│                                                               │
│  context.getClaims().claims(claim -> {                       │
│      claim.putAll(Map.of(                                    │
│          "owner", "Debugueando ideas",                       │
│          "data_request", "2026-02-26T10:30:45.123"           │
│      ))                                                       │
│  })                                                           │
│                                                               │
│  Claims después de personalizar:                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  {                                                   │    │
│  │    "sub": "account@debuggeandoieas.com",            │    │
│  │    "iss": "http://localhost:8080",                  │    │
│  │    "iat": 1709258400,                               │    │
│  │    "exp": 1709262000,                               │    │
│  │    "ROLES": ["VIEW_ACCOUNT"],                       │    │
│  │    "owner": "Debugueando ideas",        ⬅️ NUEVO    │    │
│  │    "data_request": "2026-02-26T10:30:45" ⬅️ NUEVO   │    │
│  │  }                                                   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  🔐 JWT es firmado con RSAPrivateKey                         │
└──────────────────┬───────────────────────────────────────────┘
                   ⬇️
┌──────────────────────────────────────────────────────────────┐
│  📤 Token devuelto al cliente                                │
│     {                                                         │
│       "access_token": "eyJhbGci...con claims personalizados" │
│     }                                                         │
└──────────────────────────────────────────────────────────────┘
```

---

### 🧪 Ejemplo Real: Antes y Después

#### ❌ SIN OAuth2TokenCustomizer

**Token generado (decodificado)**:

```json
{
  "alg": "RS256",
  "kid": "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a",
  "typ": "JWT"
}
.
{
  "sub": "account@debuggeandoieas.com",
  "iss": "http://localhost:8080",
  "aud": ["client-app"],
  "exp": 1709262000,
  "iat": 1709258400,
  "ROLES": ["VIEW_ACCOUNT"]
}
.
[signature]
```

**Claims disponibles**: `6 claims estándar`

---

#### ✅ CON OAuth2TokenCustomizer (tu código)

**Token generado (decodificado)**:

```json
{
  "alg": "RS256",
  "kid": "3f2e1a45-8d7c-4b9e-a1f6-5c8d2e7f9b3a",
  "typ": "JWT"
}
.
{
  "sub": "account@debuggeandoieas.com",
  "iss": "http://localhost:8080",
  "aud": ["client-app"],
  "exp": 1709262000,
  "iat": 1709258400,
  "ROLES": ["VIEW_ACCOUNT"],
  "owner": "Debugueando ideas",           ⬅️ NUEVO
  "data_request": "2026-02-26T10:30:45.123"  ⬅️ NUEVO
}
.
[signature]
```

**Claims disponibles**: `8 claims (6 estándar + 2 personalizados)`

---

#### 🔍 Decodificando el Token en tu Aplicación

```java
// En tu Resource Server, cuando llega un request:

@GetMapping("/accounts")
public ResponseEntity<?> getAccounts(@AuthenticationPrincipal Jwt jwt) {
    
    // Claims estándar
    String username = jwt.getSubject();  // "account@debuggeandoieas.com"
    List<String> roles = jwt.getClaimAsStringList("ROLES");  // ["VIEW_ACCOUNT"]
    
    // Claims personalizados ✨
    String owner = jwt.getClaimAsString("owner");  // "Debugueando ideas"
    String requestTime = jwt.getClaimAsString("data_request");  // "2026-02-26T10:30:45"
    
    log.info("Request from {} at {} by {}", username, requestTime, owner);
    
    // Lógica de negocio...
    return ResponseEntity.ok(accountService.findAll());
}
```

---

### 💡 Casos de Uso Prácticos

#### 🎯 Ejemplos de Personalización

**1️⃣ Información de Auditoría**

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> auditTokenCustomizer() {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            context.getClaims().claims(claim ->
                claim.putAll(Map.of(
                    "ip_address", request.getRemoteAddr(),
                    "user_agent", request.getHeader("User-Agent"),
                    "session_id", UUID.randomUUID().toString()
                ))
            );
        }
    };
}
```

**Token resultante**:
```json
{
  "sub": "user@example.com",
  "ip_address": "192.168.1.100",
  "user_agent": "Mozilla/5.0...",
  "session_id": "7f8e9d6c-5b4a-3c2d-1e0f-9a8b7c6d5e4f"
}
```

---

**2️⃣ Información del Tenant (Multi-tenancy)**

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> tenantCustomizer() {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            var principal = context.getPrincipal();
            var user = (CustomUserDetails) principal.getPrincipal();
            
            context.getClaims().claims(claim ->
                claim.putAll(Map.of(
                    "tenant_id", user.getTenantId(),
                    "organization", user.getOrganizationName(),
                    "department", user.getDepartment()
                ))
            );
        }
    };
}
```

**Token resultante**:
```json
{
  "sub": "user@company.com",
  "tenant_id": "TENANT_001",
  "organization": "Acme Corp",
  "department": "Engineering"
}
```

---

**3️⃣ Permisos Granulares**

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> permissionsCustomizer() {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            var authorities = context.getPrincipal().getAuthorities();
            var permissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            
            context.getClaims().claims(claim ->
                claim.put("permissions", permissions)
            );
        }
    };
}
```

**Token resultante**:
```json
{
  "sub": "user@example.com",
  "permissions": [
    "account:read",
    "account:write",
    "user:read"
  ]
}
```

---

**4️⃣ Metadata del Usuario**

```java
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> userMetadataCustomizer(UserRepository userRepo) {
    return context -> {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            var username = context.getPrincipal().getName();
            var user = userRepo.findByEmail(username).orElseThrow();
            
            context.getClaims().claims(claim ->
                claim.putAll(Map.of(
                    "full_name", user.getFirstName() + " " + user.getLastName(),
                    "email", user.getEmail(),
                    "avatar_url", user.getAvatarUrl(),
                    "locale", user.getPreferredLanguage()
                ))
            );
        }
    };
}
```

**Token resultante**:
```json
{
  "sub": "user@example.com",
  "full_name": "Juan Pérez",
  "email": "user@example.com",
  "avatar_url": "https://cdn.example.com/avatars/user123.jpg",
  "locale": "es_ES"
}
```

---

### ⚠️ Consideraciones de Seguridad

#### 🔒 Buenas Prácticas

| ✅ Hacer | ❌ NO Hacer |
|----------|-------------|
| Agregar metadatos útiles y seguros | Agregar información sensible (passwords, API keys) |
| Limitar el tamaño del token | Agregar demasiados claims (>4KB) |
| Usar claims estándar cuando sea posible | Inventar nombres de claims ambiguos |
| Validar datos antes de agregarlos | Confiar en datos del cliente sin validar |
| Agregar timestamps para auditoría | Exponer rutas internas del sistema |

---

#### ⚠️ Problemas de Tamaño del Token

```
┌─────────────────────────────────────────────────────────────┐
│  📏 Tamaño del Token                                         │
│                                                               │
│  Token pequeño (6 claims):                                   │
│  └─ ~300-500 bytes                                           │
│     └─ ✅ Rápido de transmitir                               │
│                                                               │
│  Token mediano (15 claims):                                  │
│  └─ ~800-1500 bytes                                          │
│     └─ ⚠️ Aceptable                                          │
│                                                               │
│  Token grande (50+ claims):                                  │
│  └─ ~4000+ bytes                                             │
│     └─ ❌ Problemas:                                         │
│        ├─ Lento de transmitir                               │
│        ├─ Puede exceder límites de headers HTTP             │
│        └─ Mayor carga en el servidor                        │
└─────────────────────────────────────────────────────────────┘
```

**Límite recomendado**: **< 2KB** por token

---

#### 🔐 Información Sensible

```java
// ❌ NUNCA HACER ESTO:
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> badCustomizer() {
    return context -> {
        context.getClaims().claims(claim ->
            claim.putAll(Map.of(
                "password", user.getPassword(),           // ❌ Contraseña
                "credit_card", user.getCreditCard(),      // ❌ Datos financieros
                "ssn", user.getSocialSecurityNumber(),    // ❌ Información PII
                "api_secret", env.getProperty("secret")   // ❌ Secretos
            ))
        );
    };
}
```

**¿Por qué es malo?**
- El JWT es **Base64 encoded**, no encriptado
- Cualquiera puede decodificar y leer el contenido
- Los tokens se transmiten en headers HTTP

```
Token JWT:
eyJhbGciOiJSUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1In0.signature

Decodificado (fácil):
{"password": "12345"}  ⬅️ ¡Expuesto!
```

---

#### ✅ Alternativa Segura

En lugar de poner datos sensibles en el token:

```java
// ✅ MEJOR: Solo incluir el identificador
@Bean
OAuth2TokenCustomizer<JwtEncodingContext> secureCustomizer() {
    return context -> {
        context.getClaims().claims(claim ->
            claim.put("user_id", user.getId())  // Solo el ID
        );
    };
}

// En el Resource Server, cuando necesitas más datos:
@GetMapping("/profile")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt) {
    Long userId = jwt.getClaim("user_id");
    User user = userService.findById(userId);  // Consulta a BD
    return ResponseEntity.ok(user);
}
```

---

### 🎓 Resumen para Estudiantes

```
┌─────────────────────────────────────────────────────────────┐
│  📚 CONCEPTOS CLAVE                                          │
│                                                               │
│  1️⃣ OAuth2TokenCustomizer personaliza tokens JWT            │
│     └─ Agrega claims personalizados al payload              │
│                                                               │
│  2️⃣ Se ejecuta ANTES de firmar el token                     │
│     └─ Los claims se agregan a la estructura del JWT        │
│                                                               │
│  3️⃣ Usa lambdas anidadas                                    │
│     context -> { ... claims(claim -> { ... }) }             │
│                                                               │
│  4️⃣ Solo modifica ACCESS_TOKEN                              │
│     └─ Validación: context.getTokenType() == ACCESS_TOKEN   │
│                                                               │
│  5️⃣ En tu código agregas 2 claims:                          │
│     ├─ "owner": "Debugueando ideas"                         │
│     └─ "data_request": timestamp actual                     │
│                                                               │
│  6️⃣ Usos comunes:                                           │
│     ├─ Auditoría (timestamps, IPs)                          │
│     ├─ Multi-tenancy (tenant_id, org)                       │
│     ├─ Metadata del usuario (nombre, avatar)                │
│     └─ Permisos granulares                                  │
│                                                               │
│  ⚠️ NUNCA agregar información sensible                       │
│     └─ El JWT es decodificable (no encriptado)              │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔗 Integración con otros Beans

```
UserDetailsService
     ⬇️
AuthenticationManager (valida credenciales)
     ⬇️
OAuth2TokenGenerator (comienza a crear token)
     ⬇️
OAuth2TokenCustomizer ⬅️ TU CÓDIGO (agrega claims)
     ⬇️
JWKSource (provee clave privada)
     ⬇️
Token JWT firmado (enviado al cliente)
     ⬇️
Cliente usa token en requests
     ⬇️
JwtDecoder (valida y decodifica)
     ⬇️
Resource Server lee claims personalizados
```

---

#### 💡 Analogía Final

```
🏭 Fábrica de Pasaportes (Token Generation)

1️⃣ Cliente solicita pasaporte
   └─ Proporciona identificación (username/password)

2️⃣ Oficial verifica identidad (AuthenticationManager)
   └─ Consulta base de datos de ciudadanos

3️⃣ Impresora crea pasaporte base (OAuth2TokenGenerator)
   ├─ Nombre: Juan Pérez
   ├─ Fecha de nacimiento: 1990-01-01
   └─ Fecha de expiración: 2030-01-01

4️⃣ 🎨 Departamento de personalización (OAuth2TokenCustomizer)
   └─ Agrega sellos especiales:
      ├─ "Emitido por: Debugueando ideas"
      └─ "Fecha de emisión: 2026-02-26"

5️⃣ Oficial sella con sello oficial (Firma RSA)
   └─ Garantiza autenticidad

6️⃣ Pasaporte entregado al ciudadano
   └─ Puede usarlo para viajar (acceder a recursos)
```

---

## 📝 Clase 83  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 84  - 👤🕵️‍♂🕵️‍♂🔑 🔑


---

## 📝 Clase 85  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 86  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 87  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 88  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 89  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 90  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 91 - ¿Se puede tener roles y authorities? 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 92 limpianzo codigo  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 96 - 🐛 ERROR COMÚN: GRAND_TYPES VS GRANT_TYPES 🔍

### ⚠️ Error de Sincronización entre Entidad JPA y Base de Datos

### 📑 Índice
- [🐛 El Error](#-el-error)
    - [📄 Mensaje de Error](#-mensaje-de-error)
    - [🔍 Análisis del Problema](#-análisis-del-problema)
- [🎯 Causa Raíz](#-causa-raíz)
- [✅ Solución](#-solución)
- [🔄 Verificación de Cambios](#-verificación-de-cambios)
- [📚 Conceptos Importantes](#-conceptos-importantes)
- [💡 Cómo Prevenir Este Error](#-cómo-prevenir-este-error)

---

### 🐛 El Error

#### 📄 Mensaje de Error

```
2026-02-26T22:31:29.431-05:00 ERROR 25802 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : 
ERROR: column p1_0.grand_types does not exist   
  Hint: Perhaps you meant to reference the column "p1_0.grant_types".   
  Position: 95

org.springframework.dao.InvalidDataAccessResourceUsageException: 
JDBC exception executing SQL [
  select p1_0.id,
         p1_0.authentication_methods,
         p1_0.client_id,
         p1_0.client_name,
         p1_0.client_secret,
         p1_0.grand_types,  ⬅️ ❌ ERROR AQUÍ
         p1_0.redirect_uri,
         p1_0.redirect_uri_logout,
         p1_0.scopes 
  from partners p1_0 
  where p1_0.client_id=?
] 
[ERROR: column p1_0.grand_types does not exist
```

---

#### 🔍 Análisis del Problema

```
┌─────────────────────────────────────────────────────────────┐
│  🔍 Análisis del Error                                       │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  🗄️ BASE DE DATOS (PostgreSQL)                      │    │
│  │                                                       │    │
│  │  CREATE TABLE partners (                             │    │
│  │      ...                                              │    │
│  │      grant_types varchar(256),  ⬅️ ✅ CORRECTO      │    │
│  │      ...                                              │    │
│  │  );                                                   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
│                        ❌ NO COINCIDE                         │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  ☕ ENTIDAD JPA (PartnerEntity.java)                 │    │
│  │                                                       │    │
│  │  @Entity                                             │    │
│  │  public class PartnerEntity {                        │    │
│  │      ...                                              │    │
│  │      private String grandTypes;  ⬅️ ❌ ERROR        │    │
│  │      ...                                              │    │
│  │  }                                                    │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

### 🎯 Causa Raíz

#### 🔤 Error de Tipeo (Typo)

| Palabra | Significado | Uso en OAuth2 |
|---------|-------------|---------------|
| **grant** ✅ | "conceder", "otorgar" | Tipo de concesión/autorización |
| **grand** ❌ | "grande", "grandioso" | No tiene relación con OAuth2 |

#### 📖 Terminología OAuth2

En OAuth2, **"Authorization Grant Type"** significa **"Tipo de Concesión de Autorización"**:

```
┌─────────────────────────────────────────────────────────────┐
│  🔐 Authorization Grant Types (Tipos de Concesión)          │
│                                                               │
│  1️⃣ authorization_code                                      │
│     └─ Código de autorización (más seguro)                  │
│                                                               │
│  2️⃣ client_credentials                                      │
│     └─ Credenciales del cliente (server-to-server)          │
│                                                               │
│  3️⃣ password (Resource Owner Password)                      │
│     └─ Usuario y contraseña directamente                    │
│                                                               │
│  4️⃣ refresh_token                                           │
│     └─ Renovación de tokens                                 │
│                                                               │
│  5️⃣ implicit (deprecated)                                   │
│     └─ Token directo (ya no se recomienda)                  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

La palabra correcta es **"grant"** (conceder), no **"grand"** (grande).

---

### ✅ Solución

#### 🔧 Cambios Realizados

**1️⃣ Corrección en PartnerEntity.java**

**❌ ANTES (Incorrecto)**:
```java
@Entity
@Table(name = "partners")
@Data
public class PartnerEntity {
    private BigInteger id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String scopes;
    private String grandTypes;  // ❌ ERROR: "grand"
    private String authenticationMethods;
    private String redirectUri;
    private String redirectUriLogout;
}
```

**✅ DESPUÉS (Correcto)**:
```java
@Entity
@Table(name = "partners")
@Data
public class PartnerEntity {
    private BigInteger id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String scopes;
    private String grantTypes;  // ✅ CORRECTO: "grant"
    private String authenticationMethods;
    private String redirectUri;
    private String redirectUriLogout;
}
```

---

**2️⃣ Corrección en PartnerRegisteredClientService.java**

**❌ ANTES (Incorrecto)**:
```java
@Service
@AllArgsConstructor
public class PartnerRegisteredClientService implements RegisteredClientRepository {

    private final PartnerRepository partnerRepository;

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var partnerOpt = this.partnerRepository.findByClientId(clientId);

        return partnerOpt.map(partner -> {
            // ❌ ERROR: getGrandTypes() y authorizationGranTypes
            var authorizationGranTypes = Arrays.stream(partner.getGrandTypes().split(","))
                    .map(AuthorizationGrantType::new)
                    .toList();

            // ...
            
            return RegisteredClient
                    .withId(partner.getId().toString())
                    .clientId(partner.getClientId())
                    // ...
                    .authorizationGrantType(authorizationGranTypes.get(0))  // ❌
                    .authorizationGrantType(authorizationGranTypes.get(1))  // ❌
                    .build();
        }).orElseThrow(() -> new BadCredentialsException("Client no exists"));
    }
}
```

**✅ DESPUÉS (Correcto)**:
```java
@Service
@AllArgsConstructor
public class PartnerRegisteredClientService implements RegisteredClientRepository {

    private final PartnerRepository partnerRepository;

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var partnerOpt = this.partnerRepository.findByClientId(clientId);

        return partnerOpt.map(partner -> {
            // ✅ CORRECTO: getGrantTypes() y authorizationGrantTypes
            var authorizationGrantTypes = Arrays.stream(partner.getGrantTypes().split(","))
                    .map(AuthorizationGrantType::new)
                    .toList();

            // ...
            
            return RegisteredClient
                    .withId(partner.getId().toString())
                    .clientId(partner.getClientId())
                    // ...
                    .authorizationGrantType(authorizationGrantTypes.get(0))  // ✅
                    .authorizationGrantType(authorizationGrantTypes.get(1))  // ✅
                    .build();
        }).orElseThrow(() -> new BadCredentialsException("Client no exists"));
    }
}
```

---

### 🔄 Verificación de Cambios

#### ✅ Checklist de Corrección

```
┌─────────────────────────────────────────────────────────────┐
│  ✅ Cambios Aplicados                                        │
│                                                               │
│  ☑️  1. Campo en entidad: grandTypes → grantTypes           │
│  ☑️  2. Getter en servicio: getGrandTypes() → getGrantTypes()│
│  ☑️  3. Variable local: authorizationGranTypes → ...GrantTypes│
│  ☑️  4. Nombres consistentes en todo el código              │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 🧪 Prueba de Funcionamiento

Después de realizar los cambios, la aplicación debería:

1. ✅ Compilar sin errores
2. ✅ Conectarse a la base de datos correctamente
3. ✅ Ejecutar la query SQL sin problemas:

```sql
SELECT p1_0.id,
       p1_0.authentication_methods,
       p1_0.client_id,
       p1_0.client_name,
       p1_0.client_secret,
       p1_0.grant_types,  -- ✅ Ahora coincide con la BD
       p1_0.redirect_uri,
       p1_0.redirect_uri_logout,
       p1_0.scopes 
FROM partners p1_0 
WHERE p1_0.client_id = ?
```

---

### 📚 Conceptos Importantes

#### 🔤 Convenciones de Nombres en JPA

JPA utiliza una **estrategia de nombres** (naming strategy) para mapear campos Java a columnas SQL:

```
┌─────────────────────────────────────────────────────────────┐
│  🔄 Mapeo de Nombres: Java ↔️ SQL                            │
│                                                               │
│  Java (camelCase)          →    SQL (snake_case)            │
│  ─────────────────────────────────────────────────────────  │
│                                                               │
│  grantTypes               →    grant_types                  │
│  clientId                 →    client_id                    │
│  redirectUri              →    redirect_uri                 │
│  authenticationMethods    →    authentication_methods       │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 📝 Estrategias de Nombres

**Spring Boot por defecto usa**:
- **Java**: `camelCase` (primera letra minúscula)
- **SQL**: `snake_case` (palabras separadas por guión bajo)

**Ejemplo en tu entidad**:

```java
@Entity
@Table(name = "partners")
public class PartnerEntity {
    
    // Java: grantTypes → SQL: grant_types (automático)
    private String grantTypes;
    
    // Si quieres especificar manualmente:
    @Column(name = "grant_types")
    private String grantTypes;
    
    // Puedes usar nombre diferente (NO recomendado):
    @Column(name = "grant_types")
    private String tiposDeAutorizacion;  // Confuso, evitar
}
```

---

#### 🎯 Lombok y Getters/Setters

Como usas `@Data` de Lombok, los métodos se generan automáticamente:

```java
@Data  // Genera getters, setters, toString, equals, hashCode
public class PartnerEntity {
    private String grantTypes;
    
    // Lombok genera automáticamente:
    // public String getGrantTypes() { return grantTypes; }
    // public void setGrantTypes(String grantTypes) { this.grantTypes = grantTypes; }
}
```

**⚠️ Advertencia**: Si el nombre del campo está mal (`grandTypes`), Lombok genera `getGrandTypes()`, que no coincide con la columna `grant_types`.

---

### 💡 Cómo Prevenir Este Error

#### 🛡️ Buenas Prácticas

**1️⃣ Verificar la Ortografía**

| ✅ Hacer | ❌ Evitar |
|----------|-----------|
| Revisar nombres de columnas en el script SQL | Copiar/pegar sin revisar |
| Usar nombres en inglés correctamente | Mezclar idiomas (grantTipos) |
| Consultar documentación OAuth2 | Inventar nombres |

---

**2️⃣ Usar Anotación @Column**

```java
@Entity
@Table(name = "partners")
public class PartnerEntity {
    
    // ✅ Explícito: evita confusiones
    @Column(name = "grant_types")
    private String grantTypes;
    
    @Column(name = "client_id")
    private String clientId;
    
    @Column(name = "redirect_uri")
    private String redirectUri;
}
```

**Ventajas**:
- ✅ Mapeo explícito y claro
- ✅ Fácil de revisar
- ✅ Si cambias el nombre en Java, la anotación lo mantiene consistente

---

**3️⃣ Sincronizar con la Base de Datos**

Antes de crear la entidad, revisa el esquema:

```sql
-- 1️⃣ Verifica las columnas en PostgreSQL
\d partners

-- O ejecuta:
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'partners';
```

Resultado esperado:
```
 column_name           | data_type
-----------------------|----------------
 id                    | bigint
 client_id             | varchar(256)
 client_name           | varchar(256)
 client_secret         | varchar(256)
 scopes                | varchar(256)
 grant_types           | varchar(256)  ⬅️ Aquí está
 authentication_methods| varchar(256)
 redirect_uri          | varchar(256)
 redirect_uri_logout   | varchar(256)
```

---

**4️⃣ Usar IDEs con Autocompletado**

Los IDEs modernos (IntelliJ IDEA, Eclipse) pueden:
- ✅ Sugerir nombres de columnas desde la BD
- ✅ Validar que coincidan con el esquema
- ✅ Generar entidades automáticamente

**En IntelliJ IDEA**:
```
1. View → Tool Windows → Database
2. Conecta a tu PostgreSQL
3. Click derecho en tabla "partners"
4. Generate Persistence Mapping → JPA Entity
```

---

**5️⃣ Pruebas Unitarias**

```java
@SpringBootTest
class PartnerEntityTest {
    
    @Autowired
    private PartnerRepository partnerRepository;
    
    @Test
    void testFindByClientId() {
        // Esto fallará si los nombres no coinciden
        var partner = partnerRepository.findByClientId("test-client");
        
        assertNotNull(partner);
        assertNotNull(partner.getGrantTypes());  // ✅ Verifica getter
    }
}
```

---

#### 🔍 Herramientas de Debugging

**1️⃣ Activar Logs de Hibernate**

En `application.properties`:

```properties
# Ver las queries SQL generadas
spring.jpa.show-sql=true

# Formatear SQL para mejor lectura
spring.jpa.properties.hibernate.format_sql=true

# Ver los parámetros de las queries
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

**Output esperado**:
```sql
Hibernate: 
    select
        p1_0.id,
        p1_0.authentication_methods,
        p1_0.client_id,
        p1_0.client_name,
        p1_0.client_secret,
        p1_0.grant_types,  ⬅️ Puedes verificar el nombre
        p1_0.redirect_uri,
        p1_0.redirect_uri_logout,
        p1_0.scopes 
    from
        partners p1_0 
    where
        p1_0.client_id=?
```

---

**2️⃣ Validar el Esquema al Iniciar**

```properties
# En desarrollo: valida que la entidad coincida con la BD
spring.jpa.hibernate.ddl-auto=validate

# Si hay diferencias, la app NO iniciará y verás el error
```

**Configuraciones posibles**:

| Opción | Comportamiento |
|--------|----------------|
| **validate** | Solo valida, no modifica la BD (producción) |
| **update** | Actualiza la BD si hay cambios (desarrollo) |
| **create** | Recrea las tablas al iniciar (testing) |
| **create-drop** | Crea al iniciar, elimina al cerrar (testing) |
| **none** | No hace nada (manual) |

---

### 🎓 Resumen para Estudiantes

```
┌─────────────────────────────────────────────────────────────┐
│  📚 CONCEPTOS CLAVE                                          │
│                                                               │
│  1️⃣ Error: grandTypes vs grantTypes                         │
│     └─ "grant" = conceder (OAuth2)                          │
│     └─ "grand" = grande (error de tipeo)                    │
│                                                               │
│  2️⃣ JPA mapea automáticamente:                              │
│     └─ Java camelCase → SQL snake_case                      │
│     └─ grantTypes → grant_types                             │
│                                                               │
│  3️⃣ Lombok genera getters/setters:                          │
│     └─ private String grantTypes;                           │
│     └─ → public String getGrantTypes()                      │
│                                                               │
│  4️⃣ Solución:                                               │
│     ├─ Cambiar campo en entidad                             │
│     ├─ Actualizar uso en servicios                          │
│     └─ Verificar que compile sin errores                    │
│                                                               │
│  5️⃣ Prevención:                                             │
│     ├─ Usar @Column(name="...") explícito                   │
│     ├─ Revisar nombres en SQL antes                         │
│     ├─ Activar validación en Spring Boot                    │
│     └─ Escribir pruebas unitarias                           │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

#### 🔄 Flujo del Error

```
1️⃣ Hibernate genera SQL basándose en la entidad Java
   └─ Lee: private String grandTypes;
   └─ Convierte: grand_types (snake_case)

2️⃣ Ejecuta query contra PostgreSQL
   └─ SELECT ... p1_0.grand_types ...

3️⃣ PostgreSQL responde con error
   └─ ❌ ERROR: column p1_0.grand_types does not exist
   └─ 💡 Hint: Perhaps you meant "p1_0.grant_types"

4️⃣ Spring lanza excepción
   └─ InvalidDataAccessResourceUsageException

5️⃣ Tu aplicación falla
   └─ Request devuelve error 500
```

---

#### ✅ Flujo Correcto (Después de la Corrección)

```
1️⃣ Hibernate genera SQL basándose en la entidad corregida
   └─ Lee: private String grantTypes;
   └─ Convierte: grant_types (snake_case)

2️⃣ Ejecuta query contra PostgreSQL
   └─ SELECT ... p1_0.grant_types ...

3️⃣ PostgreSQL encuentra la columna ✅
   └─ Retorna: "authorization_code,refresh_token"

4️⃣ Hibernate mapea el resultado a la entidad
   └─ partner.getGrantTypes() = "authorization_code,refresh_token"

5️⃣ Tu código procesa el valor correctamente
   └─ Arrays.stream(partner.getGrantTypes().split(","))
   └─ ["authorization_code", "refresh_token"]

6️⃣ RegisteredClient se construye exitosamente ✅
```

---





