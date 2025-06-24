package com.lksnext.parkingbercibengoa.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservasViewModel extends ViewModel {

    private MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Vehiculo>> vehiculos = new MutableLiveData<>();

    private MutableLiveData<Usuario> usuarioActual = new MutableLiveData<>();

    public LiveData<List<Reserva>> getReservas() {
        return reservas;
    }

    public LiveData<ArrayList<Vehiculo>> getVehiculos() {
        return vehiculos;
    }

    public LiveData<Usuario> getUsuarioLiveData() {
        return usuarioActual;
    }

    public void cargarReservasDelUsuario() {
        Vehiculo vehiculo1 = new Vehiculo("9359MPK", "Opel Zafira", TipoVehiculo.COCHE);
        Vehiculo vehiculo2 = new Vehiculo("5340CCB", "Toyota Yaris", TipoVehiculo.ELECTRICO);
        Vehiculo vehiculo3 = new Vehiculo("3420ATB", "Ford Fiesta", TipoVehiculo.DISCAPACITADO);

        Reserva reserva1 = new Reserva("1", null, vehiculo1, LocalDateTime.now(), Duration.of(1, ChronoUnit.HOURS), new Plaza(13, TipoVehiculo.COCHE));
        Reserva reserva2 = new Reserva("2", null, vehiculo2, LocalDateTime.now().plusDays(1), Duration.of(1, ChronoUnit.HOURS), new Plaza(14, TipoVehiculo.ELECTRICO));
        Reserva reserva3 = new Reserva("3", null, vehiculo3, LocalDateTime.now().plusDays(1).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.DISCAPACITADO));


        Reserva reserva4 = new Reserva("4", null, vehiculo1, LocalDateTime.now(), Duration.of(1, ChronoUnit.HOURS), new Plaza(13, TipoVehiculo.COCHE));
        Reserva reserva5 = new Reserva("5", null, vehiculo2, LocalDateTime.now().plusDays(4), Duration.of(1, ChronoUnit.HOURS), new Plaza(14, TipoVehiculo.COCHE));
        Reserva reserva6 = new Reserva("6", null, vehiculo3, LocalDateTime.now().plusDays(5).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.DISCAPACITADO));
        Reserva reserva7 = new Reserva("7", null, vehiculo3, LocalDateTime.now().plusDays(6).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.DISCAPACITADO));
        Reserva reserva8 = new Reserva("8", null, vehiculo3, LocalDateTime.now().plusDays(7).plusHours(2), Duration.of(1, ChronoUnit.HOURS), new Plaza(1, TipoVehiculo.DISCAPACITADO));

        ArrayList<Reserva> listaReservas = new ArrayList<>();
        listaReservas.addAll(Arrays.asList(reserva1, reserva2, reserva3, reserva4, reserva5, reserva6, reserva7, reserva8));
        reservas.setValue(listaReservas);
    }

    public void cargarVehiculos() {
        ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
        Vehiculo vehiculo1 = new Vehiculo("53450CCB", "Opel Zafira", TipoVehiculo.COCHE);
        Vehiculo vehiculo2 = new Vehiculo("12539MCD", "Toyota Yaris", TipoVehiculo.ELECTRICO);

        listaVehiculos.addAll(Arrays.asList(vehiculo1, vehiculo2));
        new Thread(() -> {
            vehiculos.postValue(listaVehiculos);
        }).start();
    }

    public void añadirVehiculo(Vehiculo nuevoVehiculo) {
        ArrayList<Vehiculo> listaActual = vehiculos.getValue();
        ArrayList<Vehiculo> nuevaLista = new ArrayList<>();

        if (listaActual != null) {
            nuevaLista.addAll(listaActual);
        }

        nuevaLista.add(nuevoVehiculo);
        vehiculos.setValue(nuevaLista);
    }

    public void cargarVehiculosDesdeRepositorio() {
        //repository.getVehiculos().observeForever(vehiculosList -> {
        //     vehiculos.setValue(vehiculosList);
        // });
    }

    public void reservarPlaza(Reserva reserva) {
        //DataRepository.getInstance().guardarReserva(reserva);
        List<Reserva> listaActual = reservas.getValue();
        ArrayList<Reserva> nuevaLista = new ArrayList<>();
        if (listaActual != null) {
            nuevaLista.addAll(listaActual);
        }
        nuevaLista.add(reserva);
        reservas.setValue(nuevaLista);
    }


    /*public void cargarUsuario(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        Usuario usuario = sessionManager.getUsuario();
        usuarioActual.setValue(usuario);
    }*/
}