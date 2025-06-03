package com.lksnext.parkingbercibengoa.domain;

public class Vehiculo {
    private String matricula;

    private String marca;
    private TipoVehiculo tipoVehiculo;

    public Vehiculo(String matricula, String marca,TipoVehiculo tipoVehiculo){
        this.matricula = matricula;
        this.marca = marca;
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getMatricula(){return matricula;}

    public String getMarca(){return marca;}
    public TipoVehiculo getTipoVehiculo(){return tipoVehiculo;}
}
