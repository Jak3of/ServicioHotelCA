package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Habitaciones;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

public class AlojamientoDAOImpl implements AlojamientoDAO {
    private static final Logger logger = LoggerFactory.getLogger(AlojamientoDAOImpl.class);

    @Override
    public void insertar(Alojamientos alojamiento) throws Exception {
        String sql = "INSERT INTO alojamientos (cliente_id, habitacion_id, fecha_entrada, fecha_salida) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, alojamiento.getCliente().getId());
            stmt.setInt(2, alojamiento.getHabitacion().getId());
            stmt.setString(3, alojamiento.getFechaEntrada().toString()); // Guardar como String
            stmt.setString(4, alojamiento.getFechaSalida().toString());  // Guardar como String
            
            stmt.executeUpdate();
            
            // Obtener el ID generado usando last_insert_rowid()
            try (Statement stmtId = conn.createStatement();
                 ResultSet rs = stmtId.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    alojamiento.setId(rs.getInt(1));
                }
            }
        }
    }

    private Alojamientos construirAlojamiento(ResultSet rs) throws SQLException {
        Clientes cliente = new Clientes(
            rs.getInt("cliente_id"),
            rs.getInt("dni"),
            rs.getString("nombre"),
            rs.getInt("telefono"),
            rs.getString("correo")
        );
        
        Habitaciones habitacion = new Habitaciones(
            rs.getInt("habitacion_id"),
            rs.getString("numero"),
            rs.getString("tipo"),
            rs.getBoolean("disponible"),
            rs.getDouble("precio")
        );
        
        // Cambiar para usar getString en lugar de getDate
        LocalDate fechaEntrada = LocalDate.parse(rs.getString("fecha_entrada"));
        LocalDate fechaSalida = LocalDate.parse(rs.getString("fecha_salida"));
        
        return new Alojamientos(
            rs.getInt("id"),
            cliente,
            habitacion,
            fechaEntrada,
            fechaSalida
        );
    }

    @Override
    public Alojamientos obtenerPorId(int id) throws Exception {
        String sql = """
            SELECT a.id, a.fecha_entrada, a.fecha_salida,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM alojamientos a
            JOIN clientes c ON a.cliente_id = c.id
            JOIN habitaciones h ON a.habitacion_id = h.id
            WHERE a.id = ?
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirAlojamientoFromJoin(rs);
                }
            }
        }
        return null;
    }

    private Alojamientos construirAlojamientoFromJoin(ResultSet rs) throws SQLException {
        Clientes cliente = new Clientes(
            rs.getInt("cliente_id"),
            rs.getInt("dni"),
            rs.getString("nombre"),
            rs.getInt("telefono"),
            rs.getString("correo")
        );
        
        Habitaciones habitacion = new Habitaciones(
            rs.getInt("habitacion_id"),
            rs.getString("numero"),
            rs.getString("tipo"),
            rs.getBoolean("disponible"),
            rs.getDouble("precio")
        );
        
        LocalDate fechaEntrada = LocalDate.parse(rs.getString("fecha_entrada"));
        LocalDate fechaSalida = LocalDate.parse(rs.getString("fecha_salida"));
        
        return new Alojamientos(
            rs.getInt("id"),
            cliente,
            habitacion,
            fechaEntrada,
            fechaSalida
        );
    }

    // Implementación de métodos para alojamiento_servicio
    @Override
    public void agregarServicio(int idAlojamiento, int idServicio) throws Exception {
        String sql = "INSERT INTO alojamiento_servicio (id_alojamiento, id_servicio) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            stmt.setInt(2, idServicio);
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminarServicio(int idAlojamiento, int idServicio) throws Exception {
        String sql = "DELETE FROM alojamiento_servicio WHERE id_alojamiento = ? AND id_servicio = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            stmt.setInt(2, idServicio);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Servicios> listarServiciosPorAlojamiento(int idAlojamiento) throws Exception {
        List<Servicios> servicios = new ArrayList<>();
        String sql = """
            SELECT s.* FROM servicios s
            JOIN alojamiento_servicio as_s ON s.id = as_s.id_servicio
            WHERE as_s.id_alojamiento = ?
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    servicios.add(new Servicios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                    ));
                }
            }
        }
        return servicios;
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
    public List<Alojamientos> listar() throws Exception {
        List<Alojamientos> lista = new ArrayList<>();
        String sql = """
            SELECT a.id, a.fecha_entrada, a.fecha_salida,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM alojamientos a
            INNER JOIN clientes c ON a.cliente_id = c.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Construir el cliente
                Clientes cliente = new Clientes(
                    rs.getInt("cliente_id"),
                    rs.getInt("dni"),
                    rs.getString("nombre"),
                    rs.getInt("telefono"),
                    rs.getString("correo")
                );
                
                // Construir la habitación
                Habitaciones habitacion = new Habitaciones(
                    rs.getInt("habitacion_id"),
                    rs.getString("numero"),
                    rs.getString("tipo"),
                    rs.getBoolean("disponible"),
                    rs.getDouble("precio")
                );
                
                // Convertir fechas desde String
                LocalDate fechaEntrada = LocalDate.parse(rs.getString("fecha_entrada"));
                LocalDate fechaSalida = LocalDate.parse(rs.getString("fecha_salida"));
                
                // Construir el alojamiento
                Alojamientos alojamiento = new Alojamientos(
                    rs.getInt("id"),
                    cliente,
                    habitacion,
                    fechaEntrada,
                    fechaSalida
                );
                
                lista.add(alojamiento);
            }
        }
        return lista;
    }
}
