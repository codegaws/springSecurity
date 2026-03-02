# 🔐 Credenciales del Proyecto Spring Security

## 📋 Información Importante sobre BCrypt

**¿Los hashes BCrypt cambian entre máquinas?**
- ✅ **SÍ**, los hashes BCrypt son **diferentes cada vez** que los generas
- ✅ **PERO** todos los hashes para la misma contraseña son **VÁLIDOS**
- ✅ **NO importa** en qué máquina los generes (PC o Laptop)
- ✅ **LO IMPORTANTE** es recordar qué contraseña en texto plano usaste

## 👥 Usuarios de la Base de Datos

**Todos los usuarios usan la contraseña: `1234`**

| Email | Contraseña | Rol |
|-------|-----------|-----|
| `account@debuggeandoieas.com` | `1234` | ADMIN |
| `cards@debuggeandoieas.com` | `1234` | ADMIN |
| `loans@debuggeandoieas.com` | `1234` | USER |
| `balance@debuggeandoieas.com` | `1234` | USER |

**Nota importante:** Antes estaba configurado con `to_be_encoded`, ahora se cambió a `1234` para mayor simplicidad.

## 🔑 Cliente OAuth2

| Campo | Valor |
|-------|-------|
| **Client ID** | `debuggeandoideas` |
| **Client Secret** | `debuggeandoideas` |
| **Scopes** | `read`, `write` |
| **Grant Types** | `authorization_code`, `refresh_token` |
| **Redirect URI** | `https://oauthdebugger.com/debug` |

## 🚨 Si no puedes iniciar sesión

### Problema: "Bad credentials"

**Causa:** El hash en la base de datos no corresponde a la contraseña que estás ingresando.

**Solución:**

1. **Opción A - Verificar la contraseña actual:**
   
   Necesitas saber qué contraseña se usó para generar el hash actual. Revisa:
   - Los comentarios en `db/sql/data.sql`
   - Tu documentación anterior
   - Si viene de GitHub, revisa el README del repositorio original

2. **Opción B - Regenerar los hashes:**
   
   Si no recuerdas la contraseña, genera nuevos hashes:

   ```bash
   # Ejecuta este script que creamos:
   ./generate_passwords.sh
   ```
   
   Esto generará:
   - Nuevos hashes para la contraseña "1234"
   - SQL para actualizar la base de datos

3. **Opción C - Actualizar manualmente la BD:**
   
   Conéctate a PostgreSQL y actualiza:
   ```sql
   -- Actualizar todos los usuarios con la contraseña "1234"
   UPDATE customers SET pwd = '$2a$10$[NUEVO_HASH_AQUI]';
   
   -- Actualizar el client secret
   UPDATE partners SET client_secret = '$2a$10$[NUEVO_HASH_AQUI]' 
   WHERE client_id = 'debuggeandoideas';
   ```

4. **Opción D - Recrear el contenedor:**
   
   ```bash
   # Detener y eliminar el contenedor
   docker-compose down -v
   
   # Recrear (esto ejecutará data.sql desde cero)
   docker-compose up -d
   ```

## 📊 Verificar qué hay en la Base de Datos

```bash
# Ver usuarios
docker exec security_bank psql -U alejandro -d security_bank \
  -c "SELECT email, left(pwd, 10) as pwd_prefix FROM customers;"

# Ver partner OAuth2
docker exec security_bank psql -U alejandro -d security_bank \
  -c "SELECT client_id, left(client_secret, 10) as secret_prefix FROM partners;"
```

## 🔍 Depuración

Si sigues teniendo problemas, revisa los logs:

```bash
# Ver logs de Spring Security
tail -f logs/spring.log | grep -i "authentication\|bcrypt\|password"
```

Busca estos mensajes:
- ✅ `Authentication successful` - Todo OK
- ❌ `Bad credentials` - Contraseña incorrecta
- ❌ `Encoded password does not look like BCrypt` - El hash no es BCrypt válido
- ❌ `User not found` - El email no existe en la BD

## 💡 Concepto Importante

```
Hash 1: $2a$10$ilrQR0yy4oUfDp0cQFpiwO8Cq78Wk0NtvTIB4TsLcVEFYWQHnHk7G
Hash 2: $2a$10$LLTE6473g2C37PWROHDkGOAgBBlWluVG9d1tfm4EZ/gawNKd5etX2

Ambos son válidos para la contraseña "1234"

BCrypt:
1. Genera un "salt" aleatorio cada vez
2. El salt se almacena en el hash mismo
3. Al verificar, BCrypt extrae el salt y lo usa
4. Por eso cada hash es diferente pero válido
```

## 🎯 Configuración Recomendada

**Para Desarrollo:**
- Usa contraseñas simples como "1234"
- Documenta bien qué contraseña usaste
- Comitea el archivo `data.sql` al repositorio

**Para Producción:**
- NUNCA uses contraseñas simples
- NUNCA comitees contraseñas reales
- Usa variables de entorno
- Usa gestores de secretos (Vault, AWS Secrets Manager, etc.)

## 📚 Referencias

- [BCrypt en Spring Security](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-bcrypt)
- [Documentación OAuth2](docs/SECCION9_OAUTH2.md)
- [Password Encoders](docs/SECCION3_PASSWORD_ENCODERS.md)

