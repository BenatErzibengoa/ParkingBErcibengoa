package com.lksnext.parkingbercibengoa.domain;

import java.time.LocalDate;

public class Reserva {

    private String id;
    private Usuario usuario;
    private Vehiculo vehiculo;
    private LocalDate fecha;
    private long horaInicio;
    private long horaFin;
    private Plaza plaza;

    public Reserva(String id, Usuario usuario, Vehiculo vehiculo, LocalDate fecha, long horaInicio, long horaFin, Plaza plaza) {
        this.id = id;
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.plaza = plaza;
    }

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
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public long getHoraInicio() {return horaInicio;}
    public void setHoraInicio(long horaInicio) {
        this.horaInicio = horaInicio;
    }
    public long getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(long horaFin) {
        this.horaFin = horaFin;
    }

}
