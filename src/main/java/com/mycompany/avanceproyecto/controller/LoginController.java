package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.service.UsuarioService;
import com.mycompany.avanceproyecto.util.SessionManager;
import com.mycompany.avanceproyecto.view.login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UsuarioService usuarioService;
    private final login view;
    
    public LoginController(login view) {
        this.view = view;
        this.usuarioService = new UsuarioService();
    }
    
    public boolean autenticar(String username, String password) {
        try {
            logger.info("🔍 Intento de login - Usuario: '{}'", username);
            
            com.mycompany.avanceproyecto.model.Usuarios usuario = usuarioService.autenticarUsuario(username, password);
            
            if (usuario != null) {
                logger.info("✅ LOGIN EXITOSO para usuario: {}", username);
                
                // Guardar usuario en sesión
                SessionManager.setUsuarioActual(usuario);
                
                return true;
            } else {
                logger.warn("❌ LOGIN FALLIDO para usuario: {}", username);
                return false;
            }
        } catch (Exception e) {
            logger.error("💥 Error durante autenticación", e);
            return false;
        }
    }
}


