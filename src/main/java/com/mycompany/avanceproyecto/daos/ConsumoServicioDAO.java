
package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.ConsumoServicio;
import java.util.List;


public interface ConsumoServicioDAO {
    void registrar(ConsumoServicio consumo) throws Exception;
    ConsumoServicio obtenerPorId(int id) throws Exception;
    List<ConsumoServicio> listarPorAlojamiento(int idAlojamiento) throws Exception;
    List<ConsumoServicio> listarTodos() throws Exception;
    void eliminar(int id) throws Exception;
}
