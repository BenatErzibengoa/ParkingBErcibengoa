package com.lksnext.parkingbercibengoa.data;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.parkingbercibengoa.data.firebase.HorarioPlazaDTO;
import com.lksnext.parkingbercibengoa.data.firebase.ReservaDTO;
import com.lksnext.parkingbercibengoa.data.firebase.VehiculoDTO;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.CallbackList;
import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataRepository {
    private static DataRepository instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DataRepository(){
    }

    public static synchronized DataRepository getInstance(){
        if (instance == null){
            instance = new DataRepository();
        }
        return instance;
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

    public void añadirVehiculoAUsuario(Usuario usuario, Vehiculo vehiculo, Callback callback) {
        if (usuario == null || usuario.getId() == null) {
            callback.onFailure("Usuario inválido");
            return;
        }
        db.collection("usuarios")
                .document(usuario.getId())
                .collection("vehiculos")
                .document(vehiculo.getMatricula())  // matricula como id del vehiculo
                .set(VehiculoDTO.toMap(vehiculo))
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
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

    public void obtenerPlazas(CallbackList<Plaza> callback) {
        db.collection("plazas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Plaza> plazas = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String id = doc.getString("id");
                        String tipoStr = doc.getString("tipo");
                        if (id != null && tipoStr != null) {
                            Plaza plaza = new Plaza();
                            plaza.setId(id);
                            try {
                                plaza.setTipo(TipoVehiculo.valueOf(tipoStr.toUpperCase()));
                            } catch (IllegalArgumentException e) {
                                Log.w("DataRepository", "TipoVehiculo desconocido: " + tipoStr);
                                continue;  // Saltar esta plaza
                            }
                            plazas.add(plaza);
                        }
                    }
                    callback.onSuccess(plazas);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }

    public void getOrCreateHorarioPlaza(Plaza plaza, LocalDate dia, LoginCallback<HorarioPlaza> callback) {
        String docId = dia.toString(); // ID --> fecha

        db.collection("plazas")
                .document(plaza.getId())
                .collection("horarios")
                .document(docId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            HorarioPlaza horario = HorarioPlazaDTO.fromMap(data);
                            callback.onSuccess(horario);
                        } else {
                            callback.onFailure("Documento vacío");
                        }
                    } else {
                        // Crear nuevo horario si no existe
                        HorarioPlaza nuevo = new HorarioPlaza(plaza, dia);
                        Map<String, Object> map = HorarioPlazaDTO.toMap(nuevo);

                        db.collection("plazas")
                                .document(plaza.getId())
                                .collection("horarios")
                                .document(docId)
                                .set(map)
                                .addOnSuccessListener(aVoid -> callback.onSuccess(nuevo))
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                    callback.onFailure("Error al crear horario: " + e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onFailure("Error al obtener horario: " + e.getMessage());
                });
    }

    public void guardarReserva(Usuario usuario, Reserva reserva, Callback callback) {
        Plaza plaza = reserva.getPlaza();
        LocalDateTime inicio = reserva.getFechaInicio();
        Duration duracion = reserva.getDuracion();
        LocalDate dia = inicio.toLocalDate();

        getOrCreateHorarioPlaza(plaza, dia, new LoginCallback<HorarioPlaza>() {
            @Override
            public void onSuccess(HorarioPlaza horarioPlaza) {
                boolean exito = horarioPlaza.reservar(inicio, duracion);

                if (!exito) {
                    callback.onFailure("No se pudo reservar la plaza en el horario indicado.");
                    return;
                }

                // Convertimos el nuevo horario en mapa y lo guardamos en Firestore
                Map<String, Object> horarioMap = HorarioPlazaDTO.toMap(horarioPlaza);

                db.collection("plazas")
                        .document(plaza.getId())
                        .collection("horarios")
                        .document(dia.toString())
                        .set(horarioMap)
                        .addOnSuccessListener(aVoid -> {
                            // Finalmente guardamos la reserva
                            db.collection("usuarios")
                                    .document(usuario.getId())
                                    .collection("reservas")
                                    .document(reserva.getId())
                                    .set(ReservaDTO.toMap(reserva))
                                    .addOnSuccessListener(aVoid2 -> callback.onSuccess())
                                    .addOnFailureListener(e -> callback.onFailure("Error al guardar la reserva: " + e.getMessage()));
                        })
                        .addOnFailureListener(e -> callback.onFailure("Error al guardar el horario actualizado: " + e.getMessage()));
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure("Error al obtener/crear horario: " + errorMessage);
            }
        });
    }


    public void eliminarReserva(Usuario usuario, Reserva reserva, Callback callback) {
        Plaza plaza = reserva.getPlaza();
        LocalDateTime inicio = reserva.getFechaInicio();
        Duration duracion = reserva.getDuracion();
        LocalDate dia = inicio.toLocalDate();

        getOrCreateHorarioPlaza(plaza, dia, new LoginCallback<HorarioPlaza>() {
            @Override
            public void onSuccess(HorarioPlaza horarioPlaza) {
                horarioPlaza.cancelarReserva(inicio, duracion);

                Map<String, Object> horarioMap = HorarioPlazaDTO.toMap(horarioPlaza);
                db.collection("plazas")
                        .document(plaza.getId())
                        .collection("horarios")
                        .document(dia.toString())
                        .set(horarioMap)
                        .addOnSuccessListener(aVoid -> {
                            db.collection("usuarios")
                                    .document(usuario.getId())
                                    .collection("reservas")
                                    .document(reserva.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid1 -> callback.onSuccess())
                                    .addOnFailureListener(e -> callback.onFailure("Error al eliminar reserva: " + e.getMessage()));
                        })
                        .addOnFailureListener(e -> callback.onFailure("Error al actualizar horario: " + e.getMessage()));
                Log.d("DataRepository", "Reserva eliminada");
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure("Error al obtener horario: " + errorMessage);
            }
        });
    }

    public void editarReserva(Usuario usuario, Reserva reservaVieja, Reserva reservaNueva, Callback callback) {
        eliminarReserva(usuario, reservaVieja, new Callback() {
            @Override
            public void onSuccess() {
                guardarReserva(usuario, reservaNueva, new Callback() {
                    @Override
                    public void onSuccess() {
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        callback.onFailure("Se eliminó la reserva antigua, pero falló al guardar la nueva: " + msg);
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                callback.onFailure("Error al eliminar la reserva antigua: " + msg);
            }
        });
    }

}

