package com.lksnext.parkingbercibengoa.data;

import android.util.Log;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class DataRepository {

    private static DataRepository instance;

    private DataRepository(){
    }

    public static synchronized DataRepository getInstance(){
        if (instance == null){
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
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    public void register(String fullName, String email, String pass, Callback callback){
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileUpdateTask -> {
                                    if (profileUpdateTask.isSuccessful()) {
                                        callback.onSuccess();
                                    } else {
                                        callback.onFailure("server_error");
                                    }
                                });
                    } else {
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    public void changePassword(String email, Callback callback){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    private String parseFirebaseError(Exception exception) {
        if (exception == null) return "unknown_error";

        if (exception instanceof FirebaseAuthUserCollisionException) {
            return "ERROR_EMAIL_ALREADY_IN_USE";
        }

        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "ERROR_WRONG_PASSWORD";
        }

        if (exception instanceof FirebaseTooManyRequestsException) {
            return "ERROR_TOO_MANY_REQUESTS";
        }

        if (exception instanceof FirebaseNetworkException) {
            return "no_connection";
        }

        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            return errorCode != null ? errorCode : "server_error";
        }

        String message = exception.getMessage() != null ? exception.getMessage().toLowerCase() : "";
        if (message.contains("network")) {
            return "no_connection";
        }

        return "server_error";
    }



}

