package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.daos.UsuarioDAO;
import com.mycompany.avanceproyecto.daos.impl.UsuarioDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }
    
    public Usuarios autenticarUsuario(String username, String password) {
        try {
            logger.info("🔍 UsuarioService - Autenticando usuario: '{}'", username);
            
            // Obtener usuario por nombre
            Usuarios usuario = usuarioDAO.obtenerPorNombre(username);
            
            // Verificar si existe y si la contraseña coincide
            if (usuario != null && usuario.getContrasena().equals(password)) {
                logger.info("✅ Usuario autenticado correctamente");
                return usuario;
            } else {
                logger.warn("❌ Credenciales incorrectas");
                return null;
            }
            
        } catch (Exception e) {
            logger.error("💥 UsuarioService - Error autenticando usuario", e);
            return null;
        }
    }
}
