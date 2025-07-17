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
        // Verificar si estamos en modo test
        boolean isTest = isTestMode();
        
        if (isTest) {
            // Para tests: usar base de datos temporal
            String tempDir = System.getProperty("java.io.tmpdir");
            String testDbPath = tempDir + File.separator + "test_" + DB_NAME;
            logger.debug("Usando base de datos de test: {}", testDbPath);
            return testDbPath;
        } else {
            // Para producción: usar directorio del usuario
            String userHome = System.getProperty("user.home");
            String appDataDir = userHome + File.separator + APP_NAME;
            
            // Crear directorio si no existe
            File appDir = new File(appDataDir);
            if (!appDir.exists()) {
                boolean created = appDir.mkdirs();
                if (created) {
                    logger.info("Directorio de aplicación creado: {}", appDataDir);
                } else {
                    logger.error("No se pudo crear directorio de aplicación: {}", appDataDir);
                    throw new RuntimeException("No se puede crear directorio de aplicación: " + appDataDir);
                }
            }
            
            String prodDbPath = appDataDir + File.separator + DB_NAME;
            logger.debug("Usando base de datos de producción: {}", prodDbPath);
            return prodDbPath;
        }
    }
    
    /**
     * Detecta si estamos ejecutando en modo test
     */
    private static boolean isTestMode() {
        // Verificar si el stack trace contiene clases de test
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            String className = element.getClassName();
            if (className.contains("Test") || 
                className.contains("junit") || 
                className.contains("org.junit") ||
                className.contains("surefire")) {
                return true;
            }
        }
        
        // Verificar propiedades del sistema
        String testProperty = System.getProperty("test.mode");
        if ("true".equals(testProperty)) {
            return true;
        }
        
        return false;
    }
    
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
            String url = "jdbc:sqlite:" + dbPath;
            
            // Verificar que el directorio padre es escribible
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.canWrite()) {
                logger.warn("Directorio padre no es escribible: {}", parentDir.getAbsolutePath());
            }
            
            // Crear conexión
            Connection conn = DriverManager.getConnection(url);
            
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
            logger.error("URL: jdbc:sqlite:{}", getDBPath());
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
    
    /**
     * Limpia la base de datos de test (solo en modo test)
     */
    public static void cleanTestDatabase() {
        if (!isTestMode()) {
            logger.warn("cleanTestDatabase() llamado fuera de modo test - ignorando");
            return;
        }
        
        try {
            String testDbPath = getDBPath();
            File testDbFile = new File(testDbPath);
            if (testDbFile.exists()) {
                boolean deleted = testDbFile.delete();
                if (deleted) {
                    logger.info("Base de datos de test eliminada: {}", testDbPath);
                } else {
                    logger.warn("No se pudo eliminar base de datos de test: {}", testDbPath);
                }
            }
        } catch (Exception e) {
            logger.error("Error limpiando base de datos de test", e);
        }
    }
    
    /**
     * Obtiene la ruta actual de la base de datos (para debugging)
     */
    public static String getCurrentDBPath() {
        return getDBPath();
    }
}
