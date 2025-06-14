package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Habitaciones;
import java.util.List;

public interface HabitacionDAO {
    void insertar(Habitaciones habitacion) throws Exception;
    Habitaciones obtenerPorId(int id) throws Exception;
    List<Habitaciones> listar() throws Exception;
    void actualizar(Habitaciones habitacion) throws Exception;
    void eliminar(int id) throws Exception;
    List<Habitaciones> listarDisponibles() throws Exception; // Método que faltaba
}
