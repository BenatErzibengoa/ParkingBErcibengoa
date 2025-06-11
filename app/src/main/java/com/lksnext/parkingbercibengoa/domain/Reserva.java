package com.lksnext.parkingbercibengoa.domain;

import java.time.Duration;
import java.time.LocalDateTime;


public class Reserva {

    private String id;
    private Usuario usuario;
    private Vehiculo vehiculo;
    private LocalDateTime fechaInicio;
    private Duration duracion;
    private Plaza plaza;

    public Reserva(String id, Usuario usuario, Vehiculo vehiculo, LocalDateTime fechaInicio, Duration duracion, Plaza plaza) {
        this.id = id;
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
        this.plaza = plaza;
    }

    public Reserva(){}



    //Getters y Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Vehiculo getVehiculo(){return vehiculo;}
    public void setVehiculo(Vehiculo vehiculo){this.vehiculo=vehiculo;}
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public Duration getDuracion(){return duracion;}
    public void setDuracion(Duration duracion){this.duracion=duracion;}
    public Plaza getPlaza(){return plaza;}
    public void setPlaza(Plaza plaza){this.plaza=plaza;}

}
