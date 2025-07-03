package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;

import java.util.HashMap;
import java.util.Map;

public class PlazaDTO {

    public static Map<String, Object> toMap(Plaza plaza) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", plaza.getId());
        map.put("tipoVehiculo", plaza.getTipo().name());
        return map;
    }

    public static Plaza fromMap(Map<String, Object> map) {
        String id = (String) map.get("id");
        TipoVehiculo tipo = TipoVehiculo.valueOf((String) map.get("tipoVehiculo"));
        return new Plaza(id, tipo);
    }
}
