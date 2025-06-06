package com.mycompany.avanceproyecto.model;


public class Habitaciones {
    private int id;
    private int numero;
    private String tipo;
    private boolean disponible;
    private double precio;

    public Habitaciones() {
    }

    
    public Habitaciones(int id, int numero, String tipo, boolean disponible, double precio) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.disponible = disponible;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    
    
    
}