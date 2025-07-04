package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Habitaciones;
import com.mycompany.avanceproyecto.daos.HabitacionDAO;
import com.mycompany.avanceproyecto.daos.impl.HabitacionDAOImpl;
import java.util.List;

public class HabitacionService {
    private final HabitacionDAO habitacionDAO;
    
    public HabitacionService() {
        this.habitacionDAO = new HabitacionDAOImpl();
    }
    
    public void guardarHabitacion(Habitaciones habitacion) throws Exception {
        habitacionDAO.insertar(habitacion);
    }
    
    public List<Habitaciones> listarHabitaciones() throws Exception {
        return habitacionDAO.listar();
    }
    
    public void eliminarHabitacion(int id) throws Exception {
        habitacionDAO.eliminar(id);
    }
    
    public void actualizarHabitacion(Habitaciones habitacion) throws Exception {
        habitacionDAO.actualizar(habitacion);
    }
    
    public void guardarOActualizarHabitacion(Habitaciones habitacion) throws Exception {
        if (habitacion.getId() != null) {
            habitacionDAO.actualizar(habitacion);
        } else {
            habitacionDAO.insertar(habitacion);
        }
    }
    
    public List<Habitaciones> listarDisponibles() throws Exception {
        
        return habitacionDAO.listarDisponibles();
    }
  public void actualizarHabitacionOcupada(int idHabitacion, boolean disponible) throws Exception {
    Habitaciones h = habitacionDAO.obtenerPorId(idHabitacion);
    if (h != null) {
        h.setDisponible(disponible);
        habitacionDAO.actualizar(h);
    }
}
}


