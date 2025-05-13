package com.lksnext.parkingbercibengoa.domain;

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

    public Usuario(String nombre, String email, String contrasena, LocalDate fechaDeNacimiento) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public void Reservar(Vehiculo vehiculo, Plaza plaza, LocalDateTime fechaInicio, Duration duracion){
        HorarioPlaza horarioPlaza = null; //TODO: Obtener horario desde la base de datos
        LocalDateTime fechaFin = fechaInicio.plus(duracion);
        boolean reserva1, reserva2 = true;
        //Si la reserva no pasa al siguinete día reservar normal, si no, dividimos la reserva entre los dos días
        if(fechaInicio.toLocalDate().equals(fechaFin.toLocalDate())){
            reserva1 = horarioPlaza.reservar(fechaInicio, duracion);
        }else{
            LocalDateTime fechaFinDia = fechaInicio.toLocalDate().plusDays(1).atTime(LocalTime.of(0,0));
            reserva1 = horarioPlaza.reservar(fechaInicio, Duration.between(fechaInicio, fechaFinDia));
            HorarioPlaza horarioPlazaDiaSiguiente = null; //TODO: Obtener horario desde la base de datos
            reserva2 = horarioPlazaDiaSiguiente.reservar(fechaFinDia, Duration.between(fechaFinDia, fechaFin));
        }
        //TODO: implementar id
        if(reserva1 && reserva2) {
            Reserva reserva = new Reserva("id", this, vehiculo, fechaInicio, duracion, plaza);
        }else {
            System.out.println("error");
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
