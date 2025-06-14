package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.model.Clientes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClienteDAOImpl.class);

    @Override
    public void insertar(Clientes cliente) throws Exception {
        logger.info("Insertando nuevo cliente con DNI: {}", cliente.getDni());
        String sql = "INSERT INTO clientes (nombre, dni, telefono, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getDni());          // Cambiado a setInt
            stmt.setInt(3, cliente.getTelefono());     // Cambiado a setInt
            stmt.setString(4, cliente.getCorreo());
            stmt.executeUpdate();
            logger.info("Cliente insertado correctamente con ID: {}", cliente.getId());
        } catch (SQLException e) {
            logger.error("Error al insertar cliente: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Clientes obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Clientes obtenerPorDni(int dni) throws Exception {  // Cambiado de String a int
        String sql = "SELECT * FROM clientes WHERE dni = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);  // Ahora usa setInt en lugar de setString
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Clientes> listar() throws Exception {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(construirCliente(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Clientes cliente) throws Exception {
        String sql = "UPDATE clientes SET nombre = ?, dni = ?, telefono = ?, correo = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getDni());          // Cambiado a setInt
            stmt.setInt(3, cliente.getTelefono());     // Cambiado a setInt
            stmt.setString(4, cliente.getCorreo());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        logger.info("Eliminando cliente con ID: {}", id);
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.debug("Cliente eliminado correctamente");
        } catch (SQLException e) {
            logger.error("Error al eliminar cliente: {}", e.getMessage());
            throw e;
        }
    }

    private Clientes construirCliente(ResultSet rs) throws SQLException {
        return new Clientes(
            rs.getInt("id"),
            rs.getInt("dni"),
            rs.getString("nombre"),
            rs.getInt("telefono"),
            rs.getString("correo")
        );
    }
}