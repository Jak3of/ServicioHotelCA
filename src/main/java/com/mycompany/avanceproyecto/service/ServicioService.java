package com.mycompany.avanceproyecto.service;

import com.mycompany.avanceproyecto.model.Servicios;
import com.mycompany.avanceproyecto.daos.ServicioDAO;
import com.mycompany.avanceproyecto.daos.impl.ServicioDAOImpl;
import java.util.List;

public class ServicioService {
    private final ServicioDAO servicioDAO;
    
    public ServicioService() {
        this.servicioDAO = new ServicioDAOImpl();
    }
    
    public void guardarOActualizarServicio(Servicios servicio) throws Exception {
        if (servicio.getId() > 0) {
            servicioDAO.actualizar(servicio);
        } else {
            servicioDAO.insertar(servicio);
        }
    }
    
    public List<Servicios> listarServicios() throws Exception {
        return servicioDAO.listar();
    }
    
    public void eliminarServicio(int id) throws Exception {
        servicioDAO.eliminar(id);
    }
}
