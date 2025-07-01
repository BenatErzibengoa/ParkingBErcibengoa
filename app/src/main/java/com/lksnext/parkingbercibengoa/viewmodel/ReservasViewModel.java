package com.lksnext.parkingbercibengoa.viewmodel;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.CallbackList;
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

public class ReservasViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> usuario = new MutableLiveData<>();

    private MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Vehiculo>> vehiculos = new MutableLiveData<>();

    private MutableLiveData<Vehiculo> vehiculoSeleccionado = new MutableLiveData<>();

    private MutableLiveData<LocalDateTime> horaInicio = new MutableLiveData<>();

    private MutableLiveData<LocalDateTime> horaFin = new MutableLiveData<>();

    private final MutableLiveData<String> errorCargarReservas = new MutableLiveData<>();
    private final MutableLiveData<String> errorCargarVehiculos = new MutableLiveData<>();
    private final MutableLiveData<String> errorAñadirReserva = new MutableLiveData<>();
    private final MutableLiveData<String> errorAñadirVehiculo = new MutableLiveData<>();





    public LiveData<Usuario> getUsuario() {
        return usuario;
    }


    public LiveData<List<Reserva>> getReservas() {
        return reservas;
    }

    public LiveData<ArrayList<Vehiculo>> getVehiculos() {
        return vehiculos;
    }

    public LiveData<Vehiculo> getVehiculoSeleccionado() {
        return vehiculoSeleccionado;
    }

    public LiveData<LocalDateTime> gethoraInicio() {return horaInicio;}
    public LiveData<LocalDateTime> gethoraFin() {return horaFin;}

    public LiveData<String> getErrorCargarReservas() {return errorCargarReservas;}
    public LiveData<String> getErrorCargarVehiculos() {return errorCargarVehiculos;}
    public LiveData<String> getErrorAñadirReserva() {return errorAñadirReserva;}

    public LiveData<String> getErrorAñadirVehiculo() {return errorAñadirVehiculo;}

    public void setVehiculoSeleccionado(Vehiculo vehiculo){vehiculoSeleccionado.setValue(vehiculo);}
    public void setHoraInicio(LocalDateTime hora){horaInicio.setValue(hora);}
    public void setHoraFin(LocalDateTime hora){horaFin.setValue(hora);}




    public ReservasViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        SessionManager sessionManager = new SessionManager(context);
        usuario.setValue(sessionManager.getUsuario());
    }

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


    /*public void cargarUsuario(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        Usuario usuario = sessionManager.getUsuario();
        usuarioActual.setValue(usuario);
    }*/
}