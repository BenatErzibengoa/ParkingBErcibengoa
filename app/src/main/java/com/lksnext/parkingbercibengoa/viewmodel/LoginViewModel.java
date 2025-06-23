package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loginUser(String email, String password) {
        DataRepository.getInstance().login(email, password, new Callback() {
            @Override
            public void onSuccess() {
                //usuarioLiveData.postValue(usuario);
                logged.setValue(Boolean.TRUE);
            }

            @Override
            public void onFailure(String errorCode) {
                logged.setValue(Boolean.FALSE);
                error.setValue(errorCode);
            }
        });
    }

    public Usuario getUsuario(){
        return usuarioLiveData.getValue();
    }
}

