package com.mycompany.avanceproyecto.repository;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.model.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioRepository {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioRepository.class);
    
    // admin 123
    public Usuarios findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Error al buscar usuario", e);
        }
        return null;
    }
}
