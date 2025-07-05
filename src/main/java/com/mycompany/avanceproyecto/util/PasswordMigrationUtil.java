package com.mycompany.avanceproyecto.util;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Utilidad para migrar contraseñas en texto plano a encriptadas
 */
public class PasswordMigrationUtil {
    private static final Logger logger = LoggerFactory.getLogger(PasswordMigrationUtil.class);
    
    /**
     * Migra todas las contraseñas en texto plano a encriptadas
     */
    public static void migratePasswords() {
        logger.info("Iniciando migración de contraseñas...");
        
        String selectSql = "SELECT id, nombre_usuario, contrasena FROM usuarios";
        String updateSql = "UPDATE usuarios SET contrasena = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement selectStmt = conn.createStatement();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             ResultSet rs = selectStmt.executeQuery(selectSql)) {
            
            int migrated = 0;
            int skipped = 0;
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("nombre_usuario");
                String currentPassword = rs.getString("contrasena");
                
                // Verificar si la contraseña ya está encriptada
                if (PasswordEncryption.isEncrypted(currentPassword)) {
                    logger.debug("Usuario '{}' ya tiene contraseña encriptada, omitiendo", username);
                    skipped++;
                    continue;
                }
                
                try {
                    // Encriptar la contraseña
                    String encryptedPassword = PasswordEncryption.encrypt(currentPassword);
                    
                    // Actualizar en la base de datos
                    updateStmt.setString(1, encryptedPassword);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    
                    logger.info("✅ Contraseña migrada para usuario: {}", username);
                    migrated++;
                    
                } catch (Exception e) {
                    logger.error("❌ Error migrando contraseña para usuario '{}': {}", username, e.getMessage());
                }
            }
            
            logger.info("Migración completada. Usuarios migrados: {}, Omitidos: {}", migrated, skipped);
            
        } catch (Exception e) {
            logger.error("Error durante la migración de contraseñas", e);
            throw new RuntimeException("Error en la migración", e);
        }
    }
    
    /**
     * Verifica que todas las contraseñas estén encriptadas
     */
    public static void verifyEncryption() {
        logger.info("Verificando encriptación de contraseñas...");
        
        String sql = "SELECT id, nombre_usuario, contrasena FROM usuarios";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean allEncrypted = true;
            
            while (rs.next()) {
                String username = rs.getString("nombre_usuario");
                String password = rs.getString("contrasena");
                
                if (!PasswordEncryption.isEncrypted(password)) {
                    logger.warn("⚠️ Usuario '{}' tiene contraseña sin encriptar", username);
                    allEncrypted = false;
                } else {
                    logger.debug("✅ Usuario '{}' tiene contraseña encriptada", username);
                }
            }
            
            if (allEncrypted) {
                logger.info("✅ Todas las contraseñas están correctamente encriptadas");
            } else {
                logger.warn("⚠️ Algunas contraseñas no están encriptadas");
            }
            
        } catch (Exception e) {
            logger.error("Error verificando encriptación", e);
        }
    }
}
