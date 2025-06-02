
package com.mycompany.avanceproyecto.model;

import com.mycompany.avanceproyecto.model.Cliente;
import com.mycompany.avanceproyecto.model.Habitacion;
import com.mycompany.avanceproyecto.model.Servicio;
import java.util.List;
import java.time.LocalDate;


public class Alojamiento {
    private int id;
    private Cliente cliente;
    private Habitacion habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private List<Servicio> serviciosConsumidos;

    public Alojamiento(int id, Cliente cliente, Habitacion habitacion, LocalDate fechaEntrada, LocalDate fechaSalida, List<Servicio> serviciosConsumidos) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.serviciosConsumidos = serviciosConsumidos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public List<Servicio> getServiciosConsumidos() {
        return serviciosConsumidos;
    }

    public void setServiciosConsumidos(List<Servicio> serviciosConsumidos) {
        this.serviciosConsumidos = serviciosConsumidos;
    }
    
    
}
