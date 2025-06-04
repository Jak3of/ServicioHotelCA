package com.mycompany.avanceproyecto.controller;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.service.UsuarioService;
import com.mycompany.avanceproyecto.view.login;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private login loginView;
    private UsuarioService usuarioService;
    
    public LoginController(login view) {
        this.loginView = view;
        this.usuarioService = new UsuarioService();
    }
    
    public boolean autenticar(String username, String password) {
        // Validación de campos vacíos usando Apache Commons Lang
        if (StringUtils.isAnyBlank(username, password)) {
            logger.warn("Intento de login con campos vacíos");
            return false;
        }

        try {
            Usuarios usuario = usuarioService.autenticarUsuario(username, password);
            boolean isValid = usuario != null;
            logger.info("Intento de login para usuario: {} - Resultado: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error durante la autenticación", e);
            return false;
        }
    }
    
    public void iniciarSesion(Usuarios usuario) {
        // Lógica para iniciar sesión y abrir ventana principal
    }
}
