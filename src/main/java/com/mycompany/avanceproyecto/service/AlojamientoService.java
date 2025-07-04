package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.daos.AlojamientoDAO;
import com.mycompany.avanceproyecto.daos.impl.AlojamientoDAOImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlojamientoService {
    private static final Logger logger = LoggerFactory.getLogger(AlojamientoService.class);
    private final AlojamientoDAO alojamientoDAO;
    
    public AlojamientoService() {
        this.alojamientoDAO = new AlojamientoDAOImpl();
    }
    
    public void guardarOActualizarAlojamiento(Alojamientos alojamiento) throws Exception {
        if (alojamiento.getId() > 0) {
            logger.info("Actualizando alojamiento ID: {}", alojamiento.getId());
            alojamientoDAO.actualizar(alojamiento);
        } else {
            logger.info("Guardando nuevo alojamiento");
            alojamientoDAO.insertar(alojamiento);
        }
    }
    
    public List<Alojamientos> listarAlojamientos() throws Exception {
        logger.info("Listando todos los alojamientos");
        return alojamientoDAO.listar();
    }
    
    public void eliminarAlojamiento(int id) throws Exception {
        logger.info("Eliminando alojamiento ID: {}", id);
        alojamientoDAO.eliminar(id);
    }
    
    // Métodos para manejar servicios asociados
    public void agregarServicio(int idAlojamiento, int idServicio) throws Exception {
        logger.info("Agregando servicio {} al alojamiento {}", idServicio, idAlojamiento);
        alojamientoDAO.agregarServicio(idAlojamiento, idServicio);
    }
    
    public void eliminarServicio(int idAlojamiento, int idServicio) throws Exception {
        logger.info("Eliminando servicio {} del alojamiento {}", idServicio, idAlojamiento);
        alojamientoDAO.eliminarServicio(idAlojamiento, idServicio);
    }
    
    public List<Servicios> listarServiciosDeAlojamiento(int idAlojamiento) throws Exception {
        logger.info("Listando servicios del alojamiento ID: {}", idAlojamiento);
        return alojamientoDAO.listarServiciosPorAlojamiento(idAlojamiento);
    }
    
    public Alojamientos obtenerAlojamiento(int id) throws Exception {
        logger.info("Obteniendo alojamiento ID: {}", id);
        return alojamientoDAO.obtenerPorId(id);
    }
    public Alojamientos obtenerPorId(int id) throws Exception {
    return alojamientoDAO.obtenerPorId(id);
}
}
