package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.daos.UsuarioDAO;
import com.mycompany.avanceproyecto.daos.impl.UsuarioDAOImpl;
import com.mycompany.avanceproyecto.util.PasswordEncryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }
    
    public Usuarios autenticarUsuario(String username, String password) {
        try {
            logger.info("🔍 UsuarioService - Autenticando usuario: '{}'", username);
            
            // Obtener usuario por nombre (la contraseña en BD está encriptada)
            Usuarios usuario = usuarioDAO.obtenerPorNombre(username);
            
            if (usuario != null) {
                // Verificar la contraseña usando la función de verificación
                boolean passwordsMatch = PasswordEncryption.verifyPassword(password, usuario.getContrasena());
                
                if (passwordsMatch) {
                    logger.info("✅ Usuario autenticado correctamente con rol: {}", usuario.getRol());
                    return usuario; // Retornar el usuario completo con su rol
                } else {
                    logger.warn("❌ Contraseña incorrecta para usuario: {}", username);
                }
            } else {
                logger.warn("❌ Usuario no encontrado: {}", username);
            }
            
            return null;
            
        } catch (Exception e) {
            logger.error("💥 UsuarioService - Error autenticando usuario", e);
            return null;
        }
    }
    
    public void guardarOActualizarUsuario(Usuarios usuario) throws Exception {
        // Validar que la contraseña no esté vacía
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        if (usuario.getId() > 0) {
            logger.info("Actualizando usuario: {}", usuario.getNombreUsuario());
            usuarioDAO.actualizar(usuario);
        } else {
            logger.info("Creando nuevo usuario: {}", usuario.getNombreUsuario());
            usuarioDAO.insertar(usuario);
        }
    }
    
    public List<Usuarios> listarUsuarios() throws Exception {
        logger.info("Listando todos los usuarios");
        List<Usuarios> usuarios = usuarioDAO.listar();
        
        // Para la vista, no mostrar las contraseñas encriptadas
        for (Usuarios usuario : usuarios) {
            // Marcar que la contraseña está encriptada sin mostrarla
            usuario.setContrasena("[ENCRIPTADA]");
        }
        
        return usuarios;
    }
    
    public void eliminarUsuario(int id) throws Exception {
        logger.info("Eliminando usuario ID: {}", id);
        usuarioDAO.eliminar(id);
    }
    
    public Usuarios buscarPorNombre(String nombreUsuario) throws Exception {
        logger.info("Buscando usuario por nombre: {}", nombreUsuario);
        Usuarios usuario = usuarioDAO.obtenerPorNombre(nombreUsuario);
        
        if (usuario != null) {
            // No mostrar la contraseña encriptada
            usuario.setContrasena("[ENCRIPTADA]");
        }
        
        return usuario;
    }
    
    /**
     * Obtiene un usuario con su contraseña encriptada (para operaciones internas)
     * Usado para login y verificación de roles
     */
    public Usuarios obtenerUsuarioCompleto(String nombreUsuario) throws Exception {
        return usuarioDAO.obtenerPorNombre(nombreUsuario);
    }
}
