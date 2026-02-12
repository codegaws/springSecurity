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