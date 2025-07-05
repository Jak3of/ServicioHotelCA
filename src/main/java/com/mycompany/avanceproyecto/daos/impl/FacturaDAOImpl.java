package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.FacturaDAO;
import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Habitaciones;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacturaDAOImpl implements FacturaDAO {
    private static final Logger logger = LoggerFactory.getLogger(FacturaDAOImpl.class);

    @Override
    public void insertar(Facturas factura) throws Exception {
        String sql = "INSERT INTO facturas (fecha, cliente_id, alojamiento_id, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, factura.getFecha().toString());
            stmt.setInt(2, factura.getCliente().getId());
            stmt.setInt(3, factura.getAlojamiento().getId());
            stmt.setDouble(4, factura.getTotal());
            
            stmt.executeUpdate();
            
            // Obtener ID generado
            try (Statement stmtId = conn.createStatement();
                 ResultSet rs = stmtId.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    factura.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Facturas> listar() throws Exception {
        List<Facturas> lista = new ArrayList<>();
        String sql = """
            SELECT f.id, f.fecha, f.total,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   a.id as alojamiento_id, a.fecha_entrada, a.fecha_salida,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM facturas f
            INNER JOIN clientes c ON f.cliente_id = c.id
            INNER JOIN alojamientos a ON f.alojamiento_id = a.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
            ORDER BY f.fecha DESC
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(construirFactura(rs));
            }
        }
        return lista;
    }

    @Override
    public Facturas obtenerPorId(int id) throws Exception {
        String sql = """
            SELECT f.id, f.fecha, f.total,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   a.id as alojamiento_id, a.fecha_entrada, a.fecha_salida,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM facturas f
            INNER JOIN clientes c ON f.cliente_id = c.id
            INNER JOIN alojamientos a ON f.alojamiento_id = a.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
            WHERE f.id = ?
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirFactura(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar(Facturas factura) throws Exception {
        String sql = "UPDATE facturas SET fecha = ?, cliente_id = ?, alojamiento_id = ?, total = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, factura.getFecha().toString());
            stmt.setInt(2, factura.getCliente().getId());
            stmt.setInt(3, factura.getAlojamiento().getId());
            stmt.setDouble(4, factura.getTotal());
            stmt.setInt(5, factura.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM facturas WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Facturas> buscarPorCliente(int clienteId) throws Exception {
        List<Facturas> lista = new ArrayList<>();
        String sql = """
            SELECT f.id, f.fecha, f.total,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   a.id as alojamiento_id, a.fecha_entrada, a.fecha_salida,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM facturas f
            INNER JOIN clientes c ON f.cliente_id = c.id
            INNER JOIN alojamientos a ON f.alojamiento_id = a.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
            WHERE c.id = ?
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirFactura(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Facturas> buscarPorFecha(String fecha) throws Exception {
        List<Facturas> lista = new ArrayList<>();
        String sql = """
            SELECT f.id, f.fecha, f.total,
                   c.id as cliente_id, c.nombre, c.dni, c.telefono, c.correo,
                   a.id as alojamiento_id, a.fecha_entrada, a.fecha_salida,
                   h.id as habitacion_id, h.numero, h.tipo, h.disponible, h.precio
            FROM facturas f
            INNER JOIN clientes c ON f.cliente_id = c.id
            INNER JOIN alojamientos a ON f.alojamiento_id = a.id
            INNER JOIN habitaciones h ON a.habitacion_id = h.id
            WHERE f.fecha = ?
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fecha);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirFactura(rs));
                }
            }
        }
        return lista;
    }

    private Facturas construirFactura(ResultSet rs) throws SQLException {
        // Construir cliente
        Clientes cliente = new Clientes(
            rs.getInt("cliente_id"),
            rs.getInt("dni"),
            rs.getString("nombre"),
            rs.getInt("telefono"),
            rs.getString("correo")
        );
        
        // Construir habitación
        Habitaciones habitacion = new Habitaciones(
            rs.getInt("habitacion_id"),
            rs.getString("numero"),
            rs.getString("tipo"),
            rs.getBoolean("disponible"),
            rs.getDouble("precio")
        );
        
        // Construir alojamiento
        Alojamientos alojamiento = new Alojamientos(
            rs.getInt("alojamiento_id"),
            cliente,
            habitacion,
            LocalDate.parse(rs.getString("fecha_entrada")),
            LocalDate.parse(rs.getString("fecha_salida"))
        );
        
        // Construir factura
        return new Facturas(
            rs.getInt("id"),
            LocalDate.parse(rs.getString("fecha")),
            cliente,
            alojamiento,
            rs.getDouble("total")
        );
    }
    
}
