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

