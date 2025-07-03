package com.lksnext.parkingbercibengoa.domain;

public interface LoginCallback<T> {
    void onSuccess(T user);
    void onFailure(String errorCode);
}