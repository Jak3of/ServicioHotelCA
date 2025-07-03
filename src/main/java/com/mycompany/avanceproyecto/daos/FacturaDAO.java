package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Facturas;
import java.util.List;

public interface FacturaDAO {
    void insertar(Facturas factura) throws Exception;
    Facturas obtenerPorId(int id) throws Exception;
    List<Facturas> listar() throws Exception;
    void actualizar(Facturas factura) throws Exception;
    void eliminar(int id) throws Exception;
    List<Facturas> buscarPorCliente(int clienteId) throws Exception;
    List<Facturas> buscarPorFecha(String fecha) throws Exception;
}