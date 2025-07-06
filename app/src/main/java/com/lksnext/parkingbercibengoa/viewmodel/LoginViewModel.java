package com.lksnext.parkingbercibengoa.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final AuthRepository authRepository;

    public LoginViewModel() {
        this.authRepository = new com.lksnext.parkingbercibengoa.data.FirebaseAuthRepository();
    }

    // Constructor para testing
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loginUser(Context context, String email, String password) {
        authRepository.login(email, password, new LoginCallback<Usuario>() {
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

