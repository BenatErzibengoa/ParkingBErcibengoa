package com.lksnext.parkingbercibengoa.configuration;

import static android.view.View.VISIBLE;

import android.widget.TextView;

public class Utils {
    public static boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
    public static void showError(String message, TextView errorText){
        errorText.setText(message);
        errorText.setVisibility(VISIBLE);
    }
}
