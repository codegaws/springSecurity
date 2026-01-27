# 🛡️ SECCION3 PASSWORD ENCODERS🛡️

## 📝 Clase 25 - QUE ES LA ENCRIPTACION 🔒 🔒 🔑🔑
-   AES Y RSA PODEMOS DESENCRIPTAR Y VOLVER A SU FORMATO ORIGINAL
## 📝 Clase 26 - QUE ES LA CODIFICACION 🔒 🔒 🔑🔑
-   ASCII-BASE64-UNICODE : AQUI PODEMOS VOLVER A SU FORMATO ORIGINAL
## 📝 Clase 27 - QUE ES EL HASHING 🔒 🔒 🔑🔑
- MD5 - SHA256 ES IRREVERSIBLE NO PUEDEN VOLVER A SU FORMATO ORIGINAL
- TODOS LOS OBJETOS EN JAVA TIENEN SU METODO HASHCODE()
## 📝 Clase 28 - CREANDO NUESTRA IMPLEMENTACION DE PASSWORDENCODER 🔒 🔒 🔑🔑

### Nota ->
```text
`@Component` se usa para que Spring detecte y gestione la clase como un bean. 
Así, Spring puede inyectar automáticamente `MyPasswordEncoder` donde lo necesites, 
por ejemplo en servicios o configuraciones de seguridad. Esto facilita la integración 
y el manejo de dependencias en tu aplicación Spring Boot.

```
```java
@Component
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword.toString().hashCode());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var passwordAsString= String.valueOf(rawPassword.toString().hashCode());
        return encodedPassword.equals(passwordAsString);
    }
}

```

- IMPLEMENTAMOS LA INTERFAZ PASSWORDENCODER DE SPRING SECURITY
- Correcto. La interfaz `PasswordEncoder` de Spring Security exige implementar dos métodos:

- `encode(CharSequence rawPassword)`: recibe la contraseña en texto plano y la transforma (normalmente la encripta o hashea) para almacenarla de forma segura.
- `matches(CharSequence rawPassword, String encodedPassword)`: compara la contraseña en texto plano con la versión almacenada (encriptada/hasheada) y verifica si coinciden.

En tu clase, ambos métodos usan el hash de la contraseña como mecanismo de "encriptación" y comparación.
- Sí, ambos métodos funcionan juntos pero en diferentes momentos:
  encode se usa cuando guardas la contraseña: transforma la contraseña en texto plano a una versión "encriptada" (en tu caso, el hash).
  matches se usa cuando validas el login: compara la contraseña ingresada (en texto plano) con la almacenada (encriptada).
  Spring Security llama automáticamente a cada uno según la operación (registro o autentic

- Haciendo pruebas :
- super_user@debuggeandoieas.com
- to_be_encoded
- HASH:-635289258

![img](img/img_8.png)

- este password esta bien solo que en la base de datos no lo tenemos codificado

### En resumen :
```text
El método loadUserByUsername de la clase CustomerUserDetails solo devuelve información: busca un usuario en la base de datos por su email y,
si lo encuentra, construye un objeto User de Spring Security con sus datos (email, contraseña y roles). No hashea ni modifica contraseñas, 
solo recupera y adapta la información para el proceso de autenticación.

La clase MyPasswordEncoder solo se encarga de hashear (o codificar) y comparar contraseñas.
El método encode transforma la contraseña en texto plano a un hash, y matches compara una contraseña en texto plano con una ya codificada.
```
## 📝 Clase 29 - BCRYPTPASSWORDENCODER 🔒 🔒 🔑🔑

- SOLO PUEDE HABER UN SOLO PASSWORD ENCODER EN LA APLICACION
- BCRYPT ES UN ALGORITMO DE HASHING FUERTE Y SEGURO.

- Agregamos estas lineas de codigo -> SecurityConfig.java
```java
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```
```java
@SpringBootApplication
@EnableWebSecurity// ya no es necesario ponerla a partir de Spring Security 5.7 viene por defecto.
public class SpringSecurityApplication implements CommandLineRunner{

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    //esto lo agregue para que pueda ver el password encriptado en la consola
    @Override
    public void run(String... args) throws Exception {
        System.out.println(passwordEncoder.encode("to_be_encoded"));
    }
}

```
password hascheado con BCRYPT : $2a$10$i8bHbMsm4YXlFcPtZnTFxOjDtzBT7HiEkcUlf4YGKFXTG789TVGCW
lo actualizas en la BD y ya puedes autenticarte correctamente. con los usuarios que hayas creado previamente.


## 📝 Clase 30 - RESUMEN DE LA SECCION 🔒 🔒 🔑🔑

