package com.mycompany.avanceproyecto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String DB_NAME = "hotel.db";
    private static final String URL = "jdbc:sqlite:" + DB_NAME;
    
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
            // Verificar que el directorio actual es escribible
            File currentDir = new File(".");
            if (!currentDir.canWrite()) {
                logger.warn("Directorio actual no es escribible: {}", currentDir.getAbsolutePath());
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
            
            logger.debug("Conexión establecida exitosamente a: {}", new File(DB_NAME).getAbsolutePath());
            return conn;
            
        } catch (Exception e) {
            logger.error("Error conectando a la base de datos", e);
            logger.error("URL: {}", URL);
            logger.error("Directorio actual: {}", new File(".").getAbsolutePath());
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
