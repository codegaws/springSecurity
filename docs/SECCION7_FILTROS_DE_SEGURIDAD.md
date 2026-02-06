# 🛡️ 🌐🔒 Spring Security — FILTROS DE SEGURIDAD

## 📝 Clase 49 - INTRODUCCION FILTROS DE SEGURIDAD 🔒 🔒 🔑🔑

- Muy relacionados a los servlets
- Los filtros de seguridad son componentes que interceptan las solicitudes HTTP antes de que lleguen a los controladores
  de Spring MVC.
- Permiten aplicar reglas de seguridad, como autenticación y autorización, a las solicitudes entrantes.
- Se configuran en la cadena de filtros de seguridad de Spring Security.
- Pueden realizar tareas como:
    - Validar tokens de autenticación.
    - Verificar permisos de usuario.
    - Registrar actividades de seguridad.
- Se implementan extendiendo la clase `OncePerRequestFilter` o implementando la interfaz `Filter`.
- Se configuran en la clase de configuración de seguridad mediante el método `addFilterBefore` o `addFilterAfter`.

---

![img](img/img_28.png)

---

![img](img/img_29.png)

---

![img](img/img_30.png)

## 📝 Clase 50 - HABILITANDO EL MODO DEBUG DE SPRING SECURITY 🔒 🔒 🔑🔑

![img](img/img_31.png)

- El modo debug de Spring Security proporciona información detallada sobre el proceso de autenticación y autorización.
- Para habilitarlo, se puede configurar en la clase de configuración de seguridad utilizando el método `debug(true)`.
- Esto permite ver en la consola los pasos que Spring Security sigue para autenticar y autorizar a los usuarios,
- lo que es útil para depurar problemas de seguridad.

- Agregar
  ![img](img/img_32.png)

### Resultado de un debug

```xml
org.apache.catalina.connector.RequestFacade@5f8407e2

servletPath:/loans
pathInfo:null
headers: 
Authorization: Basic bG9hbnNAZGVidWdnZWFuZG9pZWFzLmNvbTp0b19iZV9lbmNvZGVk
User-Agent: PostmanRuntime/7.49.1
Accept: */*
Cache-Control: no-cache
Postman-Token: 6a835fc4-ef55-4ac8-8016-19c17615f213
Host: localhost:8080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Cookie: XSRF-TOKEN=b02c9068-c173-4470-948e-12d4432930af


Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  CsrfFilter
  LogoutFilter
  UsernamePasswordAuthenticationFilter
  DefaultResourcesFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  BasicAuthenticationFilter
  CsrfCookieFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  ExceptionTranslationFilter
  AuthorizationFilter

```
---

## 📝 Clase 51 - IMPLEMENTANDO UN FILTRO PARA IMPLEMENTAR UN APIKEY🔒 🔒 🔑🔑
### 🔐 Explicación del Flujo de Filtros en Spring Security

- En security config
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);
```
-En ApiKeyFilter 

```java

public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY = "myKey";
    private static final String API_KEY_HEADER = "api_key";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final var apiKeyOpt = Optional.of(request.getHeader(API_KEY_HEADER));
            final var apiKey = apiKeyOpt.orElseThrow(() -> new BadCredentialsException("Not Header api key"));
            if (!apiKey.equals(API_KEY)) {
                throw new BadCredentialsException("Invalid api key");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid api key");
        }
        filterChain.doFilter(request, response);
    }
}
```

---

#### 📋 ¿Qué es un Filtro en Spring Security?

Un **filtro** es un componente que intercepta las peticiones HTTP **antes** de que lleguen a los controladores. Spring Security usa una **cadena de filtros** para aplicar seguridad.

---

#### 🔄 Flujo Completo de una Petición

```
Cliente (Browser/Postman)
│
▼
┌─────────────┐
│   Request   │  GET /loans + Header: api_key=myKey
└─────────────┘
│
▼
┌─────────────────────────────────────────┐
│      SPRING SECURITY FILTER CHAIN       │
├─────────────────────────────────────────┤
│  1. 🔑 ApiKeyFilter (TU FILTRO)         │ ◄── addFilterBefore()
│  2. 🔒 BasicAuthenticationFilter        │
│  3. 🛡️ CsrfFilter                       │
│  4. 📝 Otros filtros...                 │
└─────────────────────────────────────────┘
│
▼
┌─────────────┐
│ Controller  │  @GetMapping("/loans")
└─────────────┘
│
▼
┌─────────────┐
│  Response   │
└─────────────┘
```

---

#### 🧩 ¿Qué hace `addFilterBefore()`?

```java
http.addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);
```

| Parte | Significado |
|-------|-------------|
| `addFilterBefore` | Agrega un filtro **antes** de otro filtro existente |
| `new ApiKeyFilter()` | Tu filtro personalizado que valida la API Key |
| `BasicAuthenticationFilter.class` | El filtro de referencia (tu filtro se ejecuta **antes** de este) |

---

#### ⚙️ ¿Cómo funciona `ApiKeyFilter`?

```java
public class ApiKeyFilter extends OncePerRequestFilter
```

| Componente | Descripción |
|------------|-------------|
| `OncePerRequestFilter` | 🔁 Garantiza que el filtro se ejecute **solo una vez** por petición |
| `doFilterInternal()` | 🎯 Método donde va tu lógica de validación |

---

#### 🔍 Flujo dentro de `ApiKeyFilter`

```
Petición entrante
       │
       ▼
┌──────────────────────────────────┐
│ 1. Obtener header "api_key"      │
│    request.getHeader("api_key")  │
└──────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────┐
│ 2. ¿Existe el header?            │
│    ❌ NO → BadCredentialsException│
│    ✅ SÍ → Continúa               │
└──────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────┐
│ 3. ¿apiKey == "myKey"?           │
│    ❌ NO → BadCredentialsException│
│    ✅ SÍ → Continúa               │
└──────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────┐
│ 4. filterChain.doFilter()        │
│    → Pasa al siguiente filtro    │
└──────────────────────────────────┘
```

---

#### 📊 Ejemplo Práctico

| Escenario | Header Enviado | Resultado |
|-----------|----------------|-----------|
| ✅ Válido | `api_key: myKey` | Continúa al controller |
| ❌ Key incorrecta | `api_key: wrongKey` | `401 Unauthorized` |
| ❌ Sin header | (ninguno) | `401 Unauthorized` |

---

#### 🎯 Resumen Visual

```
┌─────────────────────────────────────────────────────────┐
│                    ORDEN DE EJECUCIÓN                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Request → [ApiKeyFilter] → [BasicAuth] → Controller  │
│                  ▲                                      │
│                  │                                      │
│         Tu filtro va PRIMERO                           │
│         gracias a addFilterBefore()                    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

#### 💡 Punto Clave

`filterChain.doFilter(request, response)` es **crucial**: si no lo llamas, la petición **nunca** llegará al siguiente filtro ni al controller.
