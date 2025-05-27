package com.lksnext.parkingbercibengoa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class PasswordViewModel extends ViewModel {

    private MutableLiveData<Boolean> passwordResetResult = new MutableLiveData<>(null);

    public LiveData<Boolean> getPasswordResetResult() {
        return passwordResetResult;
    }
    public void changePassword(String email) {
        DataRepository.getInstance().changePassword(email, new Callback() {
            @Override
            public void onSuccess() {
                passwordResetResult.setValue(true);
            }

            @Override
            public void onFailure() {
                passwordResetResult.setValue(false);
            }
        });
    }
}
