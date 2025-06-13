package com.mycompany.avanceproyecto.model;



import java.time.LocalDate;


public class Alojamientos {
    private Integer id;
    private Clientes cliente;
    private Habitaciones habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    public Alojamientos() {
    }

    public Alojamientos(Integer id, Clientes cliente, Habitaciones habitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) { this.cliente = cliente; }
    
    public Habitaciones getHabitacion() { return habitacion; }
    public void setHabitacion(Habitaciones habitacion) { this.habitacion = habitacion; }
    
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
}