
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Habitaciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AlojamientoDAOImpl implements AlojamientoDAO {
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final HabitacionDAO habitacionDAO = new HabitacionDAOImpl();

    @Override
    public void insertar(Alojamientos alojamiento) throws Exception {
        String sql = "INSERT INTO alojamientos (cliente_id, habitacion_id, fecha_entrada, fecha_salida) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, alojamiento.getCliente().getId());
            stmt.setInt(2, alojamiento.getHabitacion().getId());
            stmt.setString(3, alojamiento.getFechaEntrada().toString());
            stmt.setString(4, alojamiento.getFechaSalida().toString());
            stmt.executeUpdate();

            // Obtener ID generado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                alojamiento.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void actualizar(Alojamientos alojamiento) throws Exception {
        String sql = "UPDATE alojamientos SET cliente_id = ?, habitacion_id = ?, fecha_entrada = ?, fecha_salida = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, alojamiento.getCliente().getId());
            stmt.setInt(2, alojamiento.getHabitacion().getId());
            stmt.setString(3, alojamiento.getFechaEntrada().toString());
            stmt.setString(4, alojamiento.getFechaSalida().toString());
            stmt.setInt(5, alojamiento.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM alojamientos WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Alojamientos obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM alojamientos WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Clientes cliente = clienteDAO.obtenerPorId(rs.getInt("cliente_id"));
                Habitaciones habitacion = habitacionDAO.obtenerPorId(rs.getInt("habitacion_id"));
                return new Alojamientos(
                    rs.getInt("id"),
                    cliente,
                    habitacion,
                    LocalDate.parse(rs.getString("fecha_entrada")),
                    LocalDate.parse(rs.getString("fecha_salida"))
                );
            }
        }
        return null;
    }

    @Override
    public List<Alojamientos> obtenerTodos() throws Exception {
        List<Alojamientos> lista = new ArrayList<>();
        String sql = "SELECT * FROM alojamientos";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clientes cliente = clienteDAO.obtenerPorId(rs.getInt("cliente_id"));
                Habitaciones habitacion = habitacionDAO.obtenerPorId(rs.getInt("habitacion_id"));
                Alojamientos alojamiento = new Alojamientos(
                    rs.getInt("id"),
                    cliente,
                    habitacion,
                    LocalDate.parse(rs.getString("fecha_entrada")),
                    LocalDate.parse(rs.getString("fecha_salida"))
                );
                lista.add(alojamiento);
            }
        }
        return lista;
    }
}
