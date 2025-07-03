package com.lksnext.parkingbercibengoa.viewmodel;

import android.app.Application;

public class ReservasViewModelFactory {
    private static ReservasViewModel sharedInstance;

    public static ReservasViewModel getSharedInstance(Application application) {
        if (sharedInstance == null) {
            sharedInstance = new ReservasViewModel(application);
        }
        return sharedInstance;
    }

    public static void clearInstance() {
        sharedInstance = null;
    }
}
