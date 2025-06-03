package com.mycompany.avanceproyecto.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Crear todas las tablas necesarias
            createTables(conn);
            // Insertar datos iniciales
            insertInitialData(conn);
            logger.info("Base de datos inicializada correctamente");
        } catch (Exception e) {
            logger.error("Error inicializando la base de datos", e);
        }
    }

    private static void createTables(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            // Tabla Usuarios
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre_usuario TEXT NOT NULL UNIQUE,
                    contrasena TEXT NOT NULL,
                    rol TEXT NOT NULL
                )
            """);

            // Tabla Habitaciones
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS habitaciones (
                    numero INTEGER PRIMARY KEY,
                    tipo TEXT NOT NULL,
                    precio REAL NOT NULL,
                    disponible INTEGER NOT NULL
                )
            """);

            // Tabla Clientes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    dni INTEGER PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    telefono TEXT,
                    correo TEXT
                )
            """);

            // ... más tablas según necesites
        }
    }

    private static void insertInitialData(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            // Usuario administrador
            stmt.execute("""
                INSERT OR IGNORE INTO usuarios (nombre_usuario, contrasena, rol)
                VALUES ('admin', 'admin123', 'ADMIN')
            """);

            // Algunas habitaciones por defecto
            stmt.execute("""
                INSERT OR IGNORE INTO habitaciones (numero, tipo, precio, disponible)
                VALUES 
                    (101, 'SIMPLE', 100.0, 1),
                    (102, 'DOBLE', 180.0, 1),
                    (103, 'SUITE', 300.0, 1)
            """);
        }
    }
}
