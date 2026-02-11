🛡️ 🌐🔒 Spring Security — OAUTH2 🔐🔐🔑🔑
## 📝 Clase 67 - INTRODUCCION A LOS OAUTH2 👤👤🕵️‍♂🕵️‍♂🔑 🔑
- Borramos JwtAuthenticationEntryPoint - JWTRequest -JWTResponse-AuthenticationController{
- borramos todo de carpeta security - services de JWT tambien
- borramos la libreria de pomxml de JWT


- DEBEN SER LA MISMA VERSION DE SPRING BOOT PARA LOS DOS PARENTS, SI NO DA ERROR DE COMPATIBILIDAD, EN ESTE CASO USAMOS LA 3.1.2 PARA LOS DOS PARENTS, SI USAMOS LA 4.0.1 PARA LOS DOS PARENTS, SI USAMOS LA 3.1.2 PARA UNO Y LA 4.0.1 PARA EL OTRO DA ERROR DE COMPATIBILIDAD
```xml
 <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.2</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-oauth2-resource-server</artifactId>
<version>6.1.2</version>
</dependency>

<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-oauth2-authorization-server</artifactId>
<version>1.1.1</version>
</dependency>
```
## 📝 Clase 69 - OAUTH2 CREANDO RegisteredClientRepository ESTATICO👤👤🕵️‍♂🕵️‍♂🔑 🔑

# 🔐 Guía Visual: OAuth2 en Spring Security

## 🎯 ¿Qué es OAuth2?

Antes de explicar el código, es importante entender que **OAuth2 es un protocolo de autorización** que permite a aplicaciones de terceros acceder a recursos de un usuario sin necesidad de conocer sus credenciales (usuario/contraseña).

**🏨 Analogía simple:** Imagina que tienes un hotel (el servidor de autorización). Cuando un huésped (usuario) llega, tú le das una tarjeta de acceso (token) que solo abre ciertas puertas (scopes/permisos), sin darle la llave maestra del hotel.

---

## 📚 RegisteredClientRepository

### 🤔 ¿Qué es?

`RegisteredClientRepository` es una **interfaz** que define cómo se almacenan y recuperan los **clientes OAuth2 registrados** en tu servidor de autorización.

### 👤 ¿Qué es un "cliente" en OAuth2?

Un cliente es cualquier **aplicación** que quiere acceder a recursos protegidos en nombre de un usuario. Por ejemplo:
- 📱 Una aplicación móvil
- 🌐 Una aplicación web
- 💻 Una aplicación de escritorio

### 🛠️ Métodos principales

```java
public interface RegisteredClientRepository {
    // 💾 Guarda un nuevo cliente
    void save(RegisteredClient registeredClient);
    
    // 🔍 Busca un cliente por su ID interno
    RegisteredClient findById(String id);
    
    // 🔎 Busca un cliente por su Client ID (identificador público)
    RegisteredClient findByClientId(String clientId);
}
```

**📌 En tu código:** Estás usando una implementación específica llamada `InMemoryRegisteredClientRepository`.

---

## 🧠 InMemoryRegisteredClientRepository

### 💡 ¿Qué significa?

Es una implementación de `RegisteredClientRepository` que **almacena los clientes en memoria** (RAM).

### ⚖️ Características

```markdown
✅ Ventajas:
- 🚀 Fácil de configurar para desarrollo/pruebas
- 🎯 No requiere base de datos
- ⚡ Rápido acceso

❌ Desventajas:
- 💥 Los datos se pierden al reiniciar la aplicación
- 📉 No es escalable (no funciona con múltiples instancias)
- ⚠️ NO recomendado para producción
```

### 🏭 Para producción deberías usar:

- 🗄️ `JdbcRegisteredClientRepository` (base de datos)
- 🔴 Implementación personalizada con Redis
- 🍃 Implementación con MongoDB, etc.

---

## 🔑 ClientAuthenticationMethod

### 🎭 ¿Qué es?

Define **cómo el cliente se autentica** ante el servidor de autorización para demostrar su identidad.

### 🔐 CLIENT_SECRET_BASIC

Es el método de autenticación donde:

1. 📤 El cliente envía sus credenciales (`clientId` + `clientSecret`)
2. 🔄 Estas se codifican en **Base64**
3. 📨 Se envían en el header HTTP `Authorization`

**📧 Ejemplo de header HTTP:**
```http
Authorization: Basic ZGVidWd1ZWFuZG9JZGVhczpzZWNyZXQ=
```

Donde `ZGVidWd1ZWFuZG9JZGVhczpzZWNyZXQ=` es:
```
Base64("debugueandoIdeas:secret")
```

### 🔧 Otros métodos de autenticación

```java
// 📮 Envía client_id y client_secret en el BODY del request
ClientAuthenticationMethod.CLIENT_SECRET_POST

// 🎫 Usa JWT firmado por el cliente para autenticarse
ClientAuthenticationMethod.CLIENT_SECRET_JWT

// 📜 Usa certificados públicos/privados
ClientAuthenticationMethod.PRIVATE_KEY_JWT

// 🚫 No requiere autenticación (públicos, como apps móviles)
ClientAuthenticationMethod.NONE
```

---

## 🎟️ AuthorizationGrantType

### 🌊 ¿Qué es?

Define **el flujo OAuth2** que el cliente puede usar para obtener tokens de acceso.

### 📋 AUTHORIZATION_CODE

Es el flujo más seguro y común. Se usa típicamente en aplicaciones web.

**🔄 Flujo paso a paso:**

```markdown
1. 👆 Usuario hace clic en "Iniciar sesión con..."
   └─> 🔀 Cliente redirige al servidor de autorización

2. 🔒 Usuario ingresa credenciales en el servidor de autorización
   └─> ✅ Usuario autoriza los permisos (scopes)

3. ↩️ Servidor de autorización redirige de vuelta al cliente
   └─> 🎫 Incluye un CÓDIGO de autorización temporal en la URL

4. 🔄 Cliente intercambia el código por un ACCESS TOKEN
   └─> 🔐 Esta petición incluye el client_secret (por eso es seguro)

5. 🎯 Cliente usa el ACCESS TOKEN para acceder a recursos protegidos
```

**🌐 Ejemplo de URL con código:**
```
http://localhost:8080?code=ABC123XYZ
```

### 🎪 Otros tipos de Grant

```java
// 🤖 Para aplicaciones sin backend (SPA, móviles)
AuthorizationGrantType.CLIENT_CREDENTIALS

// ⚠️ Para intercambiar credenciales del usuario directamente (NO recomendado)
AuthorizationGrantType.PASSWORD

// 🔄 Para refrescar tokens expirados
AuthorizationGrantType.REFRESH_TOKEN

// 🚫 Flow implícito (DEPRECATED, no usar)
AuthorizationGrantType.IMPLICIT
```

---

## 🔍 Desglose de tu código

```java
var client = RegisteredClient
    // 🆔 ID único interno (UUID aleatorio)
    .withId(UUID.randomUUID().toString())
    
    // 👤 Identificador público del cliente
    .clientId("debugueandoIdeas")
    
    // 🔑 Contraseña del cliente (debería estar encriptada en producción)
    .clientSecret("secret")
    
    // 📖 Permisos que puede solicitar este cliente
    .scope("read")
    
    // 🔙 URL a donde redirigir después de la autorización
    .redirectUri("http://localhost:8080")  // ⚠️ Corrección: http (no hhtp)
    
    // 🔐 Método de autenticación: Basic Auth
    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    
    // 🌊 Flujo OAuth2: Authorization Code
    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    
    // 🏗️ Construye el objeto RegisteredClient
    .build();
```

---

## 🚀 Mejoras recomendadas para tu código

### 1. 🔒 Encriptar el client secret

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
RegisteredClientRepository clientRepository(PasswordEncoder passwordEncoder) {
    var client = RegisteredClient
        .withId(UUID.randomUUID().toString())
        .clientId("debugueandoIdeas")
        .clientSecret(passwordEncoder.encode("secret")) // ✅ Encriptado
        .scope("read")
        .scope("write") // 📝 Múltiples scopes
        .redirectUri("http://localhost:8080/authorized")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 🔄 Para refrescar tokens
        .build();
    
    return new InMemoryRegisteredClientRepository(client);
}
```

### 2. ⚙️ Configuración más completa

```java
var client = RegisteredClient
    .withId(UUID.randomUUID().toString())
    .clientId("debugueandoIdeas")
    .clientSecret(passwordEncoder.encode("secret"))
    
    // 🎯 Scopes
    .scope(OidcScopes.OPENID)  // 🆔 Para OpenID Connect
    .scope(OidcScopes.PROFILE) // 👤 Información de perfil
    .scope("read")             // 📖 Lectura
    .scope("write")            // ✍️ Escritura
    
    // 🔗 URLs de redirección
    .redirectUri("http://localhost:8080/login/oauth2/code/myapp")
    .redirectUri("http://localhost:8080/authorized")
    
    // 🔐 Autenticación del cliente
    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
    
    // 🎟️ Grant types
    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    
    // ⏱️ Configuración de tokens
    .tokenSettings(TokenSettings.builder()
        .accessTokenTimeToLive(Duration.ofMinutes(15))  // ⏰ Expira en 15 min
        .refreshTokenTimeToLive(Duration.ofDays(7))     // 📅 Expira en 7 días
        .build())
    
    .build();
```

---

## 🗺️ Resumen Visual

```
┌─────────────────────────────────────────────────────────┐
│    🏢 SERVIDOR DE AUTORIZACIÓN (Tu código)              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  📚 RegisteredClientRepository (Interface)              │
│         │                                               │
│         └─> 🧠 InMemoryRegisteredClientRepository      │
│                 │                                       │
│                 └─> 💾 Almacena RegisteredClient       │
│                         │                               │
│                         ├─ 👤 clientId: "debugueandoIdeas"│
│                         ├─ 🔑 clientSecret: "secret"    │
│                         ├─ 📖 scope: "read"             │
│                         ├─ 🔙 redirectUri: "..."        │
│                         ├─ 🔐 authMethod: BASIC         │
│                         └─ 🌊 grantType: AUTH_CODE      │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 🆚 Diferencia entre JWT y OAuth2

```markdown
🎫 JWT:
- Es un FORMATO de token
- Contiene información codificada
- Es autosuficiente (no necesita validación en servidor)
- Ejemplo: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

🔐 OAuth2:
- Es un PROTOCOLO/FRAMEWORK
- Define CÓMO obtener y usar tokens
- Puede usar JWT como formato de token (o no)
- Define flujos: Authorization Code, Client Credentials, etc.

💡 Conclusión: OAuth2 puede usar JWT, pero no son lo mismo
```

---

## 🎓 Diagrama de Flujo Completo

```
🧑 Usuario                 🌐 Cliente            🔐 Auth Server         💾 Resource Server
   │                          │                      │                       │
   │  1️⃣ Quiero acceder       │                      │                       │
   │─────────────────────────>│                      │                       │
   │                          │                      │                       │
   │                          │  2️⃣ Redirige         │                       │
   │                          │─────────────────────>│                       │
   │                          │                      │                       │
   │  3️⃣ Login + Autoriza     │                      │                       │
   │<─────────────────────────┼──────────────────────│                       │
   │──────────────────────────┼─────────────────────>│                       │
   │                          │                      │                       │
   │  4️⃣ Redirige con código  │                      │                       │
   │<─────────────────────────┼──────────────────────│                       │
   │──────────────────────────>│                      │                       │
   │                          │                      │                       │
   │                          │  5️⃣ Código x Token   │                       │
   │                          │─────────────────────>│                       │
   │                          │                      │                       │
   │                          │  6️⃣ Access Token     │                       │
   │                          │<─────────────────────│                       │
   │                          │                      │                       │
   │                          │  7️⃣ Request + Token  │                       │
   │                          │──────────────────────┼──────────────────────>│
   │                          │                      │                       │
   │  8️⃣ Datos protegidos     │                      │                       │
   │<─────────────────────────┼──────────────────────┼───────────────────────│
```

---

## 📝 Clase 70  - MODIFICANDO BASES DE DATOS👤👤🕵️‍♂🕵️‍♂🔑 🔑

```SQL
----------------Schema--------------
create table partners (
                          id bigserial primary key,
                          client_id varchar(256),
                          client_name varchar(256),
                          client_secret varchar(256),
                          scopes varchar(256),
                          grant_types varchar(256),
                          authentication_methods varchar(256),
                          redirect_uri varchar(256),
                          redirect_uri_logout varchar(256)
);

--------------Data-------------
insert into partners(
    client_id,
    client_name,
    client_secret,
    scopes,
    grant_types,
    authentication_methods,
    redirect_uri,
    redirect_uri_logout
)
values ('debuggeandoideas',
            'debuggeando ideas',
            'secret',
            'read,write',
            'authorization_code,refresh_token',
            'client_secret_basic,client_secret_jwt',
            'https://oauthdebugger.com/debug',
            'https://springone.io/authorized')
```
---

## 📝 Clase 71  - MAPEANDO ENTIDADES PARTNER👤👤🕵️‍♂🕵️‍♂🔑 🔑

 - se desarrollo PartnerEntity
 - se desarrollo PartnerRepository

## 📝 Clase 72  - CREANDO REGISTEREDCLIENTREPOSITORY DINAMICO👤👤🕵️‍♂🕵️‍♂🔑 🔑