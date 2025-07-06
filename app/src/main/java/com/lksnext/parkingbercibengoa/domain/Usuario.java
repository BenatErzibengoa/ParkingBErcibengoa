package com.lksnext.parkingbercibengoa.domain;

import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Usuario  {
    private String id;
    private String nombre;
    private String email;
    private List<Vehiculo> vehiculos;
    private List<Reserva> reservas;

    public Usuario(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Usuario(String nombre, String email){
        this(null, nombre, email);
    }

    public Usuario(){}

    public void reservar(Vehiculo vehiculo, Plaza plaza, LocalDateTime fechaInicio, Duration duracion) {
        HorarioPlaza horarioPlaza = new HorarioPlaza(plaza, fechaInicio.toLocalDate());  //TODO: Esto se debe obtener mediante la base de datos
        // Realizar la reserva, delegando en HorarioPlaza
        boolean reserva = horarioPlaza.reservar(fechaInicio, duracion);
        if(reserva) {
            Reserva reservaCreada = new Reserva("id", this, vehiculo, fechaInicio, duracion, plaza);
            // Añadir reserva a la lista
            reservas.add(reservaCreada);
        } else {
            Log.d("Usuario", "Error: No se pudo realizar la reserva");
        }
    }

    //Getters y Setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public List<Vehiculo> getVehiculos() { return vehiculos;}
    public void setVehiculos(List<Vehiculo> vehiculos) {this.vehiculos = vehiculos;}
    public List<Reserva> getReservas() {return reservas;}
    public void setReservas(List<Reserva> reservas) {this.reservas = reservas;}

}
