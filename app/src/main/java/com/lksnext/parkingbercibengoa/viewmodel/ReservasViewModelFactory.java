package com.lksnext.parkingbercibengoa.viewmodel;

public class ReservasViewModelFactory {
    private static ReservasViewModel sharedInstance;

    public static ReservasViewModel getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ReservasViewModel();
        }
        return sharedInstance;
    }

    public static void clearInstance() {
        sharedInstance = null;
    }
}