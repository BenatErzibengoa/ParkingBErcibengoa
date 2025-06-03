package com.lksnext.parkingbercibengoa.data;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingbercibengoa.domain.Callback;

public class DataRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth mAuth;
    private static DataRepository instance;

    private static final String SERVER_ERROR = "server_error";


    private DataRepository(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public DataRepository(FirebaseFirestore db, FirebaseAuth mAuth) {
        this.db = db;
        this.mAuth = mAuth;
    }

    public static synchronized DataRepository getInstance(){
        if (instance == null){
            instance = new DataRepository();
        }
        return instance;
    }

    public void login(String email, String pass, Callback callback){
        mAuth.signInWithEmailAndPassword(email, pass)
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
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileUpdateTask -> {
                                    if (profileUpdateTask.isSuccessful()) {
                                        callback.onSuccess();
                                    } else {
                                        callback.onFailure(SERVER_ERROR);
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
        mAuth.sendPasswordResetEmail(email)
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
            return errorCode != null ? errorCode : SERVER_ERROR;
        }

        String message = exception.getMessage() != null ? exception.getMessage().toLowerCase() : "";
        if (message.contains("network")) {
            return "no_connection";
        }

        return SERVER_ERROR;
    }



}

