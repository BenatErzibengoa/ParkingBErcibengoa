package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Usuario;

public class RegisterViewModel extends ViewModel {
    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de registro
    // Por ejemplo, un LiveData para el email, contraseña y usuario
    MutableLiveData<Boolean> registered = new MutableLiveData<>(null);
    MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<Boolean> isRegistered(){
        return registered;
    }
    public LiveData<String> getError() {
        return error;
    }

    public void registerUser(String fullName, String email, String password) {
        DataRepository.getInstance().register(fullName, email, password, new Callback() {
            @Override
            public void onSuccess() {
                registered.setValue(Boolean.TRUE);
            }

            @Override
            public void onFailure(String errorCode) {
                error.setValue(errorCode);
                registered.setValue(Boolean.FALSE);
            }
        });
    }
}