package com.mycompany.avanceproyecto.daos;

import com.mycompany.avanceproyecto.model.Alojamientos;
import com.mycompany.avanceproyecto.model.Servicios;
import java.util.List;

public interface AlojamientoDAO {
    void insertar(Alojamientos alojamiento) throws Exception;
    Alojamientos obtenerPorId(int id) throws Exception;
    List<Alojamientos> listar() throws Exception;
    void actualizar(Alojamientos alojamiento) throws Exception;
    void actualizarEstado(int id, Alojamientos.EstadoAlojamiento estado) throws Exception;
    void eliminar(int id) throws Exception;
    void eliminarAlojamientoPagado(int id) throws Exception; // Método especial para eliminar después de pagar
    
    // Métodos para la tabla alojamiento_servicio
    void agregarServicio(int idAlojamiento, int idServicio) throws Exception;
    void eliminarServicio(int idAlojamiento, int idServicio) throws Exception;
    List<Servicios> listarServiciosPorAlojamiento(int idAlojamiento) throws Exception;
}