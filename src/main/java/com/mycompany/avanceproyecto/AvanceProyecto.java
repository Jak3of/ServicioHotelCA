/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.avanceproyecto;

import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import com.mycompany.avanceproyecto.view.login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LAB-USR-LSUR
 */
public class AvanceProyecto {
    private static final Logger logger = LoggerFactory.getLogger(AvanceProyecto.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Iniciando aplicación Servicio Hotel CA...");
            
            // Configurar Look and Feel
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                logger.warn("No se pudo configurar Nimbus Look and Feel", e);
            }
            
            // FORZAR la carga del driver de SQLite
            try {
                Class.forName("org.sqlite.JDBC");
                logger.info("Driver SQLite cargado correctamente");
            } catch (ClassNotFoundException e) {
                logger.error("Error: Driver SQLite no encontrado", e);
                throw new RuntimeException("Driver SQLite no encontrado", e);
            }
            
            // Verificar que podemos crear la conexión básica
            try {
                java.sql.Connection testConn = java.sql.DriverManager.getConnection("jdbc:sqlite:hotel.db");
                testConn.close();
                logger.info("Conexión SQLite verificada correctamente");
            } catch (Exception e) {
                logger.error("Error al conectar con SQLite", e);
                throw new RuntimeException("No se puede conectar a SQLite", e);
            }
            
            // Inicializar base de datos con datos por defecto
            logger.info("Inicializando base de datos...");
            DatabaseInitializer.initializeDatabase();
            logger.info("Base de datos inicializada correctamente");
            
            // Verificar que el usuario admin existe
            verificarCredencialesAdmin();
            
            // Iniciar ventana de login
            javax.swing.SwingUtilities.invokeLater(() -> {
                logger.info("Mostrando ventana de login");
                new login().setVisible(true);
            });
            
        } catch (Exception e) {
            logger.error("Error fatal al iniciar la aplicación", e);
            
            // Mostrar mensaje de error más detallado
            String mensaje = "Error al iniciar la aplicación:\n\n" + 
                           e.getMessage() + "\n\n" +
                           "Verifique que:\n" +
                           "1. El archivo JAR incluya las dependencias\n" +
                           "2. SQLite esté disponible\n" +
                           "3. Tenga permisos de escritura en el directorio";
            
            javax.swing.JOptionPane.showMessageDialog(null, 
                mensaje,
                "Error Fatal",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Verifica que las credenciales de administrador estén configuradas correctamente
     */
    private static void verificarCredencialesAdmin() {
        logger.info("Verificando credenciales de administrador...");
        
        try (java.sql.Connection conn = com.mycompany.avanceproyecto.config.DatabaseConfig.getConnection()) {
            // Verificar que la tabla usuarios existe
            try (java.sql.Statement stmt = conn.createStatement()) {
                java.sql.ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios'");
                if (rs.next()) {
                    logger.info("✅ Tabla 'usuarios' encontrada");
                } else {
                    logger.error("❌ Tabla 'usuarios' no existe");
                    return;
                }
            }
            
            // Verificar que el usuario admin existe y está correctamente configurado
            String checkAdminSQL = "SELECT id, nombre_usuario, rol, contrasena FROM usuarios WHERE nombre_usuario = ?";
            try (java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkAdminSQL)) {
                checkStmt.setString(1, "admin");
                java.sql.ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    String contrasena = rs.getString("contrasena");
                    logger.info("✅ Usuario admin encontrado con rol: {}", rol);
                    
                    // Verificar que tenga rol de ADMIN
                    if (!"ADMIN".equals(rol)) {
                        logger.warn("⚠️ Usuario admin no tiene rol ADMIN, actualizando...");
                        String updateRolSQL = "UPDATE usuarios SET rol = 'ADMIN' WHERE nombre_usuario = 'admin'";
                        try (java.sql.Statement updateStmt = conn.createStatement()) {
                            updateStmt.executeUpdate(updateRolSQL);
                            logger.info("✅ Rol de admin actualizado a ADMIN");
                        }
                    }
                    
                    // Verificar que la contraseña esté encriptada
                    if (com.mycompany.avanceproyecto.util.PasswordEncryption.isEncrypted(contrasena)) {
                        logger.info("✅ Contraseña de admin está correctamente encriptada");
                    } else {
                        logger.info("🔐 Encriptando contraseña de admin...");
                        String contrasenaEncriptada = com.mycompany.avanceproyecto.util.PasswordEncryption.encrypt(contrasena);
                        String updatePwdSQL = "UPDATE usuarios SET contrasena = ? WHERE nombre_usuario = 'admin'";
                        try (java.sql.PreparedStatement updatePwdStmt = conn.prepareStatement(updatePwdSQL)) {
                            updatePwdStmt.setString(1, contrasenaEncriptada);
                            updatePwdStmt.executeUpdate();
                            logger.info("✅ Contraseña de admin encriptada correctamente");
                        }
                    }
                } else {
                    logger.warn("⚠️ Usuario admin NO encontrado, pero debería existir");
                    logger.info("🔧 Creando usuario admin por defecto...");
                    
                    String insertAdminSQL = "INSERT OR IGNORE INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?, ?, ?)";
                    try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertAdminSQL)) {
                        insertStmt.setString(1, "admin");
                        insertStmt.setString(2, com.mycompany.avanceproyecto.util.PasswordEncryption.encrypt("admin123"));
                        insertStmt.setString(3, "ADMIN");
                        int rowsAffected = insertStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            logger.info("✅ Usuario admin creado correctamente");
                        } else {
                            logger.info("ℹ️ Usuario admin ya existía (INSERT OR IGNORE)");
                        }
                    }
                }
            }
            
            logger.info("✅ Verificación de credenciales admin completada");
            
        } catch (Exception e) {
            logger.error("❌ Error verificando credenciales admin", e);
            logger.warn("⚠️ Continuando con la aplicación a pesar del error de verificación");
        }
    }
}
