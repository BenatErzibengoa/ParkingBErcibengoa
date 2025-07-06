package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class PasswordViewModel extends ViewModel {

    private final MutableLiveData<Boolean> passwordResetResult = new MutableLiveData<>(null);
    private final AuthRepository authRepository;

    public PasswordViewModel() {
        this.authRepository = new com.lksnext.parkingbercibengoa.data.FirebaseAuthRepository();
    }

    // Constructor para testing
    public PasswordViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> getPasswordResetResult() {
        return passwordResetResult;
    }

    public void changePassword(String email) {
        authRepository.changePassword(email, new Callback() {
            @Override
            public void onSuccess() {
                passwordResetResult.setValue(true);
            }

            @Override
            public void onFailure(String errorCode) {
                passwordResetResult.setValue(false);
            }
        });
    }
}
