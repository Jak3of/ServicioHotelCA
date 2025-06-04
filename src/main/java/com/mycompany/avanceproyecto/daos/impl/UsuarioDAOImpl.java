
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.UsuarioDAO;
import com.mycompany.avanceproyecto.model.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




public class UsuarioDAOImpl implements UsuarioDAO {

    
    @Override
    public void insertar(Usuarios usuario) throws Exception {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setString(3, usuario.getRol());
            stmt.executeUpdate();
        }
    }

    
    @Override
    public Usuarios obtenerPorNombre(String nombreUsuario) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
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
        }
        return null;
    }

  
    @Override
    public List<Usuarios> listar() throws Exception {
        String sql = "SELECT * FROM usuarios";
        List<Usuarios> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuarios(
                    rs.getInt("id"),
                    rs.getString("nombre_usuario"),
                    rs.getString("contrasena"),
                    rs.getString("rol")
                ));
            }
        }
        return lista;
    }
}
