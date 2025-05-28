package com.lksnext.parkingbercibengoa.configuration;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

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

    public static String formatearFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        LocalDate mañana = hoy.plusDays(1);
        String prefijo;

        if (fecha.equals(hoy)) {
            prefijo = "Hoy, ";
        } else if (fecha.equals(mañana)) {
            prefijo = "Mañana, ";
        } else {
            prefijo = "";
        }

        int dia = fecha.getDayOfMonth();
        String nombreMes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        return prefijo + String.format("%d de %s", dia, nombreMes);
    }

    public static String formatearTipo(Enum<?> valor) {
        String nombre = valor.name().toLowerCase();
        //Caso especial: tilde
        if (nombre.equals("electrico")) {
            return "Eléctrico";
        }
        return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
    }
}
