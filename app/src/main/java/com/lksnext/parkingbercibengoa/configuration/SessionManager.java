package com.lksnext.parkingbercibengoa.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.lksnext.parkingbercibengoa.domain.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SessionManager {

    private static final String PREF_NAME = "session_prefs";

    private static final String KEY_NOMBRE = "usuario_nombre";
    private static final String KEY_EMAIL = "usuario_email";
    private static final String KEY_CONTRASENA = "usuario_contrasena";
    private static final String KEY_FECHA_NACIMIENTO = "usuario_fecha_nacimiento";

    private final SharedPreferences prefs;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Guardar usuario en sesión
    public void guardarUsuario(Usuario usuario) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NOMBRE, usuario.getNombre());
        editor.putString(KEY_EMAIL, usuario.getEmail());
        editor.apply();
    }

    // Obtener usuario actual
    public Usuario getUsuario() {
        String nombre = prefs.getString(KEY_NOMBRE, null);
        String email = prefs.getString(KEY_EMAIL, null);
        String contrasena = prefs.getString(KEY_CONTRASENA, null);
        String fechaNacimientoStr = prefs.getString(KEY_FECHA_NACIMIENTO, null);

        if (email == null || nombre == null || contrasena == null) {
            return null; // Sesión vacía o datos incompletos
        }

        LocalDate fechaNacimiento = null;
        if (fechaNacimientoStr != null) {
            try {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr, dateFormatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);

        return usuario;
    }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }

    public boolean haySesionActiva() {
        return prefs.contains(KEY_EMAIL);
    }
}
