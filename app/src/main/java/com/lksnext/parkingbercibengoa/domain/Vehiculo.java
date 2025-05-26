package com.lksnext.parkingbercibengoa.domain;

public class Vehiculo {
    private String matricula;
    private TipoVehiculo tipoVehiculo;

    public Vehiculo(String matricula, TipoVehiculo tipoVehiculo){
        this.matricula = matricula;
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getMatricula(){return matricula;}
    public TipoVehiculo getTipoVehiculo(){return tipoVehiculo;}
}
