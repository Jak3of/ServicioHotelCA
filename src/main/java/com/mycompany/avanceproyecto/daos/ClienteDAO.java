
package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Clientes;
import java.util.List;


public interface ClienteDAO {
 

    void guardar(Clientes cliente) throws Exception;
    void actualizar(Clientes cliente) throws Exception;
    void eliminar(int id) throws Exception;
    Clientes buscarPorId(int id) throws Exception;
    Clientes buscarPorDni(String dni) throws Exception;
    List<Clientes> listarTodos() throws Exception;
    Clientes obtenerPorId(int id) throws Exception;
    Clientes obtenerPorDni(int dni)throws Exception;
    
}

