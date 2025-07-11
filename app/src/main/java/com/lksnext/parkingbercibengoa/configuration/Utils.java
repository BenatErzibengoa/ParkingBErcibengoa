package com.lksnext.parkingbercibengoa.configuration;

import static android.view.View.VISIBLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

public class Utils {
    private Utils(){}
    public static boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
    public static void showError(String message, TextView errorText){
        errorText.setText(message);
        errorText.setVisibility(VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            errorText.setVisibility(View.GONE);
        }, 5000);
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
        LocalDate ayer = hoy.minusDays(1);

        String prefijo;

        if (fecha.equals(hoy)) {
            prefijo = "Hoy, ";
        } else if (fecha.equals(mañana)) {
            prefijo = "Mañana, ";
        } else if(fecha.equals(ayer)) {
            prefijo = "Ayer, ";
        }else{
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

    public static LocalDateTime parseFechaHora(String fecha, String hora) throws DateTimeParseException {
        String fechaHora = fecha + " " + hora;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(fechaHora, formatter);
    }

    public static String parseSeleccionPlazaFecha(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());
        return dateTime.format(formatter);
    }

    public static String parseSeleccionPlazaHora(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }

    public static String formatFecha(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }

    public static String formatHora(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return fecha.format(formatter);
    }




    public static String generarUUID() {
        return UUID.randomUUID().toString();
    }

    public static void programarNotificacionesPrueba(Context context, Reserva reserva) {
        long triggerEn5Segundos = System.currentTimeMillis() + 5 * 1000;

        Log.d("Notificacion", "Programando notificación de prueba para dentro de 5 segundos");

        programarNotificacion(context, reserva, triggerEn5Segundos, 0);
    }



    public static void programarNotificaciones(Context context, Reserva reserva) {
        long tiempoReservaMillis = reserva.getFechaInicio()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        programarNotificacion(context, reserva, tiempoReservaMillis - 15 * 60 * 1000, 15);
        programarNotificacion(context, reserva, tiempoReservaMillis - 30 * 60 * 1000, 30);
    }

    private static void programarNotificacion(Context context, Reserva reserva, long triggerAtMillis, int minutosAntes) {
        if (triggerAtMillis < System.currentTimeMillis()) return;

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", "Tu reserva comienza pronto");
        intent.putExtra("message", "Tu reserva en la plaza " + reserva.getPlaza().getId() + " comienza en " + minutosAntes + " minutos.");

        int requestCode = (int) (triggerAtMillis % Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Verificación para Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Opcional: muestra un mensaje al usuario
                Toast.makeText(context, "Activa 'alarmas exactas' en Ajustes para recibir notificaciones puntuales.", Toast.LENGTH_LONG).show();

                // Abrir la pantalla de ajustes para conceder permiso
                Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingsIntent);
                return;
            }
        }

        //poner la alarma
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }


    public static String getMensajeError(String errorCode) {
        Log.d("Utils", "Error code: " + errorCode);
        if (errorCode == null) {
            return "Error desconocido";
        }
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                return "Correo electrónico inválido";
            case "ERROR_WRONG_PASSWORD":
                return "Contraseña incorrecta";
            case "ERROR_USER_NOT_FOUND":
                return "Usuario no encontrado";
            case "ERROR_USER_DISABLED":
                return "El usuario ha sido deshabilitado";
            case "ERROR_EMAIL_ALREADY_IN_USE":
                return "Ya existe un usuario con ese correo";
            case "ERROR_OPERATION_NOT_ALLOWED":
                return "Operación no permitida";
            case "ERROR_TOO_MANY_REQUESTS":
                return "Demasiados intentos, por favor intente más tarde";
            case "ERROR_WEAK_PASSWORD":
                return "La contraseña es demasiado débil";
            case "no_connection":   // AÑADIDO este caso
                return "No hay conexión a Internet";
            default:
                return "Error desconocido";
        }
    }


}
