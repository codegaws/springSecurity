# ⚙️ Configuración de IntelliJ IDEA para Spring Security

## 🎯 Configuración de Run/Debug

### Opción 1: Configuración Básica (Recomendada)

1. **Abrir Run/Debug Configurations**
   - Click en el menú `Run` → `Edit Configurations...`
   - O click en el dropdown al lado del botón "▶️ Run" → `Edit Configurations...`

2. **Crear nueva configuración**
   - Click en `+` (Add New Configuration)
   - Selecciona `Application`

3. **Configurar los campos:**
   ```
   Name: SpringSecurityApplication
   
   Build and run:
   ├─ Java: 21 (o tu versión de Java instalada)
   └─ Main class: com.george.springsecurity.SpringSecurityApplication
   
   Working directory: $MODULE_WORKING_DIR$
   
   Use classpath of module: springSecurity.main
   
   JRE: Project SDK (openjdk-21 o la que tengas)
   ```

4. **Variables de entorno (opcional):**
   ```
   SPRING_PROFILES_ACTIVE=dev
   ```

5. **VM Options (opcional, para memoria):**
   ```
   -Xms512m -Xmx1024m
   ```

### Opción 2: Usando Spring Boot Run Configuration

1. **Click derecho** en `SpringSecurityApplication.java`
2. Selecciona **"Run 'SpringSecurityApplication'"**
3. IntelliJ creará automáticamente la configuración

### Opción 3: Usando Maven

**En la terminal de IntelliJ:**
```bash
./mvnw spring-boot:run
```

**O crear configuración Maven:**
1. `Run` → `Edit Configurations...`
2. `+` → `Maven`
3. Configurar:
   ```
   Name: Spring Boot Run
   Working directory: $PROJECT_DIR$
   Command line: spring-boot:run
   ```

## 🔧 Configuración del proyecto en IntelliJ

### 1. Configurar el SDK

```
File → Project Structure → Project
├─ SDK: openjdk-21 (o tu versión)
└─ Language level: 21 - Pattern matching for switch
```

### 2. Configurar módulos

```
File → Project Structure → Modules
└─ springSecurity
    ├─ Sources: src/main/java
    ├─ Resources: src/main/resources
    └─ Test Sources: src/test/java
```

### 3. Configurar Maven

```
File → Settings → Build, Execution, Deployment → Build Tools → Maven
├─ Maven home path: (usar el bundled o instalación local)
├─ User settings file: ~/.m2/settings.xml
└─ Local repository: ~/.m2/repository
```

## 🐘 Configuración de la Base de Datos PostgreSQL

### Conectar IntelliJ a PostgreSQL (opcional pero útil)

1. **Abrir Database Tool Window**
   - `View` → `Tool Windows` → `Database`
   - O presiona `Ctrl+Shift+F10` (Linux/Windows)

2. **Agregar Data Source**
   - Click en `+` → `Data Source` → `PostgreSQL`

3. **Configurar conexión:**
   ```
   Host: localhost
   Port: 5433
   Database: security_bank
   User: alejandro
   Password: debuggeandoideas
   ```

4. **Test Connection** → **OK**

Ahora puedes ejecutar queries directamente desde IntelliJ.

## 🚀 Pasos para ejecutar el proyecto

### 1. Verificar que Docker esté corriendo

```bash
docker ps
```

Deberías ver:
```
CONTAINER ID   IMAGE           ... NAMES
xxxxxxxxxx     postgres:15.2   ... security_bank
```

Si no está corriendo:
```bash
docker-compose up -d
```

### 2. Verificar la base de datos

Conéctate y verifica:
```bash
docker exec security_bank psql -U alejandro -d security_bank \
  -c "SELECT email FROM customers LIMIT 2;"
```

### 3. Ejecutar la aplicación en IntelliJ

- Click en el botón **▶️ Run** (Shift+F10)
- O click derecho en `SpringSecurityApplication.java` → **Run**

### 4. Verificar que arrancó correctamente

Busca en la consola:
```
Started SpringSecurityApplication in X.XX seconds
Tomcat started on port(s): 8080
```

### 5. Probar el login

Abre el navegador:
```
http://localhost:8080/login
```

**Credenciales:**
- Email: `account@debuggeandoieas.com`
- Password: `1234`

## 🐛 Solución de problemas comunes

### Error: "Port 8080 already in use"

**Solución:**
```bash
# Ver qué está usando el puerto
sudo lsof -i :8080

# Matar el proceso (reemplaza PID con el número real)
kill -9 PID
```

O cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

### Error: "Connection refused to localhost:5433"

**Causa:** PostgreSQL no está corriendo.

**Solución:**
```bash
docker-compose up -d
# Espera 5 segundos para que inicie completamente
sleep 5
docker ps
```

### Error: "Bad credentials"

**Causa:** La contraseña en la BD no coincide con la que estás ingresando.

**Solución actual:**
```
Password correcta: 1234
```

Si cambias los datos, actualiza el archivo `data.sql` y recrea el contenedor:
```bash
docker-compose down -v
docker-compose up -d
```

### Error: "ClassNotFoundException" o "NoClassDefFoundError"

**Solución:**
1. `File` → `Invalidate Caches...` → **Invalidate and Restart**
2. Ejecutar en terminal:
   ```bash
   ./mvnw clean install
   ```

### La aplicación arranca pero no puedo hacer login

**Verificar el hash en la BD:**
```bash
docker exec security_bank psql -U alejandro -d security_bank \
  -c "SELECT email, left(pwd, 15) FROM customers WHERE email='account@debuggeandoieas.com';"
```

Debe empezar con `$2a$10$` (BCrypt).

**Probar qué contraseña funciona:**
```bash
cd /home/codegaws/Documentos/CURSOS/CURSO_JAVA/springSecurity
./mvnw exec:java -Dexec.mainClass="com.george.springsecurity.util.PasswordVerifier"
```

## 📝 Archivo de configuración de IntelliJ (opcional)

Crea `.idea/runConfigurations/SpringSecurityApplication.xml`:

```xml
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="SpringSecurityApplication" type="SpringBootApplicationConfigurationType">
    <option name="ACTIVE_PROFILES" />
    <module name="springSecurity.main" />
    <option name="SPRING_BOOT_MAIN_CLASS" value="com.george.springsecurity.SpringSecurityApplication" />
    <method v="2">
      <option name="Make" enabled="true" />
    </method>
  </configuration>
</component>
```

## 🔐 Credenciales actuales del proyecto

| Servicio | Usuario/ID | Contraseña/Secret |
|----------|-----------|-------------------|
| **PostgreSQL** | alejandro | debuggeandoideas |
| **Aplicación (users)** | account@debuggeandoieas.com | 1234 |
| **OAuth2 Client** | debuggeandoideas | debuggeandoideas |

## 📚 Documentos relacionados

- [CREDENCIALES.md](./CREDENCIALES.md) - Credenciales detalladas
- [README.md](./README.md) - Documentación del proyecto
- [docs/](./docs/) - Documentación técnica por sección

