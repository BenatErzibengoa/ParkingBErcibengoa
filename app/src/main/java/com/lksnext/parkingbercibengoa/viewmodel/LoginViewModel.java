package com.lksnext.parkingbercibengoa.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;

public class LoginViewModel extends ViewModel {

    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loginUser(Context context, String email, String password) {
        DataRepository.getInstance().login(email, password, new LoginCallback() {
            @Override
            public void onSuccess(Usuario usuario) {
                SessionManager sessionManager = new SessionManager(context);
                sessionManager.guardarUsuario(usuario);
                logged.setValue(Boolean.TRUE);
            }

            @Override
            public void onFailure(String errorCode) {
                logged.setValue(Boolean.FALSE);
                error.setValue(errorCode);
            }
        });
    }

}

