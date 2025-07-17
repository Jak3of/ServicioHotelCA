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
    
    public List<Alojamientos> listarAlojamientosActivos() throws Exception {
        logger.info("Listando solo alojamientos activos");
        List<Alojamientos> todosLosAlojamientos = alojamientoDAO.listar();
        return todosLosAlojamientos.stream()
                .filter(alojamiento -> alojamiento.isActivo())
                .collect(java.util.stream.Collectors.toList());
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
    
    public void actualizarEstado(int idAlojamiento, Alojamientos.EstadoAlojamiento estado) throws Exception {
        logger.info("Actualizando estado del alojamiento {} a: {}", idAlojamiento, estado.getValor());
        alojamientoDAO.actualizarEstado(idAlojamiento, estado);
    }
    
    /**
     * Método especial para eliminar alojamiento después de facturar/pagar
     * Este método bypassa la validación de facturas asociadas
     */
    public void eliminarAlojamientoPagado(int idAlojamiento) throws Exception {
        logger.info("Eliminando alojamiento pagado ID: {}", idAlojamiento);
        
        // Eliminar servicios asociados primero
        try {
            // Obtener todos los servicios del alojamiento
            List<Servicios> servicios = alojamientoDAO.listarServiciosPorAlojamiento(idAlojamiento);
            for (Servicios servicio : servicios) {
                alojamientoDAO.eliminarServicio(idAlojamiento, servicio.getId());
            }
            logger.debug("Servicios eliminados para alojamiento {}", idAlojamiento);
        } catch (Exception e) {
            logger.warn("Error al eliminar servicios del alojamiento {}: {}", idAlojamiento, e.getMessage());
        }
        
        // Eliminar directamente sin validar facturas (ya que viene del proceso de facturación)
        alojamientoDAO.eliminarAlojamientoPagado(idAlojamiento);
        logger.info("Alojamiento {} eliminado después del pago", idAlojamiento);
    }
}
