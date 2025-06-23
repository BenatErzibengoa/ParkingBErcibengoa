package com.lksnext.parkingbercibengoa.domain;
public enum TipoVehiculo {
    COCHE,
    ELECTRICO,
    DISCAPACITADO,
    MOTO;

    public String toString() {
        String s = name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
