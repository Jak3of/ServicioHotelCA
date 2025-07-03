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
    
    private static void verificarCredencialesAdmin() {
        try {
            logger.info("Verificando credenciales de administrador...");
            
            // Verificar directamente en la base de datos
            try (java.sql.Connection conn = com.mycompany.avanceproyecto.config.DatabaseConfig.getConnection()) {
                // Primero verificar que la tabla usuarios existe
                try (java.sql.Statement checkStmt = conn.createStatement();
                     java.sql.ResultSet tables = checkStmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios'")) {
                    
                    if (!tables.next()) {
                        logger.error("❌ Tabla 'usuarios' no existe");
                        throw new RuntimeException("Tabla usuarios no fue creada correctamente");
                    }
                    logger.info("✅ Tabla 'usuarios' encontrada");
                }
                
                // Ahora verificar las credenciales del admin
                try (java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?")) {
                    stmt.setString(1, "admin");
                    stmt.setString(2, "admin123");
                    
                    try (java.sql.ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            logger.info("✅ Usuario admin encontrado correctamente");
                            logger.info("ID: {}, Usuario: {}, Rol: {}", 
                                rs.getInt("id"), 
                                rs.getString("nombre_usuario"), 
                                rs.getString("rol"));
                        } else {
                            logger.error("❌ Usuario admin NO encontrado");
                            
                            // Intentar crear el usuario admin si no existe
                            logger.info("Intentando crear usuario admin...");
                            try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(
                                "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?, ?, ?)")) {
                                insertStmt.setString(1, "admin");
                                insertStmt.setString(2, "admin123");
                                insertStmt.setString(3, "ADMIN");
                                insertStmt.executeUpdate();
                                logger.info("✅ Usuario admin creado exitosamente");
                            }
                        }
                    }
                }
                
                // Verificar todos los usuarios en la base de datos para debug
                try (java.sql.Statement debugStmt = conn.createStatement();
                     java.sql.ResultSet allUsers = debugStmt.executeQuery("SELECT * FROM usuarios")) {
                    logger.info("=== Usuarios en la base de datos ===");
                    while (allUsers.next()) {
                        logger.info("Usuario: {}, Contraseña: {}, Rol: {}", 
                            allUsers.getString("nombre_usuario"),
                            allUsers.getString("contrasena"),
                            allUsers.getString("rol"));
                    }
                    logger.info("=== Fin lista usuarios ===");
                }
            }
            
        } catch (Exception e) {
            logger.error("Error verificando credenciales admin", e);
            // No lanzar excepción para permitir que la aplicación continúe
            logger.warn("Continuando con la aplicación a pesar del error de verificación");
        }
    }
}
