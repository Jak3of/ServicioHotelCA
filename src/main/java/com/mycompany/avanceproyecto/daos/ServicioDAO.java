package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Servicios;
import java.util.List;

public interface ServicioDAO {
    void insertar(Servicios servicio) throws Exception;
    Servicios obtenerPorId(int id) throws Exception;
    List<Servicios> listar() throws Exception;
    void actualizar(Servicios servicio) throws Exception;
    void eliminar(int id) throws Exception;

    List<Servicios> listarPorAlojamiento(int idAlojamiento) throws Exception;
    
}
