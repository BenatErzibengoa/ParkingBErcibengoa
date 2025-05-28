package com.lksnext.parkingbercibengoa.domain;

public class Plaza {

    private int id;
    private TipoVehiculo tipoVehiculo;

    public Plaza() {

    }

    public Plaza(int id, TipoVehiculo tipo) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

}
