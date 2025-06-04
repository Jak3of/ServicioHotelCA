
package com.mycompany.avanceproyecto.model;





public class ConsumoServicio {
    private int id;
     private Alojamientos alojamiento;
    private Servicios servicio;
    private int cantidad;

    public ConsumoServicio() {
    }

    
    
    
    public ConsumoServicio(int id, Alojamientos alojamiento, Servicios servicio, int cantidad) {
        this.id = id;
        this.alojamiento = alojamiento;
        this.servicio = servicio;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Alojamientos getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamientos alojamiento) {
        this.alojamiento = alojamiento;
    }

    public Servicios getServicio() {
        return servicio;
    }

    public void setServicio(Servicios servicio) {
        this.servicio = servicio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
    
    

    
    
    
}