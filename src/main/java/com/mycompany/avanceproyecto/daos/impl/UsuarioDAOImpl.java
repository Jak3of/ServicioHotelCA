package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.UsuarioDAO;
import com.mycompany.avanceproyecto.model.Usuarios;
import com.mycompany.avanceproyecto.util.PasswordEncryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAOImpl.class);

    @Override
    public void insertar(Usuarios usuario) throws Exception {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombreUsuario());
            // Encriptar la contraseña antes de guardarla
            String encryptedPassword = PasswordEncryption.encrypt(usuario.getContrasena());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, usuario.getRol());
            
            int filasAfectadas = stmt.executeUpdate();
            logger.info("Usuario insertado con contraseña encriptada. Filas afectadas: {}", filasAfectadas);
        }
    }

    @Override
    public Usuarios obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirUsuario(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Usuarios obtenerPorNombre(String nombreUsuario) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirUsuario(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuarios> listar() throws Exception {
        List<Usuarios> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre_usuario";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(construirUsuario(rs));
            }
        }
        return usuarios;
    }

    @Override
    public void actualizar(Usuarios usuario) throws Exception {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, contrasena = ?, rol = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombreUsuario());
            
            // Solo encriptar si la contraseña no está ya encriptada
            String passwordToStore = usuario.getContrasena();
            if (!PasswordEncryption.isEncrypted(passwordToStore)) {
                passwordToStore = PasswordEncryption.encrypt(passwordToStore);
                logger.debug("Contraseña encriptada durante actualización");
            }
            
            stmt.setString(2, passwordToStore);
            stmt.setString(3, usuario.getRol());
            stmt.setInt(4, usuario.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            logger.info("Usuario actualizado. Filas afectadas: {}", filasAfectadas);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            logger.info("Usuario eliminado. Filas afectadas: {}", filasAfectadas);
        }
    }

    private Usuarios construirUsuario(ResultSet rs) throws SQLException {
        return new Usuarios(
            rs.getInt("id"),
            rs.getString("nombre_usuario"),
            rs.getString("contrasena"), // La contraseña se mantiene encriptada
            rs.getString("rol")
        );
    }
}
