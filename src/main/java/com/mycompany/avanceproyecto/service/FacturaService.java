package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Facturas;
import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.daos.FacturaDAO;
import com.mycompany.avanceproyecto.daos.impl.FacturaDAOImpl;
import com.mycompany.avanceproyecto.service.ConsumoServicioService;
import java.util.List;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacturaService {
    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);
    private final FacturaDAO facturaDAO;
    private final ConsumoServicioService consumoService;
    
    public FacturaService() {
        this.facturaDAO = new FacturaDAOImpl();
        this.consumoService = new ConsumoServicioService();
    }
    
    public void generarFactura(Alojamientos alojamiento) throws Exception {
        logger.info("Generando factura para alojamiento ID: {}", alojamiento.getId());
        
        // Calcular total (costo habitación + servicios consumidos)
        double totalHabitacion = alojamiento.getHabitacion().getPrecio();
        double totalServicios = 0.0;
        
        try {
            List<com.mycompany.avanceproyecto.model.ConsumoServicio> consumos = 
                consumoService.listarPorAlojamiento(alojamiento.getId());
            
            for (com.mycompany.avanceproyecto.model.ConsumoServicio consumo : consumos) {
                totalServicios += consumo.getCantidad() * consumo.getServicio().getPrecio();
            }
        } catch (Exception e) {
            logger.warn("Error al calcular servicios consumidos: {}", e.getMessage());
        }
        
        double total = totalHabitacion + totalServicios;
        
        Facturas factura = new Facturas();
        factura.setFecha(LocalDate.now());
        factura.setCliente(alojamiento.getCliente());
        factura.setAlojamiento(alojamiento);
        factura.setTotal(total);
        
        facturaDAO.insertar(factura);
        logger.info("Factura generada con total: {}", total);
    }
    
    public List<Facturas> listarFacturas() throws Exception {
        logger.info("Listando todas las facturas");
        return facturaDAO.listar();
    }
    
    public void eliminarFactura(int id) throws Exception {
        logger.info("Eliminando factura ID: {}", id);
        facturaDAO.eliminar(id);
    }
    
    public List<Facturas> buscarPorCliente(int clienteId) throws Exception {
        logger.info("Buscando facturas del cliente ID: {}", clienteId);
        return facturaDAO.buscarPorCliente(clienteId);
    }
    
    public Facturas obtenerFactura(int id) throws Exception {
        logger.info("Obteniendo factura ID: {}", id);
        return facturaDAO.obtenerPorId(id);
    }
}
