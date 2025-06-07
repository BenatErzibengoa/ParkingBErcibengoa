package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class NuevaReservaViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> vehiculos = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getVehiculos() {
        return vehiculos;
    }

    public void cargarVehiculos() {
        ArrayList<String> listaVehiculos = new ArrayList<>();
        listaVehiculos.addAll(Arrays.asList("Zafira", "Yaris Cross", "Corolla"));
        new Thread(() -> {
            vehiculos.postValue(listaVehiculos);
        }).start();
    }

    public void cargarVehiculosDesdeRepositorio() {
        //repository.getVehiculos().observeForever(vehiculosList -> {
        //     vehiculos.setValue(vehiculosList);
        // });
    }
}
