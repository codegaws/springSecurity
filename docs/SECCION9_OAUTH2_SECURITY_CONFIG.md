# 🔐 SECCIÓN 9: Configuración de OAuth2 con Spring Security

## 📋 Índice
- [Introducción a la Configuración OAuth2](#introducción-a-la-configuración-oauth2)
- [Concepto de SecurityFilterChain Múltiples](#concepto-de-securityfilterchain-múltiples)
- [Filter Chain #1: OAuth2 Authorization Server](#filter-chain-1-oauth2-authorization-server)
- [Filter Chain #2: Resource Server](#filter-chain-2-resource-server)
- [Constantes de Configuración](#constantes-de-configuración)

---

## 🎯 Introducción a la Configuración OAuth2

### ¿Qué cambia respecto a JWT tradicional?

| Aspecto | JWT Tradicional | OAuth2 con Spring Authorization Server |
|---------|----------------|----------------------------------------|
| **🔧 Generación** | Tú creas el token manualmente | Spring Authorization Server lo genera automáticamente |
| **🔑 Validación** | Tú validas con tu firma secreta | Spring valida usando endpoints estándar OAuth2 |
| **📦 Complejidad** | Código custom para todo | Infraestructura completa proporcionada |
| **🌐 Estándar** | Implementación propia | Estándar OAuth2 RFC 6749 |
| **🔄 Refresh Tokens** | Debes implementarlo | Ya incluido en el framework |

> 💡 **Nota Importante**: Con OAuth2, ya NO necesitas crear manualmente el `JWTService`, `JWTValidationFilter`, ni el `AuthController` que creaste antes. Spring Authorization Server maneja todo esto automáticamente.

---

## 🔗 Concepto de SecurityFilterChain Múltiples

### ¿Por qué dos Filter Chains?

```
┌─────────────────────────────────────────────────────────┐
│              🌐 Petición HTTP Entrante                   │
└─────────────────────┬───────────────────────────────────┘
                      │
                      ▼
        ┌─────────────────────────────┐
        │  Spring Security Dispatcher  │
        └─────────────┬───────────────┘
                      │
          ┌───────────┴──────────┐
          │                      │
          ▼                      ▼
    ┏━━━━━━━━━━━┓         ┏━━━━━━━━━━━┓
    ┃ @Order(1) ┃         ┃ @Order(2) ┃
    ┗━━━━━━━━━━━┛         ┗━━━━━━━━━━━┛
    OAuth2 Server         Resource Server
    (Gestiona tokens)     (Valida tokens)
          │                      │
          ▼                      ▼
    /oauth2/token          /accounts/**
    /oauth2/authorize      /loans/**
    /oauth2/introspect     /balance/**
```

### 📊 Tabla Comparativa de Filter Chains

| Característica | Filter Chain #1 (OAuth2) | Filter Chain #2 (Resource Server) |
|----------------|--------------------------|-----------------------------------|
| **🎯 Propósito** | Servidor de Autorización | Servidor de Recursos (tu API) |
| **🔢 Orden** | `@Order(1)` - Prioridad alta | `@Order(2)` - Prioridad normal |
| **🛣️ Rutas** | `/oauth2/**`, `/login` | `/accounts`, `/loans`, `/balance`, etc. |
| **🎫 Función** | Genera y gestiona tokens | Valida tokens y protege recursos |
| **👤 Autenticación** | Form login, client credentials | JWT Bearer token |

---

## 🏢 Filter Chain #1: OAuth2 Authorization Server

### 📝 Código Completo

```java
@Bean
@Order(1)  // 👈 Prioridad ALTA - Se evalúa PRIMERO
SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
    // 1️⃣ Aplica configuración por defecto del Authorization Server
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    
    // 2️⃣ Habilita OpenID Connect 1.0
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
    
    // 3️⃣ Manejo de errores de autenticación
    http.exceptionHandling(e ->
            e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
    
    return http.build();
}
```

---

### #### 1️⃣ `OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)`

#### ¿Qué hace este método?

Este método configura **AUTOMÁTICAMENTE** los siguientes endpoints del servidor OAuth2:

| Endpoint | Método HTTP | Descripción | Ejemplo de Uso |
|----------|-------------|-------------|----------------|
| **`/oauth2/authorize`** | GET | Inicia el flujo de autorización | Redirige al usuario al login |
| **`/oauth2/token`** | POST | 🎫 **Genera tokens de acceso** | Cliente solicita token con credenciales |
| **`/oauth2/introspect`** | POST | Valida si un token es válido | Verifica estado del token |
| **`/oauth2/revoke`** | POST | Revoca un token | Invalida un token antes de expirar |
| **`/oauth2/jwks`** | GET | Public keys para validar JWT | Para validación de firma |
| **`/.well-known/oauth-authorization-server`** | GET | Metadata del servidor | Descubrimiento automático |

#### 🔍 Flujo de Obtención de Token

```
┌──────────┐                                      ┌─────────────────┐
│  Cliente │                                      │ Authorization   │
│  (App)   │                                      │ Server (Spring) │
└────┬─────┘                                      └────────┬────────┘
     │                                                     │
     │ 1. POST /oauth2/token                              │
     │    grant_type=client_credentials                   │
     │    client_id=myapp                                 │
     │    client_secret=secret                            │
     ├────────────────────────────────────────────────────>│
     │                                                     │
     │              2. Valida credenciales                │
     │                 en RegisteredClientRepository      │
     │                                                     │
     │ 3. Respuesta con token                             │
     │    {                                               │
     │      "access_token": "eyJhbGc...",                 │
     │      "token_type": "Bearer",                       │
     │      "expires_in": 3600                            │
     │    }                                               │
     │<────────────────────────────────────────────────────┤
     │                                                     │
```

#### 💡 Ejemplo Práctico con tu Código

Cuando ejecutas:

```bash
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&
client_id=debuggingideas-client&
client_secret=debuggingideas-secret
```

**Spring Authorization Server hace esto por ti:**

```
🔄 Proceso Automático:
├─ ✅ Valida el client_id y client_secret
├─ ✅ Verifica el grant_type permitido
├─ ✅ Genera el JWT con la firma correcta
├─ ✅ Incluye los scopes en el token
├─ ✅ Establece la fecha de expiración
├─ ✅ Guarda el token en el repositorio
└─ ✅ Devuelve el token al cliente
```

**NO necesitas escribir código para:**
- ❌ Generar el token manualmente
- ❌ Firmar el token con tu clave secreta
- ❌ Crear el endpoint `/oauth2/token`
- ❌ Validar las credenciales del cliente

---

### #### 2️⃣ `.oidc(Customizer.withDefaults())`

#### ¿Qué es OIDC?

**OIDC = OpenID Connect 1.0**

Es una capa de **identidad** construida sobre OAuth2.

| OAuth2 | OIDC (OpenID Connect) |
|--------|----------------------|
| 🔑 **Solo autorización** | 👤 **Autorización + Identidad** |
| Permite acceso a recursos | Permite saber QUIÉN es el usuario |
| Token opaco o JWT | Incluye **ID Token** con info del usuario |
| Scopes: `read`, `write` | Scopes: `openid`, `profile`, `email` |

#### Endpoints Adicionales que OIDC Habilita

```java
.oidc(Customizer.withDefaults());
// 👆 Esto activa automáticamente:
```

| Endpoint | Descripción | Respuesta Ejemplo |
|----------|-------------|-------------------|
| **`/.well-known/openid-configuration`** | Metadata del servidor OIDC | JSON con endpoints disponibles |
| **`/userinfo`** | 👤 Información del usuario autenticado | `{"sub": "user@mail.com", "name": "John"}` |
| **`/oauth2/jwks`** | Claves públicas JWK Set | Para validar la firma del JWT |

#### 🎫 ID Token vs Access Token

```
┌─────────────────────────────────────────────────────────┐
│                    🎫 ACCESS TOKEN                       │
├─────────────────────────────────────────────────────────┤
│ • Propósito: Acceder a RECURSOS (API)                   │
│ • Contiene: Scopes, permisos                            │
│ • Ejemplo: "scope": "read write"                        │
│ • Para: Resource Server                                 │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                     🆔 ID TOKEN                          │
├─────────────────────────────────────────────────────────┤
│ • Propósito: Identificar al USUARIO                     │
│ • Contiene: email, name, sub (subject)                  │
│ • Ejemplo: "email": "user@mail.com"                     │
│ • Para: Cliente (conocer quién inició sesión)          │
└─────────────────────────────────────────────────────────┘
```

#### 📦 Ejemplo de Respuesta con OIDC

```json
POST /oauth2/token (con scope=openid)

Respuesta:
{
  "access_token": "eyJhbGciOiJSUzI1Ni...",  // 🔑 Para acceder a recursos
  "id_token": "eyJhbGciOiJSUzI1NiIs...",     // 🆔 Información del usuario
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "openid profile"
}
```

---

### #### 3️⃣ `exceptionHandling` - Redirección al Login

```java
http.exceptionHandling(e ->
    e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
```

#### ¿Qué hace?

Cuando un usuario **NO autenticado** intenta acceder a un recurso protegido:

```
┌─────────────────────────────────────────────────────┐
│ Usuario intenta: GET /oauth2/authorize             │
│ Estado: ❌ No autenticado                           │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
      ┌──────────────────────────────┐
      │ Spring Security detecta      │
      │ falta de autenticación       │
      └──────────────┬───────────────┘
                     │
                     ▼
      ┌──────────────────────────────┐
      │ AuthenticationEntryPoint     │
      │ redirige a /login            │
      └──────────────┬───────────────┘
                     │
                     ▼
      ┌──────────────────────────────┐
      │ Usuario ve formulario login  │
      │ Ingresa usuario y contraseña │
      └──────────────────────────────┘
```

#### 🎨 Visualización del Flujo

```
Petición sin autenticación
         │
         ▼
    ❌ 401 Unauthorized
         │
         ▼
    🔀 Redirección HTTP 302
         │
         ▼
    🌐 Location: /login
         │
         ▼
    📝 Formulario de Login
```

#### ⚙️ Sin esta configuración

```java
// Sin exceptionHandling configurado:
// Respuesta: HTTP 401 Unauthorized (sin redirección)
// Usuario ve: Error 401 en lugar del formulario de login
```

#### ✅ Con esta configuración

```java
// Con exceptionHandling configurado:
// Respuesta: HTTP 302 Found
// Location: /login
// Usuario ve: Formulario de login amigable
```

---

### 🗂️ Filter Chain #2: Resource Server

#### 📝 Código Completo

```java
@Bean
@Order(2)  // 👈 Prioridad NORMAL - Se evalúa DESPUÉS del OAuth2 Server
SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
    // 1️⃣ Habilita formulario de login
    http.formLogin(Customizer.withDefaults());
    
    // 2️⃣ Define reglas de autorización
    http.authorizeHttpRequests(auth ->
            auth.requestMatchers(ADMIN_RESOURCES).hasAuthority(AUTH_WRITE)
                    .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)
                    .anyRequest().permitAll());
    
    // 3️⃣ Configura validación de JWT
    http.oauth2ResourceServer(oauth ->
            oauth.jwt(Customizer.withDefaults()));
    
    return http.build();
}
```

---

### #### 1️⃣ `formLogin(Customizer.withDefaults())`

Habilita el formulario de login estándar de Spring Security.

```html
<!-- Spring genera automáticamente esta página -->
┌─────────────────────────────────────┐
│         🔐 Please Sign In           │
├─────────────────────────────────────┤
│                                     │
│  Username: [________________]       │
│                                     │
│  Password: [________________]       │
│                                     │
│           [ Sign In ]               │
│                                     │
└─────────────────────────────────────┘
```

---

### #### 2️⃣ `authorizeHttpRequests` - Reglas de Autorización

#### 📊 Matriz de Permisos

```java
auth.requestMatchers(ADMIN_RESOURCES).hasAuthority(AUTH_WRITE)      // 🔴 Admin
    .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)        // 🔵 Usuario
    .anyRequest().permitAll());                                     // 🟢 Público
```

| Recurso | Permiso Requerido | Usuarios con Acceso | Ejemplo de Token |
|---------|-------------------|---------------------|------------------|
| **`/accounts/**`** | ✍️ `write` | Administradores | `scope: "write"` |
| **`/cards/**`** | ✍️ `write` | Administradores | `scope: "write"` |
| **`/loans/**`** | 👁️ `read` | Usuarios normales | `scope: "read"` |
| **`/balance/**`** | 👁️ `read` | Usuarios normales | `scope: "read"` |
| **`/welcome`** | 🌐 Ninguno | Todos (público) | Sin token |

#### 🔍 Flujo de Validación de Permisos

```
Petición: GET /accounts/123
Header: Authorization: Bearer eyJhbGc...
         │
         ▼
┌────────────────────────┐
│ 1. Extrae el JWT       │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│ 2. Valida firma JWT    │
│    con /oauth2/jwks    │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│ 3. Extrae "scope"      │
│    del token           │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│ 4. Verifica si tiene   │
│    authority "write"   │
└───────────┬────────────┘
            │
      ┌─────┴─────┐
      ▼           ▼
   ✅ Si       ❌ No
   200 OK      403 Forbidden
```

#### 💻 Ejemplo Práctico

```bash
# ❌ Token con scope "read" intentando acceder a /accounts
GET http://localhost:8080/accounts/123
Authorization: Bearer eyJ...  # Token con "scope": "read"

Respuesta:
HTTP/1.1 403 Forbidden
{
  "error": "access_denied",
  "message": "Insufficient scope"
}
```

```bash
# ✅ Token con scope "write" accediendo a /accounts
GET http://localhost:8080/accounts/123
Authorization: Bearer eyJ...  # Token con "scope": "write"

Respuesta:
HTTP/1.1 200 OK
{
  "accountNumber": "123",
  "balance": 5000
}
```

---

### #### 3️⃣ `oauth2ResourceServer` - Validación de JWT

```java
http.oauth2ResourceServer(oauth ->
    oauth.jwt(Customizer.withDefaults()));
```

#### ¿Qué hace esta línea?

Configura tu aplicación como un **Resource Server** que valida tokens JWT.

```
┌──────────────────────────────────────────────────────┐
│           🛡️ OAuth2 Resource Server                  │
├──────────────────────────────────────────────────────┤
│                                                      │
│  1. Recibe petición con Bearer token                │
│  2. Extrae el JWT del header Authorization          │
│  3. Descarga las claves públicas desde /oauth2/jwks │
│  4. Valida la firma del JWT                         │
│  5. Verifica que no haya expirado                   │
│  6. Extrae los scopes/authorities                   │
│  7. Autoriza o rechaza la petición                  │
│                                                      │
└──────────────────────────────────────────────────────┘
```

#### 🔐 Proceso de Validación Detallado

```
┌─────────────────────────────────────────────────────────┐
│  Cliente envía:                                         │
│  GET /loans/123                                         │
│  Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6...  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
        ┌────────────────────────────────┐
        │ 1️⃣ Spring Security intercepta  │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 2️⃣ Extrae token del header     │
        │    "Bearer " se remueve        │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 3️⃣ Decodifica header JWT       │
        │    {                           │
        │      "alg": "RS256",           │
        │      "kid": "abc123"           │
        │    }                           │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 4️⃣ Consulta clave pública      │
        │    GET /oauth2/jwks            │
        │    usando "kid" del header     │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 5️⃣ Valida firma criptográfica  │
        │    ✅ Firma válida              │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 6️⃣ Verifica expiración (exp)   │
        │    exp: 1770622371             │
        │    now: 1770604000             │
        │    ✅ No expirado               │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 7️⃣ Extrae authorities/scopes   │
        │    "scope": "read"             │
        │    Convierte a:                │
        │    SCOPE_read                  │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 8️⃣ Evalúa regla de acceso      │
        │    /loans/** requiere "read"   │
        │    Token tiene "read"          │
        │    ✅ ACCESO CONCEDIDO          │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ 9️⃣ Ejecuta el controlador      │
        │    @GetMapping("/loans/{id}")  │
        └────────────────────────────────┘
```

#### ⚙️ Configuración Automática

Con `Customizer.withDefaults()`, Spring hace esto automáticamente:

```java
// Configuración implícita que Spring aplica:
oauth2ResourceServer(oauth -> oauth
    .jwt(jwt -> jwt
        .jwkSetUri("http://localhost:8080/oauth2/jwks")  // Descarga claves públicas
        .jwtAuthenticationConverter(                     // Convierte JWT a Authentication
            new JwtAuthenticationConverter() {
                @Override
                public AbstractAuthenticationToken convert(Jwt jwt) {
                    // Extrae "scope" y lo convierte a authorities
                    Collection<GrantedAuthority> authorities = 
                        extractAuthorities(jwt);
                    return new JwtAuthenticationToken(jwt, authorities);
                }
            }
        )
    )
);
```

#### 🆚 Comparación: JWT Manual vs OAuth2 Resource Server

| Aspecto | Tu JWT Manual | OAuth2 Resource Server |
|---------|---------------|------------------------|
| **Validación** | `JWTValidationFilter` custom | Automático con `.jwt()` |
| **Clave Secreta** | `JWT_SECRET` hardcodeada | Claves públicas en `/oauth2/jwks` |
| **Extracción Claims** | `getAllClaimsFromToken()` | Automático |
| **Authorities** | Extraer manualmente de claims | Automático desde `scope` |
| **Expiración** | `isTokenExpired()` custom | Validación automática |
| **Código** | ~200 líneas | ~10 líneas |

---

## 📌 Constantes de Configuración

```java
private static final String[] USER_RESOURCES = {"/loans/**", "/balance/**"};
private static final String[] ADMIN_RESOURCES = {"/accounts/**", "/cards/**"};
private static final String AUTH_WRITE = "write";
private static final String AUTH_READ = "read";
private static final String LOGIN_RESOURCE = "/login";
```

### 📦 Tabla de Constantes

| Constante | Valor | Propósito | Usado En |
|-----------|-------|-----------|----------|
| **`USER_RESOURCES`** | `/loans/**`, `/balance/**` | Recursos para usuarios normales | `authorizeHttpRequests` |
| **`ADMIN_RESOURCES`** | `/accounts/**`, `/cards/**` | Recursos para administradores | `authorizeHttpRequests` |
| **`AUTH_WRITE`** | `"write"` | Permiso de escritura (admin) | `hasAuthority()` |
| **`AUTH_READ`** | `"read"` | Permiso de lectura (usuario) | `hasAuthority()` |
| **`LOGIN_RESOURCE`** | `"/login"` | Página de login | `LoginUrlAuthenticationEntryPoint` |

### 🎯 Cómo se Relacionan los Scopes con Authorities

```java
// En el token JWT:
{
  "scope": "read write",  // 👈 Scopes en el token
  "sub": "admin@mail.com",
  "iat": 1770604000,
  "exp": 1770622000
}

// Spring convierte automáticamente:
scope: "read"  → SCOPE_read  (Authority)
scope: "write" → SCOPE_write (Authority)

// Pero tu configuración usa:
.hasAuthority("read")   // ❌ No coincide con SCOPE_read
.hasAuthority("write")  // ❌ No coincide con SCOPE_write
```

#### ⚠️ Problema Potencial

Por defecto, Spring Security prefija los scopes con `SCOPE_`, por lo que deberías usar:

```java
// ✅ Opción 1: Usar el prefijo SCOPE_
.hasAuthority("SCOPE_read")
.hasAuthority("SCOPE_write")

// ✅ Opción 2: Personalizar el converter
@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = 
        new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthorityPrefix("");  // Sin prefijo
    
    JwtAuthenticationConverter jwtAuthenticationConverter = 
        new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
}
```

---

## 🎓 Resumen Ejecutivo

### ✅ ¿Qué hace cada Filter Chain?

```
┌─────────────────────────────────────────────────────────┐
│         🏢 Filter Chain #1: Authorization Server        │
├─────────────────────────────────────────────────────────┤
│  • Genera tokens JWT                                    │
│  • Endpoints: /oauth2/token, /oauth2/authorize          │
│  • Maneja login y redirecciones                         │
│  • OIDC: /userinfo, /.well-known/openid-configuration  │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│         🗂️ Filter Chain #2: Resource Server             │
├─────────────────────────────────────────────────────────┤
│  • Valida tokens JWT                                    │
│  • Protege tu API (/accounts, /loans, etc.)            │
│  • Verifica permisos (read/write)                       │
│  • Rechaza tokens inválidos o expirados                │
└─────────────────────────────────────────────────────────┘
```

### 🔄 Flujo Completo de Autenticación y Autorización

```
1. 📱 Cliente solicita token
   POST /oauth2/token (grant_type=client_credentials)
   ↓
2. 🏢 Authorization Server (Filter Chain #1)
   - Valida credenciales
   - Genera JWT firmado
   - Devuelve token
   ↓
3. 🔑 Cliente recibe token
   {"access_token": "eyJhbGc...", "expires_in": 3600}
   ↓
4. 📤 Cliente usa token para acceder a recurso
   GET /accounts/123
   Authorization: Bearer eyJhbGc...
   ↓
5. 🗂️ Resource Server (Filter Chain #2)
   - Valida firma JWT
   - Verifica expiración
   - Extrae scopes
   - Evalúa permisos
   ↓
6. ✅ Respuesta exitosa o ❌ 403 Forbidden
```

### 💡 Ventajas vs JWT Manual

| Característica | JWT Manual (tu código anterior) | OAuth2 Spring Authorization Server |
|----------------|--------------------------------|-----------------------------------|
| **Líneas de código** | ~500 líneas | ~50 líneas |
| **Endpoints OAuth2** | ❌ Tienes que crearlos | ✅ Automáticos |
| **Refresh tokens** | ❌ Implementar manualmente | ✅ Incluido |
| **OIDC support** | ❌ No disponible | ✅ Completo |
| **Claves públicas** | ❌ Clave secreta compartida | ✅ Par de claves RSA |
| **Estándar** | ⚠️ Implementación propia | ✅ RFC 6749 (OAuth2) |
| **Mantenimiento** | 🔴 Alto | 🟢 Bajo |
| **Escalabilidad** | ⚠️ Limitada | ✅ Empresarial |

---

## 🚀 Próximos Pasos

Para completar la configuración OAuth2, necesitas:

1. **RegisteredClientRepository**: Define los clientes OAuth2 permitidos
2. **AuthorizationServerSettings**: Configuración del servidor (issuer URL)
3. **JWKSource**: Generación de claves para firmar JWT
4. **UserDetailsService**: Carga usuarios desde BD (ya lo tienes)
5. **PasswordEncoder**: Codifica contraseñas (ya lo tienes)

---

## 📚 Referencias

- [Spring Authorization Server Docs](https://docs.spring.io/spring-authorization-server/docs/current/reference/html/)
- [OAuth 2.0 RFC 6749](https://datatracker.ietf.org/doc/html/rfc6749)
- [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html)

---

> 💡 **Conclusión**: Este código reemplaza tu implementación manual de JWT con una solución estándar, robusta y mantenible. El `@Order` permite que ambos filter chains coexistan: uno genera tokens (Authorization Server) y otro los valida (Resource Server).

