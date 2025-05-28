package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservasViewModel extends ViewModel {

    private MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();

    public LiveData<List<Reserva>> getReservas() {
        return reservas;
    }

    public void cargarReservasDelUsuario() {
        // Aquí llamas a tu base de datos o repositorio
        // Simulamos datos de prueba:
        //List<Reserva> dummy = ...; // Tu lógica aquí
        //reservas.setValue(dummy);

        Vehiculo vehiculo = new Vehiculo("1234ABC", TipoVehiculo.COCHE);
        Reserva reserva1 = new Reserva("1", null, vehiculo, LocalDateTime.now(), Duration.of(1, ChronoUnit.HOURS), new Plaza(13, TipoVehiculo.COCHE));
        Reserva reserva2 = new Reserva("2", null, vehiculo, LocalDateTime.now().plusDays(1), Duration.of(1, ChronoUnit.HOURS), new Plaza(14, TipoVehiculo.ELECTRICO));
        Reserva reserva3 = new Reserva("3", null, vehiculo, LocalDateTime.now().plusDays(1).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.ELECTRICO));

        reservas.setValue(List.of(reserva1, reserva2, reserva3));



    }
}