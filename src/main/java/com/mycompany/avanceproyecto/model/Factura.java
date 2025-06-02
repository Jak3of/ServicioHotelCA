
package com.mycompany.avanceproyecto.model;

import com.mycompany.avanceproyecto.model.Servicio;
import java.time.LocalDate;
import java.util.List;

public class Factura {
    private int id;
    private LocalDate fecha;
    private Cliente cliente;
    private Alojamiento alojamiento;
    private List<Servicio> servicios;
    private double total;

    public Factura(int id, LocalDate fecha, Cliente cliente, Alojamiento alojamiento, List<Servicio> servicios, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.servicios = servicios;
        this.total = total;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
