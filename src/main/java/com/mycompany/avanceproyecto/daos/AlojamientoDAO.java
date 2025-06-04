
package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Alojamientos;
import java.util.List;


public interface AlojamientoDAO {
    void insertar(Alojamientos alojamiento) throws Exception;
    void actualizar(Alojamientos alojamiento) throws Exception;
    void eliminar(int id) throws Exception;
    Alojamientos obtenerPorId(int id) throws Exception;
    List<Alojamientos> obtenerTodos() throws Exception;
}