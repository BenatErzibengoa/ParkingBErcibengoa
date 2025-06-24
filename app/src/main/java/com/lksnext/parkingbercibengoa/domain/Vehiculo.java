package com.lksnext.parkingbercibengoa.domain;

public class Vehiculo {
    private String matricula;
    private String modelo;
    private TipoVehiculo tipoVehiculo;

    public Vehiculo(String matricula, String modelo, TipoVehiculo tipoVehiculo){
        this.matricula = matricula;
        this.modelo = modelo;
        this.tipoVehiculo = tipoVehiculo;
    }

    public Vehiculo(){}

    public String getMatricula(){return matricula;}
    public void setMatricula(String matricula){this.matricula=matricula;}

    public String getModelo(){return modelo;}
    public void setModelo(String modelo){ this.modelo = modelo;}

    public TipoVehiculo getTipoVehiculo(){return tipoVehiculo;}
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo){this.tipoVehiculo=tipoVehiculo;}

    @Override
    public String toString() {
        return getModelo();
    }
}
