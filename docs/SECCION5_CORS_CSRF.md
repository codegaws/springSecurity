# 🛡️ 🌐🔒 Spring Security — CORS & CSRF Explained🛡️

## 📝 Clase 35 - QUE ES CORS CSRF 🔒 🔒 🔑🔑

---
## 🌐 ¿Qué es CORS?

**CORS** (Cross-Origin Resource Sharing, Compartición de Recursos entre Orígenes Cruzados)  
Permite controlar desde qué dominios externos se puede consumir los recursos (API, páginas, etc.) de tu servidor.

- 🛑 **Por defecto** los navegadores bloquean solicitudes AJAX/XHR “cruzadas” (ej: de `http://cliente.com` a `http://api.com`), a menos que el servidor lo permita.
- 📝 CORS utiliza cabeceras como `Access-Control-Allow-Origin`, que el servidor devuelve indicando si acepta o no solicitudes desde otros orígenes.
- 🦺 **Protege tu backend** de accesos no autorizados por aplicaciones web de terceros.
- ⚙️ En **Spring Security** puedes definir qué orígenes están permitidos utilizando la configuración CORS.

**Ejemplo básico de configuración en Spring:**
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
}
```
## 🛡️ ¿Qué es CSRF?

**CSRF** (Cross-Site Request Forgery, Falsificación de Peticiones entre Sitios)  
Es un ataque donde un usuario autenticado es engañado para realizar una acción no deseada gracias a su sesión activa.

- 🎯 Ejemplo: Si tienes la sesión abierta en tu banco y visitas una web maliciosa, esta podría hacer que tu navegador envíe una transferencia sin tu consentimiento.
- 📝 Para **protegerte contra CSRF**, es común exigir un _token CSRF_ en las solicitudes que modifican datos (POST, PUT, DELETE). Este token se incluye en los formularios y se valida en el backend.
- 🦺 **Spring Security** activa la protección CSRF por defecto en aplicaciones web (no en APIs REST).

**Ejemplo para desactivar CSRF en Spring (p. ej., para APIs REST):**
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
}
```





---

## ⚡ Resumen Rápido

- 🌐 **CORS:** Decide _quién_ puede acceder a tu backend desde otros dominios.
- 🛡️ **CSRF:** Protege a los usuarios autenticados evitando que su sesión sea utilizada maliciosamente por sitios externos.

---

## 📝 Clase 36 - OPCIONAL SIMULANDO UN CLIENTE FRONTEND 🔒 🔒 🔑🔑 🚀
se configuro frontend.

## 📝 Clase 37 - EL PROBLEMA DE CORS 🔒 🔒 🔑🔑 🚀

![img](img/img_9.png)

- Debemos hacer la lista ya que el CORS lo bloquea por defecto.
- El navegador bloquea la peticion.

## 📝 Clase 38 - CONFIGURACION DE CORS 🔒 🔒 🔑🔑 🚀

- Se configura el CORS en el backend.->SecurityConfig

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/loans", "/balance", "/accounts", "/cards")
                                .authenticated()
                                .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        http.cors(cors -> corsConfigurationSource());// agregamos el cors
        return http.build();
    }

 @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        //config.setAllowedOrigins(List.of("http://localhost:4200/"));//-> aqui se define que pagina esta permitida
        config.setAllowedOrigins(List.of("*"));//-> esto quiere decir que cualquier pagina esta permitida
        //config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);//-> esto quiere decir que cualquier endpoint esta permitido
        return source;
    }
```

![img](img/img_10.png)

## 📝 Clase 39 - CREANDO FILTRO CSRF 🔒 🔒 🔑🔑 🚀

![img](img/img_11.png)

¡Perfecto! Aquí tienes la explicación de **CSRF** en formato Markdown, con iconos, lista para tu `README`:

# 🛡️ ¿Qué es CSRF? (Cross-Site Request Forgery)

**CSRF** (_Falsificación de Peticiones entre Sitios_) es un ataque donde un usuario autenticado es engañado para ejecutar acciones no deseadas en una aplicación web.

---

## 🚨 ¿Cómo funciona?

1. 👤 El usuario inicia sesión en un sitio legítimo.
2. 🕵️‍♂️ Visita un sitio malicioso sin cerrar sesión.
3. 🎯 El sitio malicioso envía una petición al sitio legítimo usando la sesión activa del usuario.
4. 💥 El servidor ejecuta la acción creyendo que es legítima.

---

## ⚠️ ¿Por qué es peligroso?

- El usuario no sabe que está realizando la acción.
- El atacante puede transferir dinero, cambiar contraseñas, etc.

---

## 🛡️ ¿Cómo se previene?

- 🔑 Usando **tokens CSRF** únicos por sesión en formularios.
- 🚫 Rechazando solicitudes de orígenes no confiables.
- 🛠️ Usando cabeceras personalizadas.

---

## 💻 Ejemplo visual

```
[👤 Usuario autenticado] → [🌐 Sitio malicioso] → [📨 Petición fraudulenta] → [🏦 Sitio legítimo]
```

---

## 🏗️ En Spring Security

- 🟢 La protección CSRF está **activada por defecto** en aplicaciones web.
- 🔴 Para APIs REST, normalmente se **desactiva**.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
}
```

---

## 📝 Resumen

> **CSRF** explota la confianza de un sitio en el navegador del usuario.  
> Se previene usando tokens únicos y validaciones en el backend.

---

### Explicacion del codigo que vamos a desarrollar ->  ⚙️

> #### 🌱 Primero : 

- Creamos un filtro que extiende de OncePerRequestFilter. llamado CsrfCookieFilter
- al extender de OncePerRequestFilter nos obliga a implementar el metodo doFilterInternal
- Hacemos el casteo :  var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

```java
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        //validamos la nulabilidad del token
        if (Objects.nonNull(csrfToken.getHeaderName())) {
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());//estamos seteando el token en la cabecera
        }

        filterChain.doFilter(request, response);
    }
}

```

> ## 🌱 explicacion del codigo :
##### 🔒 Explicación del Filtro CSRF Cookie

#### 📋 Descripción General

Este filtro personalizado de Spring Security se encarga de **exponer el token CSRF en los headers de la respuesta HTTP**, permitiendo que aplicaciones cliente (SPA, aplicaciones móviles) puedan leerlo y enviarlo en peticiones posteriores.

---

#### 🏗️ Estructura del Código

#### 1️⃣ **Extensión de `OncePerRequestFilter`**

```java
public class CsrfCookieFilter extends OncePerRequestFilter
```

- Garantiza que el filtro se ejecute **una sola vez por petición**
- Evita ejecuciones duplicadas en forward/include

---

### 2️⃣ **Obtención del Token CSRF**

```java
var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
```

- Recupera el token CSRF que Spring Security almacena en los atributos de la petición
- Este token fue generado previamente por Spring Security

---

### 3️⃣ **Validación y Exposición del Token**

```java
if (Objects.nonNull(csrfToken.getHeaderName())) {
    response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
}
```

- **Valida** que el header name no sea nulo
- **Añade** el token CSRF como header en la respuesta (típicamente `X-CSRF-TOKEN`)
- Permite que el cliente JavaScript/Frontend pueda leerlo desde los headers de respuesta

---

### 4️⃣ **Continuación de la Cadena de Filtros**

```java
filterChain.doFilter(request, response);
```

- Permite que la petición continúe al siguiente filtro
- Esencial para no romper el flujo de procesamiento

---

#### 🎯 Propósito

| Componente | Función |
|------------|---------|
| **🔐 Token CSRF** | Protección contra ataques Cross-Site Request Forgery |
| **📤 Header Response** | Expone el token al cliente para futuras peticiones |
| **🔄 OncePerRequestFilter** | Ejecución única y controlada |

---

#### 💡 Uso Típico

Este filtro es necesario cuando:
- ✅ Trabajas con APIs REST y aplicaciones SPA
- ✅ El frontend necesita leer el token CSRF de los headers

### Explicacion del codigo a detalle ->
# 🎓 Explicación del Flujo CSRF para No Desarrolladores Backend

## 🔄 El Flujo Completo (Analogía con una Cafetería)

Imagina que vas a una cafetería (tu aplicación web):

---

## 1️⃣ **¿Quién genera el token?**

### ☕ Analogía
Cuando llegas a la cafetería, el cajero (Spring Security) te da un **número de orden único** (token CSRF) automáticamente.

### 💻 En tu código
```
Spring Security → Genera el token automáticamente ANTES de llegar a tu filtro
```

- **Spring Security** crea el token en filtros anteriores
- Lo guarda en los **atributos internos de la petición** (memoria temporal)
- NO está en cookies ni headers todavía (solo en memoria interna del servidor)

---

## 2️⃣ **Tu filtro `CsrfCookieFilter` NO genera, solo EXPONE**

### ☕ Analogía
El cajero escribió tu número en un papel (memoria), pero **tú necesitas verlo**. Tu filtro es como un empleado que **copia ese número en tu ticket** para que puedas leerlo.

### 💻 Flujo paso a paso

```
1. Request llega al servidor
   ↓
2. Spring Security GENERA el token (filtros anteriores)
   ↓
3. Tu filtro CsrfCookieFilter CAPTURA ese token de la memoria interna
   ↓
4. Tu filtro COPIA el token al HEADER de la RESPUESTA
   ↓
5. El navegador/frontend RECIBE la respuesta con el token visible
   ↓
6. filterChain.doFilter() continúa con el resto de filtros
```

---

## 3️⃣ **¿Dónde se guarda el token?**

### 📍 Ubicaciones en el flujo

| Momento | Ubicación | ¿Quién puede verlo? |
|---------|-----------|---------------------|
| **Generación** | `request.getAttribute()` (memoria interna) | ❌ Solo el servidor |
| **Después de tu filtro** | `response.setHeader()` (header de respuesta) | ✅ El navegador/cliente |
| **Próximas peticiones** | Cliente lo envía en headers | ✅ Ambos |

---

## 4️⃣ **¿NO se genera otro token?**

❌ **NO**, tu filtro **NO genera** ningún token nuevo.

✅ Solo toma el token que Spring Security ya creó y lo "publica" en los headers para que el cliente pueda verlo.

---

## 5️⃣ **El `filterChain.doFilter()` explicado**

### ☕ Analogía
Es como una cadena de montaje en la cafetería:

```
Cliente → Cajero → Barista → Empaquetador → Cliente recibe su café
         (filtro1) (filtro2)  (tu filtro)
```

Cada estación hace su trabajo y **pasa** el producto a la siguiente.

### 💻 En tu código

```java
filterChain.doFilter(request, response);
```

**Significa:** "Terminé mi trabajo (copiar token al header), ahora que **continúen los demás filtros**".

Si NO llamas a `doFilter()`, la petición **se detiene** y nunca llega a tu controlador.

---

## 🎯 Resumen Visual del Flujo

```
📥 REQUEST del Cliente
    ↓
🔐 [Spring Security] Genera token CSRF → Lo guarda en memoria interna
    ↓
🍪 [Tu Filtro CsrfCookieFilter] 
    • Lee el token de memoria
    • Lo copia al HEADER de respuesta (X-CSRF-TOKEN: abc123)
    • Llama a filterChain.doFilter()
    ↓
⚙️ [Otros Filtros] → Autenticación, Autorización, etc.
    ↓
🎯 [Tu Controlador] Procesa la lógica de negocio
    ↓
📤 RESPONSE al Cliente (con el token en el header)
```

---

## 🐛 Corrección a tu entendimiento

| Tu entendimiento | Realidad |
|------------------|----------|
| "recuperamos el request" | ❌ Recuperas el **token** que está dentro del request |
| "lo guardamos en la cookie del header" | ❌ Se guarda en el **header HTTP** (no en cookies) |
| "lo seteamos" | ✅ Correcto, lo seteamos en la **respuesta** |
| "lo enviamos con doFilter" | ❌ `doFilter` **NO envía** el token, solo continúa el flujo |

---

## 💡 ¿Por qué es necesario este filtro?

Por defecto, Spring Security guarda el token CSRF en memoria interna, pero aplicaciones modernas (React, Angular, Vue) necesitan **leerlo desde JavaScript**, por eso tu filtro lo expone en los headers de respuesta.