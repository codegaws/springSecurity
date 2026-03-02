#!/bin/bash

# Script para generar contraseñas BCrypt y actualizar la base de datos
# Contraseña a usar: 1234

echo "════════════════════════════════════════════════════════════════"
echo "🔐 Generador de Contraseñas BCrypt para Spring Security"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📝 Contraseña a usar: 1234"
echo "📝 Cliente secret: debuggeandoideas"
echo ""

# Compilar y ejecutar un generador Java temporal
cat > /tmp/BCryptGenerator.java << 'JAVAEOF'
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("🔑 Generando hashes BCrypt...");
        System.out.println("════════════════════════════════════════════════════════════════\n");

        String userPassword = "1234";
        String clientSecret = "debuggeandoideas";

        String userHash = encoder.encode(userPassword);
        String clientHash = encoder.encode(clientSecret);

        System.out.println("✅ Hash para usuarios (password: '1234'):");
        System.out.println(userHash);
        System.out.println();

        System.out.println("✅ Hash para cliente (secret: 'debuggeandoideas'):");
        System.out.println(clientHash);
        System.out.println();

        // Verificar que funcionan
        System.out.println("🔍 Verificando hashes...");
        boolean userMatch = encoder.matches(userPassword, userHash);
        boolean clientMatch = encoder.matches(clientSecret, clientHash);

        System.out.println("  Usuario:  " + (userMatch ? "✅ VÁLIDO" : "❌ ERROR"));
        System.out.println("  Cliente:  " + (clientMatch ? "✅ VÁLIDO" : "❌ ERROR"));
        System.out.println();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📋 SQL para actualizar la base de datos:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("-- Actualizar contraseñas de usuarios");
        System.out.println("UPDATE customers SET pwd = '" + userHash + "';");
        System.out.println();
        System.out.println("-- Actualizar secret del cliente OAuth2");
        System.out.println("UPDATE partners SET client_secret = '" + clientHash + "' WHERE client_id = 'debuggeandoideas';");
        System.out.println();
    }
}
JAVAEOF

echo "🔨 Compilando generador de hashes..."
cd /home/codegaws/Documentos/CURSOS/CURSO_JAVA/springSecurity

# Compilar usando las dependencias del proyecto
./mvnw exec:java -Dexec.mainClass="BCryptGenerator" \
    -Dexec.classpathScope=compile \
    -Dexec.cleanupDaemonThreads=false \
    -Dexec.sourceRoot=/tmp \
    2>/dev/null || {

    # Si falla, usar javac con el classpath completo
    echo "📦 Usando método alternativo..."
    CLASSPATH=$(find ~/.m2/repository -name "spring-security-crypto*.jar" | head -1)
    if [ -z "$CLASSPATH" ]; then
        echo "❌ Error: No se encontró spring-security-crypto en ~/.m2/repository"
        echo "💡 Ejecuta primero: ./mvnw dependency:resolve"
        exit 1
    fi

    javac -cp "$CLASSPATH" /tmp/BCryptGenerator.java
    java -cp "/tmp:$CLASSPATH" BCryptGenerator
}

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "📝 IMPORTANTE: Guarda estos hashes en tu archivo data.sql"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "🔄 Para aplicar los cambios:"
echo "  1. Actualiza db/sql/data.sql con los nuevos hashes"
echo "  2. Reinicia el contenedor: docker-compose down && docker-compose up -d"
echo "  3. Usa estas credenciales:"
echo "     • Email: account@debuggeandoieas.com"
echo "     • Password: 1234"
echo ""

