
package com.mycompany.avanceproyecto.daos.impl;

import com.mycompany.avanceproyecto.config.DatabaseConfig;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.FacturaDAO;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.model.Facturas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FacturaDAOImpl implements FacturaDAO {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final AlojamientoDAO alojamientoDAO = new AlojamientoDAOImpl();
    private final ServicioDAO servicioDAO = new ServicioDAOImpl();

    public void registrar(Facturas factura) {
        String sql = "INSERT INTO facturas (fecha, cliente_dni, alojamiento_id, total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, factura.getFecha().toString());
            stmt.setInt(2, factura.getCliente().getDni());
            stmt.setInt(3, factura.getAlojamiento().getId());
            stmt.setDouble(4, factura.getTotal());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Facturas> listarTodas() {
        List<Facturas> lista = new ArrayList<>();
        String sql = "SELECT * FROM facturas";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Facturas factura = new Facturas();
                factura.setId(rs.getInt("id"));
                factura.setFecha(LocalDate.parse(rs.getString("fecha")));

                Clientes cliente = clienteDAO.obtenerPorDni(rs.getInt("cliente_dni"));
                Alojamientos alojamiento = alojamientoDAO.obtenerPorId(rs.getInt("alojamiento_id"));

                factura.setCliente(cliente);
                factura.setAlojamiento(alojamiento);
                factura.setTotal(rs.getDouble("total"));

                // Lista de servicios asociados 
                factura.setServicios(servicioDAO.listarPorAlojamiento(alojamiento.getId()));

                lista.add(factura);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Facturas obtenerPorId(int id) {
        String sql = "SELECT * FROM facturas WHERE id = ?";
        Facturas factura = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                factura = new Facturas();
                factura.setId(rs.getInt("id"));
                factura.setFecha(LocalDate.parse(rs.getString("fecha")));

                Clientes cliente = clienteDAO.obtenerPorDni(rs.getInt("cliente_dni"));
                Alojamientos alojamiento = alojamientoDAO.obtenerPorId(rs.getInt("alojamiento_id"));

                factura.setCliente(cliente);
                factura.setAlojamiento(alojamiento);
                factura.setTotal(rs.getDouble("total"));

                factura.setServicios(servicioDAO.listarPorAlojamiento(alojamiento.getId()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return factura;
    }
}