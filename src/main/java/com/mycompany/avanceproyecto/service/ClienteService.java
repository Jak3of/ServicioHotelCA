package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Clientes;
import com.mycompany.avanceproyecto.daos.ClienteDAO;
import com.mycompany.avanceproyecto.daos.impl.ClienteDAOImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteService {
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteDAO clienteDAO;
    
    public ClienteService() {
        this.clienteDAO = new ClienteDAOImpl();
    }
    
    public void guardarOActualizarCliente(Clientes cliente) throws Exception {
        if (cliente.getId() > 0) {
            logger.info("Actualizando cliente ID: {}", cliente.getId());
            clienteDAO.actualizar(cliente);
        } else {
            logger.info("Guardando nuevo cliente");
            clienteDAO.insertar(cliente);
        }
    }
    
    public List<Clientes> listarClientes() throws Exception {
        logger.info("Listando todos los clientes");
        return clienteDAO.listar();
    }
    
    public void eliminarCliente(int id) throws Exception {
        logger.info("Eliminando cliente ID: {}", id);
        clienteDAO.eliminar(id);
    }
    
    public Clientes buscarPorDni(int dni) throws Exception {
        logger.info("Buscando cliente por DNI: {}", dni);
        return clienteDAO.obtenerPorDni(dni);
    }
    
    public Clientes obtenerCliente(int id) throws Exception {
        logger.info("Obteniendo cliente ID: {}", id);
        return clienteDAO.obtenerPorId(id);
    }
}
