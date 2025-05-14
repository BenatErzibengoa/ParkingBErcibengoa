package com.lksnext.parkingbercibengoa.domain;

import java.time.LocalDate;
import java.util.List;

public class Usuario {
    private String nombre;
    private String email;
    private String contraseña;
    private LocalDate fechaDeNacimiento;
    private List<Vehiculo> vehiculos;
    private List<Reserva> reservas;

    public Usuario(String nombre, String email, String contraseña, LocalDate fechaDeNacimiento) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }





    //Getters y Setters
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getContraseña() {return contraseña;}
    public void setContraseña(String contraseña) {this.contraseña = contraseña;}
    public LocalDate getFechaDeNacimiento() {return fechaDeNacimiento;}
    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {this.fechaDeNacimiento = fechaDeNacimiento;}
    public List<Vehiculo> getVehiculos() { return vehiculos;}
    public void setVehiculos(List<Vehiculo> vehiculos) {this.vehiculos = vehiculos;}
    public List<Reserva> getReservas() {return reservas;}
    public void setReservas(List<Reserva> reservas) {this.reservas = reservas;}

}
