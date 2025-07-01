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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingbercibengoa.data.firebase.ReservaDTO;
import com.lksnext.parkingbercibengoa.data.firebase.UsuarioDTO;
import com.lksnext.parkingbercibengoa.data.firebase.VehiculoDTO;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.CallbackList;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataRepository {
    private static final String SERVER_ERROR = "server_error";  // Compliant
    private static DataRepository instance;
    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private DataRepository(){
    }

    public static synchronized DataRepository getInstance(){
        if (instance == null){
            instance = new DataRepository();
        }
        return instance;
    }

    public void login(String email, String pass, LoginCallback callback){
        mauth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mauth.getCurrentUser().getUid();
                        obtenerUsuario(uid, callback);
                    } else {
                        Exception exception = task.getException();
                        String errorCode = parseFirebaseError(exception);
                        callback.onFailure(errorCode);
                    }
                });
    }

    public void obtenerUsuario(String uid, LoginCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
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



    public void register(String fullname, String email, String contraseña, Callback callback) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        if (firebaseUser == null) {
                            callback.onFailure("USER_NULL_ERROR");
                            return;
                        }

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullname)
                                .build();

                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(profileUpdateTask -> {
                            if (profileUpdateTask.isSuccessful()) {
                                FirebaseFirestore.getInstance()
                                        .collection("usuarios")
                                        .document(firebaseUser.getUid())  // es importante que sea UID y no el email, si no puede dar error
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

    public void obtenerVehiculosUsuario(String uid, CallbackList<Vehiculo> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(uid)
                .collection("vehiculos")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Vehiculo> listaVehiculos = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Vehiculo v = VehiculoDTO.fromMap(doc.getData());
                        listaVehiculos.add(v);
                    }
                    callback.onSuccess(listaVehiculos);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }

    public void obtenerReservasUsuario(String uid, CallbackList<Reserva> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(uid)
                .collection("reservas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Reserva> listaReservas = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Reserva r = ReservaDTO.fromMap(doc.getData());
                        listaReservas.add(r);
                    }
                    callback.onSuccess(listaReservas);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }




    public void changePassword(String email, Callback callback){
        mauth.getInstance().sendPasswordResetEmail(email)
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

    public void guardarReserva(Reserva reserva) {
        db.collection("reservas")
                .document(reserva.getId())
                .set(ReservaDTO.toMap(reserva))
                .addOnSuccessListener(aVoid -> {
                    Log.d("DataRepository","Reserva guardada correctamente.");
                })
                .addOnFailureListener(e -> {
                    Log.d("DataRepository","Error al guardar la reserva: " + e.getMessage());
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

    private List<Vehiculo> getVehiculos(Usuario usuario){
        return new ArrayList<Vehiculo>();
    }



}

