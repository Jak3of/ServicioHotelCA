
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.Servicios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ServicioDAOImpl implements ServicioDAO {

    @Override
    public void insertar(Servicios servicio) throws Exception {
        String sql = "INSERT INTO servicios (nombre, precio) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servicio.getNombre());
            stmt.setDouble(2, servicio.getPrecio());
            stmt.executeUpdate();
        }
    }

    @Override
    public Servicios obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM servicios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Servicios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Servicios> listar() throws Exception {
        String sql = "SELECT * FROM servicios";
        List<Servicios> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Servicios(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio")
                ));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Servicios servicio) throws Exception {
        String sql = "UPDATE servicios SET nombre = ?, precio = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servicio.getNombre());
            stmt.setDouble(2, servicio.getPrecio());
            stmt.setInt(3, servicio.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM servicios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    @Override
    public List<Servicios> listarPorAlojamiento(int idAlojamiento) {
    List<Servicios> servicios = new ArrayList<>();
    String sql = """
        SELECT s.* FROM servicios s
        JOIN alojamiento_servicio as ON s.id = as.id_servicio
        WHERE as.id_alojamiento = ?
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idAlojamiento);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Servicios s = new Servicios();
            s.setId(rs.getInt("id"));
            s.setNombre(rs.getString("nombre"));
            s.setPrecio(rs.getDouble("precio"));
            servicios.add(s);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return servicios;
}
}
