package com.lksnext.parkingbercibengoa.data;

import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;

public interface AuthRepository {
    void login(String email, String password, LoginCallback<Usuario> callback);
    void register(String fullname, String email, String password, Callback callback);
    void changePassword(String email, Callback callback);
    void obtenerUsuario(String uid, LoginCallback<Usuario> callback);
}
