package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;
import com.lksnext.parkingbercibengoa.domain.Plaza;

import java.time.LocalDate;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class HorarioPlazaDTO {

    public static Map<String, Object> toMap(HorarioPlaza hp) {
        Map<String, Object> map = new HashMap<>();
        map.put("plaza", PlazaDTO.toMap(hp.getPlaza()));
        map.put("dia", hp.getDia().toString());
        map.put("horario", bitSetToString(hp.getHorario()));
        return map;
    }

    public static HorarioPlaza fromMap(Map<String, Object> map) {
        Plaza plaza = PlazaDTO.fromMap((Map<String, Object>) map.get("plaza"));
        LocalDate dia = LocalDate.parse((String) map.get("dia"));
        HorarioPlaza hp = new HorarioPlaza(plaza, dia);
        hp.setHorario(stringToBitSet((String) map.get("horario")));
        return hp;
    }

    private static String bitSetToString(BitSet bitSet) {
        StringBuilder sb = new StringBuilder(1440);
        for (int i = 0; i < 1440; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    private static BitSet stringToBitSet(String binary) {
        BitSet bitSet = new BitSet(1440);
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }
}
