package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public static Reserva fromMap(Map<String, Object> data) {
        String id = (String) data.get("id");

        // Usuario
        Map<String, Object> usuarioMap = (Map<String, Object>) data.get("usuario");
        Usuario usuario = new Usuario(
                (String) usuarioMap.get("nombre"),
                (String) usuarioMap.get("email")
        );

        // Vehículo
        Map<String, Object> vehiculoMap = (Map<String, Object>) data.get("vehiculo");
        Vehiculo vehiculo = new Vehiculo(
                (String) vehiculoMap.get("matricula"),
                (String) vehiculoMap.get("marca"),
                TipoVehiculo.valueOf((String) vehiculoMap.get("tipoVehiculo"))
        );

        // Fecha y duración
        LocalDateTime fechaInicio = LocalDateTime.parse((String) data.get("fechaInicio"));
        Duration duracion = Duration.ofMinutes((Long) data.get("duracionMinutos"));

        // Plaza
        Map<String, Object> plazaMap = (Map<String, Object>) data.get("plaza");
        Plaza plaza = new Plaza(
                (String) plazaMap.get("numero"),
                TipoVehiculo.valueOf((String) plazaMap.get("tipo"))
        );

        return new Reserva(id, usuario, vehiculo, fechaInicio, duracion, plaza);
    }
}