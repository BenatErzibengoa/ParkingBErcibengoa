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

    public Vehiculo(){}

    public String getMatricula(){return matricula;}
    public void setMatricula(String matricula){this.matricula=matricula;}

    public String getMarca(){return marca;}
    public void setMarca(String marca){this.marca=marca;}

    public TipoVehiculo getTipoVehiculo(){return tipoVehiculo;}
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo){this.tipoVehiculo=tipoVehiculo;}

    @Override
    public String toString() {
        return getMarca();
    }
}
