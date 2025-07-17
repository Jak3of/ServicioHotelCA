package com.mycompany.avanceproyecto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String APP_NAME = "ServicioHotelCA";
    private static final String DB_NAME = "hotel.db";
    
    // Método para obtener la ruta robusta de la base de datos
    private static String getDBPath() {
        String userHome = System.getProperty("user.home");
        String appDataDir = userHome + File.separator + APP_NAME;
        
        // Crear directorio si no existe
        File appDir = new File(appDataDir);
        if (!appDir.exists()) {
            boolean created = appDir.mkdirs();
            if (created) {
                logger.info("Directorio de aplicación creado: {}", appDataDir);
            } else {
                logger.warn("No se pudo crear directorio de aplicación: {}", appDataDir);
                // Fallback: usar directorio actual
                return DB_NAME;
            }
        }
        
        return appDataDir + File.separator + DB_NAME;
    }
    
    private static final String URL = "jdbc:sqlite:" + getDBPath();
    
    static {
        try {
            // Cargar el driver SQLite explícitamente
            Class.forName("org.sqlite.JDBC");
            logger.info("Driver SQLite cargado en static block");
        } catch (ClassNotFoundException e) {
            logger.error("Error cargando driver SQLite", e);
            throw new RuntimeException("Driver SQLite no disponible", e);
        }
    }
    
    public static Connection getConnection() {
        try {
            String dbPath = getDBPath();
            
            // Verificar que el directorio padre es escribible
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.canWrite()) {
                logger.warn("Directorio padre no es escribible: {}", parentDir.getAbsolutePath());
            }
            
            // Crear conexión
            Connection conn = DriverManager.getConnection(URL);
            
            // Configurar SQLite para mejor rendimiento
            try (java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA journal_mode = WAL");
                stmt.execute("PRAGMA synchronous = NORMAL");
                stmt.execute("PRAGMA busy_timeout = 30000");
            }
            
            logger.debug("Conexión establecida exitosamente a: {}", dbFile.getAbsolutePath());
            return conn;
            
        } catch (Exception e) {
            logger.error("Error conectando a la base de datos", e);
            logger.error("URL: {}", URL);
            logger.error("Ruta DB calculada: {}", getDBPath());
            throw new RuntimeException("Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            logger.error("Test de conexión falló", e);
            return false;
        }
    }
}
