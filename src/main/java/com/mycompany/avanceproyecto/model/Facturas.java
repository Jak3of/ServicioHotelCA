package com.mycompany.avanceproyecto.model;

import java.time.LocalDate;
import java.util.List;



public class Facturas {
    private int id;
    private LocalDate fecha;
    private Clientes cliente;
    private Alojamientos alojamiento;
    private double total;
    private List<Servicios> servicios;
    
    public Facturas() {
    }

    public Facturas(int id, LocalDate fecha, Clientes cliente, Alojamientos alojamiento, double total, List<Servicios> servicios) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.total = total;
        this.servicios = servicios;
    }

    // Agregar constructor sin servicios
    public Facturas(int id, LocalDate fecha, Clientes cliente, Alojamientos alojamiento, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.total = total;
        this.servicios = null; // Inicializar como null o lista vacía
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

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Alojamientos getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamientos alojamiento) {
        this.alojamiento = alojamiento;
    }

   

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }

  
    
    
}