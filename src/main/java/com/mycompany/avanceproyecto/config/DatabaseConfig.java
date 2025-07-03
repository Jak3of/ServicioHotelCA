package com.mycompany.avanceproyecto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    // Agregar parámetros para evitar el lock de la base de datos
    private static final String URL = "jdbc:sqlite:hotel.db?journal_mode=WAL&busy_timeout=30000";
    
    public static Connection getConnection() {
        try {
            // Remover la inicialización automática para evitar conflictos
            Connection conn = DriverManager.getConnection(URL);
            return conn;
        } catch (Exception e) {
            logger.error("Error conectando a la base de datos", e);
            throw new RuntimeException("Error de conexión a la base de datos", e);
        }
    }

    
    private static void initDatabase(Connection conn) {
        // Crear tabla si no existe
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_usuario TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL,
                rol TEXT NOT NULL
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            // Insertar usuario admin si no existe
            stmt.execute("""
                INSERT OR IGNORE INTO usuarios (nombre_usuario, contrasena, rol)
                VALUES ('admin', 'admin123', 'ADMIN')
            """);
        } catch (Exception e) {
            logger.error("Error inicializando la base de datos", e);
        }
    }
}
