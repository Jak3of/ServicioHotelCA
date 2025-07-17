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
        String sql = "INSERT INTO alojamientos (cliente_id, habitacion_id, fecha_entrada, fecha_salida, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, alojamiento.getCliente().getId());
            stmt.setInt(2, alojamiento.getHabitacion().getId());
            stmt.setString(3, alojamiento.getFechaEntrada().toString()); // Guardar como String
            stmt.setString(4, alojamiento.getFechaSalida().toString());  // Guardar como String
            stmt.setString(5, alojamiento.getEstado().getValor());       // Guardar estado
            
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
        
        // Obtener estado del alojamiento
        String estadoStr = rs.getString("estado");
        Alojamientos.EstadoAlojamiento estado = Alojamientos.EstadoAlojamiento.fromString(estadoStr);
        
        return new Alojamientos(
            rs.getInt("id"),
            cliente,
            habitacion,
            fechaEntrada,
            fechaSalida,
            estado
        );
    }

    @Override
    public Alojamientos obtenerPorId(int id) throws Exception {
        String sql = """
            SELECT a.id, a.fecha_entrada, a.fecha_salida, a.estado,
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
        
        // Obtener estado del alojamiento
        String estadoStr = rs.getString("estado");
        Alojamientos.EstadoAlojamiento estado = Alojamientos.EstadoAlojamiento.fromString(estadoStr);
        
        return new Alojamientos(
            rs.getInt("id"),
            cliente,
            habitacion,
            fechaEntrada,
            fechaSalida,
            estado  // ← ¡AHORA SÍ INCLUYE EL ESTADO!
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
        String sql = "UPDATE alojamientos SET cliente_id = ?, habitacion_id = ?, fecha_entrada = ?, fecha_salida = ?, estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, alojamiento.getCliente().getId());
            stmt.setInt(2, alojamiento.getHabitacion().getId());
            stmt.setString(3, alojamiento.getFechaEntrada().toString());
            stmt.setString(4, alojamiento.getFechaSalida().toString());
            stmt.setString(5, alojamiento.getEstado().getValor());
            stmt.setInt(6, alojamiento.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void actualizarEstado(int id, Alojamientos.EstadoAlojamiento estado) throws Exception {
        String sql = "UPDATE alojamientos SET estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado.getValor());
            stmt.setInt(2, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("No se encontró el alojamiento con ID: " + id);
            }
            logger.info("Estado del alojamiento {} actualizado a: {}", id, estado.getValor());
        }
    }

    
    @Override
    public void eliminar(int id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Verificar si tiene facturas asociadas (NUEVA LÓGICA)
            String sqlCheckFacturas = "SELECT COUNT(*) FROM facturas WHERE alojamiento_id = ?";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckFacturas)) {
                stmtCheck.setInt(1, id);
                try (ResultSet rs = stmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        conn.rollback();
                        throw new Exception("No se puede eliminar el alojamiento porque tiene facturas asociadas. " +
                                          "Debe procesar el pago en el módulo de Facturas para liberar la habitación.");
                    }
                }
            }
            
            // 2. Eliminar servicios asociados al alojamiento
            String sqlServicios = "DELETE FROM alojamiento_servicio WHERE id_alojamiento = ?";
            try (PreparedStatement stmtServicios = conn.prepareStatement(sqlServicios)) {
                stmtServicios.setInt(1, id);
                int serviciosEliminados = stmtServicios.executeUpdate();
                logger.debug("Eliminados {} servicios asociados al alojamiento {}", serviciosEliminados, id);
            }
            
            // 3. Eliminar el alojamiento
            String sqlAlojamiento = "DELETE FROM alojamientos WHERE id = ?";
            try (PreparedStatement stmtAlojamiento = conn.prepareStatement(sqlAlojamiento)) {
                stmtAlojamiento.setInt(1, id);
                int filasAfectadas = stmtAlojamiento.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback();
                    throw new Exception("No se encontró el alojamiento con ID: " + id);
                }
                logger.debug("Alojamiento {} eliminado exitosamente", id);
            }
            
            conn.commit(); // Confirmar transacción
            logger.info("Alojamiento {} eliminado exitosamente (sin facturar)", id);
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    logger.error("Error al hacer rollback", rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeEx) {
                    logger.error("Error al cerrar conexión", closeEx);
                }
            }
        }
    }

    @Override
    public List<Alojamientos> listar() throws Exception {
        List<Alojamientos> lista = new ArrayList<>();
        String sql = """
            SELECT a.id, a.fecha_entrada, a.fecha_salida, a.estado,
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
                
                // Obtener estado del alojamiento
                String estadoStr = rs.getString("estado");
                Alojamientos.EstadoAlojamiento estado = Alojamientos.EstadoAlojamiento.fromString(estadoStr);
                
                // Construir el alojamiento con estado
                Alojamientos alojamiento = new Alojamientos(
                    rs.getInt("id"),
                    cliente,
                    habitacion,
                    fechaEntrada,
                    fechaSalida,
                    estado  // ← ¡INCLUIR EL ESTADO!
                );
                
                lista.add(alojamiento);
            }
        }
        return lista;
    }

    @Override
    public void eliminarAlojamientoPagado(int id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarAlojamientoPagado'");
    }
}
