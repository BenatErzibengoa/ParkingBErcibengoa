package com.lksnext.parkingbercibengoa.data.firebase;

import com.lksnext.parkingbercibengoa.domain.Usuario;

import java.util.HashMap;
import java.util.Map;

public class UsuarioDTO {

    public static Map<String, Object> toMap(Usuario usuario) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", usuario.getNombre());
        map.put("email", usuario.getEmail());
        return map;
    }

    // fromMap si quieres reconstruir luego el objeto desde Firebase
    public static Usuario fromMap(Map<String, Object> map) {
        Usuario usuario = new Usuario();
        usuario.setNombre((String) map.get("nombre"));
        usuario.setEmail((String) map.get("email"));
        return usuario;
    }
}
