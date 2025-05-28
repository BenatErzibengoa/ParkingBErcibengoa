package com.lksnext.parkingbercibengoa.configuration;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;

public class Utils {
    public static boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
    public static void showError(String message, TextView errorText){
        errorText.setText(message);
        errorText.setVisibility(VISIBLE);
    }

    public static int getColorByTipo(TipoVehiculo tipo, Context context) {
        switch (tipo) {
            case COCHE:
                return ContextCompat.getColor(context, R.color.blueMain);
            case MOTO:
                return ContextCompat.getColor(context, R.color.blueMain);
            case DISCAPACITADO:
                return ContextCompat.getColor(context, R.color.blueMain);
            case ELECTRICO:
                return ContextCompat.getColor(context, R.color.greenMain);
            default:
                return ContextCompat.getColor(context, android.R.color.darker_gray);
        }
    }
}
