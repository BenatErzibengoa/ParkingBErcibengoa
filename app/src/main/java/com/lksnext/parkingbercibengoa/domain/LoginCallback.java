package com.lksnext.parkingbercibengoa.domain;

public interface LoginCallback {
    void onSuccess(Usuario usuario);
    void onFailure(String errorCode);
}