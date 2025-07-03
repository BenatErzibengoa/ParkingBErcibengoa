package com.lksnext.parkingbercibengoa.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private static final String PREF_NAME = "session_prefs";

    private static final String KEY_ID = "usuario_id";
    private static final String KEY_NOMBRE = "usuario_nombre";
    private static final String KEY_EMAIL = "usuario_email";

    private static final String KEY_VEHICULOS = "usuario_vehiculos";
    private static final String KEY_RESERVAS = "usuario_reservas";

    private final SharedPreferences prefs;
    private final Gson gson;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Guardar usuario en sesión
    public void guardarUsuario(Usuario usuario) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ID, usuario.getId());
        editor.putString(KEY_NOMBRE, usuario.getNombre());
        editor.putString(KEY_EMAIL, usuario.getEmail());

        // Convertir listas a JSON para guardar
        String vehiculosJson = gson.toJson(usuario.getVehiculos());
        editor.putString(KEY_VEHICULOS, vehiculosJson);

        String reservasJson = gson.toJson(usuario.getReservas());
        editor.putString(KEY_RESERVAS, reservasJson);

        editor.apply();
    }

    // Obtener usuario actual
    public Usuario getUsuario() {
        String id = prefs.getString(KEY_ID, null);
        String nombre = prefs.getString(KEY_NOMBRE, null);
        String email = prefs.getString(KEY_EMAIL, null);

        if (email == null || nombre == null) {
            return null; // Sesión vacía o datos incompletos
        }

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setEmail(email);

        // Leer listas desde JSON
        String vehiculosJson = prefs.getString(KEY_VEHICULOS, null);
        if (vehiculosJson != null) {
            Type vehiculosType = new TypeToken<List<Vehiculo>>() {}.getType();
            List<Vehiculo> vehiculos = gson.fromJson(vehiculosJson, vehiculosType);
            usuario.setVehiculos(vehiculos != null ? vehiculos : new ArrayList<>());
        } else {
            usuario.setVehiculos(new ArrayList<>());
        }

        String reservasJson = prefs.getString(KEY_RESERVAS, null);
        if (reservasJson != null) {
            Type reservasType = new TypeToken<List<Reserva>>() {}.getType();
            List<Reserva> reservas = gson.fromJson(reservasJson, reservasType);
            usuario.setReservas(reservas != null ? reservas : new ArrayList<>());
        } else {
            usuario.setReservas(new ArrayList<>());
        }

        return usuario;
    }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }

    public boolean haySesionActiva() {
        return prefs.contains(KEY_EMAIL);
    }
}
