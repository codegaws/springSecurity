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
# 🔐 AuthenticationProvider y AuthorizationServerSettings

## 📑 Índice
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

## 🔍 AuthenticationProvider

### 🎯 ¿Qué es un AuthenticationProvider?

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

## ⚙️ AuthorizationServerSettings

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

## 🎓 Resumen Final

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

## 📝 Clase 79  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 80  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 81  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 82  - 👤🕵️‍♂🕵️‍♂🔑 🔑

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
