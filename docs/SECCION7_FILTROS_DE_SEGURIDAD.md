# 🛡️ 🌐🔒 Spring Security — FILTROS DE SEGURIDAD

## 📝 Clase 41 - INTRODUCCION FILTROS DE SEGURIDAD 🔒 🔒 🔑🔑

- Muy relacionados a los servlets
- Los filtros de seguridad son componentes que interceptan las solicitudes HTTP antes de que lleguen a los controladores de Spring MVC.
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

