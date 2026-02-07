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
