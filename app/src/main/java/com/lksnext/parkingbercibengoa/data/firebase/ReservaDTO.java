package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.util.HashMap;
import java.util.Map;

public class ReservaDTO {

    public static Map<String, Object> toMap(Reserva reserva) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", reserva.getId());
        data.put("usuario", mapUsuario(reserva.getUsuario()));
        data.put("vehiculo", mapVehiculo(reserva.getVehiculo()));
        data.put("fechaInicio", reserva.getFechaInicio().toString());
        data.put("duracionMinutos", reserva.getDuracion().toMinutes());
        data.put("plaza", mapPlaza(reserva.getPlaza()));
        return data;
    }

    private static Map<String, Object> mapUsuario(Usuario usuario) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", usuario.getNombre());
        map.put("email", usuario.getEmail());
        return map;
    }

    private static Map<String, Object> mapVehiculo(Vehiculo v) {
        Map<String, Object> map = new HashMap<>();
        map.put("matricula", v.getMatricula());
        map.put("marca", v.getModelo());
        map.put("tipoVehiculo", v.getTipoVehiculo().name());
        return map;
    }

    private static Map<String, Object> mapPlaza(Plaza plaza) {
        Map<String, Object> map = new HashMap<>();
        map.put("numero", plaza.getId());
        map.put("tipo", plaza.getTipo().name());
        return map;
    }

    // Para leer desde Firebase luego (si te interesa)
    public static Reserva fromMap(Map<String, Object> data) {
        // Esto lo podemos hacer luego si necesitás
        return null;
    }
}