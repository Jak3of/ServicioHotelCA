package com.mycompany.avanceproyecto.model;

import java.time.LocalDate;

public class Alojamientos {
    
    // Enum para estados del alojamiento
    public enum EstadoAlojamiento {
        ACTIVO("ACTIVO"),           // Alojamiento creado, habitación ocupada
        PAGADO("PAGADO"),           // Alojamiento facturado, habitación liberada  
        FINALIZADO("FINALIZADO");   // Proceso completado
        
        private final String valor;
        
        EstadoAlojamiento(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static EstadoAlojamiento fromString(String valor) {
            for (EstadoAlojamiento estado : EstadoAlojamiento.values()) {
                if (estado.valor.equalsIgnoreCase(valor)) {
                    return estado;
                }
            }
            return ACTIVO; // Default
        }
    }
    
    private Integer id;
    private Clientes cliente;
    private Habitaciones habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoAlojamiento estado;

    public Alojamientos() {
        this.estado = EstadoAlojamiento.ACTIVO; // Estado por defecto
    }

    public Alojamientos(Integer id, Clientes cliente, Habitaciones habitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = EstadoAlojamiento.ACTIVO; // Estado por defecto
    }
    
    public Alojamientos(Integer id, Clientes cliente, Habitaciones habitacion, LocalDate fechaEntrada, LocalDate fechaSalida, EstadoAlojamiento estado) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
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
    
    public EstadoAlojamiento getEstado() { return estado; }
    public void setEstado(EstadoAlojamiento estado) { this.estado = estado; }
    
    // Métodos de utilidad para estados
    public boolean isActivo() {
        return estado == EstadoAlojamiento.ACTIVO;
    }
    
    public boolean isPagado() {
        return estado == EstadoAlojamiento.PAGADO;
    }
    
    public boolean isFinalizado() {
        return estado == EstadoAlojamiento.FINALIZADO;
    }
}