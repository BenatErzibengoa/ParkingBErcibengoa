package com.lksnext.parkingbercibengoa.domain;

import java.util.List;

public interface CallbackList<T> {
    void onSuccess(List<T> lista);
    void onFailure(String error);
}