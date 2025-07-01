package com.lksnext.parkingbercibengoa.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.CallbackList;
import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservasViewModel extends AndroidViewModel {

    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Vehiculo>> vehiculos = new MutableLiveData<>();
    private final MutableLiveData<Vehiculo> vehiculoSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<LocalDateTime> horaInicio = new MutableLiveData<>();
    private final MutableLiveData<LocalDateTime> horaFin = new MutableLiveData<>();
    private final MutableLiveData<List<Plaza>> plazas = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Plaza, HorarioPlaza>> horariosPorPlazaLiveData = new MutableLiveData<>();

    // Errores
    private final MutableLiveData<String> errorCargarReservas = new MutableLiveData<>();
    private final MutableLiveData<String> errorCargarVehiculos = new MutableLiveData<>();
    private final MutableLiveData<String> errorAñadirReserva = new MutableLiveData<>();
    private final MutableLiveData<String> errorAñadirVehiculo = new MutableLiveData<>();
    private final MutableLiveData<String> errorCargarPlazas = new MutableLiveData<>();

    public ReservasViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        SessionManager sessionManager = new SessionManager(context);
        usuario.setValue(sessionManager.getUsuario());
    }

    // Getters
    public LiveData<Usuario> getUsuario() { return usuario; }
    public LiveData<List<Reserva>> getReservas() { return reservas; }
    public LiveData<ArrayList<Vehiculo>> getVehiculos() { return vehiculos; }
    public LiveData<Vehiculo> getVehiculoSeleccionado() { return vehiculoSeleccionado; }
    public LiveData<LocalDateTime> gethoraInicio() { return horaInicio; }
    public LiveData<LocalDateTime> gethoraFin() { return horaFin; }
    public LiveData<List<Plaza>> getPlazas() { return plazas; }
    public LiveData<String> getErrorCargarReservas() { return errorCargarReservas; }
    public LiveData<String> getErrorCargarVehiculos() { return errorCargarVehiculos; }
    public LiveData<String> getErrorAñadirReserva() { return errorAñadirReserva; }
    public LiveData<String> getErrorAñadirVehiculo() { return errorAñadirVehiculo; }
    public LiveData<String> getErrorCargarPlazas() { return errorCargarPlazas; }

    public LiveData<HashMap<Plaza, HorarioPlaza>> getHorariosPorPlaza() {return horariosPorPlazaLiveData;}


    // Setters
    public void setVehiculoSeleccionado(Vehiculo vehiculo) { vehiculoSeleccionado.setValue(vehiculo); }
    public void setHoraInicio(LocalDateTime hora) { horaInicio.setValue(hora); }
    public void setHoraFin(LocalDateTime hora) { horaFin.setValue(hora); }

    public void cargarReservasDelUsuario() {
        if (reservas.getValue() != null && !reservas.getValue().isEmpty()) {
            return; // si los vehiculos han sido cargados, no hacer nada
        }
        DataRepository.getInstance().obtenerReservasUsuario(usuario.getValue().getId(), new CallbackList<Reserva>() {
            @Override
            public void onSuccess(List<Reserva> lista) {
                reservas.setValue(lista);
            }
            @Override
            public void onFailure(String error) {
                errorCargarReservas.setValue(error);
            }
        });
    }

    public void cargarVehiculos() {
        if (vehiculos.getValue() != null && !vehiculos.getValue().isEmpty()) {
            return; // Si ya están cargados, no hacer nada
        }
        DataRepository.getInstance().obtenerVehiculosUsuario(usuario.getValue().getId(), new CallbackList<Vehiculo>() {
            @Override
            public void onSuccess(List<Vehiculo> lista) {
                Log.d("ReservasViewModel", "Vehiculos cargados correctamente: " + lista.toString());
                vehiculos.postValue(new ArrayList<>(lista));
            }
            @Override
            public void onFailure(String error) {
                errorCargarVehiculos.postValue("Error al cargar vehículos: " + error);
            }
        });
    }




    public void añadirVehiculo(Vehiculo nuevoVehiculo) {
        DataRepository.getInstance().añadirVehiculoAUsuario(usuario.getValue(), nuevoVehiculo, new Callback() {
                    @Override
                    public void onSuccess() {
                        ArrayList<Vehiculo> listaActual = vehiculos.getValue();
                        ArrayList<Vehiculo> nuevaLista = new ArrayList<>();

                        if (listaActual != null) {
                            nuevaLista.addAll(listaActual);
                        }

                        nuevaLista.add(nuevoVehiculo);
                        vehiculos.setValue(nuevaLista);
                        Log.d("NuevoVehiculoFragment", "Vehículo guardado correctamente");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("NuevoVehiculoFragment", "Error al guardar el vehículo: " + message);
                        errorAñadirVehiculo.setValue(message);
                    }
        });
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

    public void obtenerPlazas(LocalDate dia) {
        DataRepository.getInstance().obtenerPlazas(new CallbackList<Plaza>() {
            @Override
            public void onSuccess(List<Plaza> lista) {
                plazas.setValue(lista);
                cargarHorariosPorPlaza(dia);
            }
            @Override
            public void onFailure(String error) {
                errorCargarPlazas.setValue("Error al obtener plazas: " + error);
            }
        });
    }


    public void cargarHorariosPorPlaza(LocalDate dia) {
        List<Plaza> listaPlazas = plazas.getValue();
        if (listaPlazas == null || listaPlazas.isEmpty()) {
            Log.e("ReservasViewModel", "No hay plazas disponibles para cargar los horarios.");
            return;
        }

        HashMap<Plaza, HorarioPlaza> mapaHorarios = new HashMap<>();
        AtomicInteger plazasProcesadas = new AtomicInteger(0);
        int totalPlazas = listaPlazas.size();

        for (Plaza plaza : listaPlazas) {
            DataRepository.getInstance().getOrCreateHorarioPlaza(plaza, dia, new LoginCallback<HorarioPlaza>() {
                @Override
                public void onSuccess(HorarioPlaza horarioPlaza) {
                    mapaHorarios.put(plaza, horarioPlaza);

                    if (plazasProcesadas.incrementAndGet() == totalPlazas) {
                        horariosPorPlazaLiveData.postValue(mapaHorarios);
                        Log.d("ReservasViewModel", "Todos los horarios cargados correctamente.");
                    }
                }

                @Override
                public void onFailure(String error) {
                    Log.e("ReservasViewModel", "Error al obtener horario de plaza " + plaza.getId() + ": " + error);

                    if (plazasProcesadas.incrementAndGet() == totalPlazas) {
                        horariosPorPlazaLiveData.postValue(mapaHorarios);  // Publica lo que sí se pudo cargar
                        Log.d("ReservasViewModel", "Carga de horarios finalizada con errores.");
                    }
                }
            });
        }
    }



    public void printHorariosPorPlaza() {
        HashMap<Plaza, HorarioPlaza> mapa = horariosPorPlazaLiveData.getValue();
        if (mapa == null || mapa.isEmpty()) {
            Log.d("ReservasViewModel", "El mapa de horarios está vacío o es nulo.");
            return;
        }
        for (Plaza plaza : mapa.keySet()) {
            HorarioPlaza horario = mapa.get(plaza);
            Log.d("ReservasViewModel", "Plaza: " + plaza.toString() + " -> Horario: " + horario.getHorario().toString());
        }
    }


}