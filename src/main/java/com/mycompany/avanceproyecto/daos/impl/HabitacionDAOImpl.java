package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.model.Habitaciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class HabitacionDAOImpl implements HabitacionDAO {

    @Override
    public void insertar(Habitaciones habitacion) throws Exception {
        String sql = "INSERT INTO habitaciones (numero, tipo, precio, disponible) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, habitacion.getNumero());
            stmt.setString(2, habitacion.getTipo());
            stmt.setDouble(3, habitacion.getPrecio());
            stmt.setInt(4, habitacion.isDisponible() ? 1 : 0); // Cambiado a isDisponible
            stmt.executeUpdate();
        }
    }

    @Override
    public Habitaciones obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM habitaciones WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Habitaciones(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getString("tipo"),                        
                        rs.getInt("disponible") == 1,
                        rs.getDouble("precio")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Habitaciones> listar() throws Exception {
        String sql = "SELECT * FROM habitaciones";
        List<Habitaciones> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Habitaciones(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    rs.getString("tipo"),                  
                    rs.getInt("disponible") == 1,
                    rs.getDouble("precio")
                ));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Habitaciones habitacion) throws Exception {
        String sql = "UPDATE habitaciones SET numero = ?, tipo = ?, precio = ?, disponible = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, habitacion.getNumero());
            stmt.setString(2, habitacion.getTipo());
            stmt.setDouble(3, habitacion.getPrecio());
            stmt.setInt(4, habitacion.isDisponible() ? 1 : 0); // Cambiado a isDisponible
            stmt.setInt(5, habitacion.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM habitaciones WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Habitaciones> listarDisponibles() throws Exception {
        String sql = "SELECT * FROM habitaciones WHERE disponible = 1";
        List<Habitaciones> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Habitaciones(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    rs.getString("tipo"),
                    true,
                    rs.getDouble("precio")
                ));
            }
        }
        return lista;
    }
}
