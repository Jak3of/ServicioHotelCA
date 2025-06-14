package com.mycompany.avanceproyecto.model;


public class Habitaciones {
    private Integer id;
    private String numero;
    private String tipo;
    private Double precio;
    private Boolean disponible;

    // Constructor por defecto
    public Habitaciones() {
        this.disponible = true; // Por defecto disponible
    }

    // Constructor con parámetros
    public Habitaciones(Integer id, String numero, String tipo, Boolean disponible, Double precio) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.disponible = disponible;
        this.precio = precio;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    
    public Boolean isDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}