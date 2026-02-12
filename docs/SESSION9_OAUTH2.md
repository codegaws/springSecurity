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
## 📝 Clase 73  - CustomerUserDetails 👤🕵️‍♂🕵️‍♂🔑 🔑
# 🔐 Análisis completo de CustomerUserDetails

## 🔄 Consulta 1: ¿Por qué usar `@Transactional` y cuándo es necesario?

### 🎯 ¿Qué hace `@Transactional`?

`@Transactional` **gestiona transacciones de base de datos** automáticamente. Spring se encarga de:

- ✅ Abrir una transacción al inicio del método
- ✅ Hacer commit si todo sale bien
- ✅ Hacer rollback si hay una excepción
- ✅ Cerrar la conexión a la BD

### 📊 Comparación: `CustomerUserDetails` vs `PartnerRegisteredClientService`

| Aspecto | CustomerUserDetails | PartnerRegisteredClientService |
|---------|-------------------|-------------------------------|
| **@Transactional** | ✅ SÍ necesario | ❌ NO necesario |
| **Relaciones** | `@OneToMany` con EAGER | Sin relaciones |
| **Operación** | Lectura + carga de relaciones | Solo lectura simple |
| **Lazy Loading** | Posible problema sin transacción | No aplica |

---

### 🔍 ¿Por qué `CustomerUserDetails` SÍ necesita `@Transactional`?

#### 📦 Tu entidad tiene una relación `@OneToMany`:

```java
@Entity
@Table(name = "customers")
public class CustomerEntity {
    
    @OneToMany(fetch = FetchType.EAGER)  // ⚠️ Relación con otra tabla
    @JoinColumn(name = "id_customer")
    private List<RoleEntity> roles;  // ← Esto requiere otra consulta SQL
}
```

#### 🔄 Flujo sin `@Transactional` (PROBLEMA ❌):

```
1. customerRepository.findByEmail(username)
   ↓ Ejecuta: SELECT * FROM customers WHERE email = ?
   ↓ Obtiene: CustomerEntity
   ↓ Cierra la conexión a la BD ⚠️
   
2. customer.getRoles()
   ↓ Intenta cargar roles desde BD
   ❌ ERROR: LazyInitializationException
   ❌ La sesión/conexión ya está cerrada
```

#### 🔄 Flujo con `@Transactional` (CORRECTO ✅):

```
1. @Transactional abre transacción
   ↓
2. customerRepository.findByEmail(username)
   ↓ Ejecuta: SELECT * FROM customers WHERE email = ?
   ↓ Obtiene: CustomerEntity
   ↓ Conexión sigue ABIERTA ✅
   
3. customer.getRoles()
   ↓ Ejecuta: SELECT * FROM roles WHERE id_customer = ?
   ↓ Obtiene: List<RoleEntity>
   ↓ Todo funciona ✅
   
4. @Transactional cierra transacción
```

---

### 🔍 ¿Por qué `PartnerRegisteredClientService` NO necesita `@Transactional`?

#### 📦 La entidad Partner NO tiene relaciones complejas:

```java
@Entity
public class Partner {
    private Long id;
    private String clientId;
    private String clientSecret;
    private String scopes;  // String simple, NO relación
    private String grantTypes;  // String simple, NO relación
    private String authenticationMethods;  // String simple, NO relación
    // Sin @OneToMany, @ManyToOne, etc.
}
```

#### 🔄 Flujo simple (sin relaciones):

```
1. partnerRepository.findByClientId(clientId)
   ↓ Ejecuta: SELECT * FROM partners WHERE client_id = ?
   ↓ Obtiene: Partner (con todos los campos)
   ↓ Cierra conexión
   ✅ No hay problema porque NO hay otras tablas que cargar
```

---

### 📋 Reglas para decidir cuándo usar `@Transactional`

| Situación | ¿Necesita @Transactional? | Razón |
|-----------|---------------------------|-------|
| Entidad con `@OneToMany`, `@ManyToOne`, `@ManyToMany` | ✅ SÍ | Puede necesitar múltiples consultas |
| Fetch `LAZY` | ✅ SÍ | Lazy loading requiere sesión abierta |
| Solo lectura simple sin relaciones | ❌ NO | Una sola consulta, no hay problema |
| Operaciones de escritura (INSERT, UPDATE, DELETE) | ✅ SÍ | Necesita commit/rollback |
| Múltiples operaciones que deben ser atómicas | ✅ SÍ | Todo o nada |

---

### 💡 Ejemplo visual de la diferencia

#### Con relaciones (CustomerUserDetails):
```
CustomerEntity
├── id
├── email
├── password
└── roles  ─────────┐
                    ├──> RoleEntity 1
                    ├──> RoleEntity 2
                    └──> RoleEntity 3
                    
⚠️ Necesita @Transactional para cargar roles
```

#### Sin relaciones (PartnerRegisteredClientService):
```
Partner
├── id
├── clientId
├── clientSecret
├── scopes (String)
├── grantTypes (String)
└── authenticationMethods (String)

✅ NO necesita @Transactional, todo en una tabla
```

---

### 🎯 Conclusión de Consulta 1

- **CustomerUserDetails necesita `@Transactional`** porque carga relaciones (`@OneToMany` con roles)
- **PartnerRegisteredClientService NO necesita `@Transactional`** porque solo hace una lectura simple sin relaciones
- La transacción mantiene la **sesión abierta** mientras se cargan las relaciones

---

## 🔍 Consulta 2: Explicación del primer `map` y Optional

### 🎯 Código analizado:

```java
return this.customerRepository.findByEmail(username)
    .map(customer -> {
        // Transformación aquí
    })
```

### ✅ Tu entendimiento es CORRECTO

**SÍ**, el `map` transforma un `Optional<CustomerEntity>` en un `Optional<UserDetails>`.

---

### 📊 Flujo completo paso a paso

#### 📥 Entrada:
```java
String username = "juan@gmail.com"  // Lo que el usuario ingresó en el login
```

#### 🔄 Paso 1: Buscar en BD

```java
this.customerRepository.findByEmail(username)
```

**Consulta SQL ejecutada:**
```sql
SELECT c.id, c.email, c.pwd 
FROM customers c 
WHERE c.email = 'juan@gmail.com'
```

**Resultado posible 1 (existe ✅):**
```java
Optional[CustomerEntity{
  id: 1,
  email: "juan@gmail.com",
  password: "$2a$10$encrypted...",
  roles: [...]
}]
```

**Resultado posible 2 (NO existe ❌):**
```java
Optional.empty()
```

---

#### 🔄 Paso 2: Transformar con `.map()`

```java
.map(customer -> {
    // Si Optional contiene CustomerEntity, ejecuta esto
    // Si Optional está vacío, salta esto
})
```

**Si existe el customer:**
```
Optional[CustomerEntity]
        ↓ .map()
Ejecuta la lambda y transforma CustomerEntity → UserDetails
        ↓
Optional[UserDetails]
```

**Si NO existe:**
```
Optional.empty()
        ↓ .map()
NO ejecuta la lambda
        ↓
Optional.empty()
```

---

### 🎨 Visualización del Optional

```
findByEmail("juan@gmail.com")
        ↓
    ¿Existe?
    /      \
  SÍ       NO
   ↓        ↓
Optional[  Optional.
Customer]  empty()
   ↓           ↓
  map()      map()
ejecuta    NO ejecuta
   ↓           ↓
Optional[  Optional.
UserDetails] empty()
   ↓           ↓
orElseThrow() ←─┘
   ↓
Exception
```

---

### 💻 Código equivalente sin Optional

```java
// Con Optional.map() (actual)
return this.customerRepository.findByEmail(username)
    .map(customer -> transformar(customer))
    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

// Sin Optional (equivalente)
CustomerEntity customer = this.customerRepository.findByEmailDirecto(username);
if (customer != null) {
    return transformar(customer);
} else {
    throw new UsernameNotFoundException("User not found");
}
```

---

### 📋 Resumen de Consulta 2

| Concepto | Explicación |
|----------|-------------|
| **findByEmail(username)** | Busca en BD por email, retorna `Optional<CustomerEntity>` |
| **Si existe** | `Optional[CustomerEntity{...}]` |
| **Si NO existe** | `Optional.empty()` |
| **.map()** | Transforma `CustomerEntity` → `UserDetails` |
| **Resultado** | `Optional<UserDetails>` |

---

## 🎭 Consulta 3: Extracción y transformación de roles

### 🎯 Código analizado:

```java
final var roles = customer.getRoles();
final var authorities = roles
```

### ✅ Tu entendimiento es CORRECTO

**SÍ**, obtienes la lista de roles del customer y luego la conviertes en Stream para transformarla.

---

### 📊 Flujo detallado

#### 📥 Paso 1: Obtener roles del customer

```java
final var roles = customer.getRoles();
```

**SQL ejecutado (gracias a `@OneToMany`):**
```sql
SELECT r.id, r.name, r.id_customer 
FROM roles r 
WHERE r.id_customer = 1
```

**Resultado:**
```java
List<RoleEntity> roles = [
  RoleEntity{id: 1, name: "ROLE_USER", idCustomer: 1},
  RoleEntity{id: 2, name: "ROLE_ADMIN", idCustomer: 1}
]
```

---

#### 🔄 Paso 2: Convertir a Stream

```java
final var authorities = roles.stream()
```

**Transformación:**
```
List[RoleEntity, RoleEntity]
        ↓ .stream()
Stream[RoleEntity, RoleEntity]
```

**Visualización:**
```
roles (List)
┌─────────────────────────┐
│ RoleEntity("ROLE_USER") │
│ RoleEntity("ROLE_ADMIN")│
└─────────────────────────┘
        ↓ .stream()
Stream
├─> RoleEntity("ROLE_USER")
└─> RoleEntity("ROLE_ADMIN")
```

---

### 🎯 ¿Por qué convertir a Stream?

Para poder usar **operaciones funcionales** como `map()`, `filter()`, `collect()`:

```java
roles.stream()
    .map(role -> transformar(role))      // Transformar cada elemento
    .filter(role -> filtrar(role))        // Filtrar elementos
    .collect(Collectors.toList())         // Convertir a lista
```

---

### 📋 Resumen de Consulta 3

| Paso | Código | Tipo | Contenido |
|------|--------|------|-----------|
| 1 | `customer.getRoles()` | `List<RoleEntity>` | `[RoleEntity, RoleEntity]` |
| 2 | `.stream()` | `Stream<RoleEntity>` | `Stream[RoleEntity, RoleEntity]` |
| 3 | Listo para transformar | - | Siguiente paso: `.map()` |

---

## 🔐 Consulta 4: Transformación de roles a authorities y creación de User

### 🎯 Código completo analizado:

```java
final var authorities = roles
    .stream()
    .map(role -> new SimpleGrantedAuthority(role.getName()))
    .collect(Collectors.toList());
return new User(customer.getEmail(), customer.getPassword(), authorities);
```

---

### 🔄 Paso a paso completo

#### 📥 Estado inicial:

```java
List<RoleEntity> roles = [
  RoleEntity{name: "ROLE_USER"},
  RoleEntity{name: "ROLE_ADMIN"}
]
```

---

#### 🔄 Paso 1: Stream

```java
roles.stream()
```

```
Stream[
  RoleEntity{name: "ROLE_USER"},
  RoleEntity{name: "ROLE_ADMIN"}
]
```

---

#### 🔄 Paso 2: Map (transformación)

```java
.map(role -> new SimpleGrantedAuthority(role.getName()))
```

**Proceso elemento por elemento:**

```
RoleEntity{name: "ROLE_USER"}
        ↓ role.getName()
"ROLE_USER"
        ↓ new SimpleGrantedAuthority(...)
SimpleGrantedAuthority("ROLE_USER")

RoleEntity{name: "ROLE_ADMIN"}
        ↓ role.getName()
"ROLE_ADMIN"
        ↓ new SimpleGrantedAuthority(...)
SimpleGrantedAuthority("ROLE_ADMIN")
```

**Resultado del Stream:**
```
Stream[
  SimpleGrantedAuthority("ROLE_USER"),
  SimpleGrantedAuthority("ROLE_ADMIN")
]
```

---

#### 🔄 Paso 3: Collect (convertir a lista)

```java
.collect(Collectors.toList())
```

```
Stream[SimpleGrantedAuthority, SimpleGrantedAuthority]
        ↓ .collect(Collectors.toList())
List[
  SimpleGrantedAuthority("ROLE_USER"),
  SimpleGrantedAuthority("ROLE_ADMIN")
]
```

---

#### 🔄 Paso 4: Crear User de Spring Security

```java
return new User(customer.getEmail(), customer.getPassword(), authorities);
```

**Parámetros:**
- `username`: `customer.getEmail()` → `"juan@gmail.com"`
- `password`: `customer.getPassword()` → `"$2a$10$encrypted..."`
- `authorities`: `List<SimpleGrantedAuthority>` → `["ROLE_USER", "ROLE_ADMIN"]`

**Resultado:**
```java
User{
  username: "juan@gmail.com",
  password: "$2a$10$encrypted...",
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

### 🔑 ¿Qué es `SimpleGrantedAuthority`?

#### 📖 Definición:
`SimpleGrantedAuthority` es una **implementación de la interfaz `GrantedAuthority`** de Spring Security que representa un **permiso o rol**.

#### 🎯 ¿Para qué sirve?

Spring Security usa `GrantedAuthority` para:

- ✅ **Control de acceso**: Verificar si un usuario tiene permisos
- ✅ **Autorización**: Decidir qué recursos puede acceder
- ✅ **Roles**: Representar roles como ROLE_USER, ROLE_ADMIN

---

### 📊 Jerarquía de interfaces

```
GrantedAuthority (interfaz)
        ↑
        │ implementa
        │
SimpleGrantedAuthority (clase)
```

**Código de SimpleGrantedAuthority:**
```java
public class SimpleGrantedAuthority implements GrantedAuthority {
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

### 🔐 Uso en Spring Security

#### Ejemplo de autorización en un controller:

```java
@GetMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // ← Busca SimpleGrantedAuthority("ROLE_ADMIN")
public String adminPanel() {
    return "admin";
}

@GetMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ← Busca cualquiera de estos roles
public String userPanel() {
    return "user";
}
```

**Spring Security internamente:**
```
1. Usuario hace request a /admin
2. Spring Security carga UserDetails
3. Obtiene authorities: [SimpleGrantedAuthority("ROLE_ADMIN")]
4. Verifica si contiene "ROLE_ADMIN"
5. Si SÍ → permite acceso ✅
6. Si NO → 403 Forbidden ❌
```

---

### ❌ Manejo cuando NO se encuentra el usuario

```java
.orElseThrow(() -> new UsernameNotFoundException("User not found"));
```

#### 🔄 Flujo:

```
findByEmail("usuario-inexistente@gmail.com")
        ↓
Optional.empty()
        ↓
.map() NO se ejecuta (Optional vacío)
        ↓
.orElseThrow() se ejecuta
        ↓
throw new UsernameNotFoundException("User not found")
```

**Spring Security captura esta excepción y:**
- ❌ Rechaza el login
- 📝 Retorna error 401 Unauthorized
- 🔒 NO revela si el email existe o no (seguridad)

---

### 📊 Tabla comparativa de transformaciones

| Paso | Tipo | Contenido |
|------|------|-----------|
| 1. `customer.getRoles()` | `List<RoleEntity>` | `[RoleEntity{name:"ROLE_USER"}, ...]` |
| 2. `.stream()` | `Stream<RoleEntity>` | `Stream[RoleEntity, ...]` |
| 3. `.map(role -> new SimpleGrantedAuthority(role.getName()))` | `Stream<SimpleGrantedAuthority>` | `Stream[SimpleGrantedAuthority("ROLE_USER"), ...]` |
| 4. `.collect(Collectors.toList())` | `List<SimpleGrantedAuthority>` | `[SimpleGrantedAuthority("ROLE_USER"), ...]` |
| 5. `new User(email, password, authorities)` | `User` (UserDetails) | `User{username, password, authorities}` |

---

### 🎨 Visualización completa del flujo

```
CustomerEntity (BD)
├── email: "juan@gmail.com"
├── password: "$2a$10$..."
└── roles: List[RoleEntity]
           ├── RoleEntity{name: "ROLE_USER"}
           └── RoleEntity{name: "ROLE_ADMIN"}
                    ↓ .stream()
           Stream[RoleEntity, RoleEntity]
                    ↓ .map(role -> new SimpleGrantedAuthority(role.getName()))
           Stream[SimpleGrantedAuthority, SimpleGrantedAuthority]
                    ↓ .collect(Collectors.toList())
           List[SimpleGrantedAuthority, SimpleGrantedAuthority]
                    ↓ new User(email, password, authorities)
User (Spring Security)
├── username: "juan@gmail.com"
├── password: "$2a$10$..."
└── authorities: [
    ├── SimpleGrantedAuthority("ROLE_USER")
    └── SimpleGrantedAuthority("ROLE_ADMIN")
]
```

---

## 🎓 Resumen final del flujo completo

```
1. Usuario ingresa: email + password
        ↓
2. Spring Security llama: loadUserByUsername(email)
        ↓
3. Busca en BD: customerRepository.findByEmail(email)
        ↓
4. Si existe:
   a. Obtiene CustomerEntity con roles
   b. Transforma roles → authorities
      - List<RoleEntity> → Stream
      - Stream → map(new SimpleGrantedAuthority)
      - Stream → collect(toList)
   c. Crea User de Spring Security
   d. Retorna UserDetails
        ↓
5. Spring Security valida password
        ↓
6. Si coincide: Autenticación exitosa ✅
7. Si NO: Autenticación fallida ❌
```

---

## 🏆 Conclusión general

- ✅ **@Transactional** es necesario cuando hay relaciones (@OneToMany)
- ✅ **Optional.map()** transforma CustomerEntity → UserDetails
- ✅ **Stream** permite transformar List<RoleEntity> → List<SimpleGrantedAuthority>
- ✅ **SimpleGrantedAuthority** representa roles para Spring Security
- ✅ **User** es la implementación de UserDetails que Spring Security usa para autenticación
---

# 🔐 Diferencia entre RegisteredClientRepository y UserDetailsService en OAuth2

## 🎯 Respuesta directa

Son **dos cosas completamente diferentes** que cumplen roles distintos en OAuth2:

| Aspecto | RegisteredClientRepository | UserDetailsService |
|---------|---------------------------|-------------------|
| **Representa** | 🖥️ Aplicaciones cliente | 👤 Usuarios finales |
| **Busca** | clientId (app) | username (persona) |
| **Retorna** | RegisteredClient | UserDetails |
| **Valida** | Credenciales de la aplicación | Credenciales del usuario |
| **Usado en** | Autenticación de cliente OAuth2 | Autenticación del usuario |

---

## 🏗️ Arquitectura OAuth2 - Los 3 actores principales

```
┌─────────────────────────────────────────────────────────────┐
│                    FLUJO OAUTH2 COMPLETO                     │
└─────────────────────────────────────────────────────────────┘

1️⃣ USUARIO (Resource Owner)
   👤 Juan Pérez
   📧 juan@gmail.com
   🔑 password123
   
2️⃣ APLICACIÓN CLIENTE (Client)
   🖥️ App "Debuggeando Ideas"
   🆔 clientId: "debuggeandoideas"
   🔐 clientSecret: "secret"
   
3️⃣ SERVIDOR DE AUTORIZACIÓN (Authorization Server)
   🏢 Tu aplicación Spring Boot
   ├─ RegisteredClientRepository ← Valida la APLICACIÓN
   └─ UserDetailsService ← Valida al USUARIO
```

---

## 🔄 Flujo completo OAuth2 - ¿Cuándo se usa cada uno?

### 📱 Escenario real: Login con OAuth2

```
Usuario Juan quiere acceder a su cuenta en la app "Debuggeando Ideas"
```

---

### 🎬 ACTO 1: La aplicación se presenta

```
┌────────────────────────────────────────────────────────────┐
│ 1. App "Debuggeando Ideas" hace una petición:             │
│                                                             │
│    GET /oauth2/authorize?                                  │
│        client_id=debuggeandoideas                          │
│        &response_type=code                                 │
│        &redirect_uri=https://oauthdebugger.com/debug       │
│        &scope=read,write                                   │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 2. Spring Security llama a:                                │
│    RegisteredClientRepository.findByClientId(              │
│        "debuggeandoideas"                                  │
│    )                                                       │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 3. ¿Esta aplicación está registrada?                       │
│                                                             │
│    ✅ SÍ existe: RegisteredClient{                         │
│        clientId: "debuggeandoideas",                       │
│        clientSecret: "secret",                             │
│        redirectUri: "https://oauthdebugger.com/debug",     │
│        scopes: ["read", "write"]                           │
│    }                                                       │
│                                                             │
│    ✅ Aplicación VALIDADA                                  │
└────────────────────────────────────────────────────────────┘
```

**🎯 RegisteredClientRepository valida que la APLICACIÓN esté autorizada**

---

### 🎬 ACTO 2: El usuario se autentica

```
┌────────────────────────────────────────────────────────────┐
│ 4. Spring Security muestra pantalla de login:             │
│                                                             │
│    ┌─────────────────────────────┐                        │
│    │   🔐 Login                  │                        │
│    │                              │                        │
│    │   Email: [juan@gmail.com  ] │                        │
│    │   Password: [••••••••••••] │                        │
│    │                              │                        │
│    │   [  Iniciar Sesión  ]      │                        │
│    └─────────────────────────────┘                        │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 5. Usuario ingresa sus credenciales y envía               │
│                                                             │
│    POST /login                                             │
│    username=juan@gmail.com                                 │
│    password=password123                                    │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 6. Spring Security llama a:                                │
│    UserDetailsService.loadUserByUsername(                  │
│        "juan@gmail.com"                                    │
│    )                                                       │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 7. ¿Este usuario existe?                                   │
│                                                             │
│    ✅ SÍ existe: UserDetails{                              │
│        username: "juan@gmail.com",                         │
│        password: "$2a$10$encrypted...",                    │
│        authorities: ["ROLE_USER", "ROLE_ADMIN"]            │
│    }                                                       │
│                                                             │
│    ✅ Usuario VALIDADO                                     │
└────────────────────────────────────────────────────────────┘
```

**🎯 UserDetailsService valida que el USUARIO sea legítimo**

---

### 🎬 ACTO 3: Generación del código de autorización

```
┌────────────────────────────────────────────────────────────┐
│ 8. Spring Security genera código de autorización:         │
│                                                             │
│    Authorization Code: "abc123xyz789"                      │
│                                                             │
│    Este código está asociado a:                            │
│    - Usuario: juan@gmail.com                               │
│    - Cliente: debuggeandoideas                             │
│    - Scopes: read, write                                   │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 9. Redirección a la aplicación:                            │
│                                                             │
│    HTTP/1.1 302 Found                                      │
│    Location: https://oauthdebugger.com/debug?              │
│              code=abc123xyz789                             │
└────────────────────────────────────────────────────────────┘
```

---

### 🎬 ACTO 4: La aplicación intercambia el código por token

```
┌────────────────────────────────────────────────────────────┐
│ 10. Aplicación hace petición:                              │
│                                                             │
│     POST /oauth2/token                                     │
│     grant_type=authorization_code                          │
│     code=abc123xyz789                                      │
│     client_id=debuggeandoideas                             │
│     client_secret=secret                                   │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 11. Spring Security vuelve a validar el cliente:           │
│     RegisteredClientRepository.findByClientId(             │
│         "debuggeandoideas"                                 │
│     )                                                      │
│                                                             │
│     ✅ Cliente válido                                      │
│     ✅ Client secret correcto                              │
│     ✅ Código válido                                       │
└────────────────────────────────────────────────────────────┘
        ↓
┌────────────────────────────────────────────────────────────┐
│ 12. Spring Security genera tokens:                         │
│                                                             │
│     {                                                      │
│       "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6...",  │
│       "token_type": "Bearer",                              │
│       "expires_in": 28800,                                 │
│       "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cC...",   │
│       "scope": "read write"                                │
│     }                                                      │
└────────────────────────────────────────────────────────────┘
```

---

## 📊 Comparación detallada

### 🖥️ RegisteredClientRepository

#### 🎯 Propósito:
Gestionar **aplicaciones cliente** que quieren acceder a recursos protegidos.

#### 📝 Datos que maneja:

```java
RegisteredClient {
    id: "1",
    clientId: "debuggeandoideas",           // ← Identificador de la APP
    clientSecret: "secret",                  // ← Password de la APP
    clientName: "Debuggeando Ideas",
    authorizationGrantTypes: [               // ← Cómo puede obtener tokens
        AUTHORIZATION_CODE,
        REFRESH_TOKEN
    ],
    clientAuthenticationMethods: [           // ← Cómo se autentica la APP
        CLIENT_SECRET_BASIC,
        CLIENT_SECRET_JWT
    ],
    redirectUris: [                          // ← A dónde redirigir después
        "https://oauthdebugger.com/debug"
    ],
    scopes: ["read", "write"]                // ← Qué permisos solicita
}
```

#### 🔍 Búsqueda:
```java
RegisteredClient client = registeredClientRepository
    .findByClientId("debuggeandoideas");
```

#### ✅ Valida:
- ¿La aplicación está registrada?
- ¿El client_secret es correcto?
- ¿La redirect_uri es válida?
- ¿Los scopes solicitados están permitidos?

---

### 👤 UserDetailsService

#### 🎯 Propósito:
Gestionar **usuarios** que son dueños de los recursos.

#### 📝 Datos que maneja:

```java
UserDetails {
    username: "juan@gmail.com",              // ← Email del USUARIO
    password: "$2a$10$encrypted...",         // ← Password del USUARIO
    authorities: [                            // ← Roles del USUARIO
        SimpleGrantedAuthority("ROLE_USER"),
        SimpleGrantedAuthority("ROLE_ADMIN")
    ],
    enabled: true,
    accountNonExpired: true,
    credentialsNonExpired: true,
    accountNonLocked: true
}
```

#### 🔍 Búsqueda:
```java
UserDetails user = userDetailsService
    .loadUserByUsername("juan@gmail.com");
```

#### ✅ Valida:
- ¿El usuario existe?
- ¿La contraseña es correcta?
- ¿La cuenta está activa?
- ¿Qué roles/permisos tiene?

---

## 🤝 ¿Cómo interactúan en OAuth2?

### 🔄 Secuencia de validaciones

```
┌─────────────────────────────────────────────────────────────┐
│              FLUJO COMPLETO DE VALIDACIONES                  │
└─────────────────────────────────────────────────────────────┘

1. Validación de la APLICACIÓN
   ↓
   RegisteredClientRepository
   ├─ ¿clientId existe?
   ├─ ¿clientSecret correcto?
   ├─ ¿redirect_uri válida?
   └─ ¿scopes permitidos?
   
2. Validación del USUARIO
   ↓
   UserDetailsService
   ├─ ¿username existe?
   ├─ ¿password correcto?
   ├─ ¿cuenta activa?
   └─ ¿tiene permisos?

3. Generación de tokens
   ↓
   Token contiene información de:
   ├─ Usuario (juan@gmail.com)
   ├─ Cliente (debuggeandoideas)
   └─ Scopes (read, write)
```

---

## 🎭 Analogía del mundo real

### 🏦 Como un banco:

```
🏢 BANCO (Authorization Server)
├─ 🏪 Ventanilla de empresas (RegisteredClientRepository)
│   │
│   └─ Valida que la EMPRESA esté registrada
│      "¿Tienes RUC? ¿Estás autorizada para hacer retiros?"
│
└─ 👥 Ventanilla de personas (UserDetailsService)
    │
    └─ Valida que la PERSONA sea cliente del banco
       "¿Tienes cuenta? ¿Tu DNI es correcto? ¿Tu PIN es válido?"
```

#### 📝 Escenario:

```
Una empresa de contabilidad (CLIENT) quiere acceder
a la cuenta bancaria de Juan (USER)

1. Banco verifica: ¿La empresa está registrada? ✅
   (RegisteredClientRepository)

2. Banco pregunta a Juan: ¿Autorizas a esta empresa? 
   Juan ingresa su DNI y PIN ✅
   (UserDetailsService)

3. Banco genera un permiso temporal para que la empresa
   acceda solo a lo autorizado ✅
   (Access Token)
```

---

## 📋 Tabla de diferencias clave

| Característica | RegisteredClientRepository | UserDetailsService |
|----------------|---------------------------|-------------------|
| **Representa** | Aplicación/Cliente OAuth2 | Usuario final |
| **Busca por** | clientId | username/email |
| **Credencial** | clientSecret | password |
| **Retorna** | RegisteredClient | UserDetails |
| **Cuándo se usa** | Al autorizar cliente | Al autenticar usuario |
| **Información** | Configuración OAuth2 de la app | Datos y roles del usuario |
| **Tabla BD** | `partners` (en tu caso) | `customers` (en tu caso) |
| **Se valida** | 2 veces (authorize + token) | 1 vez (login) |

---

## 🔐 ¿Por qué son AMBOS necesarios?

### ❌ Sin RegisteredClientRepository:

```
Cualquier aplicación podría:
├─ Solicitar tokens sin estar registrada
├─ Usar redirect_uri maliciosas (phishing)
├─ Solicitar scopes no autorizados
└─ Robar tokens de otras aplicaciones

🚨 PELIGRO: No sabrías qué aplicación está accediendo
```

### ❌ Sin UserDetailsService:

```
Cualquiera podría:
├─ Generar tokens sin autenticar al usuario real
├─ Acceder a recursos sin validar identidad
├─ No habría control de roles/permisos
└─ No sabrías a nombre de quién se accede

🚨 PELIGRO: No sabrías QUÉ usuario está detrás
```

### ✅ Con AMBOS:

```
Seguridad completa:
├─ ✅ Aplicación verificada (RegisteredClientRepository)
├─ ✅ Usuario autenticado (UserDetailsService)
├─ ✅ Permisos controlados (scopes + roles)
└─ ✅ Trazabilidad completa

🎯 Token contiene:
   - Qué aplicación lo solicitó
   - Para qué usuario
   - Qué puede hacer
```

---

## 🎯 Visualización del token resultante

```json
{
  "sub": "juan@gmail.com",              ← UserDetailsService
  "aud": "debuggeandoideas",            ← RegisteredClientRepository
  "scope": ["read", "write"],           ← RegisteredClientRepository
  "authorities": [                       ← UserDetailsService
    "ROLE_USER",
    "ROLE_ADMIN"
  ],
  "iss": "http://localhost:9000",
  "exp": 1234567890,
  "iat": 1234567890
}
```

---

## 🏁 Conclusión

### 🎯 Respuestas directas:

**¿Se parecen?**
- Sí, en estructura (ambos buscan y validan)
- No, en propósito (uno valida apps, otro usuarios)

**¿Cómo interactúan?**
- Trabajan en SECUENCIA:
  1. Primero valida la aplicación
  2. Luego valida al usuario
  3. Juntos generan el token

**¿Por qué son necesarios?**
- **RegisteredClientRepository**: Controla QUÉ APLICACIONES acceden
- **UserDetailsService**: Controla QUÉ USUARIOS permiten el acceso
- **Juntos**: Proporcionan seguridad completa en OAuth2

### 📊 Regla mnemotécnica:

```
RegisteredClientRepository = "¿QUIÉN pregunta?" (la app)
UserDetailsService = "¿A NOMBRE DE QUIÉN?" (el usuario)
```

**Ambos son necesarios porque OAuth2 es un protocolo de DELEGACIÓN**:
El usuario DELEGA a una aplicación el acceso a sus recursos.

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









