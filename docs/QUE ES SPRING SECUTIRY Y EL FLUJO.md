# 🔐 Spring Security — El Ecosistema Completo

¡Tranquilo! Es normal sentirse confundido porque Spring Security es como una **cebolla con muchas capas**. Déjame darte el panorama completo para que todo encaje.

---

## 🧠 La Gran Imagen: ¿Qué es Spring Security realmente?

Spring Security **no es una sola cosa**, es un **framework de seguridad completo** que resuelve estas preguntas:

```
1. 🚪 ¿QUIÉN ERES?          → Autenticación (Authentication)
2. 🔑 ¿QUÉ PUEDES HACER?    → Autorización (Authorization)
3. 🛡️ ¿CÓMO TE PROTEJO?     → Protecciones (CSRF, CORS, Headers, etc.)
```

Todo lo que estás aprendiendo (CSRF, CORS, filtros, JWT, OAuth2) son **herramientas dentro de este framework** para responder esas 3 preguntas.

---

## 🏗️ La Arquitectura: El Flujo Completo

```
                        🌐 CLIENTE (Browser / App Móvil / Postman)
                                      │
                                      ▼
                        ┌─────────────────────────────┐
                        │      SERVIDOR (Spring Boot)  │
                        │                              │
                        │  ┌────────────────────────┐  │
                        │  │  SECURITY FILTER CHAIN  │  │  ← Aquí vive Spring Security
                        │  │                         │  │
                        │  │  ┌──────────────────┐   │  │
                        │  │  │ CORS Filter       │   │  │  ← Capa 1: ¿De dónde vienes?
                        │  │  ├──────────────────┤   │  │
                        │  │  │ CSRF Filter       │   │  │  ← Capa 2: ¿Es request legítimo?
                        │  │  ├──────────────────┤   │  │
                        │  │  │ Authentication    │   │  │  ← Capa 3: ¿Quién eres?
                        │  │  │ Filter            │   │  │     (aquí entra JWT/OAuth2/
                        │  │  │                   │   │  │      Session/Basic Auth)
                        │  │  ├──────────────────┤   │  │
                        │  │  │ TUS FILTROS       │   │  │  ← Capa 4: Tu lógica custom
                        │  │  │ PERSONALIZADOS    │   │  │
                        │  │  ├──────────────────┤   │  │
                        │  │  │ Authorization     │   │  │  ← Capa 5: ¿Tienes permiso?
                        │  │  │ Filter            │   │  │
                        │  │  └──────────────────┘   │  │
                        │  └────────────────────────┘  │
                        │              │                │
                        │              ▼                │
                        │     ┌─────────────────┐      │
                        │     │  TU CONTROLLER   │      │  ← Si pasó todo, llega aquí
                        │     └─────────────────┘      │
                        └─────────────────────────────┘
```

### Lo que ya aprendiste son **piezas de este flujo**:
| Lo que sabes | Dónde encaja | Qué resuelve |
|---|---|---|
| **CORS** | Filtro temprano | Controla qué dominios pueden hacer requests |
| **CSRF** | Filtro temprano | Evita que sitios maliciosos hagan requests por ti |
| **Filtros personalizados** | En cualquier punto de la cadena | Tu lógica de seguridad custom |

---

## 🔄 Ahora... ¿Dónde entra JWT y OAuth2?

Aquí es donde se confunde la gente. Son **estrategias de AUTENTICACIÓN** (el "¿quién eres?"):

### 📊 Las formas de autenticarse en Spring Security:

```
 ¿CÓMO DEMUESTRAS QUIÉN ERES?
 ─────────────────────────────────────────────────────────────
 │
 ├── 🍪 SESIONES (lo tradicional - Web con formulario)
 │   │   Usuario envía user/password → Server crea una sesión
 │   │   → Guarda cookie JSESSIONID → Cada request la envía
 │   │
 │   └── Aquí CSRF es IMPORTANTE (porque el browser
 │       envía cookies automáticamente)
 │
 ├── 🎫 JWT (lo moderno - APIs REST)
 │   │   Usuario envía user/password → Server genera un TOKEN
 │   │   → Cliente guarda el token → Lo envía en cada request
 │   │   en el header: Authorization: Bearer eyJhbGciOi...
 │   │
 │   └── Aquí CSRF generalmente se DESACTIVA
 │       (el token NO se envía automáticamente)
 │
 └── 🌐 OAUTH2 (delegación - "Login con Google/GitHub")
     │   Usuario NO te da su password a TI
     │   → Lo redirige a Google → Google lo autentica
     │   → Google te da un token → Tú confías en Google
     │
     └── Puede usar JWT internamente
         (los tokens de OAuth2 muchas veces SON JWTs)
```

---

## 🎫 JWT a Fondo — El Flujo

```
 CLIENTE                          TU API (Spring Boot)
 ──────                          ──────────────────────
    │                                     │
    │  1. POST /auth/login                │
    │     { "user":"ana", "pass":"123" }  │
    │ ──────────────────────────────────► │
    │                                     │ 2. Valida credenciales
    │                                     │    contra DB
    │                                     │
    │  3. Respuesta:                      │ 
    │     { "token": "eyJhbGciOi..." }   │ 4. Genera JWT firmado
    │ ◄────────────────────────────────── │    con clave secreta
    │                                     │
    │  5. Guarda el token (localStorage)  │
    │                                     │
    │  ═══════════════════════════════════│═══ (Requests futuros)
    │                                     │
    │  6. GET /api/productos              │
    │     Header: Authorization:          │
    │       Bearer eyJhbGciOi...          │
    │ ──────────────────────────────────► │
    │                                     │ 7. TU FILTRO JWT:
    │                                     │    - Extrae token del header
    │                                     │    - Valida firma
    │                                     │    - Extrae usuario
    │                                     │    - Crea Authentication
    │                                     │    - Lo pone en SecurityContext
    │                                     │
    │  8. { "productos": [...] }          │ 9. Authorization Filter verifica
    │ ◄────────────────────────────────── │    roles/permisos → OK → Controller
```

**¿Ves?** Tu filtro personalizado que ya aprendiste es **exactamente** lo que usas para validar el JWT en cada request. ¡Ya tenías la base!

---

## 🌐 OAuth2 a Fondo — El Flujo

```
 USUARIO          TU APP            GOOGLE (Proveedor OAuth2)
 ───────          ──────            ──────────────────────────
    │                │                        │
    │ 1. Click      │                        │
    │ "Login con    │                        │
    │  Google"      │                        │
    │ ────────────► │                        │
    │               │ 2. Redirect a Google   │
    │               │ ─────────────────────► │
    │               │                        │
    │ 3. Google muestra pantalla de login    │
    │ ◄──────────────────────────────────────│
    │                                        │
    │ 4. Usuario pone SU password de GOOGLE  │
    │ ──────────────────────────────────────►│
    │                                        │
    │               │ 5. Google devuelve un  │
    │               │    "authorization code"│
    │               │ ◄─────────────────────│
    │               │                        │
    │               │ 6. Tu app intercambia  │
    │               │    el code por un      │
    │               │    ACCESS TOKEN        │
    │               │ ─────────────────────► │
    │               │                        │
    │               │ 7. Google devuelve     │
    │               │    access_token (+ JWT)│
    │               │ ◄───────────────────── │
    │               │                        │
    │ 8. Tu app ya sabe quién es el usuario  │
    │    y puede crear una sesión o su       │
    │    propio JWT interno                  │
    │ ◄──────────── │                        │
```

---

## 🧩 Cómo encaja TODO junto

```
                    SPRING SECURITY
         ┌──────────────────────────────────┐
         │                                   │
         │   PROTECCIÓN          AUTENTICACIÓN          AUTORIZACIÓN
         │   ──────────          ──────────────          ────────────
         │   • CORS              Elige UNA (o más):      • Roles
         │   • CSRF              ┌─────────────────┐     • Authorities
         │   • Headers           │ 🍪 Sesiones      │     • @PreAuthorize
         │   • Rate Limiting     │ 🎫 JWT           │     • .hasRole()
         │   (filtros custom)    │ 🌐 OAuth2        │     • .hasAuthority()
         │                       │ 📝 Basic Auth    │
         │                       └─────────────────┘
         │                                   │
         │      Todo pasa por la FILTER CHAIN │
         └──────────────────────────────────┘
```

### La relación entre JWT y OAuth2:

```
• JWT    = Un FORMATO de token (como un "carnet de identidad digital")
• OAuth2 = Un PROTOCOLO de autorización (como el "proceso para obtener el carnet")

OAuth2 puede usar JWT como formato de sus tokens.
JWT puede usarse SIN OAuth2 (tú generas tu propio token).

         ┌──────────────────────────────┐
         │         OAuth2               │
         │  (el proceso/protocolo)      │
         │                              │
         │    ┌──────────────────┐      │
         │    │      JWT         │      │
         │    │  (el formato     │      │
         │    │   del token)     │      │
         │    └──────────────────┘      │
         └──────────────────────────────┘
        
  OAuth2 PUEDE usar JWT... o tokens opacos.
  JWT PUEDE existir sin OAuth2.
```

---

## 📋 Roadmap: Lo que deberías saber para Abril/Junio

### ✅ Nivel 1 — Fundamentos (lo que ya tienes)
- [x] Cómo funciona la Filter Chain
- [x] CORS y CSRF
- [x] Filtros personalizados
- [x] Configuración básica con `SecurityFilterChain`

### 🔲 Nivel 2 — Autenticación con JWT (apréndelo bien)
- [ ] Crear endpoint `/auth/login` y `/auth/register`
- [ ] Generar tokens JWT (con librería `jjwt` o `java-jwt`)
- [ ] Crear un `JwtAuthenticationFilter` (tu filtro custom)
- [ ] Validar tokens y setear `SecurityContextHolder`
- [ ] Refresh Tokens (importante para producción)
- [ ] Manejo de expiración de tokens

### 🔲 Nivel 3 — OAuth2 (al menos lo básico)
- [ ] Login con Google/GitHub usando `spring-boot-starter-oauth2-client`
- [ ] Entender Authorization Code Flow
- [ ] Resource Server (`spring-boot-starter-oauth2-resource-server`)
- [ ] Diferencia entre OAuth2 Client vs Resource Server

### 🔲 Nivel 4 — Lo que te hará destacar en entrevistas
- [ ] **Role-based access control (RBAC)** con `@PreAuthorize`
- [ ] **Method-level security** (`@Secured`, `@RolesAllowed`)
- [ ] **Manejo de excepciones** de seguridad (`AuthenticationEntryPoint`, `AccessDeniedHandler`)
- [ ] **Password encoding** con `BCryptPasswordEncoder`
- [ ] **Arquitectura**: saber explicar `AuthenticationManager` → `AuthenticationProvider` → `UserDetailsService`
- [ ] **Testing** de seguridad con `@WithMockUser`

### 🔲 Nivel 5 — Bonus (te diferencia de otros juniors)
- [ ] Integrar Spring Security con un **proyecto real** (CRUD + auth)
- [ ] Tener un proyecto en GitHub que muestre todo esto
- [ ] Entender HTTPS y por qué JWT sin HTTPS es inseguro
- [ ] Conceptos básicos de **OWASP Top 10**

---

## 💡 Consejo Final para tu CV

Para abril/junio, lo que más te va a servir es tener **un proyecto en GitHub** que muestre:

```
📁 mi-proyecto-spring-security/
├── 🔐 Registro y Login con JWT
├── 🌐 Login con Google (OAuth2)
├── 👥 Roles (ADMIN, USER)
├── 🛡️ Endpoints protegidos por rol
├── 📝 Documentación clara en README
└── ✅ Tests de seguridad
```

> **Un proyecto funcional en tu GitHub vale más que decir "sé Spring Security" en el CV.**

¡Mucho éxito con tu búsqueda de empleo! El hecho de que estés profundizando en Spring Security ya te pone por delante de muchos candidatos junior. 💪