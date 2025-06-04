
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.model.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAOImpl implements ClienteDAO {

    public void guardar(Clientes cliente) throws Exception {
        String sql = "INSERT INTO clientes (nombre, dni, telefono, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getDni());
            stmt.setInt(3, cliente.getTelefono());
            stmt.setString(4, cliente.getCorreo());
            stmt.executeUpdate();
        }
    }

    public void actualizar(Clientes cliente) throws Exception {
        String sql = "UPDATE clientes SET nombre=?, telefono=?, correo=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getTelefono());
            stmt.setString(3, cliente.getCorreo());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Clientes buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM clientes WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearCliente(rs);
            }
        }
        return null;
    }

    public Clientes buscarPorDni(String dni) throws Exception {
        String sql = "SELECT * FROM clientes WHERE dni=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearCliente(rs);
            }
        }
        return null;
    }

    public List<Clientes> listarTodos() throws Exception {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        }
        return lista;
    }

    private Clientes mapearCliente(ResultSet rs) throws SQLException {
        Clientes cliente = new Clientes();
        cliente.setId(rs.getInt("id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setDni(rs.getInt("dni"));
        cliente.setTelefono(rs.getInt("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        return cliente;
    }
    @Override
public Clientes obtenerPorId(int id) throws Exception {
    String sql = "SELECT * FROM clientes WHERE id = ?";
    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Clientes(
                rs.getInt("id"),
                rs.getInt("dni"),
                rs.getString("nombre"),
                rs.getInt("telefono"),
                rs.getString("correo")
            );
        }
    }
    return null;
}

public Clientes obtenerPorDni(int dni) {
    String sql = "SELECT * FROM clientes WHERE dni = ?";
    Clientes cliente = null;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, dni);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cliente = new Clientes();
            cliente.setId(rs.getInt("id"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setDni(rs.getInt("dni"));
            cliente.setTelefono(rs.getInt("telefono"));
            cliente.setCorreo(rs.getString("correo"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return cliente;
}
}
