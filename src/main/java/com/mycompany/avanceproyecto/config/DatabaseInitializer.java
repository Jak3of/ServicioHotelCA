package com.mycompany.avanceproyecto.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initializeDatabase() {
        logger.info("Iniciando inicialización de base de datos...");
        
        // Verificar que SQLite está disponible
        if (!DatabaseConfig.testConnection()) {
            throw new RuntimeException("No se puede establecer conexión con SQLite");
        }
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            logger.info("Conexión establecida, creando tablas...");
            
            // Crear todas las tablas necesarias
            createTables(conn);
            logger.info("Tablas creadas correctamente");
            
            // Insertar datos iniciales
            insertInitialData(conn);
            logger.info("Datos iniciales insertados correctamente");
            
            // Migrar contraseñas existentes a encriptadas
            logger.info("Ejecutando migración de contraseñas...");
            com.mycompany.avanceproyecto.util.PasswordMigrationUtil.migratePasswords();
            
            // Verificar que los datos se insertaron
            verifyData(conn);
            
            logger.info("Base de datos inicializada correctamente");
        } catch (Exception e) {
            logger.error("Error inicializando la base de datos", e);
            throw new RuntimeException("Error inicializando base de datos: " + e.getMessage(), e);
        }
    }
    
    private static void verifyData(Connection conn) throws Exception {
        try (java.sql.Statement stmt = conn.createStatement()) {
            // Verificar usuarios
            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios")) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Usuarios en BD: {}", count);
                    if (count == 0) {
                        throw new RuntimeException("No se insertaron usuarios");
                    }
                }
            }
            
            // Verificar admin específicamente
            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE nombre_usuario = 'admin'")) {
                if (rs.next()) {
                    logger.info("Usuario admin verificado: {}", rs.getString("nombre_usuario"));
                } else {
                    throw new RuntimeException("Usuario admin no encontrado después de la inserción");
                }
            }
        }
    }

   private static void createTables(Connection conn) throws Exception {
    try (Statement stmt = conn.createStatement()) {

        // Usuarios
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS usuarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_usuario TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL,
                rol TEXT NOT NULL
            )
        """);

        // Habitaciones
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS habitaciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero TEXT NOT NULL UNIQUE,
                tipo TEXT NOT NULL,
                precio REAL NOT NULL,
                disponible INTEGER NOT NULL CHECK (disponible IN (0,1))
            )
        """);

        // Clientes
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                dni TEXT NOT NULL UNIQUE,
                telefono TEXT,
                correo TEXT
            )
        """);

        // Servicios
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS servicios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL
            )
        """);

        // Alojamientos
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS alojamientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                habitacion_id INTEGER NOT NULL,
                fecha_entrada TEXT NOT NULL,
                fecha_salida TEXT NOT NULL,
                estado TEXT NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'PAGADO', 'FINALIZADO')),
                FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id)
            )
        """);

        // Consumo Servicio
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS consumo_servicio (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_alojamiento INTEGER NOT NULL,
                id_servicio INTEGER NOT NULL,
                cantidad INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id) ON DELETE CASCADE,
                FOREIGN KEY (id_servicio) REFERENCES servicios(id) ON DELETE CASCADE
            )
        """);

        // Facturas
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS facturas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha TEXT NOT NULL,
                cliente_id INTEGER NOT NULL,
                alojamiento_id INTEGER NOT NULL,
                total REAL NOT NULL,
                FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                FOREIGN KEY (alojamiento_id) REFERENCES alojamientos(id)
            )
        """);

        // Alojamiento_Servicio
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS alojamiento_servicio (
                id_alojamiento INTEGER NOT NULL,
                id_servicio INTEGER NOT NULL,
                PRIMARY KEY (id_alojamiento, id_servicio),
                FOREIGN KEY (id_alojamiento) REFERENCES alojamientos(id) ON DELETE CASCADE,
                FOREIGN KEY (id_servicio) REFERENCES servicios(id) ON DELETE CASCADE
            )
        """);
    }
    
    // Ejecutar migraciones de esquema para bases de datos existentes
    executeSchemaMigrations(conn);
}

private static void executeSchemaMigrations(Connection conn) throws Exception {
    try (Statement stmt = conn.createStatement()) {
        // Verificar si la columna estado ya existe en la tabla alojamientos
        boolean estadoColumnExists = false;
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, "alojamientos", "estado")) {
            estadoColumnExists = rs.next();
        }
        
        // Si la columna estado no existe, agregarla
        if (!estadoColumnExists) {
            logger.info("Agregando columna 'estado' a la tabla alojamientos...");
            stmt.execute("ALTER TABLE alojamientos ADD COLUMN estado TEXT NOT NULL DEFAULT 'ACTIVO'");
            
            // Agregar el constraint CHECK por separado (SQLite requiere recrear la tabla para constraints complejas)
            // Por simplicidad, solo agregamos la columna con el valor por defecto
            logger.info("Columna 'estado' agregada exitosamente");
        }
    }
}
    private static void insertInitialData(Connection conn) throws Exception {
    try (Statement stmt = conn.createStatement()) {

        // Usuario - con contraseña en texto plano que será encriptada después
        stmt.execute("""
            INSERT OR IGNORE INTO usuarios (nombre_usuario, contrasena, rol)
            VALUES ('admin', 'admin123', 'ADMIN')
        """);

        // Habitaciones
        stmt.execute("""
            INSERT OR IGNORE INTO habitaciones (numero, tipo, precio, disponible)
            VALUES 
                (101, 'SIMPLE', 100.0, 1),
                (102, 'DOBLE', 180.0, 1),
                (103, 'SUITE', 300.0, 1)
        """);

        // Clientes
        stmt.execute("""
            INSERT OR IGNORE INTO clientes (dni, nombre, telefono, correo)
            VALUES
                ('12345678', 'Juan Pérez', '555-1234', 'juan@example.com'),
                ('87654321', 'María Gómez', '555-5678', 'maria@example.com')
        """);

        // Servicios
        stmt.execute("""
            INSERT OR IGNORE INTO servicios (id, nombre, precio)
            VALUES
                (1, 'Desayuno', 15.0),
                (2, 'Limpieza extra', 25.0),
                (3, 'Wi-Fi premium', 10.0)
        """);

        // Alojamientos (cliente_id 1, 2 / habitacion_id 1, 2)
        stmt.execute("""
            INSERT OR IGNORE INTO alojamientos (id, cliente_id, habitacion_id, fecha_entrada, fecha_salida, estado)
            VALUES
                (1, 1, 1, '2025-06-01', '2025-06-05', 'ACTIVO'),
                (2, 2, 2, '2025-06-03', '2025-06-07', 'ACTIVO')
        """);

        // Facturas
        stmt.execute("""
            INSERT OR IGNORE INTO facturas (id, fecha, cliente_id, alojamiento_id, total)
            VALUES
                (1, '2025-06-05', 1, 1, 400.0),
                (2, '2025-06-07', 2, 2, 500.0)
        """);

        // Alojamiento - Servicio
        stmt.execute("""
            INSERT OR IGNORE INTO alojamiento_servicio (id_alojamiento, id_servicio)
            VALUES
                (1, 1),
                (1, 3),
                (2, 2)
        """);

        // Consumo Servicio
        stmt.execute("""
            INSERT OR IGNORE INTO consumo_servicio (id, id_alojamiento, id_servicio, cantidad)
            VALUES
                (1, 1, 1, 2),
                (2, 1, 3, 1),
                (3, 2, 2, 1)
        """);
        }
    }
}
