package com.lksnext.parkingbercibengoa.domain;

import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Usuario {
    private String nombre;
    private String email;
    private String contrasena;
    private LocalDate fechaDeNacimiento;
    private List<Vehiculo> vehiculos;
    private List<Reserva> reservas;

    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
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
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getContraseña() {return contrasena;}
    public void setContraseña(String contraseña) {this.contrasena = contrasena;}
    public LocalDate getFechaDeNacimiento() {return fechaDeNacimiento;}
    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {this.fechaDeNacimiento = fechaDeNacimiento;}
    public List<Vehiculo> getVehiculos() { return vehiculos;}
    public void setVehiculos(List<Vehiculo> vehiculos) {this.vehiculos = vehiculos;}
    public List<Reserva> getReservas() {return reservas;}
    public void setReservas(List<Reserva> reservas) {this.reservas = reservas;}

}
