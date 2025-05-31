package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class LoginViewModel extends ViewModel {

    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de inicio de sesión
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loginUser(String email, String password) {
        //Clase para comprobar si los datos de inicio de sesión son correctos o no
        DataRepository.getInstance().login(email, password, new Callback() {
            //En caso de que el login sea correcto, que se hace
            @Override
            public void onSuccess() {
                //TODO
                logged.setValue(Boolean.TRUE);
            }

            //En caso de que el login sea incorrecto, que se hace
            @Override
            public void onFailure(String errorCode) {
                //TODO
                logged.setValue(Boolean.FALSE);
                error.setValue(errorCode);
            }
        });
    }
}

