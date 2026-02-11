🛡️ 🌐🔒 Spring Security — OAUTH2 🔐🔐🔑🔑
📝 Clase 67 - INTRODUCCION A LOS OAUTH2 👤👤🕵️‍♂🕵️‍♂🔑 🔑
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
