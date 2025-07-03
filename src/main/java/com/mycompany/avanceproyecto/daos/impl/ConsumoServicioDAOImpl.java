package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;

import com.mycompany.avanceproyecto.daos.ConsumoServicioDAO;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.ConsumoServicio;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.model.Servicios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumoServicioDAOImpl implements ConsumoServicioDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConsumoServicioDAOImpl.class);
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAOImpl();
    private final ServicioDAO servicioDAO = new ServicioDAOImpl();

    @Override
    public void registrar(ConsumoServicio consumo) throws Exception {
        String sql = "INSERT INTO consumo_servicio (id_alojamiento, id_servicio, cantidad) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consumo.getAlojamiento().getId());
            stmt.setInt(2, consumo.getServicio().getId());
            stmt.setInt(3, consumo.getCantidad());
            stmt.executeUpdate();
        }
    }

    @Override
    public ConsumoServicio obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM consumo_servicio WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ConsumoServicio(
                    rs.getInt("id"),
                    alojamientoDAO.obtenerPorId(rs.getInt("id_alojamiento")),
                    servicioDAO.obtenerPorId(rs.getInt("id_servicio")),
                    rs.getInt("cantidad")
                );
            }
        }
        return null;
    }

    @Override
    public List<ConsumoServicio> listarPorAlojamiento(int idAlojamiento) throws Exception {
        List<ConsumoServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM consumo_servicio WHERE id_alojamiento = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ConsumoServicio cs = new ConsumoServicio(
                    rs.getInt("id"),
                    alojamientoDAO.obtenerPorId(rs.getInt("id_alojamiento")),
                    servicioDAO.obtenerPorId(rs.getInt("id_servicio")),
                    rs.getInt("cantidad")
                );
                lista.add(cs);
            }
        }
        return lista;
    }

    @Override
    public List<ConsumoServicio> listarTodos() throws Exception {
        List<ConsumoServicio> lista = new ArrayList<>();
        String sql = """
            SELECT cs.id, cs.id_alojamiento, cs.id_servicio, cs.cantidad,
                   s.nombre as servicio_nombre, s.precio as servicio_precio,
                   a.fecha_entrada, a.fecha_salida,
                   c.id as cliente_id, c.nombre as cliente_nombre, c.dni, c.telefono, c.correo,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio as habitacion_precio
            FROM consumo_servicio cs
            INNER JOIN servicios s ON cs.id_servicio = s.id
            INNER JOIN alojamientos a ON cs.id_alojamiento = a.id
            INNER JOIN clientes c ON a.cliente_id = c.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    // Construir el cliente
                    Clientes cliente = new Clientes(
                        rs.getInt("cliente_id"),
                        rs.getInt("dni"),
                        rs.getString("cliente_nombre"),
                        rs.getInt("telefono"),
                        rs.getString("correo")
                    );
                    
                    // Construir la habitación
                    Habitaciones habitacion = new Habitaciones(
                        rs.getInt("habitacion_id"),
                        rs.getString("numero"),
                        rs.getString("tipo"),
                        rs.getBoolean("disponible"),
                        rs.getDouble("habitacion_precio")
                    );
                    
                    // Construir el alojamiento completo
                    Alojamientos alojamiento = new Alojamientos(
                        rs.getInt("id_alojamiento"),
                        cliente,
                        habitacion,
                        LocalDate.parse(rs.getString("fecha_entrada")),
                        LocalDate.parse(rs.getString("fecha_salida"))
                    );
                    
                    // Crear servicio directamente desde el ResultSet
                    Servicios servicio = new Servicios(
                        rs.getInt("id_servicio"),
                        rs.getString("servicio_nombre"),
                        rs.getDouble("servicio_precio")
                    );
                    
                    ConsumoServicio cs = new ConsumoServicio(
                        rs.getInt("id"),
                        alojamiento,
                        servicio,
                        rs.getInt("cantidad")
                    );
                    lista.add(cs);
                } catch (Exception e) {
                    logger.error("Error al construir ConsumoServicio desde ResultSet: {}", e.getMessage());
                    // Continuar con el siguiente registro en lugar de fallar completamente
                }
            }
        }
        return lista;
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM consumo_servicio WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}