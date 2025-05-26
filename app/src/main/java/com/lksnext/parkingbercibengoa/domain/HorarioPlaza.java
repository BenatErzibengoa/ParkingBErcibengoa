package com.lksnext.parkingbercibengoa.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public boolean reservar(LocalDateTime fechaInicio, Duration duracion) {
        LocalDateTime fechaFin = fechaInicio.plus(duracion);
        if (fechaInicio.toLocalDate().equals(fechaFin.toLocalDate())) {
            return realizarReserva(fechaInicio, duracion);
        } else {
            HorarioPlaza horarioPlazaSiguienteDia = null; //TODO: Esto se debe obtener mediante la base de datos
            // Dividir la reserva en dos días
            Duration duracionDia1 = Duration.between(fechaInicio, fechaInicio.toLocalDate().plusDays(1).atStartOfDay());
            Duration duracionDia2 = Duration.between(fechaInicio.toLocalDate().plusDays(1).atStartOfDay(), fechaFin);

            boolean reservaDia1 = realizarReserva(fechaInicio, duracionDia1);
            boolean reservaDia2 = horarioPlazaSiguienteDia.realizarReserva(fechaInicio.toLocalDate().plusDays(1).atStartOfDay(), duracionDia2);

            return reservaDia1 && reservaDia2;
        }
    }
    public boolean realizarReserva(LocalDateTime fechaInicio, Duration duracion){
        if(estaLibreDurante(fechaInicio, duracion)){
            int indiceInicio = fechaInicio.getHour() * 60 + fechaInicio.getMinute();
            int indiceFinal = indiceInicio + (int)duracion.toMinutes();
            for(int i = indiceInicio; i <= indiceFinal; i++){
                if(horario.get(i)){
                    return false;
                }
                horario.set(i);
            }
            return true;
        }
        return false;
    }

    public boolean estaLibreDurante(LocalDateTime fechaInicio, Duration duracion){
        int indiceInicio = fechaInicio.getHour() * 60 + fechaInicio.getMinute();
        int indiceFinal = indiceInicio + (int)duracion.toMinutes();
        for(int i = indiceInicio; i <= indiceFinal; i++){
            if(horario.get(i)){
                return false;
            }
        }
        return true;
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
