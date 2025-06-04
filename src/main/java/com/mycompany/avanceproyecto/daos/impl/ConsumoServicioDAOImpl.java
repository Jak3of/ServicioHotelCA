
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;

import com.mycompany.avanceproyecto.daos.ConsumoServicioDAO;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.ConsumoServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ConsumoServicioDAOImpl implements ConsumoServicioDAO {

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
        String sql = "SELECT * FROM consumo_servicio";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM consumo_servicio WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}