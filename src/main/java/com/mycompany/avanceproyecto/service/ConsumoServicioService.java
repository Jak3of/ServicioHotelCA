package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.ConsumoServicio;
import com.mycompany.avanceproyecto.daos.ConsumoServicioDAO;
import com.mycompany.avanceproyecto.daos.impl.ConsumoServicioDAOImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumoServicioService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumoServicioService.class);
    private final ConsumoServicioDAO consumoDAO;
    
    public ConsumoServicioService() {
        this.consumoDAO = new ConsumoServicioDAOImpl();
    }
    
    public void registrarConsumo(ConsumoServicio consumo) throws Exception {
        logger.info("Registrando consumo de servicio para alojamiento ID: {}", 
                   consumo.getAlojamiento().getId());
        consumoDAO.registrar(consumo);
    }
    
    public List<ConsumoServicio> listarTodos() throws Exception {
        logger.info("Listando todos los consumos");
        return consumoDAO.listarTodos();
    }
    
    public List<ConsumoServicio> listarPorAlojamiento(int idAlojamiento) throws Exception {
        logger.info("Listando consumos para alojamiento ID: {}", idAlojamiento);
        return consumoDAO.listarPorAlojamiento(idAlojamiento);
    }
    
    public void eliminarConsumo(int id) throws Exception {
        logger.info("Eliminando consumo ID: {}", id);
        consumoDAO.eliminar(id);
    }
    
    public ConsumoServicio obtenerPorId(int id) throws Exception {
        logger.info("Obteniendo consumo ID: {}", id);
        return consumoDAO.obtenerPorId(id);
    }
}
