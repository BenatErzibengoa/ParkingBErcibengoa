package com.lksnext.parkingbercibengoa.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.BitSet;

public class HorarioPlaza {
    private Plaza plaza;
    private LocalDate dia;

    private BitSet horario;

    public HorarioPlaza(Plaza plaza, LocalDate dia) {
        this.plaza = plaza;
        this.dia = dia;
        horario = new BitSet(1440);
    }

    public void printHorarioPlaza() {
        LocalTime time = LocalTime.of(0, 0);
        for (int i = 0; i < 1440; i++) {
            boolean isReserved = horario.get(i);
            System.out.println(time + ": " + isReserved);
            time = time.plusMinutes(1);
        }
    }
}
