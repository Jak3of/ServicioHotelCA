
package com.mycompany.avanceproyecto.model;

import com.mycompany.avanceproyecto.model.Servicio;
import java.time.LocalDateTime;


public class ConsumoServicio {
    private int id;
    private int numeroHabitacion;
    private Servicio servicio;
    private LocalDateTime fechaHora;

    public ConsumoServicio(int id, int numeroHabitacion, Servicio servicio, LocalDateTime fechaHora) {
        this.id = id;
        this.numeroHabitacion = numeroHabitacion;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(int numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    
    
}
