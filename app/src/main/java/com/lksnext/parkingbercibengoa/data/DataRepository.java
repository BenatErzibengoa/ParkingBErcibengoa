package com.lksnext.parkingbercibengoa.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class DataRepository {

    private static DataRepository instance;


    private DataRepository(){
    }

    //Creación de la instancia en caso de que no exista.
    public static synchronized DataRepository getInstance(){
        if (instance==null){
            instance = new DataRepository();
        }
        return instance;
    }

    public void login(String email, String pass, Callback callback){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        Log.d(":::", "Login Error: " + task.getException().getMessage());
                        callback.onFailure();
                    }
                });
    }

    public void register(String email, String pass, Callback callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        Log.d(":::", "Register Error: " + task.getException().getMessage());
                        callback.onFailure();
                    }
                });
    }

    public void changePassword(String email, Callback callback){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                        Log.d("Firebase", "Correo de restablecimiento enviado");
                    } else {
                        Log.e("Firebase", "Error al enviar el correo", task.getException());
                        callback.onFailure();
                    }
                });
    }

}
