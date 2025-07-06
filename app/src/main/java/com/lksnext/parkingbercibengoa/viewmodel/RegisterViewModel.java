package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class RegisterViewModel extends ViewModel {
    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de registro
    // Por ejemplo, un LiveData para el email, contraseña y usuario
    private final MutableLiveData<Boolean> registered = new MutableLiveData<>(null);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final AuthRepository authRepository;

    public RegisterViewModel() {
        this.authRepository = new com.lksnext.parkingbercibengoa.data.FirebaseAuthRepository();
    }

    // Constructor para testing
    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> isRegistered(){
        return registered;
    }
    public LiveData<String> getError() {
        return error;
    }

    public void registerUser(String fullName, String email, String password) {
        authRepository.register(fullName, email, password, new Callback() {
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