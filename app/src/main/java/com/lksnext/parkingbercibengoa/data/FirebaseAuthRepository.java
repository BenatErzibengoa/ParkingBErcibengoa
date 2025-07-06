package com.lksnext.parkingbercibengoa.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingbercibengoa.data.firebase.UsuarioDTO;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;

import java.util.Map;

public class FirebaseAuthRepository implements AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    public FirebaseAuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password, LoginCallback<Usuario> callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        obtenerUsuario(uid, callback);
                    } else {
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    @Override
    public void register(String fullname, String email, String password, Callback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        if (firebaseUser == null) {
                            callback.onFailure("USER_NULL_ERROR");
                            return;
                        }

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullname)
                                .build();

                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(profileUpdateTask -> {
                            if (profileUpdateTask.isSuccessful()) {
                                firestore.collection("usuarios")
                                        .document(firebaseUser.getUid())
                                        .set(UsuarioDTO.toMap(new Usuario(fullname, email)))
                                        .addOnSuccessListener(aVoid -> callback.onSuccess())
                                        .addOnFailureListener(e -> callback.onFailure("DB_WRITE_ERROR"));
                            } else {
                                callback.onFailure("PROFILE_UPDATE_ERROR");
                            }
                        });
                    } else {
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    @Override
    public void changePassword(String email, Callback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
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

    @Override
    public void obtenerUsuario(String uid, LoginCallback<Usuario> callback) {
        firestore.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        Usuario usuario = UsuarioDTO.fromMap(data);
                        usuario.setId(uid);
                        callback.onSuccess(usuario);
                    } else {
                        callback.onFailure("ERROR_USER_NOT_FOUND");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }

    private String parseFirebaseError(Exception exception) {
        if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
            return "INVALID_CREDENTIALS";
        } else if (exception instanceof com.google.firebase.auth.FirebaseAuthUserCollisionException) {
            return "USER_COLLISION";
        } else if (exception instanceof com.google.firebase.FirebaseNetworkException) {
            return "NETWORK_ERROR";
        } else if (exception instanceof com.google.firebase.FirebaseTooManyRequestsException) {
            return "TOO_MANY_REQUESTS";
        } else {
            return "UNKNOWN_ERROR";
        }
    }
}
