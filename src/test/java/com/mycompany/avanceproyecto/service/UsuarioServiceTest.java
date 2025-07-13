package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.config.DatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {
    
    private UsuarioService usuarioService;
    
    @BeforeAll
    static void initDatabase() {
        try {
            DatabaseInitializer.initializeDatabase();
            System.out.println("✅ Base de datos inicializada para tests");
        } catch (Exception e) {
            System.err.println("❌ Error inicializando BD para tests: " + e.getMessage());
            throw e;
        }
    }
    
    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }
    
    @Test
    void testAutenticarUsuarioExitoso() {
        // Test con la contrasena correcta del admin (sabemos que es "admin")
        Usuarios usuario = usuarioService.autenticarUsuario("admin", "admin");
        
        assertNotNull(usuario, "El usuario admin deberia autenticarse correctamente");
        assertEquals("admin", usuario.getNombreUsuario());
        assertEquals("ADMIN", usuario.getRol());
    }
    
    @Test
    void testAutenticarUsuarioCredencialesIncorrectas() {
        // Test con credenciales incorrectas
        Usuarios usuario = usuarioService.autenticarUsuario("admin", "wrongpassword");
        
        assertNull(usuario, "No deberia autenticar con contrasena incorrecta");
    }
    
    @Test
    void testAutenticarUsuarioNoExiste() {
        // Test con usuario que no existe
        Usuarios usuario = usuarioService.autenticarUsuario("noexiste", "password");
        
        assertNull(usuario, "No deberia autenticar usuario inexistente");
    }
}
