package com.george.springsecurity.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para verificar qué contraseña corresponde al hash actual en la BD
 */
public class PasswordVerifier {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Hash actual en la base de datos
        String storedHash = "$2a$10$ilrQR0yy4oUfDp0cQFpiwO8Cq78Wk0NtvTIB4TsLcVEFYWQHnHk7G";

        // Contraseñas comunes a probar
        String[] possiblePasswords = {
            "1234",
            "12345",
            "password",
            "debuggeandoideas",
            "to_be_encoded",
            "admin",
            "user"
        };

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("🔍 Verificando Contraseñas contra el Hash Almacenado");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("\n📌 Hash actual: " + storedHash);
        System.out.println("\n🔎 Probando contraseñas...\n");

        boolean found = false;
        for (String pwd : possiblePasswords) {
            boolean matches = encoder.matches(pwd, storedHash);
            String status = matches ? "✅ MATCH ENCONTRADO" : "❌ No coincide";
            System.out.printf("%-20s : %s%n", pwd, status);

            if (matches) {
                found = true;
                System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
                System.out.println("║ 🎉 ¡CONTRASEÑA ENCONTRADA!                                  ║");
                System.out.println("║                                                              ║");
                System.out.println("║ Contraseña correcta: " + pwd + "                                    ║");
                System.out.println("╚══════════════════════════════════════════════════════════════╝");
            }
        }

        if (!found) {
            System.out.println("\n⚠️  Ninguna contraseña común coincide con el hash");
            System.out.println("💡 Necesitas recordar qué contraseña usaste para generar este hash");
            System.out.println("\n📝 Soluciones:");
            System.out.println("   1. Busca en tus notas o documentación");
            System.out.println("   2. Genera un nuevo hash con una contraseña conocida");
            System.out.println("   3. Ejecuta: ./generate_passwords.sh");
        }

        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("🔐 Generando nuevos hashes para contraseña '1234':");
        System.out.println("════════════════════════════════════════════════════════════════\n");

        String newUserHash = encoder.encode("1234");
        String newClientHash = encoder.encode("debuggeandoideas");

        System.out.println("Usuario (1234):");
        System.out.println(newUserHash);
        System.out.println();

        System.out.println("Cliente (debuggeandoideas):");
        System.out.println(newClientHash);
        System.out.println();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("📋 SQL para actualizar la BD:");
        System.out.println("════════════════════════════════════════════════════════════════\n");

        System.out.println("UPDATE customers SET pwd = '" + newUserHash + "';");
        System.out.println();
        System.out.println("UPDATE partners SET client_secret = '" + newClientHash + "' WHERE client_id = 'debuggeandoideas';");
        System.out.println();
    }
}

