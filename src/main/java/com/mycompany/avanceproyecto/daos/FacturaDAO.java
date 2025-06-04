
package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Facturas;
import java.util.List;


public interface FacturaDAO {
    void registrar(Facturas factura);
    List<Facturas> listarTodas();
    Facturas obtenerPorId(int id);
}