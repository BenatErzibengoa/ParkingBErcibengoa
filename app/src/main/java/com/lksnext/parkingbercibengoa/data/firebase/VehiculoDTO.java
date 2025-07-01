package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.util.HashMap;
import java.util.Map;

public class VehiculoDTO {

    public static Map<String, Object> toMap(Vehiculo vehiculo) {
        Map<String, Object> map = new HashMap<>();
        map.put("matricula", vehiculo.getMatricula());
        map.put("modelo", vehiculo.getModelo());
        map.put("tipoVehiculo", vehiculo.getTipoVehiculo().name());
        return map;
    }

    public static Vehiculo fromMap(Map<String, Object> map) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula((String) map.get("matricula"));
        vehiculo.setModelo((String) map.get("marca"));
        vehiculo.setTipoVehiculo(TipoVehiculo.valueOf((String) map.get("tipoVehiculo")));
        return vehiculo;
    }
}
