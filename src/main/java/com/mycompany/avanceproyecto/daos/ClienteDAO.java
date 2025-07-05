package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Clientes;
import java.util.List;

public interface ClienteDAO {
    void insertar(Clientes cliente) throws Exception;
    Clientes obtenerPorId(int id) throws Exception;
    Clientes obtenerPorDni(int dni) throws Exception;  // Cambiado de String a int
    List<Clientes> listar() throws Exception;
    void actualizar(Clientes cliente) throws Exception;
    void eliminar(int id) throws Exception;   
}

