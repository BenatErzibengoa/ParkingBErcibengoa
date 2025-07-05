package com.lksnext.parkingbercibengoa.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.databinding.ReservaItemBinding;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

public class ReservasFragment extends BaseReservasFragment<ReservasViewModel> {

    private FragmentReservasBinding binding;
    private ReservasViewModel viewModel;

    NavController navController;


    @Override
    protected View inflarLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentReservasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ViewGroup getContainerReservas() {
        return binding.containerReservas;
    }

    @Override
    protected ReservasViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ReservasViewModelFactory.getSharedInstance(requireActivity().getApplication());
        }
        return viewModel;
    }

    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(), reservas -> {
            mostrarReservas(reservas, true);
        });

        getViewModel().getErrorCargarReservas().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error al cargar reservas: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        if (getViewModel().getReservas().getValue() == null || getViewModel().getReservas().getValue().isEmpty()) {
            getViewModel().cargarReservasDelUsuario();
        }
    }

    @Override
    protected void onExtraViewReady() {
        navController = Navigation.findNavController(requireActivity(), R.id.flFragment);
        binding.btnNuevaReserva.setOnClickListener(v -> {
            navController.navigate(R.id.action_reservasFragment_to_nuevaReservaFragment);
        });
    }

    @Override
    protected void configurarListenersReserva(ReservaItemBinding card, Reserva reserva, boolean esFuturo) {
        // Solo configurar el long click para reservas futuras
        // (se supone que en este fragment solo se muestran reservas futuras, pero por si acaso)
        if (esFuturo) {
            card.getRoot().setOnLongClickListener(v -> {
                mostrarOpcionesReserva(reserva);
                return true;
            });
            card.getRoot().setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white)); // vuelve al color original
                        break;

                    default: ;
                }
                return false; // permite que el long click se dispare
            });

        }
    }

    private void mostrarOpcionesReserva(Reserva reserva) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Opciones de reserva");

        // Crear vista personalizada para el diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_opciones_reserva, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Configurar botones
        Button btnEditar = dialogView.findViewById(R.id.btnEditar);
        Button btnCancelar = dialogView.findViewById(R.id.btnEliminar);
        Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);

        btnEditar.setOnClickListener(v -> {
            dialog.dismiss();
            editarReserva(reserva);
        });

        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
            confirmarCancelacion(reserva);
        });

        btnCerrar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void editarReserva(Reserva reserva) {
        viewModel.setReservaAEditar(reserva);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.flFragment);
        navController.navigate(R.id.action_reservasFragment_to_nuevaReservaFragment);
    }

    private void confirmarCancelacion(Reserva reserva) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar cancelación")
                .setMessage("¿Estás seguro de que quieres cancelar esta reserva?")
                .setPositiveButton("Sí, cancelar", (dialog, which) -> {
                    cancelarReserva(reserva);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarReserva(Reserva reserva) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cancelando reserva...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getViewModel().cancelarReserva(reserva, new Callback() {
            @Override
            public void onSuccess() {

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Reserva cancelada exitosamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al cancelar reserva: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

}
