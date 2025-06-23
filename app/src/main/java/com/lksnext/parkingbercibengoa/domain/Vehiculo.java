package com.lksnext.parkingbercibengoa.domain;

public class Vehiculo {
    private String matricula;
    private String nombre;
    private TipoVehiculo tipoVehiculo;

    public Vehiculo(String matricula, String nombre, TipoVehiculo tipoVehiculo){
        this.matricula = matricula;
        this.nombre = nombre;
        this.tipoVehiculo = tipoVehiculo;
    }

    public Vehiculo(){}

    public String getMatricula(){return matricula;}
    public void setMatricula(String matricula){this.matricula=matricula;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){ this.nombre = nombre;}

    public TipoVehiculo getTipoVehiculo(){return tipoVehiculo;}
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo){this.tipoVehiculo=tipoVehiculo;}

    @Override
    public String toString() {
        return getNombre();
    }
}
