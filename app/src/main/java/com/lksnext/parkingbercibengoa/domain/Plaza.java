package com.lksnext.parkingbercibengoa.domain;

public class Plaza {

    private long id;
    private TipoVehiculo tipoVehiculo;

    public Plaza() {

    }

    public Plaza(long id, TipoVehiculo tipo) {
        this.id = id;
        this.tipoVehiculo = tipo;
    }


    //Getters y Setters
    public TipoVehiculo getTipo() {
        return tipoVehiculo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipoVehiculo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
