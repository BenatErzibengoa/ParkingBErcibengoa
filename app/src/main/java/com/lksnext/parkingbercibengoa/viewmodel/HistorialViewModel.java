package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HistorialViewModel extends ViewModel {

    private MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();

    public LiveData<List<Reserva>> getReservas() {
        return reservas;
    }

    public void cargarReservasDelUsuario() {
        Vehiculo vehiculo1 = new Vehiculo("9359MPK", "Opel Zafira", TipoVehiculo.COCHE);
        Vehiculo vehiculo2 = new Vehiculo("5340CCB", "Toyota Yaris", TipoVehiculo.ELECTRICO);
        Vehiculo vehiculo3 = new Vehiculo("3420ATB", "Ford Fiesta", TipoVehiculo.DISCAPACITADO);

        Reserva reserva1 = new Reserva("1", null, vehiculo1, LocalDateTime.now().minusDays(1), Duration.of(1, ChronoUnit.HOURS), new Plaza(13, TipoVehiculo.COCHE));
        Reserva reserva2 = new Reserva("2", null, vehiculo2, LocalDateTime.now().minusDays(1), Duration.of(1, ChronoUnit.HOURS), new Plaza(14, TipoVehiculo.ELECTRICO));
        Reserva reserva3 = new Reserva("3", null, vehiculo3, LocalDateTime.now().minusDays(1).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.ELECTRICO));


        Reserva reserva4 = new Reserva("4", null, vehiculo1, LocalDateTime.now().minusDays(1), Duration.of(1, ChronoUnit.HOURS), new Plaza(13, TipoVehiculo.COCHE));
        Reserva reserva5 = new Reserva("5", null, vehiculo2, LocalDateTime.now().minusDays(4), Duration.of(1, ChronoUnit.HOURS), new Plaza(14, TipoVehiculo.COCHE));
        Reserva reserva6 = new Reserva("6", null, vehiculo3, LocalDateTime.now().minusDays(5).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.MOTO));
        Reserva reserva7 = new Reserva("7", null, vehiculo3, LocalDateTime.now().minusDays(6).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.DISCAPACITADO));
        Reserva reserva8 = new Reserva("8", null, vehiculo3, LocalDateTime.now().minusDays(7).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.COCHE));

        reservas.setValue(List.of(reserva1, reserva2, reserva3, reserva4, reserva5, reserva6, reserva7, reserva8));



    }
}