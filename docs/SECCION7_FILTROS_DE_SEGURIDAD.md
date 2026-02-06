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
]```