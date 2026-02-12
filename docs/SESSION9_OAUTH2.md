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

## Detalle del CAMELCASE y COINCIDENCIA de NOMBRE EN EL METODO FINDBYCLIENTID

### 📝 ¿Importa el nombre del campo en la entidad para los métodos de Spring Data?

#### 🟢 **No importa que esté escrito como `private String clientId;`**

Spring Data JPA sigue la convención de **camelCase** para los nombres de los atributos en Java. El método `findByClientId` funciona correctamente porque:

- El nombre del campo en la entidad es `clientId` (camelCase, como es estándar en Java).
- El método en el repositorio es `findByClientId`, respetando el mismo nombre y formato.

#### ⚠️ **No uses mayúscula inicial en atributos**
- En Java, los atributos de clase deben empezar en minúscula (camelCase): `clientId` ✅
- `ClientId` ❌ sería incorrecto para un atributo.

#### 📦 **Resumen**
| Correcto en entidad      | Correcto en repositorio         |
|-------------------------|---------------------------------|
| `private String clientId;` | `Optional<PartnerEntity> findByClientId(String clientId);` |

- **Spring Data mapea automáticamente** el nombre del método al campo de la entidad, siempre que coincidan en camelCase.
- No importa que el campo sea `clientId` y no `ClientId`.

> 🧩 **Conclusión:**
> ¡Tu código está correcto! Sigue usando camelCase para los atributos y los métodos de consulta funcionarán perfectamente.

---

## 📝 Clase 72  - CREANDO REGISTEREDCLIENTREPOSITORY DINAMICO👤👤🕵️‍♂🕵️‍♂🔑 🔑

# 🔐 Análisis completo de PartnerRegisteredClientService

## 📋 Consulta 1: ¿Qué es y para qué sirve `RegisteredClientRepository`?

### 🎯 Definición
`RegisteredClientRepository` es una **interfaz de Spring Authorization Server** que define el contrato para gestionar clientes OAuth2 registrados.

### 🛠️ ¿Para qué sirve?
Es el **repositorio de configuraciones de clientes OAuth2**. Permite a Spring Security:

- ✅ **Buscar clientes** por su `clientId`
- ✅ **Guardar nuevos clientes**
- ✅ **Buscar clientes** por su `id` interno
- ✅ **Validar credenciales** durante el flujo OAuth2

### 🔄 Flujo de uso
```
Cliente hace petición OAuth2
        ↓
Spring Security llama a findByClientId("debuggeandoideas")
        ↓
Busca en la BD a través de PartnerRepository
        ↓
Construye un RegisteredClient con esos datos
        ↓
Valida credenciales y genera tokens
```

### 📝 Métodos obligatorios

| Método | Parámetro | Retorna | Propósito |
|--------|-----------|---------|-----------|
| `findByClientId` | String clientId | RegisteredClient | Buscar cliente por clientId |
| `save` | RegisteredClient | void | Guardar cliente nuevo |
| `findById` | String id | RegisteredClient | Buscar por ID interno |

### 💡 Analogía
Es como un **catálogo de aplicaciones autorizadas**. Cada vez que una app intenta autenticarse, Spring busca en este catálogo si existe y qué permisos tiene.

---

## 📦 Consulta 2: ¿Qué guarda `partnerOpt`?

### 🎯 Respuesta corta
**SÍ**, `partnerOpt` es un `Optional<Partner>` que **puede o no contener** el registro de la base de datos que coincide con el `clientId` recibido.

### 🔍 Desglose del flujo

```java
var partnerOpt = this.partnerRepository.findByClientId(clientId);
```

#### Escenario 1: Cliente existe ✅
```
Petición: findByClientId("debuggeandoideas")
        ↓
Busca en BD: SELECT * FROM partners WHERE client_id = 'debuggeandoideas'
        ↓
Encuentra el registro:
{
  id: 1,
  client_id: "debuggeandoideas",
  client_name: "debuggeando ideas",
  client_secret: "secret",
  scopes: "read,write",
  grant_types: "authorization_code,refresh_token",
  authentication_methods: "client_secret_basic,client_secret_jwt",
  redirect_uri: "https://oauthdebugger.com/debug",
  redirect_uri_logout: "https://springone.io/authorized"
}
        ↓
partnerOpt = Optional[Partner{...}]  // Contiene el objeto
```

#### Escenario 2: Cliente NO existe ❌
```
Petición: findByClientId("cliente-inexistente")
        ↓
Busca en BD: SELECT * FROM partners WHERE client_id = 'cliente-inexistente'
        ↓
No encuentra nada
        ↓
partnerOpt = Optional.empty()  // Vacío
```

### 📊 Estructura del objeto Partner que se guarda

Basándome en tus datos SQL:

```java
Partner {
  id = 1,
  clientId = "debuggeandoideas",
  clientName = "debuggeando ideas",
  clientSecret = "secret",
  scopes = "read,write",  // ⚠️ String separado por comas
  grantTypes = "authorization_code,refresh_token",  // ⚠️ String separado por comas
  authenticationMethods = "client_secret_basic,client_secret_jwt",  // ⚠️ String separado por comas
  redirectUri = "https://oauthdebugger.com/debug",
  redirectUriLogout = "https://springone.io/authorized"
}
```

### 🎨 Visualización del Optional

```
Optional<Partner>
├── Si existe: Optional[Partner{clientId="debuggeandoideas", ...}]
└── Si NO existe: Optional.empty()
```

---

## 🔄 Consulta 3: ¿Este código transforma un objeto BD en RegisteredClient?

```java
return partnerOpt.map(partner -> 
```

### 🎯 Respuesta: **¡Exactamente! SÍ**

### 📖 Explicación detallada

#### ¿Qué hace `.map()`?

```java
partnerOpt.map(partner -> {
    // Transformación aquí
})
```

- 🔍 **Si `partnerOpt` contiene un valor** (`Optional[Partner]`):
    - Ejecuta la lambda
    - Transforma `Partner` → `RegisteredClient`
    - Retorna `Optional[RegisteredClient]`

- ❌ **Si `partnerOpt` está vacío** (`Optional.empty()`):
    - NO ejecuta la lambda
    - Retorna `Optional.empty()`

### 🔄 Flujo de transformación

```
Partner (Base de datos)          →          RegisteredClient (Spring Security)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

id: 1                            →          id: "1"
clientId: "debuggeandoideas"     →          clientId: "debuggeandoideas"
clientName: "debuggeando ideas"  →          clientName: "debuggeando ideas"
clientSecret: "secret"           →          clientSecret: "secret"
scopes: "read,write"             →          scopes: ["read", "write"]
grantTypes: "authorization_code,refresh_token"  →  grantTypes: [AUTHORIZATION_CODE, REFRESH_TOKEN]
authenticationMethods: "client_secret_basic,client_secret_jwt"  →  [CLIENT_SECRET_BASIC, CLIENT_SECRET_JWT]
redirectUri: "https://..."       →          redirectUri: "https://..."
redirectUriLogout: "https://..." →          postLogoutRedirectUri: "https://..."
```

### 💻 Código equivalente sin Optional.map()

```java
// Con Optional.map (código actual)
return partnerOpt.map(partner -> transformar(partner))
                .orElseThrow(...);

// Sin Optional.map (equivalente)
if (partnerOpt.isPresent()) {
    Partner partner = partnerOpt.get();
    return transformar(partner);
} else {
    throw new BadCredentialsException("Client no exists");
}
```

### 🎯 Ventaja de usar `.map()`
- ✅ Código más limpio
- ✅ Programación funcional
- ✅ Evita if-else anidados
- ✅ Manejo automático del Optional

---

## 🧩 Consulta 4: Explicación del proceso de transformación de Strings a objetos

```java
var authorizationGranTypes = Arrays.stream(partner.getGrandTypes().split(","))
        .map(AuthorizationGrantType::new)
        .toList();

var clientAuthorizationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
        .map(ClientAuthenticationMethod::new)
        .toList();

var scopes = Arrays.stream(partner.getScopes().split(",")).toList();
```

### 🎯 Objetivo general
Convertir **Strings separados por comas** de la BD en **Listas de objetos** que Spring Security entiende.

---

### 🔧 Parte 1: `authorizationGranTypes`

#### 📥 Entrada (desde BD)
```java
partner.getGrandTypes() = "authorization_code,refresh_token"
```

#### 🔄 Proceso paso a paso

```java
// Paso 1: Split por comas
partner.getGrandTypes().split(",")
// Resultado: ["authorization_code", "refresh_token"]

// Paso 2: Convertir array a Stream
Arrays.stream(["authorization_code", "refresh_token"])

// Paso 3: Transformar cada String en AuthorizationGrantType
.map(AuthorizationGrantType::new)
// Equivalente a: .map(str -> new AuthorizationGrantType(str))

// Resultado intermedio:
Stream[
  AuthorizationGrantType("authorization_code"),
  AuthorizationGrantType("refresh_token")
]

// Paso 4: Convertir Stream a List
.toList()

// Resultado final:
List[
  AuthorizationGrantType.AUTHORIZATION_CODE,
  AuthorizationGrantType.REFRESH_TOKEN
]
```

#### 📊 Visualización del flujo

```
"authorization_code,refresh_token"  (String en BD)
        ↓ split(",")
["authorization_code", "refresh_token"]  (Array)
        ↓ Arrays.stream()
Stream["authorization_code", "refresh_token"]  (Stream)
        ↓ map(AuthorizationGrantType::new)
Stream[AuthorizationGrantType, AuthorizationGrantType]  (Stream de objetos)
        ↓ toList()
List[AuthorizationGrantType, AuthorizationGrantType]  (Lista final)
```

---

### 🔐 Parte 2: `clientAuthorizationMethods`

#### 📥 Entrada (desde BD)
```java
partner.getAuthenticationMethods() = "client_secret_basic,client_secret_jwt"
```

#### 🔄 Proceso idéntico

```java
Arrays.stream(partner.getAuthenticationMethods().split(","))
    .map(ClientAuthenticationMethod::new)
    .toList();

// Transformación:
"client_secret_basic,client_secret_jwt"
        ↓
["client_secret_basic", "client_secret_jwt"]
        ↓
[ClientAuthenticationMethod.CLIENT_SECRET_BASIC, 
 ClientAuthenticationMethod.CLIENT_SECRET_JWT]
```

---

### 🏷️ Parte 3: `scopes`

#### 📥 Entrada (desde BD)
```java
partner.getScopes() = "read,write"
```

#### 🔄 Proceso simplificado

```java
Arrays.stream(partner.getScopes().split(",")).toList();

// Transformación:
"read,write"
        ↓
["read", "write"]
        ↓
List["read", "write"]  // Se queda como Strings, no se convierte a objetos
```

#### ⚠️ Diferencia importante
- Los **scopes** se quedan como `List<String>`
- Los **grant types** y **auth methods** se convierten a objetos específicos

---

### 🧠 ¿Por qué usar Streams?

#### ❌ Sin Streams (código imperativo)
```java
String[] grantTypesArray = partner.getGrandTypes().split(",");
List<AuthorizationGrantType> authorizationGranTypes = new ArrayList<>();
for (String gt : grantTypesArray) {
    authorizationGranTypes.add(new AuthorizationGrantType(gt));
}
```

#### ✅ Con Streams (código funcional)
```java
var authorizationGranTypes = Arrays.stream(partner.getGrandTypes().split(","))
    .map(AuthorizationGrantType::new)
    .toList();
```

### 📈 Ventajas de Streams
- ✅ Menos código
- ✅ Más legible
- ✅ Inmutable (`.toList()` crea lista inmutable)
- ✅ Funcional y declarativo

---

### 🎯 Uso posterior en el builder

```java
.authorizationGrantType(authorizationGranTypes.get(0))  // AUTHORIZATION_CODE
.authorizationGrantType(authorizationGranTypes.get(1))  // REFRESH_TOKEN
.clientAuthenticationMethod(clientAuthorizationMethods.get(0))  // CLIENT_SECRET_BASIC
.clientAuthenticationMethod(clientAuthorizationMethods.get(1))  // CLIENT_SECRET_JWT
.scope(scopes.get(0))  // "read"
.scope(scopes.get(1))  // "write"
```

---

### 📊 Tabla resumen de transformaciones

| Variable | Tipo en BD | Valor BD | Proceso | Tipo final | Valor final |
|----------|------------|----------|---------|------------|-------------|
| `authorizationGranTypes` | String | `"authorization_code,refresh_token"` | split → stream → map → toList | `List<AuthorizationGrantType>` | `[AUTHORIZATION_CODE, REFRESH_TOKEN]` |
| `clientAuthorizationMethods` | String | `"client_secret_basic,client_secret_jwt"` | split → stream → map → toList | `List<ClientAuthenticationMethod>` | `[CLIENT_SECRET_BASIC, CLIENT_SECRET_JWT]` |
| `scopes` | String | `"read,write"` | split → stream → toList | `List<String>` | `["read", "write"]` |

---

## 🎓 Resumen general del algoritmo completo

```
1. Recibe clientId del endpoint
        ↓
2. Busca en BD: partnerOpt = partnerRepository.findByClientId(clientId)
        ↓
3. Si existe (Optional contiene Partner):
   a. Extrae y transforma grant types (String → List<AuthorizationGrantType>)
   b. Extrae y transforma auth methods (String → List<ClientAuthenticationMethod>)
   c. Extrae scopes (String → List<String>)
        ↓
4. Construye RegisteredClient con builder pattern
        ↓
5. Retorna RegisteredClient
        ↓
6. Si NO existe: lanza BadCredentialsException
```

# 🛠️ Explicación del código PartnerRegisteredClientService

## 📄 ¿Qué hace esta clase?

`PartnerRegisteredClientService` implementa la interfaz `RegisteredClientRepository` de Spring Authorization Server. Su objetivo es buscar y construir un objeto `RegisteredClient` a partir de los datos almacenados en la base de datos (a través de `PartnerRepository`).

## 🔍 Flujo del método `findByClientId`

### Recibe un `clientId`:
El método busca en la base de datos un partner (cliente OAuth2) con ese `clientId`.

### Si existe el partner:
- Extrae los tipos de grant (`grantTypes`), métodos de autenticación (`authenticationMethods`) y scopes, separando los valores por comas.
- Crea listas de objetos a partir de esos valores.
- Construye un objeto `RegisteredClient` usando los datos del partner.

### Si no existe:
Lanza una excepción `BadCredentialsException`.

## ⚠️ ¿Por qué hay error en las líneas 44 y 45?

```java
.clientAuthenticationMethod(clientAuthorizationMethods.get(0))
.clientAuthenticationMethod(clientAuthorizationMethods.get(1))
```

### Problema:
El método `clientAuthenticationMethod` espera un objeto de tipo `ClientAuthenticationMethod`, pero tú le estás pasando un objeto de tipo `AuthorizationGrantType`.

### Causa:
En la línea:
```java
var clientAuthorizationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
    .map(AuthorizationGrantType::new)
    .toList();
```

Estás usando `AuthorizationGrantType::new` en vez de `ClientAuthenticationMethod::new`.

Por eso, la lista `clientAuthorizationMethods` contiene objetos del tipo incorrecto.

### Solución:
Debes cambiar esa línea por:
```java
var clientAuthorizationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
    .map(ClientAuthenticationMethod::new)
    .toList();
```

## 📝 Resumen en tabla

| Línea | Qué hace | Problema | Solución |
|-------|----------|----------|----------|
| 44-45 | Añade métodos de autenticación al builder | Usa tipo incorrecto (`AuthorizationGrantType`) | Usar `ClientAuthenticationMethod` |

## 🧩 Ejemplo corregido

```java
var clientAuthorizationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
    .map(ClientAuthenticationMethod::new)
    .toList();
```

## 🧠 ¿Cómo funciona el flujo completo?

1. El cliente OAuth2 hace una petición.
2. Spring Security llama a `findByClientId` para buscar la configuración del cliente.
3. Si existe, se construye un `RegisteredClient` con los datos de la base.
4. Si no, se lanza una excepción.

## 🏁 Conclusión

- El error es por usar el tipo incorrecto al mapear los métodos de autenticación.
- Cambia a `ClientAuthenticationMethod::new` para solucionarlo.
- El flujo permite que Spring Security gestione clientes OAuth2 de forma dinámica desde la base de datos.

---
## 📝 Clase 73  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 74  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 75  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 76  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 77  - 👤🕵️‍♂🕵️‍♂🔑 🔑

---

## 📝 Clase 78  - 👤🕵️‍♂🕵️‍♂🔑 🔑

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









